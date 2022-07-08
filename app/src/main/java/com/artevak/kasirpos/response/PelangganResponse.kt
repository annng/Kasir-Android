package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.model.Pelanggan

data class PelangganResponse(
    @SerializedName("current_page")
    val current_page: String,
    @SerializedName("data")
    val data_pelanggan : ArrayList<Pelanggan>

)