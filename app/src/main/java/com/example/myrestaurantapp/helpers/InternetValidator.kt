package com.example.myrestaurantapp.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object InternetValidator {

    @Suppress("DEPRECATION")
    fun checkInternetConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun showErrorMessage(context: Context, msg: Int) {
        val builder = AlertDialog.Builder(context)

        builder.setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("Error")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { p0, p1 ->
                val activity: Activity = context as Activity
                activity?.finish()
            }.show()

    }
}