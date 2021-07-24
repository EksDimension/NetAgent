package com.eks.netagent.processors.retrofit.http

import com.eks.netagent.processors.retrofit.responsebody.ProgressResponseBody

/**
 * Created by Riggs on 9/10/2019
 */
class ApiService(baseUrl: String, progressListener: ProgressResponseBody.ProgressListener? = null) {
    var iApiService = ApiServiceHelper.getApiService(baseUrl, progressListener)
}