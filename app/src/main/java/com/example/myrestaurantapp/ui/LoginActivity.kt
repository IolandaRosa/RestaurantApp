package com.example.myrestaurantapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.myrestaurantapp.R
import com.example.myrestaurantapp.helpers.APIConstants
import services.AsyncTaskLoginPost
import services.OnUpdateListener
import viewModels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    companion object {
        val LOGIN_POST_URL_USERNAME = APIConstants.baseUrl + APIConstants.postLoginUsername
        val LOGIN_POST_URL_EMAIL = APIConstants.baseUrl + APIConstants.postLoginEmail
        val TAG = "LoginActivity"
    }

    private lateinit var usernameTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var btnLogin: Button

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var myTask: AsyncTaskLoginPost

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
                myTask = AsyncTaskLoginPost(jsonObject)

                performLogin()

            }
        }
    }

    fun performLogin() {

        myTask.setUpdateListener(object : OnUpdateListener {
            override fun onUpdate(jsonResponse: String) {

                //guardar o token na store e pedir as informações do utilizador

                Log.d(TAG, jsonResponse)
                //guardar token e fazer update da UI
            }
        })

        val loginUrl = if (loginViewModel.isEmail) {
            LOGIN_POST_URL_EMAIL
        } else {
            LOGIN_POST_URL_USERNAME
        }

        myTask.execute(loginUrl)
    }
}
