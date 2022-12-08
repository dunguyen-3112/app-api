package com.test.cdcn_appmobile.data.repository

import com.test.cdcn_appmobile.data.models.ResponseRetrofit
import com.test.cdcn_appmobile.data.models.User
import com.test.cdcn_appmobile.extension.errorTimeoutFlow
import com.test.cdcn_appmobile.extension.flowWithCatch
import com.test.cdcn_appmobile.utils.Constant
import kotlinx.coroutines.flow.Flow


/*
 * Created by tuyen.dang on 11/27/2022
 */

object UserRepository {
    suspend fun login(email: String, pass: String): Flow<ResponseRetrofit<out User?>> {
        return try {
            val options: MutableMap<String, String> = HashMap()
            options["userName"] = email
            options["password"] = pass
            val res = Constant.getRetrofit().login(options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    suspend fun register(
        email: String,
        pass: String,
        name: String,
    ): Flow<ResponseRetrofit<out Any?>> {
        return try {
            val options: MutableMap<String, String> = HashMap()
            options["userName"] = email
            options["email"] = email
            options["password"] = pass
            options["name"] = name
            val res = Constant.getRetrofit().register(options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    suspend fun updateUser(
        authToken: String,
        idUser: String,
        options: Map<String, String>,
    ): Flow<ResponseRetrofit<out User?>> {
        return try {
            val res = Constant.getRetrofit().updateUser(authToken, idUser, options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    suspend fun updatePass(
        authToken: String,
        idUser: String,
        options: Map<String, String>
    ): Flow<ResponseRetrofit<out Any?>> {
        return try {
            val res = Constant.getRetrofit().updatePass(authToken, idUser, options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }
}
