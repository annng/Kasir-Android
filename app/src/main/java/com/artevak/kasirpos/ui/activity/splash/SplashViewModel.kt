package com.artevak.kasirpos.ui.activity.splash

import com.artevak.kasirpos.base.BaseViewModel

class SplashViewModel(private val useCase : SplashUseCase) : BaseViewModel() {

    fun getUsername() = useCase.getUsername()
}