package com.eks.netagent.processors.retrofit

import com.eks.netagent.core.*
import com.eks.netagent.processors.retrofit.http.ApiService
import com.eks.netagent.processors.retrofit.http.ApiServiceHelper
import com.eks.netagent.processors.retrofit.responsebody.UploadProgressRequestBody
import com.eks.netagent.processors.retrofit.responsebody.ProgressResponseBody
import com.eks.netagent.utils.FileUtil
import com.eks.netagent.utils.UrlUtil
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.lang.Exception

/**
 * Created by Riggs on 2020/3/2
 */
class RetrofitProcessor : INetProcessor {

    /**
     * common baseurl , it will be effected in the whole app journey
     */
    private var commonBaseUrl: String? = null

    override fun setBaseUrl(baseUrl: String) {
        commonBaseUrl = baseUrl
    }

    override fun setHeaders(headers: HashMap<String, String>) {
        ApiServiceHelper.headers = headers
    }

    override fun addHeader(key: String, value: String) {
        ApiServiceHelper.headers[key] = value
    }

    override fun removeHeader(key: String) {
        ApiServiceHelper.headers.remove(key)
    }

    override fun request(
        requestType: RequestType,
        designatedBaseUrl: String?,
        url: String,
        params: Map<String, String>?,
        headers: Map<String, String>?,
        callback: ICallback
    ) {
        GlobalScope.launch(Dispatchers.Main) Scope@{
            try {
                var baseUrl: String?
                baseUrl = commonBaseUrl
                if (designatedBaseUrl != null) {
                    baseUrl = designatedBaseUrl
                }
                if (baseUrl == null || baseUrl.isEmpty()) return@Scope
                val response: Response<ResponseBody> =
                    processRequest(requestType, params, headers, baseUrl, url)
                val responseString = processResponse(response)
                callback.onSucceed(responseString)
            } catch (e: Exception) {
                callback.onFailed(e)
            }
        }
    }

    private suspend fun processRequest(
        requestType: RequestType,
        params: Map<String, String>?,
        headers: Map<String, String>?,
        baseUrl: String,
        url: String
    ): Response<ResponseBody> {
        return withContext(Dispatchers.IO) {
            if (requestType == RequestType.GET) {
                if (params != null && headers != null) {
                    ApiService(baseUrl).iApiService.getWithQueryHeaderMaps(url, params, headers)
                } else if (params != null && headers == null) {
                    ApiService(baseUrl).iApiService.getWithQueryMap(url, params)
                } else if (params == null && headers != null) {
                    ApiService(baseUrl).iApiService.getWithHeaderMap(url, headers)
                } else {
                    ApiService(baseUrl).iApiService.get(url)
                }
            } else {
                if (params != null && headers != null) {
                    ApiService(baseUrl).iApiService.postWithFieldHeaderMaps(
                        url,
                        params,
                        headers
                    )
                } else if (params != null && headers == null) {
                    ApiService(baseUrl).iApiService.postWithFieldMap(url, params)
                } else if (params == null && headers != null) {
                    ApiService(baseUrl).iApiService.postWithHeaderMap(url, headers)
                } else {
                    ApiService(baseUrl).iApiService.post(url)
                }
            }
        }
    }

    override fun downloadFile(
        url: String,
        savePath: String,
        callback: ICallback,
        downloadListener: DownloadListener?
    ) {
        GlobalScope.launch(Dispatchers.Main) Scope@{
            try {
                val splitUrlArr = UrlUtil.splitUrl(url)
                val response: Response<ResponseBody> = withContext(Dispatchers.IO) {
                    ApiService(splitUrlArr[0], downloadListener?.let {
                        object : ProgressResponseBody.ProgressListener {
                            override fun onProgress(totalSize: Long, downSize: Long) {
                                downloadListener.onProgress(totalSize, downSize)
                            }
                        }
                    }).iApiService.downloadFile(splitUrlArr[1])
                }
                val responseString = processDownloadResponse(response, savePath)
                if (responseString.isEmpty() || responseString.isBlank()) {
                    throw IllegalAccessError()
                } else {
                    callback.onSucceed(responseString)
                }
            } catch (e: Exception) {
                callback.onFailed(e)
            }
        }
    }

    override fun uploadFile(
        url: String,
        uploadFileMap: Map<String, File>,
        params: Map<String, String>,
        callback: ICallback,
        uploadListener: UploadListener?
    ) {
        GlobalScope.launch(Dispatchers.Main) Scope@{
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            for (uploadFileEntry in uploadFileMap.entries) {
                val fileBody =
                    UploadProgressRequestBody(uploadFileEntry.value, "multipart/form-data", object :
                        UploadProgressRequestBody.UploadCallbacks {
                        override fun onProgressUpdate(totalSize: Long, uploadedSize: Long) {
                            uploadListener?.onProgress(totalSize, uploadedSize)
                        }
                    })
                builder.addFormDataPart(uploadFileEntry.key, uploadFileEntry.value.name, fileBody)
                for (paramsEntry in params.entries) {
                    builder.addFormDataPart(paramsEntry.key, paramsEntry.value)
                }
                val parts = builder.build().parts
                val splitUrlArr = UrlUtil.splitUrl(url)
                try {
                    val response: Response<ResponseBody> = withContext(Dispatchers.IO) {
                        ApiService(splitUrlArr[0]).iApiService.uploadFile(splitUrlArr[1], parts)
                    }
                    val responseString = processResponse(response)
                    callback.onSucceed(responseString)
                } catch (e: Exception) {
                    callback.onFailed(e)
                }
            }
        }
    }

    @Suppress("unused")
    private fun generatePostBody(params: Map<String, String>): RequestBody {
        val paramString = JSONObject(params).toString()
            .replace("\"{", "{")
            .replace("}\"", "}")
            .replace("\\\"", "\"")
            .replace("\"[", "[")
            .replace("]\"", "]")
        return paramString
            .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
    }

    private suspend fun processResponse(response: Response<ResponseBody>): String {
        return withContext(Dispatchers.IO) {
            response.body()?.string() ?: ""
        }
    }

    private suspend fun processDownloadResponse(
        response: Response<ResponseBody>,
        savePath: String
    ): String {
        return withContext(Dispatchers.IO) {
            val responseBody = response.body()
            if (responseBody != null) {
                val file = FileUtil.saveFile(savePath, responseBody)
                file?.path ?: ""
            } else {
                ""
            }
        }
    }
}