package com.tapisdev.penjualankasir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class DataChartPenjualan(
    var date: String? = "",
    var jumlah: Int? = 0,
) : Parcelable