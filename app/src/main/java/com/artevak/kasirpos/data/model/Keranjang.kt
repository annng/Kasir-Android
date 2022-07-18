package com.artevak.kasirpos.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Keranjang(
    var nama_barang: String? = "",
    var jumlah_beli: Int? = 0,
    var harga_barang: Int? = 0,
    var deskripsi: String? = "",
    var picture: String? = "",
    var satuan: String? = "",
    var subtotal: Int? = 0,
    var id_barang: String? = "",
    var untung: Int? = 0,
) : Parcelable