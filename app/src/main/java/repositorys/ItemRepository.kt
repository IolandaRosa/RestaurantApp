package repositorys

import android.util.Log
import helpers.JsonConvertersSingleton
import models.Item
import org.json.JSONObject

class ItemRepository {
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

}