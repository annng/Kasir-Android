package com.artevak.kasirpos.data.model

import com.google.gson.annotations.SerializedName

data class HutangInfo (
    @SerializedName("id_pelanggan") val id_pelanggan: String?,
    @SerializedName("pelanggan_type") val pelanggan_type: String?,
    @SerializedName("hutang") val hutang: Int?,
    @SerializedName("hutang_type") val hutang_type: String?,
    @SerializedName("deskripsi") val deskripsi: String?,
)