package com.artevak.kasirpos.ui.activity.customer.add

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.data.repository.CustomerRepository
import com.artevak.kasirpos.response.firebase.ResponseProcess

class TambahPelangganUseCase(private val repo: CustomerRepository) {
    fun addCustomer(item: Customer, response: MutableLiveData<ResponseProcess<String>>) =
        repo.addCustomer(item, response)
}