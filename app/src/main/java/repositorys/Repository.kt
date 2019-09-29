package repositorys

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.example.myrestaurantapp.helpers.JsonConvertersSingleton
import com.example.myrestaurantapp.models.Item
import org.json.JSONObject

class Repository {
    private lateinit var sharedPreferences:SharedPreferences

    companion object{
        val TOKEN_KEY = "token"
        val SHARED_PREFERENCE_NAME="RestaurantAppSharedPreference"
    }


    fun getAllItem(jsonResponse: String): MutableList<Item> {
        val items = mutableListOf<Item>()
        try {
            val response = JSONObject(jsonResponse)

            val responseArray = response.getJSONArray("data")

            for (index in 0..responseArray.length()) {

                val item =
                    JsonConvertersSingleton.jsonObjectToItem(responseArray.getJSONObject(index))
                items.add(item!!)
            }

        } catch (e: Exception) {
            Log.d("TAG", e.message)
        } finally {
            return items
        }
    }

    fun validateData(username: String?, password: String?): JSONObject? {
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            return null
        }

        val jsonObject = JSONObject()

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            jsonObject.put("email", username)
        } else {
            jsonObject.put("username", username)
        }

        jsonObject.put("password", password)

        return jsonObject
    }

    fun getJsonToken(jsonResponse: String): String {
        var token = ""
        try {
            val response = JSONObject(jsonResponse)

            token = response.getString("access_token")

        } catch (e: Exception) {
            Log.d("TAG", e.message)
        } finally {
            return token
        }
    }

    fun saveUserToken(token: String, context:Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY,token)
        editor.apply()
    }

    fun getUserToken(context: Context):String? {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY,"")
    }

}