package com.artevak.kasirpos.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
    var item_name: String? = "",
    var profit: Int? = 0,
    var subtotal: Int? = 0,
    var picture: String? = "",
    var unit: String? = "",
    var id_barang: String? = "",
) : Parcelable