package com.test.cdcn_appmobile.ui.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cdcn_appmobile.data.models.User
import com.test.cdcn_appmobile.data.repository.UserRepository
import com.test.cdcn_appmobile.extension.EMAIL_ADDRESS
import com.test.cdcn_appmobile.extension.PASSWORD
import com.test.cdcn_appmobile.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * Created by tuyenpc on 10/9/2022
 */

class LaunchViewModel(private val userRepository: UserRepository) : ViewModel() {

    companion object {

    }

    fun loginUser(
        email: String,
        pass: String,
        onStart: () -> Unit,
        onResult: (Boolean, String) -> Unit,
    ) {
        onStart()
        viewModelScope.launch(Dispatchers.IO) {
            val res = userRepository.login(email, pass)
            res.collect {
                if (it.isSuccessed) {
                    Constant.USER = it.resultObj ?: User("", "", "", "", "", "")
                    Constant.USER.password = pass
                }
                withContext(Dispatchers.Main) {
                    onResult(it.isSuccessed, it.message ?: "")
                }
            }
        }
    }

    fun register(users: User, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = userRepository.register(users.email, users.password, users.name)
            res.collect {
                withContext(Dispatchers.Main) {
                    onResult(it.isSuccessed, it.message ?: "")
                }
            }
        }
    }

    fun validEmail(mail: String): String =
        if (EMAIL_ADDRESS.matches(mail)) "" else "Sai định dạng Email"

    fun validPassWord(pass: String): String =
        if (PASSWORD.matches(pass)) "" else "Sai định dạng mật khẩu"
}
