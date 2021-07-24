package com.eks.netagent.processors.retrofit.http

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Riggs on 12/18/2019
 */
interface IApiService {
    @POST
    fun post(
        @Url url: String,
        @QueryMap params: HashMap<String, Any>
    ): Observable<Response<ResponseBody>>

    @GET
    fun get(
        @Url url: String,
        @QueryMap params: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @GET
    fun get(
        @Url url: String,
        @QueryMap params: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @Multipart
    @POST
    fun uploadFile(
        @Url url: String,
        @Part partList: List<MultipartBody.Part>
    ): Observable<Response<ResponseBody>>

    @Streaming
    @GET
    fun downloadFile(@Url url: String): Observable<ResponseBody>
}