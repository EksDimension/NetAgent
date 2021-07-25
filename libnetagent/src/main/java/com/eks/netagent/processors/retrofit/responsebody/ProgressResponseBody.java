package com.eks.netagent.processors.retrofit.responsebody;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Riggs on 12/19/2019
 */
public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private ProgressListener progressListener;
    private long lastReadTime;

    private static long TIME_GAP_TO_READ = 16;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }


    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        lastReadTime = 0L;
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead;
                long currentTime = System.currentTimeMillis();
                if (lastReadTime == 0) {
                    lastReadTime = currentTime;
                    progressListener.onProgress(responseBody.contentLength(), totalBytesRead);
                } else if (currentTime - lastReadTime > TIME_GAP_TO_READ) {
                    lastReadTime = currentTime;
                    progressListener.onProgress(responseBody.contentLength(), totalBytesRead);
                } else if(responseBody.contentLength() == totalBytesRead){
                    lastReadTime = currentTime;
                    progressListener.onProgress(responseBody.contentLength(), totalBytesRead);
                }
                return bytesRead;
            }
        };
    }

    public interface ProgressListener {
        void onProgress(long totalSize, long downSize);
    }
}