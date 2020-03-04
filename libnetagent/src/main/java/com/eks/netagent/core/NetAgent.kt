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


    override fun post(url: String, params: Map<String, Any>, callback: ICallback) {
        mINetProcessor?.post(url, params, callback)
    }

    override fun get(url: String, params: Map<String, Any>, callback: ICallback) {
        mINetProcessor?.get(url, params, callback)
    }

    override fun downloadFile(url: String, savePath: String, callback: ICallback, downloadListener: DownloadListener?) {
        mINetProcessor?.downloadFile(url, savePath, callback, downloadListener)
    }

    override fun uploadFile(url: String, uploadFileMap: Map<String, File>, params: Map<String, String>, callback: ICallback, uploadListener: UploadListener?) {
        mINetProcessor?.uploadFile(url, uploadFileMap, params, callback, uploadListener)
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