package com.eks.netagent.processors.retrofit

import com.eks.netagent.core.*
import com.eks.netagent.processors.retrofit.http.ApiService
import com.eks.netagent.processors.retrofit.http.ApiServiceHelper
import com.eks.netagent.processors.retrofit.responsebody.DownloadProgressRequestBody
import com.eks.netagent.processors.retrofit.responsebody.ProgressResponseBody
import com.eks.netagent.utils.FileUtil
import com.eks.netagent.utils.UrlUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
                val responseString = processRequestResponse(response)
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

    private suspend fun processRequestResponse(response: Response<ResponseBody>): String {
        return withContext(Dispatchers.IO) {
            response.body()?.string() ?: ""
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
                        ProgressResponseBody.ProgressListener { totalSize, downSize ->
                            downloadListener.onProgress(totalSize, downSize)
                        }
                    }).iApiService.downloadFile(splitUrlArr[1])
                }
                val responseString = processDownloadResponse(response, savePath)
                callback.onSucceed(responseString)
            } catch (e: Exception) {
                callback.onFailed(e)
            }
        }
    }

    private suspend fun processDownloadResponse(
        response: Response<ResponseBody>,
        savePath: String
    ): String {
        return withContext(Dispatchers.IO) {
            val it = response.body()
            val file = FileUtil.saveFile(savePath, it)
            file.path
        }
    }

    override fun uploadFile(
        url: String,
        uploadFileMap: Map<String, File>,
        params: Map<String, String>,
        callback: ICallback,
        uploadListener: UploadListener?
    ) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (uploadFileEntry in uploadFileMap.entries) {
            val fileBody = DownloadProgressRequestBody(
                uploadFileEntry.value, "multipart/form-data"
            ) { totalSize, uploadedSize ->
                uploadListener?.onProgress(totalSize, uploadedSize)
            }
            builder.addFormDataPart(uploadFileEntry.key, uploadFileEntry.value.name, fileBody)
            for (paramsEntry in params.entries) {
                builder.addFormDataPart(paramsEntry.key, paramsEntry.value)
            }
            val parts = builder.build().parts
            val splitUrlArr = UrlUtil.splitUrl(url)
            ApiService(splitUrlArr[0]).iApiService.uploadFile(splitUrlArr[1], parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<ResponseBody>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        callback.onSucceed(t.body()?.string() ?: "")
                    }

                    override fun onError(e: Throwable) {
                        callback.onFailed(e)
                    }
                })
        }

    }

}