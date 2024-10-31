// WearApp.kt
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.wear.compose.material.MaterialTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WearApp(
    compra: String = "-",
    venta: String,
    diff: Double = 0.5,
    time: String = "-:-",
    onUpdate: () -> Unit // Lambda function to trigger update
) {
    // Set the color based on diff value
    val diffColor = if (diff <= 0.0) Color.Red else Color.Green
    val diffText = String.format("%.2f", diff)

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(top = 30.dp)
            ) {

                Text(text = "$$compra - $$venta", fontSize = 25.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$diffText%", color = diffColor, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$time", color = MaterialTheme.colors.primaryVariant, fontSize = 20.sp)

                // Add button to manually reload data
//                IconButton(onClick = onUpdate, Modifier.size(150.dp)) {     Icon(Icons. Outlined. Refresh, contentDescription = "Localized description") }

            }
        }
    }
}