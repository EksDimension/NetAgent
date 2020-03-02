package com.eks.netagent.processors.retrofit.http

import com.eks.netagent.processors.retrofit.responsebody.ProgressResponseBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Riggs on 9/10/2019
 */
object ApiServiceHelper {
    fun getApiService(
            baseUrl: String,
            headerInterceptor: Interceptor? = null
            , progressListener: ProgressResponseBody.ProgressListener? = null
    ): IApiService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val mBuilder = OkHttpClient.Builder()
//                                                .cache(cache)

                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
        //如果有请求头拦截器就加入
        headerInterceptor?.let { mBuilder.addInterceptor(it) }
        //如果有下载拦截器就加入
        progressListener?.let { pL ->
            mBuilder.addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder().body(ProgressResponseBody(response.body, ProgressResponseBody.ProgressListener { totalSize, downSize ->
                    pL.onProgress(totalSize, downSize)
                })).build()
            }
        }
        //如果没有下载拦截器 就加入日志
        if (progressListener == null) mBuilder.addInterceptor(httpLoggingInterceptor)
        val retrofit: Retrofit
        val serviceI: IApiService
        retrofit = Retrofit.Builder()
                .client(mBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        serviceI = retrofit.create<IApiService>(IApiService::class.java)
        return serviceI
    }
}