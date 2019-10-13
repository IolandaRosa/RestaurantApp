package repositorys

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myrestaurantapp.helpers.JsonConvertersSingleton
import com.example.myrestaurantapp.models.Item
import com.example.myrestaurantapp.models.UnauthorizedAcess
import com.example.myrestaurantapp.models.User
import org.json.JSONObject

class Repository {
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val TOKEN_KEY = "token"
        const val UNAUTHORIZED_COUNT_KEY = "unauthorized_count"
        const val UNAUTHORIZED_TIMEINITIAL_KEY = "unauthorized_time_initial"
        const val UNAUTHORIZED_TIMEFINAL_KEY = "unauthorized_time_final"
        const val SHARED_PREFERENCE_NAME = "RestaurantAppSharedPreference"
        const val TAG = "Repository"
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
            Log.d(TAG, e.message!!)
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
            Log.d(TAG, e.message!!)
        } finally {
            return token
        }
    }

    fun saveUserToken(token: String, context: Context) {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getUserToken(context: Context): String? {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, "")
    }

    fun setUserInfo(jsonResponse: String, token: String): User? {

        var user: User? = null

        try {
            val response = JSONObject(jsonResponse)

            val jsonUser = response.getJSONObject("data")

            user = JsonConvertersSingleton.jsonObjectToUser(jsonUser)

            user?.token = token

        } catch (e: Exception) {
            Log.d(TAG, e.message!!)
        } finally {
            return user
        }
    }

    fun saveUnauthorizedAccessInfo(context: Context, username:String) {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        //Se a sharedPreferencesJa não tem gravado info
        if (!sharedPreferences.contains(username+UNAUTHORIZED_TIMEINITIAL_KEY)) {
            editor.putLong(username+UNAUTHORIZED_TIMEINITIAL_KEY, System.currentTimeMillis())
            editor.putInt(username+UNAUTHORIZED_COUNT_KEY, 0)
        }
        else {
            //senão atualiza a info
            var count = sharedPreferences.getInt(username+UNAUTHORIZED_COUNT_KEY, 0)
            count+=1
            editor.putInt(username+UNAUTHORIZED_COUNT_KEY, count)
            editor.putLong(username+UNAUTHORIZED_TIMEFINAL_KEY, System.currentTimeMillis())
        }

        editor.apply()
    }

    fun getUnauthorizedAccessInfo(context: Context, username:String): UnauthorizedAcess {

        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        val count = sharedPreferences.getInt(username+UNAUTHORIZED_COUNT_KEY, 0)

        val timeInit = sharedPreferences.getLong(username+UNAUTHORIZED_TIMEINITIAL_KEY, 0)

        val timeFinal = sharedPreferences.getLong(username+UNAUTHORIZED_TIMEFINAL_KEY,0)

        return UnauthorizedAcess(count, timeInit, timeFinal)
    }

    fun resetUnauthorizedAccessAttempts(context: Context, username:String){
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.remove(username+UNAUTHORIZED_COUNT_KEY)
        editor.remove(username+UNAUTHORIZED_TIMEFINAL_KEY)
        editor.remove(username+UNAUTHORIZED_TIMEINITIAL_KEY)

        editor.apply()
    }

}