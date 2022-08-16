package com.artevak.kasirpos.ui.activity.customer.add

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.response.firebase.ResponseProcess

class TambahPelangganViewModel(private val useCase: TambahPelangganUseCase) : BaseViewModel() {
    private val _addCustomer = MutableLiveData<ResponseProcess<String>>()
    val addCustomer = _addCustomer

    fun addCustomer(item : Customer){
        useCase.addCustomer(item, addCustomer)
    }
}