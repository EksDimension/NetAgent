package com.eks.netagent.core

/**
 * 网络请求的顶层回调接口
 * Created by Riggs on 2020/3/1
 */
interface ICallback {
    fun onSucceed(result: String)
    fun onFailed(e: Any)
}