package com.test.cdcn_appmobile.ui.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.data.repository.UserRepository
import com.test.cdcn_appmobile.ui.launch.LaunchViewModel

/*
 * Created by tuyen.dang on 12/4/2022
 */

class UserViewModelFactory (private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    private val illegalArgumentException = "Unknown ViewModel class"
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException(illegalArgumentException)
    }
}
