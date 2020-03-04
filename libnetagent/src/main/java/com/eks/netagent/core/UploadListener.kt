package com.eks.netagent.core

/**
 * Created by Riggs on 2020/3/4
 */
interface UploadListener {
    fun onProgress(totalSize: Long, uploadedSize: Long)
}