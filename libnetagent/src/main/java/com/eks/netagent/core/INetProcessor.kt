package com.eks.netagent.core

/**
 * 网络代理接口
 * Created by Riggs on 2020/3/1
 */
interface INetProcessor {
    fun post(url: String, params: Map<String, Any>,callback : ICallback)

    fun get(url: String, params: Map<String, Any>,callback : ICallback)
}