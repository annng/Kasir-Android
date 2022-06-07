package com.tapisdev.penjualankasir.response

import com.google.gson.annotations.SerializedName
import com.tapisdev.penjualankasir.model.Barang

data class AllBarangResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data_barang : ArrayList<Barang>

)