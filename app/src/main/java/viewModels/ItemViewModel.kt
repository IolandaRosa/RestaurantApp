package viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myrestaurantapp.models.Item
import repositorys.Repository

class ItemViewModel(application: Application):AndroidViewModel(application) {

    private var itemsRepository: Repository? = null

    init {
        itemsRepository = Repository()
    }

    fun getAllItems(jsonResponse:String):MutableList<Item>{
        return itemsRepository?.getAllItem(jsonResponse)!!
    }

}