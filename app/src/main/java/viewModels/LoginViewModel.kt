package viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.myrestaurantapp.models.User
import org.json.JSONObject
import repositorys.Repository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var isEmail = false
    var loginRepository: Repository? = null

    init {
        loginRepository = Repository()
    }


    fun validateData(username: String?, password: String?): JSONObject? {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(username!!).matches()) {
            isEmail = true
        }

        return loginRepository?.validateData(username, password)

    }

    fun getJsonToken(jsonResponse: String): String? {
        return loginRepository?.getJsonToken(jsonResponse)
    }

    fun saveUserToken(token: String, context: Context) {
        loginRepository?.saveUserToken(token, context)
    }

    fun setUserInfo(jsonResponse: String, token: String): User? {
        return loginRepository?.setUserInfo(jsonResponse, token)
    }
}