package com.artevak.kasirpos.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Barang(
    var name: String? = "",
    var harga_beli: Long? = 0,
    var harga_jual: Long? = 0,
    var stok: Int? = 0,
    var deskripsi: String? = "",
    var picture: String? = "",
    var satuan: String? = "",
) : Parcelable