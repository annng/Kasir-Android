package com.artevak.kasirpos.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    val name: String? = "",
    val phone: String? = "",
    val alamat: String? = "",
) : Parcelable