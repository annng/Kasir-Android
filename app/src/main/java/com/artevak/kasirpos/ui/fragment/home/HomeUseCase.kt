package com.artevak.kasirpos.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.data.repository.CustomerRepository
import com.artevak.kasirpos.data.repository.ItemRepository
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class HomeUseCase(private val customerRepo : CustomerRepository, private val itemRepo : ItemRepository) {
    fun getCustomers(response: MutableLiveData<ResponseProcess<ArrayList<ResponseData<Customer>>?>>) = customerRepo.getCustomers(response)
    fun getItem(response: MutableLiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>>) = itemRepo.getItems(response)
}