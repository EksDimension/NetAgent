package com.eks.netagent.processors.retrofit

import com.eks.netagent.core.DownloadListener
import com.eks.netagent.core.ICallback
import com.eks.netagent.core.INetProcessor
import com.eks.netagent.core.UploadListener
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
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

/**
 * Created by Riggs on 2020/3/2
 */
class RetrofitProcessor : INetProcessor {

    override fun post(url: String, params: Map<String, Any>, callback: ICallback) {
        val splitUrlArr = UrlUtil.splitUrl(url)
        ApiService(splitUrlArr[0]).iApiService.post(splitUrlArr[1], params as HashMap<String, Any>)
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

    override fun get(url: String, params: Map<String, String>, callback: ICallback) {
        val splitUrlArr = UrlUtil.splitUrl(url)
        ApiService(splitUrlArr[0]).iApiService.get(splitUrlArr[1], params)
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

    override fun get(
        url: String,
        params: Map<String, String>?,
        headers: Map<String, String>?,
        callback: ICallback
    ) {
        val splitUrlArr = UrlUtil.splitUrl(url)
        if (params != null && headers != null) {
            ApiService(splitUrlArr[0]).iApiService.get(
                splitUrlArr[1],
                params,
                headers
            )
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

    override fun downloadFile(
        url: String,
        savePath: String,
        callback: ICallback,
        downloadListener: DownloadListener?
    ) {
        val splitUrlArr = UrlUtil.splitUrl(url)
        ApiService(splitUrlArr[0], downloadListener?.let {
            ProgressResponseBody.ProgressListener { totalSize, downSize ->
                downloadListener.onProgress(totalSize, downSize)
            }
        }).iApiService.downloadFile(splitUrlArr[1])
            .map {
                val file = FileUtil.saveFile(savePath, it)
                file.path
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: String) {
                    callback.onSucceed(t)
                }

                override fun onError(e: Throwable) {
                    callback.onFailed(e)
                }
            })
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
//            val fileBody = uploadFileEntry.value.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val fileBody = DownloadProgressRequestBody(uploadFileEntry.value, "multipart/form-data",
                DownloadProgressRequestBody.UploadCallbacks { totalSize, uploadedSize ->
                    uploadListener?.onProgress(totalSize, uploadedSize)
                })
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

    override fun setHeaders(headers: HashMap<String, String>) {
        ApiServiceHelper.headers = headers
    }

    override fun addHeader(key: String, value: String) {
        ApiServiceHelper.headers[key] = value
    }

    override fun removeHeader(key: String) {
        ApiServiceHelper.headers.remove(key)
    }

}