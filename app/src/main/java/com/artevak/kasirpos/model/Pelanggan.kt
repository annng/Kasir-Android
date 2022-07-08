package com.artevak.kasirpos.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pelanggan(
    var id: String? = "",
    var name: String? = "",
    var phone: String? = "",
    var alamat: String? = "",
) : Parcelable