package com.artevak.kasirpos.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataChartPenjualan(
    var date: String? = "",
    var jumlah: Int? = 0,
) : Parcelable