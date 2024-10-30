package com.example.bluedolarapp.complication
import android.app.PendingIntent
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.ComponentName
import android.util.Log
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceService
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import androidx.wear.watchface.complications.data.*
import androidx.wear.watchface.complications.datasource.ComplicationRequest

class MainComplicationService : ComplicationDataSourceService() {

    companion object {
        const val ACTION_UPDATE_COMPLICATION = "com.example.bluedolarapp.UPDATE_COMPLICATION"
        const val PREFS_NAME = "ComplicationPrefs"
        const val COUNTER_KEY = "counter"
        const val TAG = "MainComplicationService"
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Register the receiver for updates
        val filter = IntentFilter(ACTION_UPDATE_COMPLICATION)
        registerReceiver(complicationReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver to prevent leaks
        unregisterReceiver(complicationReceiver)
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SHORT_TEXT) return null
        return createComplicationData(0) // Preview starts at 0
    }

    override fun onComplicationRequest(request: ComplicationRequest, listener: ComplicationRequestListener) {
        Log.d(TAG, "Complication request received")
        // Retrieve the current counter, increment it, and save the new value
        val counter = sharedPreferences.getInt(COUNTER_KEY, 0)
        sharedPreferences.edit().putInt(COUNTER_KEY, counter).apply()

        // Create updated complication data with the new counter value and send it to the listener
        listener.onComplicationData(createComplicationData(counter))
    }

    private fun createComplicationData(counter: Int): ShortTextComplicationData {
        // Create an intent for the update action
        val ACTION_UPDATE_COMPLICATION = "android.support.wearable.complications.ACTION_COMPLICATION_UPDATE_REQUEST"

        val updateIntent = Intent(ACTION_UPDATE_COMPLICATION).apply {
            setPackage(packageName)
        }

        // Create the PendingIntent for tap action
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            updateIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Return the complication data with the tap action
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(counter.toString()).build(),
            contentDescription = PlainComplicationText.Builder("Counter: $counter").build()
        ).setTapAction(pendingIntent).build()
    }




    private val complicationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
//            if (intent?.action == ACTION_UPDATE_COMPLICATION) {
//                Log.d("ComplicationReceiver", "Received update action")
//                // Retrieve and increment the counter
//                val currentCounter = sharedPreferences.getInt(COUNTER_KEY, 0) + 1
//                sharedPreferences.edit().putInt(COUNTER_KEY, currentCounter).apply()
//
//                // Request an update for the complication
//                val requester = ComplicationDataSourceUpdateRequester.create(
//                    context,
//                    ComponentName(context, MainComplicationService::class.java)
//                )
//                requester.requestUpdateAll()
//            }
        }
    }
}
