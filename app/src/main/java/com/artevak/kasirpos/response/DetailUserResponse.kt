package com.artevak.kasirpos.response

import com.google.gson.annotations.SerializedName
import com.artevak.kasirpos.data.model.UserModel

data class DetailUserResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("http_status")
    val http_status: String,
    @SerializedName("data")
    val userModel : UserModel

)