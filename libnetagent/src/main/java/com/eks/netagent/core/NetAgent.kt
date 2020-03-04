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


    override fun post(baseUrl: String, url: String, params: Map<String, Any>, callback: ICallback) {
        mINetProcessor?.post(baseUrl, url, params, callback)
    }

    override fun get(baseUrl: String, url: String, params: Map<String, Any>, callback: ICallback) {
        mINetProcessor?.get(baseUrl, url, params, callback)
    }

    override fun downloadFile(url: String, savePath: String, downloadListener: DownloadListener?) {
        mINetProcessor?.downloadFile(url, savePath, downloadListener)
    }

    override fun uploadFile(baseUrl: String, url: String, uploadFileMap: Map<String, File>, params: Map<String, String>, callback: ICallback) {
        mINetProcessor?.uploadFile(baseUrl, url, uploadFileMap, params, callback)
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