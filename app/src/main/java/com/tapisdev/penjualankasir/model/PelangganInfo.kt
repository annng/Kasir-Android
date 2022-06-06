package com.tapisdev.penjualankasir.model

import com.google.gson.annotations.SerializedName

data class PelangganInfo (
    @SerializedName("name") val name: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("alamat") val alamat: String?
)