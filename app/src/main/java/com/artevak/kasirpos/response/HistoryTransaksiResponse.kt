package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.model.HistoryTransaksi

data class HistoryTransaksiResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data_history : ArrayList<HistoryTransaksi>

)