package com.jp.test.pagingwithdbdemo.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiProducts(
    var limit: Int? = null,
    var products: List<Product?>? = null,
    var skip: Int? = null,
    var total: Int? = null
) : Parcelable{
    @Parcelize
    data class Product(
        var brand: String? = null,
        var category: String? = null,
        var description: String? = null,
        var discountPercentage: Double? = null,
        var id: Int? = null,
        var images: List<String?>? = null,
        var price: Int? = null,
        var rating: Double? = null,
        var stock: Int? = null,
        var thumbnail: String? = null,
        var title: String? = null
    ) : Parcelable
}