package com.example.myrestaurantapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrestaurantapp.R
import com.example.myrestaurantapp.adapter.ItemsAdapter
import com.example.myrestaurantapp.helpers.APIConstants
import com.example.myrestaurantapp.helpers.InternetValidator
import com.example.myrestaurantapp.models.Item
import com.example.myrestaurantapp.models.User
import services.AsyncTaskResponseGet
import services.AsyncTaskResponseGetAuth
import services.OnUpdateListener
import viewModels.ItemViewModel
import javax.xml.validation.Validator

class MainActivity : AppCompatActivity() {

    companion object {
        const val GET_ITEMS_URL = APIConstants.getItemsURL
        const val TAG = "MainActivity"
        const val LOGIN_ACTIVITY_RESULT_CODE = 1
        const val INTENT_USER_KEY = "user"
        const val LOGOUT_URL = APIConstants.logoutUrl
    }

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var itemsTask: AsyncTaskResponseGet
    private var items: MutableList<Item> = mutableListOf()
    private lateinit var waitLayout: LinearLayout
    private lateinit var itemsRecyclerView: RecyclerView
    private var user: User? = null
    private lateinit var menu: Menu
    private lateinit var logoutTask: AsyncTaskResponseGetAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Termina aplicação se não exitir a ligação wifi
        if(!InternetValidator.checkInternetConnection(this)){
            InternetValidator.showErrorMessage(this, R.string.connectionError)
            return
        }

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        itemsTask = AsyncTaskResponseGet()

        waitLayout = findViewById(R.id.waitItemsLinearLayout)
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)

        waitLayout.visibility = View.VISIBLE

        setupGetItemsTask()

    }

    private fun setupGetItemsTask() {
        itemsTask.setUpdateListener(object : OnUpdateListener {
            override fun onUpdate(jsonResponse: String) {
                try {

                    items = itemViewModel.getAllItems(jsonResponse)

                    updateUiView()

                } catch (e: Exception) {
                    Log.d(TAG, e.message!!)
                }
            }
        })

        itemsTask.execute(GET_ITEMS_URL)
    }

    fun updateUiView() {
        waitLayout.visibility = View.GONE
        itemsRecyclerView.layoutManager = LinearLayoutManager(this)
        itemsRecyclerView.adapter = ItemsAdapter(items, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        this.menu = menu!!
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.action_home ->
                return true
            R.id.action_login ->
                startLoginActivity()
            R.id.action_logout ->
                logout()
            R.id.action_profile ->
                loadProfile()
        }

        return true
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)

        startActivityForResult(intent, LOGIN_ACTIVITY_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_ACTIVITY_RESULT_CODE && resultCode == Activity.RESULT_OK) {

            user = data?.getSerializableExtra(INTENT_USER_KEY) as User

            //alterar vista para user logado
            setupLogedUserUI()
        }
    }

    private fun setupLogedUserUI() {
        updateMenuInfo()
    }

    private fun updateMenuInfo() {

        if (user != null) {
            //index do login
            menu.getItem(1).isVisible = false
            //My profile
            menu.getItem(2).isVisible = true
            //logout
            menu.getItem(3).isVisible = true
        } else {
            menu.getItem(1).isVisible = true
            menu.getItem(2).isVisible = false
            menu.getItem(3).isVisible = false
        }

    }

    private fun logout() {
        //fazer pedido a api
        if (user != null) {
            logoutTask = AsyncTaskResponseGetAuth(user!!.token)
            logoutTask.setUpdateListener(object : OnUpdateListener {
                override fun onUpdate(jsonResponse: String) {
                    Toast.makeText(this@MainActivity, "User logged out", Toast.LENGTH_SHORT).show()
                    user = null
                    updateMenuInfo()
                }
            })

            logoutTask.execute(LOGOUT_URL)
        }
    }

    private fun loadProfile() {
        val intent = Intent(this, MyProfileActivity::class.java)

        val bundle = Bundle()
        bundle.putSerializable(INTENT_USER_KEY, user)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}
