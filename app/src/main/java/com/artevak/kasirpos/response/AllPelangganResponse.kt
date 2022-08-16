package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.data.model.Customer

data class AllPelangganResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data_customer : ArrayList<Customer>

)