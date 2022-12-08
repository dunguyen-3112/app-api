package com.test.cdcn_appmobile.data.repository

import com.test.cdcn_appmobile.data.models.Category
import com.test.cdcn_appmobile.data.models.Expenditure
import com.test.cdcn_appmobile.data.models.ResponseRetrofit
import com.test.cdcn_appmobile.extension.errorTimeoutFlow
import com.test.cdcn_appmobile.extension.flowWithCatch
import com.test.cdcn_appmobile.utils.Constant
import kotlinx.coroutines.flow.Flow

object ExpenditureRepository {
    internal suspend fun getExpenditure(
        authToken: String,
        idUser: String,
        date: String,
    ): Flow<ResponseRetrofit<out ArrayList<Expenditure>?>> {
        return try {
            val options: MutableMap<String, String> = HashMap()
            options["date"] = date
            options["id"] = idUser
            val res = Constant.getRetrofit().getExpenditure(authToken, options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    internal suspend fun getCategory(
        authToken: String,
        idType: Int,
    ): Flow<ResponseRetrofit<out ArrayList<Category>?>> {
        return try {
            val res = Constant.getRetrofit().getCategory(authToken, idType)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    internal suspend fun addExpenditure(
        authToken: String,
        options: Map<String, String>,
    ): Flow<ResponseRetrofit<out Any?>> {
        return try {
            val res = Constant.getRetrofit().addExpenditure(authToken, options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    internal suspend fun updateExpenditure(
        authToken: String,
        idExpenditure: String,
        options: Map<String, String>,
    ): Flow<ResponseRetrofit<out Any?>> {
        return try {
            val res = Constant.getRetrofit().updateExpenditure(authToken, idExpenditure, options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    internal suspend fun delExpenditure(
        authToken: String,
        idExpenditure: String,
    ): Flow<ResponseRetrofit<out Any?>> {
        return try {
            val res = Constant.getRetrofit().deleteExpenditure(authToken, idExpenditure)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }
}