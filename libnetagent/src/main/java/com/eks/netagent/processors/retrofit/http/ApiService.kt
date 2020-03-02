package com.eks.netagent.processors.retrofit.http

/**
 * Created by Riggs on 9/10/2019
 */
class ApiService(baseUrl: String) {

    var iApiService: IApiService =
            ApiServiceHelper.getApiService(baseUrl)
}