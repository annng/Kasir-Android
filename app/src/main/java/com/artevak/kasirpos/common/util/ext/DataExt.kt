package com.artevak.kasirpos.common.util.ext

import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.response.firebase.ResponseData

fun String?.dashIfEmpty() : String{
    return this ?: "-"
}

fun Int?.parseString() : String{
    return this?.toString() ?: "0"
}

fun ArrayList<ResponseData<User>>.isExist(username: String, password: String): ResponseData<User>? {
    var isExist : ResponseData<User>? = null
    for (i in this) {
        if ((i.keys.equals(username, true) || i.data.username.equals(
                username,
                true
            )) && i.data.password == password
        )
            isExist = i
    }

    return isExist
}