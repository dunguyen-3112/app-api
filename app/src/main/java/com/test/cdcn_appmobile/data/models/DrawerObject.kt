package com.test.cdcn_appmobile.data.models

import com.google.gson.annotations.SerializedName

data class DrawerObject(
    @SerializedName("spendMoney") var spendMoney: Long,
    @SerializedName("receiveMoney") var receivedMoney: Long,
    @SerializedName("time") var time: String
) {
    fun getMaxMoney(): Long = if (spendMoney > receivedMoney) spendMoney else receivedMoney
}
