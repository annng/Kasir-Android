package com.artevak.kasirpos.data.repository

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.data.service.CustomerService
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class CustomerRepository(private val services: CustomerService) {
    fun addCustomer(item: Customer, response: MutableLiveData<ResponseProcess<String>>) =
        services.addCustomer(item, response)

    fun updateCustomer(key: String, item: Customer) = services.updateCustomer(item, key)
    fun getCustomers(response: MutableLiveData<ResponseProcess<ArrayList<ResponseData<Customer>>?>>) {
        services.getCustomers(response)
    }

    fun deleteCustomer(key: String) = services.deleteCustomer(key)
}