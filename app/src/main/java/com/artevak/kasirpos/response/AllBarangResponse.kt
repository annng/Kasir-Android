package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.data.model.Barang

data class AllBarangResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data_barang : ArrayList<Barang>

)