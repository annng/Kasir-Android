package com.tapisdev.penjualankasir.response

import com.google.gson.annotations.SerializedName
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.model.HistoryTransaksi

data class HistoryTransaksiResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data_history : ArrayList<HistoryTransaksi>

)