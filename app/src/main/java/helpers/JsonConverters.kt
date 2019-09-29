package helpers

import models.Item
import org.json.JSONObject
import java.lang.Exception

object JsonConvertersSingleton {

    fun jsonObjectToItem(jsonObject: JSONObject): Item? {
        var item:Item?=null

        try {
            item = Item(jsonObject.getInt("id"),
                jsonObject.getString("name"),
                jsonObject.getString("type"),
                jsonObject.getString("description"),
                jsonObject.getString("photo_url"),
                jsonObject.getDouble("price"),
                deleted_at = !jsonObject.getString("deleted_at").isNullOrEmpty()
            )

        } catch (e:Exception) {
            e.printStackTrace()
        }
        finally {
            return item
        }
    }

}