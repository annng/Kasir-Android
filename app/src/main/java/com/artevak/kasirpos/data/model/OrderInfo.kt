package com.artevak.kasirpos.data.model

import com.google.gson.annotations.SerializedName

data class OrderInfo (
    @SerializedName("id_pelanggan") val id_pelanggan: String?,
    @SerializedName("pelanggan_type") val pelanggan_type: String?,
    @SerializedName("total_bayar") val total_bayar: Int?,
    @SerializedName("list_keranjang") val list_keranjang : ArrayList<Keranjang>,
)