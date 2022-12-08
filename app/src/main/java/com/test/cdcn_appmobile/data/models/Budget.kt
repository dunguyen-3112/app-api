package com.test.cdcn_appmobile.data.models

import com.google.gson.annotations.SerializedName

data class Budget(
    @SerializedName("id") val id: String,
    @SerializedName("limitMoney") val limitedMoney: Long,
    @SerializedName("spendMoney") val spendMoney: Long,
    @SerializedName("remainMoney") val remainMoney: Long,
    @SerializedName("unitTime") val unitTime: Int
)
