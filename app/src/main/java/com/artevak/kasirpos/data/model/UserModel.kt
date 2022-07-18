package com.artevak.kasirpos.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var id: String? = "",
    var name: String? = "",
    var email: String? = "",
    var nama_umkm: String? = "",
    var alamat: String? = "",
    var phone: String? = "",
) : Parcelable