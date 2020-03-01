package com.eks.netagent.processors

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.eks.netagent.core.ICallback
import com.eks.netagent.core.INetProcessor

/**
 * Volley请求框架的具体处理类
 * Created by Riggs on 2020/3/1
 */
class VolleyProcessor(context: Context) : INetProcessor {
    companion object {
        var mQueue: RequestQueue? = null
    }

    init {
        mQueue = Volley.newRequestQueue(context)
    }

    override fun post(url: String, params: Map<String, Any>, callback: ICallback) {
        val stringRequest = StringRequest(Request.Method.POST, url, Response.Listener<String> {
            callback.onSucceed(it)
        }, Response.ErrorListener {
            callback.onFailed(it.message.toString())
        })
        mQueue?.add(stringRequest)
    }

    override fun get(url: String, params: Map<String, Any>, callback: ICallback) {
    }

}