package com.artevak.kasirpos.response.firebase

data class ResponseData<ITEM : Any>(
    var data : ITEM,
    var keys : String
) {
    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        return data.toString()
    }
}