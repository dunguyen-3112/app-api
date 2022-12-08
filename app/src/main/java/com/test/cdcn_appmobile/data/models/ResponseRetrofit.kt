package com.test.cdcn_appmobile.data.models

import com.google.gson.annotations.SerializedName

/*
 * Created by tuyen.dang on 11/9/2022
 */
class ResponseRetrofit<T>(
    @SerializedName("isSuccessed") var isSuccessed: Boolean,
    @SerializedName("message") var message: String?,
    @SerializedName("resultObj") var resultObj: T,
)