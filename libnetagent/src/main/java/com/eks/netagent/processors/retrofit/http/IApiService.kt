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
@Suppress("unused")
interface IApiService {

    @GET("{path}")
    suspend fun get(@Path("path") path: String): Response<ResponseBody>

    @GET("{path}")
    suspend fun getWithQueryMap(
        @Path("path") path: String,
        @QueryMap params: Map<String, String>
    ): Response<ResponseBody>

    @GET("{path}")
    suspend fun getWithHeaderMap(
        @Path("path") path: String,
        @HeaderMap header: Map<String, String>
    ): Response<ResponseBody>

    @GET("{path}")
    suspend fun getWithQueryHeaderMaps(
        @Path("path") path: String,
        @QueryMap params: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Response<ResponseBody>

    @POST("{path}")
    suspend fun postWithBody(
        @Path("path") path: String,
        @Body body: RequestBody
    ): Response<ResponseBody>

    @POST("{path}")
    suspend fun postWithBodyHeaderMaps(
        @Path("path") path: String,
        @Body body: RequestBody,
        @HeaderMap header: Map<String, String>
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST("{path}")
    suspend fun postWithFieldMap(
        @Path("path") path: String,
        @FieldMap fieldMap: Map<String, String>
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST("{path}")
    suspend fun postWithFieldHeaderMaps(
        @Path("path") path: String,
        @FieldMap fieldMap: Map<String, String>,
        @HeaderMap header: Map<String, String>
    ): Response<ResponseBody>

    @POST("{path}")
    suspend fun postWithHeaderMap(
        @Path("path") path: String,
        @HeaderMap header: Map<String, String>
    ): Response<ResponseBody>

    @POST("{path}")
    suspend fun post(@Path("path") path: String): Response<ResponseBody>

    @Multipart
    @POST
    fun uploadFile(
        @Url url: String,
        @Part partList: List<MultipartBody.Part>
    ): Observable<Response<ResponseBody>>

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}