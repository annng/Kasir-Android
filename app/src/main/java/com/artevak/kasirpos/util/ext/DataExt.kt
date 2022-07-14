package com.artevak.kasirpos.util.ext

fun String?.dashIfEmpty() : String{
    return this ?: "-"
}