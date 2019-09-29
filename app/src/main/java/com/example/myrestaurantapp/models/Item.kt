package com.example.myrestaurantapp.models

data class Item(
    val id: Int = -1,
    var name: String = "",
    var type: String = "",
    var description: String = "",
    var photo_url: String = "",
    var price: Double = -1.0,
    var deleted_at: Boolean = false
)