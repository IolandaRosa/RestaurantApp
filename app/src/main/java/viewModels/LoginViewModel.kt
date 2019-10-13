package viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.myrestaurantapp.models.User
import com.example.myrestaurantapp.ui.LoginActivity
import org.json.JSONObject
import repositorys.Repository
import java.io.File


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

    fun showErrorToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun handleUnauthorizedAccess(context: Context, username: String): File? {

        var file: File? = null
        //Escrever informação no log
        Log.v(LoginActivity.UNAUTHORIZED, "Acesso não autorizado de utilizador - password inválida")

        //Atualizar nas sharedPreferences acesso indevido
        loginRepository?.saveUnauthorizedAccessInfo(context, username)

        //verificar se foram feitas 5 tentativas em menos de 10 min
        val accessInfo = loginRepository?.getUnauthorizedAccessInfo(context, username)

        if (accessInfo != null && accessInfo.count >= 1) {

            var timeDiff = accessInfo.timeFinal - accessInfo.timeInit

            timeDiff = (timeDiff / 1000 / 60)

            if (timeDiff <= 10) {
                //Enviar para o servidor info
                file =
                    loginRepository?.sendUnauthorizedAccessToServer(context, username, accessInfo)

                Log.d(LoginActivity.UNAUTHORIZED, "send server")
            }

            loginRepository?.resetUnauthorizedAccessAttempts(context, username)
        }

        return file
    }


}