package com.artevak.kasirpos.data.repository

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.shared.SharedPref
import com.artevak.kasirpos.data.service.ItemService
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class ItemRepository(
    private val services: ItemService
) {
    fun addItem(item: Barang, response: MutableLiveData<ResponseProcess<String>>) = services.addItem(item, response)

    fun updateItem(key : String, item : Barang) = services.updateItem(item, key)
    fun getItems(response: MutableLiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>>){
        services.getStock(response)
    }

    fun deleteItem(key : String) = services.deleteItem(key)

}