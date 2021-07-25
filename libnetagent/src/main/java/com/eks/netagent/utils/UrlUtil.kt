package com.eks.netagent.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * Created by Riggs on 2020/3/3
 */
class UrlUtil {
    companion object {
        @Suppress("unused")
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

        /**
         * 分割URL为数组,第一位为baseUrl,第二位为剩余Url
         */
        fun splitUrl(url: String): Array<String> {
            val baseUrlBuilder = StringBuilder()
            var replaced = ""
            if (url.contains("http://")) {
                baseUrlBuilder.append("http://")
                replaced = url.replace("http://", "")

            } else if (url.contains("https://")) {
                baseUrlBuilder.append("https://")
                replaced = url.replace("https://", "")
            }
            val baseUrl = baseUrlBuilder.append(replaced.substring(0, replaced.indexOf("/"))).toString()
            val restUrl = replaced.substring(replaced.indexOf("/"))
            return arrayOf(baseUrl, restUrl)
        }
    }
}