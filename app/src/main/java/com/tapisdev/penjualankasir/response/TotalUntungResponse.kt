package com.tapisdev.penjualankasir.response

import com.google.gson.annotations.SerializedName

data class TotalUntungResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("http_status")
    val http_status: String,
    @SerializedName("data")
    val data: String

)