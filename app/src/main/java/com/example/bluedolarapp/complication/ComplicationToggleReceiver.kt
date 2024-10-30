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
            CoroutineScope(Dispatchers.IO).launch {
                val latestCompra = fetchLatestCompra(context)

                // Save the latestCompra value to SharedPreferences
                if (latestCompra != null) {
                    val sharedPreferences = context.getSharedPreferences(MainComplicationService.PREFS_NAME, Context.MODE_PRIVATE)
//                    sharedPreferences.edit().putString("latestCompra", latestCompra).apply()
                    sharedPreferences.edit().putString(MainComplicationService.COUNTER_KEY, latestCompra).apply()

                    // Request the update for the complication
                    val requester = ComplicationDataSourceUpdateRequester.create(
                        context,
                        ComponentName(context, MainComplicationService::class.java)
                    )
                    requester.requestUpdateAll()
                }
            }
        }
    }
    private suspend fun fetchLatestCompra(context: Context): String? {
        val apiService = Retrofit.Builder()
            .baseUrl("https://backend-ifa-production-a92c.up.railway.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        return try {
            val response = apiService.getCurrencyData()
            if (response.isSuccessful) {
                val compra = response.body()?.panel?.find { it.titulo == "Dólar Blue" }?.compra
                val venta = response.body()?.panel?.find { it.titulo == "Dólar Blue" }?.venta
                "$$compra"
            } else {
                Log.e("ComplicationToggleReceiver", "Error fetching data: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ComplicationToggleReceiver", "Exception fetching data: ${e.message}")
            null
        }
    }
}