package com.artevak.kasirpos.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class HomeViewModel(private val useCase: HomeUseCase) : BaseViewModel() {

    private val _customers = MutableLiveData<ResponseProcess<ArrayList<ResponseData<Customer>>?>>()
    val customers : LiveData<ResponseProcess<ArrayList<ResponseData<Customer>>?>> = _customers

    private val _items = MutableLiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>>()
    val items : LiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>> = _items


    fun getCustomers(){
        useCase.getCustomers(_customers)
    }

    fun getItem(){
        useCase.getItem(_items)
    }
}