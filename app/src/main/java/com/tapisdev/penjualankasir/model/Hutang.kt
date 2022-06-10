package com.tapisdev.penjualankasir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Hutang(
    var id_pelanggan: String? = "",
    var nama_pelanggan: String? = "",
    var hutang: Int? = 0,
    var status: String? = "",
    var tgl_hutang: String? = "",

) : Parcelable