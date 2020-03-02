package com.eks.netagent.core

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * Created by Riggs on 2020/3/3
 */
class UrlUtil {
    companion object {
        fun appendParams(baseUrl: String, url: String, params: Map<String, Any>?): String {
            if (params == null || params.isEmpty()) {
                return url
            }
            val urlBuilder = StringBuilder(baseUrl)
            urlBuilder.append(url)
            if (urlBuilder.indexOf("?") <= 0) {
                urlBuilder.append("?")
            } else {
                if (!urlBuilder.toString().endsWith("?")) {
                    urlBuilder.append("&")
                }
            }
            for ((key, value) in params) {
                urlBuilder.append("&$key")
                        .append("=")
                        .append(encode(value.toString()))
            }
            return urlBuilder.toString()
        }

        private fun encode(str: String): String {
            try {
                return URLEncoder.encode(str, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                throw RuntimeException()
            }

        }
    }
}