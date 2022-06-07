package com.tapisdev.penjualankasir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Keranjang(
    var nama_barang: String? = "",
    var jumlah_beli: Int? = 0,
    var harga_barang: Int? = 0,
    var deskripsi: String? = "",
    var picture: String? = "",
    var satuan: String? = "",
    var subtotal: Int? = 0,
) : Parcelable