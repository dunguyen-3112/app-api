package com.test.cdcn_appmobile.ui.main.transactions.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.data.repository.ExpenditureRepository

/*
 * Created by tuyen.dang on 12/3/2022
 */

class ExDetailViewModelFactory(private val expenditureRepository: ExpenditureRepository) :
    ViewModelProvider.NewInstanceFactory() {
    private val illegalArgumentException = "Unknown ViewModel class"
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExDetailViewModel(expenditureRepository) as T
        }
        throw IllegalArgumentException(illegalArgumentException)
    }
}