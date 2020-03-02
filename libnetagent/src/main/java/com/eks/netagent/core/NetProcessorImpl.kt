package com.eks.netagent.core

/**
 * 网络代理接口实现类(实际上不是他自身做网络请求,是由具体的框架来实现的)
 * Created by Riggs on 2020/3/1
 */
object NetProcessorImpl : INetProcessor {

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



}