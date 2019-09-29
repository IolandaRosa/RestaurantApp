package viewModels

import android.app.Application
import android.content.Context
import android.util.JsonReader
import androidx.lifecycle.AndroidViewModel
import org.json.JSONObject
import repositorys.Repository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var isEmail=false
    var loginRepository: Repository? = null

    init {
        loginRepository = Repository()
    }


    fun validateData(username: String?, password: String?): JSONObject? {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            isEmail=true
        }

        return loginRepository?.validateData(username, password)

    }

    fun getJsonToken(jsonResponse: String):String?{
        return loginRepository?.getJsonToken(jsonResponse)
    }

    fun saveUserToken(token:String, context: Context) {
        loginRepository?.saveUserToken(token, context)
    }
}