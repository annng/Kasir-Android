package com.tapisdev.penjualankasir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class UserModel(
    var id: String? = "",
    var name: String? = "",
    var email: String? = "",
    var nama_umkm: String? = "",
    var alamat: String? = "",
    var phone: String? = "",
) : Parcelable