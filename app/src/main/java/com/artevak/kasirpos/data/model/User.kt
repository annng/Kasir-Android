package com.artevak.kasirpos.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name: String = "",
    var username: String = "",
    var password: String = "",
    var shopeName: String? = "",
    var address: String? = "",
    var phone: String? = "",
) : Parcelable