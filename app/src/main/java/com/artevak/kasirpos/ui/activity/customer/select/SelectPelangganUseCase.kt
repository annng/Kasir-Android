package com.artevak.kasirpos.ui.activity.customer.select

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.data.repository.CustomerRepository
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class SelectPelangganUseCase(private val repo : CustomerRepository) {
    fun getCustomers(response: MutableLiveData<ResponseProcess<ArrayList<ResponseData<Customer>>?>>) = repo.getCustomers(response)
}