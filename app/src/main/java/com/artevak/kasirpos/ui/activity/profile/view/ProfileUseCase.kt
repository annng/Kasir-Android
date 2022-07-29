package com.artevak.kasirpos.ui.activity.profile.view

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.data.repository.UserRepository
import com.artevak.kasirpos.response.firebase.ResponseData

class ProfileUseCase(private val userRepository: UserRepository) {
    fun getMyAccount(response : MutableLiveData<User>) = userRepository.getMyAccount(response)

    fun saveUsername(username : String, password : String) = userRepository.saveUsername(username, password)
}