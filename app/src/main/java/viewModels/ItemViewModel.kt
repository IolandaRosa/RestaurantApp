package viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myrestaurantapp.models.Item
import repositorys.ItemRepository

class ItemViewModel(application: Application):AndroidViewModel(application) {

    var itemsRepository: ItemRepository? = null

    init {
        itemsRepository = ItemRepository()
    }

    fun getAllItems(jsonResponse:String):MutableList<Item>{
        return itemsRepository?.getAllItem(jsonResponse)!!
    }

}