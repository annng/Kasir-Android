package com.artevak.kasirpos.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hutang(
    var id: String? = "",
    var id_pelanggan: String? = "",
    var nama_pelanggan: String? = "",
    var hutang: Int? = 0,
    var status: String? = "",
    var tgl_hutang: String? = "",
    var hutang_type: String? = "",
    var deskripsi: String? = "",

) : Parcelable