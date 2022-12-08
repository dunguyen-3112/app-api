package com.test.cdcn_appmobile.data.models

import com.google.gson.annotations.SerializedName

/*
 * Created by tuyen.dang on 12/3/2022
 */

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("linkIcon") val linkIcon: String,
    @SerializedName("categoryType") val categoryType: Int,
    @SerializedName("typeOfCategoryName") val typeOfCategoryName: String,
)
