package com.artevak.kasirpos.widget.binding

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.databinding.BindingAdapter

class ImageViewBinding {
    companion object{
        @SuppressLint("SetTextI18n")
        @JvmStatic
        @BindingAdapter("image")
        fun image(view: ImageView, param: Int?){
            param?.let {
              view.setImageResource(it)
           }
        }

    }
}