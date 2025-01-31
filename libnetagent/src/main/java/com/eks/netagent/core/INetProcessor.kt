package com.eks.netagent.core

import java.io.File

/**
 * 网络代理接口
 * Created by Riggs on 2020/3/1
 */
interface INetProcessor {

    fun request(
        requestType: RequestType,
        designatedBaseUrl: String?,
        url: String,
        params: Map<String, String>?,
        headers: Map<String, String>?,
        callback: ICallback
    )

    fun downloadFile(
        url: String,
        savePath: String,
        callback: ICallback,
        downloadListener: DownloadListener?
    )

    fun uploadFile(
        url: String,
        uploadFileMap: Map<String, File>,
        params: Map<String, String>,
        callback: ICallback,
        uploadListener: UploadListener?
    )

    fun setBaseUrl(baseUrl: String)

    fun setHeaders(headers: HashMap<String, String>)

    fun addHeader(key: String, value: String)

    fun removeHeader(key: String)
}