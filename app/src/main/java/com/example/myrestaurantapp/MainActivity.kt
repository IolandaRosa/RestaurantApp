package com.example.myrestaurantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import helpers.APIConstants
import kotlinx.coroutines.*
import services.AsyncTaskItems
import services.OnUpdateListener
import viewModels.ItemViewModel
import models.Item
import kotlin.coroutines.CoroutineContext

class MainActivity: AppCompatActivity() {

    companion object {
        val GET_ITEMS_URL = APIConstants.baseUrl+ APIConstants.getItemsURL
        val TAG = "MainActivity"
    }

    private lateinit var itemViewModel: ItemViewModel

    private lateinit var myTask: AsyncTaskItems

    private lateinit var myTextView:TextView

    private var items:MutableList<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        myTask = AsyncTaskItems()
        myTextView = findViewById(R.id.myTextView)

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
        myTextView.text = items[0].description
    }
}
