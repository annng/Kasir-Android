package com.artevak.kasirpos.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pelanggan(
    val id: String? = "",
    val name: String? = "",
    val phone: String? = "",
    val alamat: String? = "",
) : Parcelable