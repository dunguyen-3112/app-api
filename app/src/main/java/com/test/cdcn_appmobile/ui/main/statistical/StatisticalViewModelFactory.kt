package com.test.cdcn_appmobile.ui.main.statistical

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.data.repository.DrawerRepository

/*
 * Created by tuyen.dang on 12/4/2022
 */

class StatisticalViewModelFactory(private val drawerRepository: DrawerRepository) :
    ViewModelProvider.NewInstanceFactory() {
    private val illegalArgumentException = "Unknown ViewModel class"
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticalViewModel(drawerRepository) as T
        }
        throw IllegalArgumentException(illegalArgumentException)
    }
}