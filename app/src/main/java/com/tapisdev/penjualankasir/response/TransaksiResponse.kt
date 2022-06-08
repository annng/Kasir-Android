package com.tapisdev.penjualankasir.response

import com.google.gson.annotations.SerializedName
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.model.Keranjang
import com.tapisdev.penjualankasir.model.Pelanggan

data class TransaksiResponse(
    @SerializedName("current_page")
    val current_page: String,
    @SerializedName("data")
    val data_transaksi : ArrayList<Keranjang>

)