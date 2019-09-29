package com.example.myrestaurantapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myrestaurantapp.R
import com.example.myrestaurantapp.helpers.APIConstants
import com.example.myrestaurantapp.models.User

class MyProfileActivity : AppCompatActivity() {

    private lateinit var photoImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var shiftTextView: TextView
    private lateinit var typeTextView: TextView

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        photoImageView = findViewById(R.id.userProfilePhotoImageView)
        nameTextView = findViewById(R.id.userProfileNameTextView)
        emailTextView = findViewById(R.id.userProfileEmailTextView)
        usernameTextView = findViewById(R.id.userProfileUsernameTextView)
        shiftTextView = findViewById(R.id.userProfileShiftActiveTextView)
        typeTextView = findViewById(R.id.userProfileTypeTextView)

        user = intent.getSerializableExtra(MainActivity.INTENT_USER_KEY) as User

        setupUserInfo()
    }

    fun setupUserInfo() {

        nameTextView.text = user.name
        emailTextView.text = "Email: ${user.email}"
        usernameTextView.text = "Username: ${user.username}"
        typeTextView.text = user.type
        shiftTextView.text =
            if (user.shiftActive) "Shift State: Currently Working" else "Shift State: Not Working"

        Glide.with(this).load(APIConstants.imageUserURL+user.photoUrl).into(photoImageView)
    }
}
