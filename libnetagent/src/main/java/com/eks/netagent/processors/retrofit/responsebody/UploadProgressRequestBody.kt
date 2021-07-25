package com.eks.netagent.processors.retrofit.responsebody

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Created by Riggs on 2020/3/5
 */
class UploadProgressRequestBody : RequestBody {
    private var mFile: File

    @Suppress("unused")
    private val mPath: String? = null
    private var mMediaType: String
    private var mListener: UploadCallbacks
    private var mEachBufferSize = 1024
    private var lastReadTime: Long = 0

    constructor(file: File, mediaType: String, listener: UploadCallbacks) {
        mFile = file
        mMediaType = mediaType
        mListener = listener
        lastReadTime = 0L
    }

    @Suppress("unused")
    constructor(file: File, mediaType: String, eachBufferSize: Int, listener: UploadCallbacks) {
        mFile = file
        mMediaType = mediaType
        mEachBufferSize = eachBufferSize
        mListener = listener
    }

    override fun contentType(): MediaType? {
        // i want to upload only images
        return mMediaType.toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(mEachBufferSize)
        val `in` = FileInputStream(mFile)
        var uploaded: Long = 0
        `in`.use { inStream ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (inStream.read(buffer).also { read = it } != -1) {
                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) :
        Runnable {
        override fun run() {
            val currentTime = System.currentTimeMillis()
            when {
                lastReadTime == 0L -> {
                    lastReadTime = currentTime
                    mListener.onProgressUpdate(mTotal, mUploaded)
                }
                currentTime - lastReadTime > TIME_GAP_TO_READ -> {
                    lastReadTime = currentTime
                    mListener.onProgressUpdate(mTotal, mUploaded)
                }
                mTotal == mUploaded -> {
                    lastReadTime = currentTime
                    mListener.onProgressUpdate(mTotal, mUploaded)
                }
            }
        }
    }

    interface UploadCallbacks {
        fun onProgressUpdate(totalSize: Long, uploadedSize: Long)
    }

    companion object {
        private const val TIME_GAP_TO_READ: Long = 16
    }
}