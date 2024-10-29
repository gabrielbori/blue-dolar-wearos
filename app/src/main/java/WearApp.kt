// WearApp.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WearApp(
    compra: String = "Fetching data...",
    venta: String,
    diff: Double = 0.5,
    time: String = "11:11",
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
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "$$compra - $$venta", fontSize = 25.sp, color = Color.White)
                Text("$diffText%", color = diffColor, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$time", color = Color.Blue, fontSize = 20.sp)

                // Add button to manually reload data
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = onUpdate, modifier=Modifier.height(30.dp)) {
                    Text("Actualizar", fontSize=10.sp, textAlign = TextAlign.Center,)
                }
            }
        }
    }
}