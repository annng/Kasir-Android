package com.artevak.kasirpos.ui.fragment.stock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.response.firebase.ResponseProcess

class StokViewModel(val useCase : StokUseCase) : BaseViewModel() {
    val _items = MutableLiveData<ResponseProcess<ArrayList<Barang>?>>()
    val items : LiveData<ResponseProcess<ArrayList<Barang>?>> = _items

    fun getItems(){
        useCase.getItems(_items)
    }
}