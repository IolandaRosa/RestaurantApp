package com.example.myrestaurantapp.helpers

object APIConstants {
    private const val baseUrlAPI: String = "http://cyberproject.xyz/api/"
    //private const val baseUrlAPI: String = "http://e51ff16f.ngrok.io/api/"
    private const val baseUrlImages: String = "http://cyberproject.xyz/storage/"
    //private const val baseUrlImages: String = "http://e51ff16f.ngrok.io/storage/"
    const val getItemsURL: String = baseUrlAPI + "items"
    const val imageItemURL: String = baseUrlImages + "items/"
    const val imageUserURL: String = baseUrlImages + "profiles/"
    const val postLoginUsername: String = baseUrlAPI + "loginUsername"
    const val postLoginEmail: String = baseUrlAPI + "login"
    const val getUserMe: String = baseUrlAPI + "users/me"
    const val logoutUrl: String = baseUrlAPI + "logout"
    const val unauthorizedUrl: String = baseUrlAPI + "unauthorizedAccess"
    const val unauthorizedResult: String = "Unauthorized"
}