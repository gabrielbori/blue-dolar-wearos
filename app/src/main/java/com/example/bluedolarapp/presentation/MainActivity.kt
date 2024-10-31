/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.bluedolarapp.presentation

import ApiService
import WearApp
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.bluedolarapp.R
import com.example.bluedolarapp.presentation.theme.BlueDolarAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var apiService: ApiService
    private val updateInterval = 1 * 60 * 1000L  // 1 minute in milliseconds

    // Mutable states for UI
    var compra by mutableStateOf("-")
    private var venta by mutableStateOf("")
    private var time by mutableStateOf("-")
    private var diff by mutableStateOf(0.0)
    override fun onResume() {
        super.onResume()
        Log.d("Mainapp", "onResume event")

        lifecycleScope.launch {
            fetchAndUpdateData()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("TouchEvent", "Action: DOWN at (${event.x}, ${event.y})")
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d("TouchEvent", "Action: MOVE at (${event.x}, ${event.y})")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("TouchEvent", "Action: UP at (${event.x}, ${event.y})")
                    lifecycleScope.launch {
                        fetchAndUpdateData()
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    Log.d("TouchEvent", "Action: CANCEL at (${event.x}, ${event.y})")
                }
                MotionEvent.ACTION_OUTSIDE -> {
                    Log.d("TouchEvent", "Action: OUTSIDE at (${event.x}, ${event.y})")
                }
                else -> {
                    Log.d("TouchEvent", "Unknown action: ${event.action} at (${event.x}, ${event.y})")
                }
            }
        }
        return super.onTouchEvent(event)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Retrofit
        apiService = Retrofit.Builder()
            .baseUrl("https://backend-ifa-production-a92c.up.railway.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        // Start periodic updates
//        lifecycleScope.launch {
//            while (true) {
//                fetchAndUpdateData()  // Fetch data from API
//                delay(updateInterval) // Wait for the next update interval
//            }
//        }

        // Set the content to WearApp with manual update functionality
        setContent {
            WearApp(
                compra = compra,
                venta = venta,
                diff = diff,
                time = time,
                onUpdate = {
                    lifecycleScope.launch {
                        fetchAndUpdateData()
                    }
                }
            )
        }
        lifecycleScope.launch {
            fetchAndUpdateData()
        }
    }

    private suspend fun fetchAndUpdateData() {
        val values = updateValue()

        venta = values?.get(0) ?: ""
        compra = values?.get(1) ?: ""
        val cierre = values?.get(2)?.toDoubleOrNull() ?: 999.0

        // Calculate the percentage difference
        val ventaDouble = venta.toDoubleOrNull() ?: 0.0
        diff = if (ventaDouble != 0.0) {
            ((ventaDouble - cierre) / cierre) * 100
        } else {
            0.0
        }

        // Update the time
        val timen = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        time = formatter.format(timen)
    }

    private suspend fun updateValue(): Array<String?>? {
        time="Loading..."
        venta=""
        compra=""
        diff=0.0
        return try {
            val response = apiService.getCurrencyData()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                val dolarBlue = apiResponse?.panel?.find { it.titulo == "Dólar Blue" }
                val latestVenta = dolarBlue?.venta
                val latestCompra = dolarBlue?.compra
                val cierre = dolarBlue?.cierre

                arrayOf(latestVenta, latestCompra, cierre)
            } else {
                Log.e("MainActivity", "Response error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error fetching value: ${e.message}")
            null
        }
    }
}


@Composable
fun WearApp(greetingName: String) {
    BlueDolarAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}