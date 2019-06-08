package com.currencyconverter.util

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

object DataBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageResource")
    fun setImageResource(imageView: AppCompatImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("textResource")
    fun setTextResource(textView: TextView, resource: Int) {
        if (resource != 0) {
            textView.text = textView.context.getString(resource)
        }
    }
}