package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.data.model.Hutang

data class HutangResponse(
    @SerializedName("current_page")
    val current_page: String,
    @SerializedName("data")
    val data_hutang : ArrayList<Hutang>

)