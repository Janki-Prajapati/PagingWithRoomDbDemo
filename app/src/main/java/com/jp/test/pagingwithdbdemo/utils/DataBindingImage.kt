package com.jp.test.pagingwithdbdemo.utils

import android.media.Image
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

object DataBindingImage {

    @JvmStatic
    @BindingAdapter("imageLoad")
    fun loadImage(image: ImageView, imagePath : String?){
        image.load(imagePath)
    }
}