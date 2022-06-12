package com.tapisdev.penjualankasir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class HistoryTransaksi(
    var nama_barang: String? = "",
    var untung: Int? = 0,
    var subtotal: Int? = 0,
    var picture: String? = "",
    var satuan: String? = "",
    var id_barang: String? = "",
) : Parcelable