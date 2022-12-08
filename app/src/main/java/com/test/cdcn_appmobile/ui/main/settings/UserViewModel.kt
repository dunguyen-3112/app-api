package com.test.cdcn_appmobile.ui.main.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cdcn_appmobile.data.models.User
import com.test.cdcn_appmobile.data.repository.UserRepository
import com.test.cdcn_appmobile.extension.PASSWORD
import com.test.cdcn_appmobile.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * Created by tuyen.dang on 12/4/2022
 */

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    companion object {
        private val user: MutableLiveData<User> = MutableLiveData()
    }

    init {
        user.value = Constant.USER
    }

    fun updateUser(
        authToken: String,
        user: User,
        onSuccess: (String?) -> Unit
    ) {
        val options: MutableMap<String, String> = HashMap()
        options["id"] = user.id
        options["name"] = user.name
        val token = Constant.USER.tokenJWT
        val pass = Constant.USER.password
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(
                authToken,
                user.id,
                options
            ).collect {
                if (it.isSuccessed) {
                    Constant.USER = it.resultObj ?: User("", "", "", "", "", "")
                    Constant.USER.tokenJWT = token
                    Constant.USER.password = pass
                }
                withContext(Dispatchers.Main) {
                    onSuccess(it.message)
                }
            }
        }
    }

    fun updatePass(
        authToken: String,
        idUser: String,
        newPass: String,
        onSuccess: (String?) -> Unit
    ) {
        val options: MutableMap<String, String> = HashMap()
        options["password"] = newPass
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updatePass(
                authToken,
                idUser,
                options
            ).collect {
                if (it.isSuccessed) {
                    Constant.USER.password = newPass
                }
                withContext(Dispatchers.Main) {
                    onSuccess(it.message)
                }
            }
        }
    }

    fun validPassWord(pass: String): String =
        if (PASSWORD.matches(pass)) "" else "Sai định dạng mật khẩu"
}
