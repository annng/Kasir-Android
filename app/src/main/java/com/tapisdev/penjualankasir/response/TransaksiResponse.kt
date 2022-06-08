package com.tapisdev.penjualankasir.response

import com.google.gson.annotations.SerializedName
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.model.Keranjang
import com.tapisdev.penjualankasir.model.Pelanggan

data class TransaksiResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("http_status")
    val http_status: String,
    /*@SerializedName("data")
    val data_transaksi : ArrayList<Keranjang>*/

)