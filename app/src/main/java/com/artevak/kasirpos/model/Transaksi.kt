package com.artevak.kasirpos.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaksi(
    var id: String? = "",
    var id_pelanggan: String? = "",
    var nama_pelanggan: String? = "",
    var total_bayar: Int? = 0,
    var total_untung: Int? = 0,
    var status_pembayaran: String? = "",
    var tgl_transaksi: String? = "",

) : Parcelable