package com.eks.netagent.core

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

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


    override fun post(url: String, params: Map<String, Any>, callback: ICallback) {
        //整串地址 http://www.aaa.bbb/index?&user=eks&pwd=123   有的框架是一串的

        //URL地址: http://www.aaa.bbb/index   Params键值对:&user=eks&pwd=123   有的框架是这样分开的

        //考虑兼容性,全都统一为整串地址
        val finalUrl = appendParams(url, params)
        mINetProcessor?.post(finalUrl, params, callback)
    }

    override fun get(url: String, params: Map<String, Any>, callback: ICallback) {
    }


    private fun appendParams(url: String, params: Map<String, Any>?): String {
        if (params == null || params.isEmpty()) {
            return url
        }
        val urlBuilder = StringBuilder(url)
        if (urlBuilder.indexOf("?") <= 0) {
            urlBuilder.append("?")
        } else {
            if (!urlBuilder.toString().endsWith("?")) {
                urlBuilder.append("&")
            }
        }
        for ((key, value) in params) {
            urlBuilder.append("&$key")
                    .append("=")
                    .append(encode(value.toString()))
        }
        return urlBuilder.toString()
    }

    private fun encode(str: String): String {
        try {
            return URLEncoder.encode(str, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            throw RuntimeException()
        }

    }
}