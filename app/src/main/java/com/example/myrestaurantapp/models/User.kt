package com.example.myrestaurantapp.models

import java.io.Serializable

data class User(
    val id: Int = -1,
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var type: String = "",
    var blocked: Boolean = false,
    var photoUrl: String = "",
    var shiftActive: Boolean = false,
    var token: String = ""
) : Serializable

data class UnauthorizedAcess(
    var count: Int = 0,
    var timeInit: Long = System.currentTimeMillis(),
    var timeFinal: Long = System.currentTimeMillis()
)