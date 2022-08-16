package com.artevak.kasirpos.ui.activity.customer.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class SelectPelangganViewModel(private val useCase: SelectPelangganUseCase) : BaseViewModel() {

    val _items = MutableLiveData<ResponseProcess<ArrayList<ResponseData<Customer>>?>>()
    val items : LiveData<ResponseProcess<ArrayList<ResponseData<Customer>>?>> = _items

    fun getCustomers(){
        useCase.getCustomers(_items)
    }
}