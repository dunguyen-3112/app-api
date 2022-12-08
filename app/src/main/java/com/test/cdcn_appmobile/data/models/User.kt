package com.test.cdcn_appmobile.data.models

import com.google.gson.annotations.SerializedName


/*
 * Created by tuyen.dang on 11/27/2022
 */

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("token") var tokenJWT: String,
)
