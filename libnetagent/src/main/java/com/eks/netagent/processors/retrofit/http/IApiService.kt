package com.eks.netagent.processors.retrofit.http

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Riggs on 12/18/2019
 */
interface IApiService {

    @GET("{path}")
    fun get(@Path("path") path: String): Observable<Response<ResponseBody>>

    @GET("{path}")
    fun getWithQueryMap(
        @Path("path") path: String,
        @QueryMap params: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @GET("{path}")
    fun getWithHeaderMap(
        @Path("path") path: String,
        @HeaderMap header: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @GET("{path}")
    fun getWithQueryHeaderMaps(
        @Path("path") path: String,
        @QueryMap params: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @POST("{path}")
    fun postWithBody(
        @Path("path") path: String,
        @Body body: RequestBody
    ): Observable<Response<ResponseBody>>

    @POST("{path}")
    fun postWithBodyHeaderMaps(
        @Path("path") path: String,
        @Body body: RequestBody,
        @HeaderMap header: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @POST("{path}")
    fun postWithFieldMap(
        @Path("path") path: String,
        @FieldMap fieldMap: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @POST("{path}")
    fun postWithFieldHeaderMaps(
        @Path("path") path: String,
        @FieldMap fieldMap: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @POST("{path}")
    fun postWithHeaderMap(
        @Path("path") path: String,
        @HeaderMap header: Map<String, String>
    ): Observable<Response<ResponseBody>>

    @POST("{path}")
    fun post(@Path("path") path: String): Observable<Response<ResponseBody>>

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