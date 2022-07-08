package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.model.DataChartPenjualan

data class ChartTransaksiResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data_chart : ArrayList<DataChartPenjualan>

)