package com.tapisdev.penjualankasir.response

import com.google.gson.annotations.SerializedName
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.model.Transaksi

data class UntungResponse(
    @SerializedName("current_page")
    val current_page: String,
    @SerializedName("data")
    val data_untung : ArrayList<Transaksi>

)