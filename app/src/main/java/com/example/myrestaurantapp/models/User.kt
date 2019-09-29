package com.example.myrestaurantapp.models

data class User(val id:Int=-1,
                var name:String="",
                var username:String="",
                var type:String="",
                var blocked:Boolean=false,
                var photoUrl:String="",
                var shiftActive:Boolean=false)