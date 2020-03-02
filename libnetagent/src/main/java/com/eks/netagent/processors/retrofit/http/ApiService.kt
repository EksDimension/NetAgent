package com.eks.netagent.processors.retrofit.http

import okhttp3.Interceptor

/**
 * Created by Riggs on 9/10/2019
 */
class ApiService(baseUrl: String, headerInterceptor: Interceptor? = null) {

    var baseHeaderInterceptor = Interceptor { chain ->
        // 以拦截到的请求为基础创建一个新的请求对象，然后插入Header
        val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
//                .addHeader("token", "e5708f0f-7515-4dbf-88a8-3e8c0d29e46f")
                .build()
        // 开始请求
        chain.proceed(newRequest)
    }

    var iApiService: IApiService =
            ApiServiceHelper.getApiService(baseUrl, headerInterceptor ?: baseHeaderInterceptor)
}