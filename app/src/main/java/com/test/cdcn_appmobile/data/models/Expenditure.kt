package com.test.cdcn_appmobile.data.models

import com.google.gson.annotations.SerializedName

data class Expenditure(
    @SerializedName("id") val id: String,
    @SerializedName("cost") var cost: Long,
    @SerializedName("date") var date: String,
    @SerializedName("imageIcon") val imageIcon: String,
    @SerializedName("categoryName") var categoryName: String,
    @SerializedName("categoryId") var categoryId: String,
    @SerializedName("categoryType") var categoryType: Int,
    @SerializedName("categoryTypeName") var categoryTypeName: String,
    @SerializedName("note") var note: String
)
