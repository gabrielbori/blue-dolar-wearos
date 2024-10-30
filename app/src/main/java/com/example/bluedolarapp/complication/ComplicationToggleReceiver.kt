package com.example.bluedolarapp.complication // Change to your package name

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ApiService
import com.example.bluedolarapp.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComplicationToggleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "android.support.wearable.complications.ACTION_COMPLICATION_UPDATE_REQUEST") {
            // Log to check if the action was received
            Log.d("ComplicationToggleReceiver", "Update action received")
            // For example, you can retrieve the current counter and increment it
            val sharedPreferences = context.getSharedPreferences(MainComplicationService.PREFS_NAME, Context.MODE_PRIVATE)
            val currentCounter = sharedPreferences.getInt(MainComplicationService.COUNTER_KEY, 0) + 1
            sharedPreferences.edit().putInt(MainComplicationService.COUNTER_KEY, currentCounter).apply()
            // Request the update for the complication
            val requester = ComplicationDataSourceUpdateRequester.create(
                context,
                ComponentName(context, MainComplicationService::class.java)
            )
            requester.requestUpdateAll()
        }
    }
}