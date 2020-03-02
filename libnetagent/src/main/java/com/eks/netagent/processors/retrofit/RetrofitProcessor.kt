package com.eks.netagent.processors.retrofit

import com.eks.netagent.core.ICallback
import com.eks.netagent.core.INetProcessor
import com.eks.netagent.processors.retrofit.http.ApiService
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
}