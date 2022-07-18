package com.artevak.kasirpos.data.model

import com.google.gson.annotations.SerializedName

data class RegisterInfo (
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("nama_umkm") val nama_umkm: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("alamat") val alamat: String?
)