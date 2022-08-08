package com.artevak.kasirpos.ui.fragment.transaction

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.repository.ItemRepository
import com.artevak.kasirpos.data.repository.TransactionRepository
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class TransactionUseCase(private val transactionRepo : TransactionRepository,
                         private val itemRepo : ItemRepository) {

    fun getItems(response: MutableLiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>>) = itemRepo.getItems(response)
}