package com.example.myrestaurantapp.helpers

import com.example.myrestaurantapp.models.Item
import com.example.myrestaurantapp.models.User
import org.json.JSONObject

object JsonConvertersSingleton {

    fun jsonObjectToItem(jsonObject: JSONObject): Item? {
        var item: Item? = null

        try {
            item = Item(
                jsonObject.getInt("id"),
                jsonObject.getString("name"),
                jsonObject.getString("type"),
                jsonObject.getString("description"),
                jsonObject.getString("photo_url"),
                jsonObject.getDouble("price"),
                deleted_at = !jsonObject.getString("deleted_at").isNullOrEmpty()
            )

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return item
        }
    }

    fun jsonObjectToUser(jsonObject: JSONObject): User? {
        var user: User? = null

        try {
            user = User(
                jsonObject.getInt("id"),
                jsonObject.getString("name"),
                jsonObject.getString("username"),
                jsonObject.getString("email"),
                jsonObject.getString("type"),
                blocked = jsonObject.getInt("blocked") == 1,
                photoUrl = jsonObject.getString("photo_url"),
                shiftActive = jsonObject.getInt("shift_active") == 1,
                token = ""
            )

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return user
        }
    }

}