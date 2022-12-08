package com.test.cdcn_appmobile.ui.main.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.data.repository.ExpenditureRepository

class TransactionsViewModelFactory(private val expenditureRepository: ExpenditureRepository) :
    ViewModelProvider.NewInstanceFactory() {
    private val illegalArgumentException = "Unknown ViewModel class"
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(expenditureRepository) as T
        }
        throw IllegalArgumentException(illegalArgumentException)
    }
}