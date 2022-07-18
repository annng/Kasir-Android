package com.artevak.kasirpos.common.util.ext

import com.google.gson.Gson
import com.google.gson.JsonParser

inline fun <reified ITEM> String.toList(): List<ITEM> {
    val vList = ArrayList<ITEM>()
    val arry = JsonParser().parse(this).asJsonArray
    for (jsonElement in arry) {
        vList.add(Gson().fromJson(jsonElement, ITEM::class.java))
    }
    return vList
}

inline fun <reified ITEM> String.getObject(): ITEM {
    return Gson().fromJson(this, ITEM::class.java)
}

inline fun <reified ITEM> ITEM.getString(): String {
    return Gson().toJson(this, ITEM::class.java)
}