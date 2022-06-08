package com.tapisdev.penjualankasir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Transaksi(
    var id_pelanggan: String? = "",
    var nama_pelanggan: String? = "",
    var total_bayar: Int? = 0,
    var total_untung: Int? = 0,
    var status_pembayaran: String? = "",
    var created_at: String? = "",

) : Parcelable