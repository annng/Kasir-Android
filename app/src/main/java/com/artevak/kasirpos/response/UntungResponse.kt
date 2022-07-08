package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.model.Transaksi

data class UntungResponse(
    @SerializedName("current_page")
    val current_page: String,
    @SerializedName("data")
    val data_untung : ArrayList<Transaksi>

)