package com.artevak.kasirpos.common.util.ext

fun String?.dashIfEmpty() : String{
    return this ?: "-"
}

fun Int?.parseString() : String{
    return this?.toString() ?: "0"
}