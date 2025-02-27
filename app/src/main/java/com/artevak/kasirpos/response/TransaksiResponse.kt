package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.model.Keranjang

data class TransaksiResponse(
    @SerializedName("current_page")
    val current_page: String,
    @SerializedName("data")
    val data_transaksi : ArrayList<Keranjang>

)