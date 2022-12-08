package com.test.cdcn_appmobile.data.repository

import com.test.cdcn_appmobile.data.models.DrawerObject
import com.test.cdcn_appmobile.data.models.ResponseRetrofit
import com.test.cdcn_appmobile.extension.errorTimeoutFlow
import com.test.cdcn_appmobile.extension.flowWithCatch
import com.test.cdcn_appmobile.utils.Constant
import kotlinx.coroutines.flow.Flow

/*
 * Created by tuyen.dang on 12/4/2022
 */

object DrawerRepository {
    suspend fun getDrawObjectByYear(
        authToken: String,
        options: Map<String, String>
    ): Flow<ResponseRetrofit<out ArrayList<DrawerObject>?>> {
        return try {
            val res = Constant.getRetrofit().getDrawObjectByYear(authToken, options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    suspend fun getDrawObjectByMonth(
        authToken: String,
        options: Map<String, String>
    ): Flow<ResponseRetrofit<out ArrayList<DrawerObject>?>> {
        return try {
            val res = Constant.getRetrofit().getDrawObjectByMonth(authToken, options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }
}