package com.eks.netagent.processors.volley

/**
 * Volley Processor , deprecated now
 * Created by Riggs on 2020/3/1
 */
/*
class VolleyProcessor(context: Context) : INetProcessor {
    override fun uploadFile(baseUrl: String, url: String, uploadFileMap: Map<String, File>, params: Map<String, String>) {
    }

    override fun setHeaders(headers: HashMap<String, String>) {
    }

    override fun addHeader(key: String, value: String) {
    }

    override fun removeHeader(key: String) {
    }


    companion object {
        var mQueue: RequestQueue? = null
    }

    init {
        mQueue = Volley.newRequestQueue(context)
    }

    override fun post(baseUrl: String, url: String, params: Map<String, Any>, callback: ICallback) {
        val finalUrl = UrlUtil.appendParams(baseUrl, url, params)
        val stringRequest = StringRequest(Request.Method.POST, finalUrl, Response.Listener<String> {
            callback.onSucceed(it)
        }, Response.ErrorListener {
            callback.onFailed(it.message.toString())
        })
        mQueue?.add(stringRequest)
    }

    override fun get(baseUrl: String, url: String, params: Map<String, Any>, callback: ICallback) {
        val finalUrl = UrlUtil.appendParams(baseUrl, url, params)
        val stringRequest = StringRequest(Request.Method.GET, finalUrl, Response.Listener<String> {
            callback.onSucceed(it)
        }, Response.ErrorListener {
            callback.onFailed(it.message.toString())
        })
        mQueue?.add(stringRequest)
    }

    override fun downloadFile(url: String, savePath: String, downloadListener: DownloadListener?) {
    }

}*/
