package com.artevak.kasirpos.di

import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.common.const.DBConst
import com.artevak.kasirpos.data.model.shared.SharedPref
import com.artevak.kasirpos.data.repository.ItemRepository
import com.artevak.kasirpos.data.repository.TransactionRepository
import com.artevak.kasirpos.data.repository.UserRepository
import com.artevak.kasirpos.data.service.AuthService
import com.artevak.kasirpos.data.service.ItemService
import com.artevak.kasirpos.data.service.TransactionService
import com.artevak.kasirpos.ui.activity.auth.login.LoginUseCase
import com.artevak.kasirpos.ui.activity.auth.login.LoginViewModel
import com.artevak.kasirpos.ui.activity.auth.register.RegisterUseCase
import com.artevak.kasirpos.ui.activity.auth.register.RegisterViewModel
import com.artevak.kasirpos.ui.activity.item.add.TambahBarangUseCase
import com.artevak.kasirpos.ui.activity.item.add.TambahBarangViewModel
import com.artevak.kasirpos.ui.activity.item.detail.DetailBarangUseCase
import com.artevak.kasirpos.ui.activity.item.detail.DetailBarangViewModel
import com.artevak.kasirpos.ui.activity.item.edit.UbahBarangUseCase
import com.artevak.kasirpos.ui.activity.item.edit.UbahBarangViewModel
import com.artevak.kasirpos.ui.activity.profile.edit.ProfileEditUseCase
import com.artevak.kasirpos.ui.activity.profile.edit.ProfileEditViewModel
import com.artevak.kasirpos.ui.activity.profile.view.ProfileUseCase
import com.artevak.kasirpos.ui.activity.profile.view.ProfileViewModel
import com.artevak.kasirpos.ui.activity.splash.SplashUseCase
import com.artevak.kasirpos.ui.activity.splash.SplashViewModel
import com.artevak.kasirpos.ui.activity.transaction.history.HistoryTransactionUseCase
import com.artevak.kasirpos.ui.activity.transaction.history.HistoryTransactionViewModel
import com.artevak.kasirpos.ui.fragment.stock.StokUseCase
import com.artevak.kasirpos.ui.fragment.stock.StokViewModel
import com.artevak.kasirpos.ui.fragment.transaction.TransactionUseCase
import com.artevak.kasirpos.ui.fragment.transaction.TransactionViewModel
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val singleton = module {
    single { SharedPref(get()) }
    single { FirebaseDatabase.getInstance(DBConst.DB.URL) }
}

val networkModule = module {
    single { AuthService(get(), get(), get()) }
    single { ItemService(get(), get(), get()) }
    single { TransactionService(get(), get(), get()) }
}

val dataSourceModule = module {
    single { UserRepository(get()) }
    single { ItemRepository(get()) }
    single { TransactionRepository(get()) }
}

val useCaseModule = module {
    single { RegisterUseCase(get()) }
    single { ProfileUseCase(get()) }
    single { ProfileEditUseCase(get()) }
    single { LoginUseCase(get()) }
    single { SplashUseCase(get()) }
    single { DetailBarangUseCase(get()) }
    single { TambahBarangUseCase(get()) }
    single { UbahBarangUseCase(get()) }
    single { StokUseCase(get()) }
    single { TransactionUseCase(get(), get()) }
    single { HistoryTransactionUseCase(get()) }
}

val viewModelModule = module {
    viewModel { BaseViewModel() }
    viewModel { RegisterViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProfileEditViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { DetailBarangViewModel(get()) }
    viewModel { TambahBarangViewModel(get()) }
    viewModel { UbahBarangViewModel(get()) }
    viewModel { StokViewModel(get()) }
    viewModel { TransactionViewModel(get()) }
    viewModel { HistoryTransactionViewModel(get()) }
}


val appComponent: List<Module> = listOf(
    singleton,
    dataSourceModule,
    networkModule,
    viewModelModule,
    useCaseModule,
)