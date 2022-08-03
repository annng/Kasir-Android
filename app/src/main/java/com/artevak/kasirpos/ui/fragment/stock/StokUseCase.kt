package com.artevak.kasirpos.ui.fragment.stock

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.repository.ItemRepository
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class StokUseCase(val itemRepo : ItemRepository) {

    fun getItems(response: MutableLiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>>) = itemRepo.getItems(response)
}