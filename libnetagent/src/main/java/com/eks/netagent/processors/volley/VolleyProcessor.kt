package com.eks.netagent.processors.volley

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.eks.netagent.core.ICallback
import com.eks.netagent.core.INetProcessor
import com.eks.netagent.core.UrlUtil

/**
 * Volley请求框架的具体处理类
 * Created by Riggs on 2020/3/1
 */
class VolleyProcessor(context: Context) : INetProcessor {
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

}