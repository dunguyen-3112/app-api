package com.test.cdcn_appmobile.data.service

import com.test.cdcn_appmobile.data.models.*
import retrofit2.Response
import retrofit2.http.*

/*
 * Created by tuyen.dang on 11/27/2022
 */

interface ApiService {
    @POST("/api/Account/login")
    suspend fun login(@Body options: Map<String, String>): Response<ResponseRetrofit<User?>>


    @POST("/api/Account/register")
    suspend fun register(@Body options: Map<String, String>): Response<ResponseRetrofit<Any?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @GET("/api/Budget/{id}")
    suspend fun getBudget(
        @Header("authorization") token: String,
        @Path("id") idUser: String
    ): Response<ResponseRetrofit<Budget?>>


    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @PUT("/api/Budget/update/{id}")
    suspend fun updateBudget(
        @Header("authorization") token: String,
        @Path("id") idUser: String,
        @Body options: Map<String, Long>
    ): Response<ResponseRetrofit<Budget?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @GET("/api/Expenditure/")
    suspend fun getExpenditure(
        @Header("authorization") token: String,
        @QueryMap options: Map<String, String>
    ): Response<ResponseRetrofit<ArrayList<Expenditure>?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @GET("/api/Category/get-cate-{type}")
    suspend fun getCategory(
        @Header("authorization") token: String,
        @Path("type") idType: Int
    ): Response<ResponseRetrofit<ArrayList<Category>?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @POST("/api/Expenditure")
    suspend fun addExpenditure(
        @Header("authorization") token: String,
        @Body options: Map<String, String>
    ): Response<ResponseRetrofit<Any?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @PUT("/api/Expenditure/{id}")
    suspend fun updateExpenditure(
        @Header("authorization") token: String,
        @Path("id") idExpenditure: String,
        @Body options: Map<String, String>
    ): Response<ResponseRetrofit<Any?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @DELETE("/api/Expenditure/{id}")
    suspend fun deleteExpenditure(
        @Header("authorization") token: String,
        @Path("id") idExpenditure: String
    ): Response<ResponseRetrofit<Any?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @PUT("/api/Account/{id}")
    suspend fun updateUser(
        @Header("authorization") token: String,
        @Path("id") idUser: String,
        @Body options: Map<String, String>
    ): Response<ResponseRetrofit<User?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @PUT("/api/Account/change-password/{id}")
    suspend fun updatePass(
        @Header("authorization") token: String,
        @Path("id") idUser: String,
        @Body options: Map<String, String>
    ): Response<ResponseRetrofit<Any?>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @GET("/api/Statistic/year")
    suspend fun getDrawObjectByYear(
        @Header("authorization") token: String,
        @QueryMap options: Map<String, String>
    ): Response<ResponseRetrofit<ArrayList<DrawerObject>?>>


    @Headers(
        "Accept: application/json",
        "Content-Type: application/json; charset=utf-8",
    )
    @GET("/api/Statistic/month")
    suspend fun getDrawObjectByMonth(
        @Header("authorization") token: String,
        @QueryMap options: Map<String, String>
    ): Response<ResponseRetrofit<ArrayList<DrawerObject>?>>
}
