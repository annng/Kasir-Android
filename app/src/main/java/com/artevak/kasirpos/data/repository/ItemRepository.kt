package com.artevak.kasirpos.data.repository

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.shared.SharedPref
import com.artevak.kasirpos.data.service.ItemService
import com.artevak.kasirpos.response.firebase.ResponseProcess

class ItemRepository(
    private val sharedPref: SharedPref,
    private val services: ItemService
) {
    fun addItem(item: Barang, response: MutableLiveData<ResponseProcess<String>>) = services.addItem(sharedPref.getUsername(), item, response)

    fun getItems(response: MutableLiveData<ResponseProcess<ArrayList<Barang>?>>){
        services.getStock(sharedPref.getUsername(), response)
    }

}