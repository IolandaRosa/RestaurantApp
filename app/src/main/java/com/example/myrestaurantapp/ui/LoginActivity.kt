package com.example.myrestaurantapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.myrestaurantapp.R
import com.example.myrestaurantapp.helpers.APIConstants
import services.AsyncTaskLoginPost
import services.AsyncTaskResponseGet
import services.AsyncTaskResponseGetAuth
import services.OnUpdateListener
import viewModels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    companion object {
        val LOGIN_POST_URL_USERNAME = APIConstants.baseUrl + APIConstants.postLoginUsername
        val LOGIN_POST_URL_EMAIL = APIConstants.baseUrl + APIConstants.postLoginEmail
        val TAG = "LoginActivity"
        val GET_USER_USER = APIConstants.baseUrl + APIConstants.getUserMe
    }

    private lateinit var usernameTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var btnLogin: Button

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var loginTask: AsyncTaskLoginPost
    private lateinit var userTask: AsyncTaskResponseGetAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        usernameTextView = findViewById(R.id.loginUsernameEditText)
        passwordTextView = findViewById(R.id.loginPasswordEditText)
        btnLogin = findViewById(R.id.loginBtn)

        btnLogin.setOnClickListener {
            val jsonObject = loginViewModel.validateData(
                usernameTextView.text.toString(),
                passwordTextView.text.toString()
            )

            if (jsonObject != null) {
                loginTask = AsyncTaskLoginPost(jsonObject)

                performLogin()

            }
        }
    }

    fun performLogin() {

        loginTask.setUpdateListener(object : OnUpdateListener {
            override fun onUpdate(jsonResponse: String) {

                if(jsonResponse.isNullOrEmpty()){
                    Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_LONG).show()
                    return
                }

                //guardar o token na store e pedir as informações do utilizador
                val token = loginViewModel.getJsonToken(jsonResponse)

                if(token!=null){
                    saveUserToken(token)

                    getUserInformation(token)
                }

                Log.d(TAG, token!!)
            }
        })

        val loginUrl = if (loginViewModel.isEmail) {
            LOGIN_POST_URL_EMAIL
        } else {
            LOGIN_POST_URL_USERNAME
        }

        loginTask.execute(loginUrl)
    }

    fun getUserInformation(token:String){
        userTask= AsyncTaskResponseGetAuth(token)

        userTask.setUpdateListener(object : OnUpdateListener{
            override fun onUpdate(jsonResponse: String) {
                Log.d(TAG, jsonResponse)
            }
        })

        userTask.execute(GET_USER_USER)
    }

    fun saveUserToken(token:String){
        //guardar token nas shared preferences
        loginViewModel.saveUserToken(token, this)
    }
}
