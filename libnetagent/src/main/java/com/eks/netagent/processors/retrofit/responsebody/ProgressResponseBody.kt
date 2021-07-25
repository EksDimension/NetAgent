package com.eks.netagent.processors.retrofit.responsebody

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * Created by Riggs on 12/19/2019
 */
class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: ProgressListener
) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null
    private var lastReadTime: Long = 0
    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        lastReadTime = 0L
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += bytesRead
                val currentTime = System.currentTimeMillis()
                when {
                    lastReadTime == 0L -> {
                        lastReadTime = currentTime
                        progressListener.onProgress(responseBody.contentLength(), totalBytesRead)
                    }
                    currentTime - lastReadTime > TIME_GAP_TO_READ -> {
                        lastReadTime = currentTime
                        progressListener.onProgress(responseBody.contentLength(), totalBytesRead)
                    }
                    responseBody.contentLength() == totalBytesRead -> {
                        lastReadTime = currentTime
                        progressListener.onProgress(responseBody.contentLength(), totalBytesRead)
                    }
                }
                return bytesRead
            }
        }
    }

    interface ProgressListener {
        fun onProgress(totalSize: Long, downSize: Long)
    }

    companion object {
        private const val TIME_GAP_TO_READ: Long = 16
    }
}