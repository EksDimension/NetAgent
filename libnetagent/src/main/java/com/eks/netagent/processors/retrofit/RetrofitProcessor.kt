package com.eks.netagent.processors.retrofit

import com.eks.netagent.core.DownloadListener
import com.eks.netagent.core.ICallback
import com.eks.netagent.core.INetProcessor
import com.eks.netagent.processors.retrofit.http.ApiService
import com.eks.netagent.processors.retrofit.http.ApiServiceHelper
import com.eks.netagent.processors.retrofit.responsebody.ProgressResponseBody
import com.eks.netagent.utils.FileUtil
import com.eks.netagent.utils.UrlUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Created by Riggs on 2020/3/2
 */
class RetrofitProcessor : INetProcessor {

    override fun post(baseUrl: String, url: String, params: Map<String, Any>, callback: ICallback) {
        ApiService(baseUrl).iApiService.post(url, params as HashMap<String, Any>)
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
                        callback.onFailed(e.message.toString())
                    }
                })
    }

    override fun get(baseUrl: String, url: String, params: Map<String, Any>, callback: ICallback) {
        ApiService(baseUrl).iApiService.get(url, params as HashMap<String, Any>)
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
                        callback.onFailed(e.message.toString())
                    }
                })
    }

    override fun downloadFile(url: String, savePath: String, downloadListener: DownloadListener?) {
        val splitUrlArr = UrlUtil.splitUrl(url)
        ApiService(splitUrlArr[0], downloadListener?.let {
            ProgressResponseBody.ProgressListener { totalSize, downSize ->
                //借助rxandroid封装的
                AndroidSchedulers.mainThread().scheduleDirect {
                    downloadListener.onProgress(totalSize, downSize)
                }
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
                        downloadListener?.onDownloadSucceed(t)
                    }

                    override fun onError(e: Throwable) {
                        downloadListener?.onDownloadFailed(e.message ?: "")
                    }
                })
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