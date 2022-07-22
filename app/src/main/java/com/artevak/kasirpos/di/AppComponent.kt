package com.artevak.kasirpos.di

import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.common.const.DBConst
import com.artevak.kasirpos.data.model.shared.SharedPref
import com.artevak.kasirpos.data.repository.UserRepository
import com.artevak.kasirpos.data.service.AuthService
import com.artevak.kasirpos.ui.activity.auth.login.LoginUseCase
import com.artevak.kasirpos.ui.activity.auth.login.LoginViewModel
import com.artevak.kasirpos.ui.activity.auth.register.RegisterUseCase
import com.artevak.kasirpos.ui.activity.auth.register.RegisterViewModel
import com.artevak.kasirpos.ui.activity.profile.edit.ProfileEditUseCase
import com.artevak.kasirpos.ui.activity.profile.edit.ProfileEditViewModel
import com.artevak.kasirpos.ui.activity.profile.view.ProfileUseCase
import com.artevak.kasirpos.ui.activity.profile.view.ProfileViewModel
import com.artevak.kasirpos.ui.activity.splash.SplashUseCase
import com.artevak.kasirpos.ui.activity.splash.SplashViewModel
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val singleton = module {
    single { SharedPref(get()) }
    single { FirebaseDatabase.getInstance(DBConst.DB.URL) }
}

val networkModule = module {
    single { AuthService(get(), get()) }
}

val dataSourceModule = module {
    single { UserRepository(get(), get()) }
}

val useCaseModule = module {
    single { RegisterUseCase(get()) }
    single { ProfileUseCase(get()) }
    single { ProfileEditUseCase(get()) }
    single { LoginUseCase(get()) }
    single { SplashUseCase(get()) }
}

val viewModelModule = module {
    viewModel { BaseViewModel() }
    viewModel { RegisterViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProfileEditViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SplashViewModel(get()) }
}


val appComponent: List<Module> = listOf(
    singleton,
    dataSourceModule,
    networkModule,
    viewModelModule,
    useCaseModule,
)