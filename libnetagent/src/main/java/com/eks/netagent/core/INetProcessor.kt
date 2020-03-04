package com.eks.netagent.core

import java.io.File

/**
 * 网络代理接口
 * Created by Riggs on 2020/3/1
 */
interface INetProcessor {
    fun post(baseUrl: String, url: String, params: Map<String, Any>, callback: ICallback)

    fun get(baseUrl: String, url: String, params: Map<String, Any>, callback: ICallback)

    fun downloadFile(url: String, savePath: String, downloadListener: DownloadListener?)

    fun uploadFile(baseUrl: String, url: String, uploadFileMap: Map<String, File>, params: Map<String, String>, callback: ICallback)

    fun setHeaders(headers: HashMap<String, String>)

    fun addHeader(key: String, value: String)

    fun removeHeader(key: String)
}