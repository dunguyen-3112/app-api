package com.test.cdcn_appmobile.ui.main.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.data.repository.BudgetRepository

class BudgetViewModelFactory(private val budgetRepository: BudgetRepository) :
    ViewModelProvider.NewInstanceFactory() {
    private val illegalArgumentException = "Unknown ViewModel class"
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(budgetRepository) as T
        }
        throw IllegalArgumentException(illegalArgumentException)
    }
}