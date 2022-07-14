package com.artevak.kasirpos.util.ext

fun String?.dashIfEmpty() : String{
    return this ?: "-"
}

fun Int?.parseString() : String{
    return this?.toString() ?: "0"
}