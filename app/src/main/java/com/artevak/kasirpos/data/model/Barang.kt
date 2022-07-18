package com.artevak.kasirpos.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Barang(
    var id: String? = "",
    var name: String? = "",
    var harga_beli: Int? = 0,
    var harga_jual: Int? = 0,
    var stok: Int? = 0,
    var deskripsi: String? = "",
    var picture: String? = "",
    var satuan: String? = "",
) : Parcelable