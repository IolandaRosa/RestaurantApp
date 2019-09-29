package viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.json.JSONObject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var isEmail=false

    fun validateData(username: String?, password: String?): JSONObject? {

        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            return null
        }

        val jsonObject = JSONObject()

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            jsonObject.put("email", username)
            isEmail=true
        } else {
            jsonObject.put("username", username)
        }

        jsonObject.put("password", password)

        return jsonObject

    }
}