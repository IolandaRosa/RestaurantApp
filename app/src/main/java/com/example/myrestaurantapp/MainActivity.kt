package com.example.myrestaurantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import services.AsyncTaskItems
import services.OnUpdateListener

class MainActivity : AppCompatActivity() {

    private lateinit var myItemsTask:AsyncTaskItems
    private lateinit var myTextView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myTextView = findViewById(R.id.myTextView)

        this.myItemsTask=AsyncTaskItems()

        this.myItemsTask.setUpdateListener(object : OnUpdateListener {
            override fun onUpdate(jsonResponse: String) {
                myTextView.text = jsonResponse
            }
        })

        myItemsTask.execute("http://abcb0c1e.ngrok.io/api/items")

    }
}
