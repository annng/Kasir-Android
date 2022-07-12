package com.artevak.kasirpos.util.ext

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import com.artevak.kasirpos.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if(value) View.VISIBLE else View.GONE
    }

infix fun ImageView.loadImageCircle(strUrl: String) {
    try {
        Glide.with(this).load(strUrl)
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
    } catch (e: Exception) {

    }
}

infix fun ImageView.loadImageRounded(strUrl: String) {
    try {
        Glide.with(this)
            .load(strUrl)
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(this)
    }catch (e : Exception){

    }

}

inline fun <reified ITEM> AutoCompleteTextView.init(context : Context, items : ArrayList<ITEM>) : ArrayAdapter<ITEM> {
    val adapter = ArrayAdapter(context, R.layout.item_spinner, items)
    this.threshold = 1

    return adapter
}

fun String.phoneNumber() : String{
    return "+$this"
}
