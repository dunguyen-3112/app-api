package com.test.cdcn_appmobile.utils

import com.google.gson.GsonBuilder
import com.test.cdcn_appmobile.data.models.User
import com.test.cdcn_appmobile.data.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Constant {
    var USER = User("", "", "", "", "", "")

    private var gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    const val baseURL = "https://9150-2a09-bac1-7ac0-30-00-247-5f.ap.ngrok.io"
    private val builder = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create(gson))

    private val retrofit = builder.build()

    fun getRetrofit() = retrofit.create(ApiService::class.java)
}
