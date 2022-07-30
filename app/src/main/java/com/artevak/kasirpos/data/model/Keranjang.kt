package com.artevak.kasirpos.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Keranjang(
    var nama_barang: String? = "",
    var jumlah_beli: Long? = 0,
    var harga_barang: Long? = 0,
    var deskripsi: String? = "",
    var picture: String? = "",
    var satuan: String? = "",
    var subtotal: Long? = 0,
    var id_barang: String? = "",
    var untung: Long? = 0,
) : Parcelable