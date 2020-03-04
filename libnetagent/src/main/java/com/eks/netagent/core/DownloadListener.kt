package com.eks.netagent.core

/**
 * Created by Riggs on 2020/3/4
 */
interface DownloadListener {
    fun onProgress(totalSize: Long, downSize: Long)
}