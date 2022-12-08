package com.test.cdcn_appmobile.data.repository

import com.test.cdcn_appmobile.data.models.Budget
import com.test.cdcn_appmobile.data.models.ResponseRetrofit
import com.test.cdcn_appmobile.extension.errorTimeoutFlow
import com.test.cdcn_appmobile.extension.flowWithCatch
import com.test.cdcn_appmobile.utils.Constant
import kotlinx.coroutines.flow.Flow

object BudgetRepository {
    suspend fun getBudget(authToken: String, idUser: String): Flow<ResponseRetrofit<out Budget?>> {
        return try {
            val res = Constant.getRetrofit().getBudget(authToken, idUser)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }

    suspend fun updateBudget(
        authToken: String,
        idUser: String,
        limitMoney: String,
        unitTime: Int,
    ): Flow<ResponseRetrofit<out Budget?>> {
        return try {
            val options: MutableMap<String, Long> = HashMap()
            options["limitMoney"] = limitMoney.toLong()
            options["unitTime"] = unitTime.toLong()
            val res = Constant.getRetrofit().updateBudget(authToken, idUser, options)
            flowWithCatch(res)
        } catch (e: Exception) {
            errorTimeoutFlow()
        }
    }
}
