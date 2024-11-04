package com.example.bluedolarapp.complication // Adjust to your package name

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComplicationToggleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "android.support.wearable.complications.ACTION_COMPLICATION_UPDATE_REQUEST") {
            Log.d("ComplicationToggleReceiver", "Update action received")

            // Step 1: Set SharedPreferences to loading state
            val sharedPreferences = context.getSharedPreferences(MainComplicationService.PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(MainComplicationService.COUNTER_KEY, "Load...").apply()

            // Step 2: Show "Loading..." immediately in complication
            updateComplication(context, "Loading...")

            // Step 3: Fetch data asynchronously
            CoroutineScope(Dispatchers.IO).launch {
                val latestCompra = fetchLatestCompra(context)

                withContext(Dispatchers.Main) {
                    // Step 4: If data is fetched, store and update complication
                    if (latestCompra != null) {
                        sharedPreferences.edit().putString(MainComplicationService.COUNTER_KEY, latestCompra).apply()
                        updateComplication(context, latestCompra)
                    } else {
                        Log.e("ComplicationToggleReceiver", "Failed to fetch data")
                    }
                }
            }
        }
    }

    // Function to update complication with given text
    private fun updateComplication(context: Context, text: String) {
        val requester = ComplicationDataSourceUpdateRequester.create(
            context,
            ComponentName(context, MainComplicationService::class.java)
        )
        requester.requestUpdateAll()

        // Optional log for debugging
        Log.d("ComplicationToggleReceiver", "Complication updated with text: $text")
    }

    // Function to fetch latest data from API
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
                compra?.let { "$$it" }
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
