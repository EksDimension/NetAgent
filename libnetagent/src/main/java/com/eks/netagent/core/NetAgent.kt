package com.eks.netagent.core

import java.io.File

/**
 * 网络代理接口实现类(实际上不是他自身做网络请求,是由具体的框架来实现的)
 * Created by Riggs on 2020/3/1
 */
object NetAgent : INetProcessor {

    //真正网络请求框架
    private var mINetProcessor: INetProcessor? = null

    //定义一个设置网络请求框架的方法
    fun init(iNetProcessor: INetProcessor) {
        mINetProcessor = iNetProcessor
    }

    /**
     * core request method
     * @param designatedBaseUrl set only if a specially designated BaseUrl is needed
     * @param url the rest url behind BaseUrl
     * @param params the params for the request
     * @param headers the supplement headers for the request except for common headers
     * @param callback the callback with success or fail response
     */
    override fun request(
        requestType: RequestType,
        designatedBaseUrl: String?,
        url: String,
        params: Map<String, String>?,
        headers: Map<String, String>?,
        callback: ICallback
    ) {
        mINetProcessor?.request(requestType, designatedBaseUrl, url, params, headers, callback)
    }

    override fun downloadFile(
        url: String,
        savePath: String,
        callback: ICallback,
        downloadListener: DownloadListener?
    ) {
        mINetProcessor?.downloadFile( url, savePath, callback, downloadListener)
    }

    override fun uploadFile(
        url: String,
        uploadFileMap: Map<String, File>,
        params: Map<String, String>,
        callback: ICallback,
        uploadListener: UploadListener?
    ) {
        mINetProcessor?.uploadFile(url, uploadFileMap, params, callback, uploadListener)
    }

    override fun setBaseUrl(baseUrl: String) {
        mINetProcessor?.setBaseUrl(baseUrl)
    }

    override fun setHeaders(headers: HashMap<String, String>) {
        mINetProcessor?.setHeaders(headers)
    }

    override fun addHeader(key: String, value: String) {
        mINetProcessor?.addHeader(key, value)
    }

    override fun removeHeader(key: String) {
        mINetProcessor?.removeHeader(key)
    }
}