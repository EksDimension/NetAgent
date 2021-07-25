package com.eks.netagent.processors.retrofit.http

import com.eks.netagent.processors.retrofit.responsebody.ProgressResponseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Riggs on 9/10/2019
 */
object ApiServiceHelper {

    var headers = HashMap<String, String>()

    fun getApiService(
        baseUrl: String,
        progressListener: ProgressResponseBody.ProgressListener? = null
    ): IApiService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val mBuilder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
        // add header interceptor
        mBuilder.addInterceptor(defaultHeaderInterceptor)
        // add logging interceptor
        mBuilder.addInterceptor(httpLoggingInterceptor)
        // add download progress listener
        addDownloadProgressListener(progressListener, mBuilder)
        val serviceI: IApiService
        val retrofit: Retrofit = Retrofit.Builder()
            .client(mBuilder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
        serviceI = retrofit.create(IApiService::class.java)
        return serviceI
    }

    private fun addDownloadProgressListener(
        progressListener: ProgressResponseBody.ProgressListener?,
        mBuilder: OkHttpClient.Builder
    ) {
        progressListener?.let { pL ->
            mBuilder.addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                val responseBody = response.body
                val progressResponseBody: ProgressResponseBody? = if (responseBody != null) {
                    ProgressResponseBody(responseBody,
                        object : ProgressResponseBody.ProgressListener {
                            override fun onProgress(totalSize: Long, downSize: Long) {
                                GlobalScope.launch(Dispatchers.IO) {
                                    withContext(Dispatchers.Main) {
                                        pL.onProgress(totalSize, downSize)
                                    }
                                }
                            }
                        }
                    )
                } else {
                    null
                }
                response.newBuilder().body(progressResponseBody).build()
            }
        }
    }

    private var defaultHeaderInterceptor = Interceptor { chain ->
        // create a new request based on the intercepted chain, then insert headers
        var builder = chain.request().newBuilder()
        builder = addHeaders(builder)
        val newRequest = builder.build()
        // then start request
        chain.proceed(newRequest)
    }

    private fun addHeaders(builder: Request.Builder): Request.Builder {
        headers.entries.forEach {
            builder.addHeader(it.key, it.value)
        }
        return builder
    }

}