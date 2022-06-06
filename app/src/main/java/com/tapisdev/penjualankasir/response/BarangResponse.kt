package com.tapisdev.penjualankasir.response

import com.google.gson.annotations.SerializedName
import com.tapisdev.penjualankasir.model.Barang

data class BarangResponse(
    @SerializedName("current_page")
    val current_page: String,
    @SerializedName("data")
    val data_barang : ArrayList<Barang>

)