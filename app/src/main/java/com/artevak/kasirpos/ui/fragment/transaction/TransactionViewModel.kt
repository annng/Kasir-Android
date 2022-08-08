package com.artevak.kasirpos.ui.fragment.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class TransactionViewModel(private val useCase : TransactionUseCase) : BaseViewModel() {

    val _items = MutableLiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>>()
    val items : LiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>> = _items

    fun getItems(){
        useCase.getItems(_items)
    }
}