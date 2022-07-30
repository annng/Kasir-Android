package com.artevak.kasirpos.ui.activity.item.add

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.repository.ItemRepository
import com.artevak.kasirpos.response.firebase.ResponseProcess

class TambahBarangUseCase(val repo : ItemRepository) {

    fun addItem(item: Barang, response: MutableLiveData<ResponseProcess<String>>) = repo.addItem(item, response)
}