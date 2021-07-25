package com.eks.netagent.processors.retrofit.responsebody;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by Riggs on 2020/3/5
 */
public class UploadProgressRequestBody extends RequestBody {
    private File mFile;
    private String mPath;
    private String mMediaType;
    private UploadCallbacks mListener;

    private int mEachBufferSize = 1024;

    public UploadProgressRequestBody(final File file, String mediaType, final UploadCallbacks listener) {
        mFile = file;
        mMediaType = mediaType;
        mListener = listener;
    }

    public UploadProgressRequestBody(final File file, String mediaType, int eachBufferSize, final UploadCallbacks listener) {
        mFile = file;
        mMediaType = mediaType;
        mEachBufferSize = eachBufferSize;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        // i want to upload only images
        return MediaType.parse(mMediaType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[mEachBufferSize];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));
                uploaded += read;
                sink.write(buffer, 0, read);

            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgressUpdate(mTotal, mUploaded);
        }
    }

    public interface UploadCallbacks {
        void onProgressUpdate(long totalSize, long uploadedSize);
    }
}
