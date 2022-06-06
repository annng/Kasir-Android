package com.tapisdev.penjualankasir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Pelanggan(
    var id: String? = "",
    var name: String? = "",
    var phone: String? = "",
    var alamat: String? = "",
) : Parcelable