package viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myrestaurantapp.models.Item
import com.example.myrestaurantapp.models.User
import repositorys.Repository

class ItemViewModel(application: Application):AndroidViewModel(application) {

    var itemsRepository: Repository? = null

    init {
        itemsRepository = Repository()
    }

    fun getAllItems(jsonResponse:String):MutableList<Item>{
        return itemsRepository?.getAllItem(jsonResponse)!!
    }

}