package com.example.myrestaurantapp

import adapter.ItemsAdapter
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import helpers.APIConstants
import services.AsyncTaskItems
import services.OnUpdateListener
import viewModels.ItemViewModel
import models.Item

class MainActivity: AppCompatActivity() {

    companion object {
        val GET_ITEMS_URL = APIConstants.baseUrl+ APIConstants.getItemsURL
        val TAG = "MainActivity"
    }

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var myTask: AsyncTaskItems
    private var items:MutableList<Item> = mutableListOf()
    private lateinit var waitLayout: LinearLayout
    private lateinit var itemsRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        myTask = AsyncTaskItems()
        waitLayout = findViewById(R.id.waitItemsLinearLayout)
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)

        waitLayout.visibility = View.VISIBLE

        setupGetItemsTask()

    }

    fun setupGetItemsTask(){
        myTask.setUpdateListener(object : OnUpdateListener {
            override fun onUpdate(jsonResponse: String) {
                try {

                    items = itemViewModel.getAllItems(jsonResponse)

                    updateUiView()

                }
                catch (e:Exception){
                    Log.d(TAG,e.message!!)
                }
            }
        })

        myTask.execute(GET_ITEMS_URL)
    }

    fun updateUiView(){
        waitLayout.visibility = View.GONE

        itemsRecyclerView.layoutManager = LinearLayoutManager(this)

        itemsRecyclerView.adapter = ItemsAdapter(items, this)
    }
}
