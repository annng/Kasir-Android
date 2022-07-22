package com.artevak.kasirpos.ui.activity.auth.login

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.data.model.param.LoginParam
import com.artevak.kasirpos.data.repository.UserRepository
import com.artevak.kasirpos.response.firebase.ResponseProcess

class LoginUseCase (private val userRepository: UserRepository){
    fun login(loginParam: LoginParam, response : MutableLiveData<ResponseProcess<User?>>) = userRepository.login(loginParam, response)
    fun saveSession(username : String, password : String) = userRepository.saveUsername(username, password)
}