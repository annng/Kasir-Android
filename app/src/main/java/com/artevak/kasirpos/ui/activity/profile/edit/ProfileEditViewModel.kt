package com.artevak.kasirpos.ui.activity.profile.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.User

class ProfileEditViewModel(private val useCase : ProfileEditUseCase) : BaseViewModel() {
    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    fun getMyAccount(){
        useCase.getAccount(_user)
    }

    fun getPassword() = useCase.getPassword()

    fun isNeedUpdatePassword(password : String, confirmPassword : String) : Boolean{
        val isDifferentPasswordExisting = password != useCase.getPassword()
        val isConfirmed = password == confirmPassword
        val isEmptyPassword = password.isEmpty() && confirmPassword.isEmpty()
        if (confirmPassword.isEmpty())
            return false

        if (isEmptyPassword){
            return false
        }

        if (!isDifferentPasswordExisting && isConfirmed){
            return false
        }

        return true
    }

    fun updateAccount(user : User){
        useCase.updateAccount(user)
        useCase.saveAccount(user.username, user.password)
    }

}