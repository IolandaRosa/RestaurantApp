package com.example.myrestaurantapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrestaurantapp.R
import com.example.myrestaurantapp.adapter.ItemsAdapter
import com.example.myrestaurantapp.helpers.APIConstants
import com.example.myrestaurantapp.models.Item
import services.AsyncTaskResponseGet
import services.OnUpdateListener
import viewModels.ItemViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        val GET_ITEMS_URL = APIConstants.baseUrl + APIConstants.getItemsURL
        val TAG = "MainActivity"
        val LOGIN_ACTIVITY_RESULT_CODE = 1
    }

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var myTask: AsyncTaskResponseGet
    private var items: MutableList<Item> = mutableListOf()
    private lateinit var waitLayout: LinearLayout
    private lateinit var itemsRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        myTask = AsyncTaskResponseGet()
        waitLayout = findViewById(R.id.waitItemsLinearLayout)
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)

        waitLayout.visibility = View.VISIBLE

        setupGetItemsTask()

    }

    fun setupGetItemsTask() {
        myTask.setUpdateListener(object : OnUpdateListener {
            override fun onUpdate(jsonResponse: String) {
                try {

                    items = itemViewModel.getAllItems(jsonResponse)

                    updateUiView()

                } catch (e: Exception) {
                    Log.d(TAG, e.message!!)
                }
            }
        })

        myTask.execute(GET_ITEMS_URL)
    }

    fun updateUiView() {
        waitLayout.visibility = View.GONE

        itemsRecyclerView.layoutManager = LinearLayoutManager(this)

        itemsRecyclerView.adapter = ItemsAdapter(items, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.action_home ->
                return true
            R.id.action_login ->
                startLoginActivity()
        }

        return true
    }

    fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)

        startActivityForResult(intent, LOGIN_ACTIVITY_RESULT_CODE)
    }
}
