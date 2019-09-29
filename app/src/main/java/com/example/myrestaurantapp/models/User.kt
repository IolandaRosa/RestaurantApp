package com.example.myrestaurantapp.models

import java.io.Serializable

data class User(val id:Int=-1,
                var name:String="",
                var username:String="",
                var email:String="",
                var type:String="",
                var blocked:Boolean=false,
                var photoUrl:String="",
                var shiftActive:Boolean=false,
                var token:String="") : Serializable