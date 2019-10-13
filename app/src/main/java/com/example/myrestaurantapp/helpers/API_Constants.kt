package com.example.myrestaurantapp.helpers

object APIConstants {
    private const val baseUrlAPI: String = "https://projetociberseguranca.azurewebsites.net/api/"
    private const val baseUrlImages: String = "https://projetociberseguranca.azurewebsites.net/storage/"
    const val getItemsURL: String = baseUrlAPI+"items"
    const val imageItemURL: String = baseUrlImages+"items/"
    const val imageUserURL: String = baseUrlImages+"profiles/"
    const val postLoginUsername: String = baseUrlAPI+"loginUsername"
    const val postLoginEmail: String = baseUrlAPI+"login"
    const val getUserMe: String = baseUrlAPI+"users/me"
    const val logoutUrl: String = baseUrlAPI+"logout"
}