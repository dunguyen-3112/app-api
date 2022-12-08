package com.test.cdcn_appmobile.ui.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.data.repository.UserRepository

/*
 * Created by tuyen.dang on 10/9/2022
 */

class LaunchViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    private val illegalArgumentException = "Unknown ViewModel class"
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaunchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LaunchViewModel(userRepository) as T
        }
        throw IllegalArgumentException(illegalArgumentException)
    }
}
