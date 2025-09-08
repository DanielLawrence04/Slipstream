package com.example.formula1

// Android and Compose Imports
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.formula1.AppComponents.QualifyingPredictionsComposable
import com.example.formula1.AppComponents.RegularText
import com.example.formula1.AppComponents.SelectTrack
import com.example.formula1.AppComponents.TrackDetails
import com.example.formula1.AppComponents.getTrackNumber
import com.opencsv.CSVReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.StringReader

class SimulateQualifyingScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}

// Data model for a single qualifying prediction
data class QualifyingPredictionClass(
    val position: Int,
    val driverName: String,
    val teamName: String,
    val sector1Time: Double,
    val sector2Time: Double,
    val sector3Time: Double,
    val sector1SpeedMin: Double, val sector1SpeedMax: Double, val sector1SpeedAvg: Double,
    val sector1RPMAvg: Double, val sector1ThrottleAvg: Double, val sector1BrakeAvg: Double,
    val sector2SpeedMin: Double, val sector2SpeedMax: Double, val sector2SpeedAvg: Double,
    val sector2RPMAvg: Double, val sector2ThrottleAvg: Double, val sector2BrakeAvg: Double,
    val sector3SpeedMin: Double, val sector3SpeedMax: Double, val sector3SpeedAvg: Double,
    val sector3RPMAvg: Double, val sector3ThrottleAvg: Double, val sector3BrakeAvg: Double,
    val speedI1: Double, val speedI2: Double, val speedFL: Double, val speedST: Double,
    val lapTime: Double
)

@Composable
fun SimulateQualifyingScreen(onExit: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        // UI state management for screen visibility
        val isSelectTrackVisible = remember { mutableStateOf(true) }
        val isTrackDetailsVisible = remember { mutableStateOf(false) }
        val isQualifyingPredictionsVisible = remember { mutableStateOf(false) }
        val qualifyingIsLoading = remember { mutableStateOf(false) }

        // State to store the user’s selected track and resulting predictions
        val trackSelected = remember { mutableStateOf("") }
        val predictions = remember { mutableStateOf<List<QualifyingPredictionClass>>(emptyList()) }

        // Step 1: Track selection component
        SelectTrack(
            enabled = isSelectTrackVisible.value && !isTrackDetailsVisible.value && !isQualifyingPredictionsVisible.value,
            onTrackSelected = { trackName ->
                trackSelected.value = trackName
                isTrackDetailsVisible.value = true
            },
            onDismiss = {
                isSelectTrackVisible.value = false
                onExit()
            }
        )

        // Step 2: Show track information and simulate button
        AnimatedVisibility(
            visible = isTrackDetailsVisible.value,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
        ) {
            val context = LocalContext.current
            TrackDetails(
                track = trackSelected.value,
                onReturnButtonClicked = { isTrackDetailsVisible.value = false },
                onContinueButtonClicked = {
                    // Reset previous state before simulation begins
                    predictions.value = emptyList()
                    isQualifyingPredictionsVisible.value = true
                    qualifyingIsLoading.value = true

                    val trackNumber = getTrackNumber(trackSelected.value)

                    // Launch background task to call Python and simulate results
                    CoroutineScope(Dispatchers.IO).launch {
                        val startTime = System.currentTimeMillis()
                        try {
                            // Start Python environment if not already running
                            if (!Python.isStarted()) {
                                Python.start(AndroidPlatform(context))
                            }

                            // Call the Python qualifying module
                            val python = Python.getInstance()
                            val pyObjectResult = python.getModule("qualifying")
                                .callAttr("main", trackNumber)

                            // Parse returned CSV string into structured data
                            val reader = CSVReader(StringReader(pyObjectResult.toString()))
                            val rows = reader.readAll()

                            val parsedResult = rows.drop(1).mapNotNull { parts ->
                                try {
                                    QualifyingPredictionClass(
                                        position = parts[0].toInt(),
                                        driverName = parts[1],
                                        teamName = parts[2],
                                        sector1Time = parts[3].toDouble(),
                                        sector2Time = parts[4].toDouble(),
                                        sector3Time = parts[5].toDouble(),
                                        sector1SpeedMin = parts[6].toDouble(),
                                        sector1SpeedMax = parts[7].toDouble(),
                                        sector1SpeedAvg = parts[8].toDouble(),
                                        sector1RPMAvg = parts[9].toDouble(),
                                        sector1ThrottleAvg = parts[10].toDouble(),
                                        sector1BrakeAvg = parts[11].toDouble(),
                                        sector2SpeedMin = parts[12].toDouble(),
                                        sector2SpeedMax = parts[13].toDouble(),
                                        sector2SpeedAvg = parts[14].toDouble(),
                                        sector2RPMAvg = parts[15].toDouble(),
                                        sector2ThrottleAvg = parts[16].toDouble(),
                                        sector2BrakeAvg = parts[17].toDouble(),
                                        sector3SpeedMin = parts[18].toDouble(),
                                        sector3SpeedMax = parts[19].toDouble(),
                                        sector3SpeedAvg = parts[20].toDouble(),
                                        sector3RPMAvg = parts[21].toDouble(),
                                        sector3ThrottleAvg = parts[22].toDouble(),
                                        sector3BrakeAvg = parts[23].toDouble(),
                                        speedI1 = parts[24].toDouble(),
                                        speedI2 = parts[25].toDouble(),
                                        speedFL = parts[26].toDouble(),
                                        speedST = parts[27].toDouble(),
                                        lapTime = parts[28].toDouble()
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    null
                                }
                            }

                            // Wait if execution was too fast (helps UI transitions)
                            val elapsedTime = System.currentTimeMillis() - startTime
                            val delayTime = 1500 - elapsedTime
                            if (delayTime > 0) delay(delayTime)

                            // Update UI state on main thread
                            withContext(Dispatchers.Main) {
                                predictions.value = parsedResult
                                qualifyingIsLoading.value = false
                                Log.d("Parsed Result", parsedResult.toString())
                            }
                        } catch (e: Exception) {
                            // Fallback in case of Python or parsing failure
                            Log.d("Python Error", e.toString())
                            withContext(Dispatchers.Main) {
                                qualifyingIsLoading.value = false
                            }
                        }
                    }
                }
            )
        }

        // Step 3: Display qualifying results or loading overlay
        AnimatedVisibility(
            visible = isQualifyingPredictionsVisible.value,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                // Main results screen — rendered even if predictions are empty
                QualifyingPredictionsComposable(
                    trackSelected.value,
                    predictions = predictions.value,
                    onReturnButtonClicked = { isQualifyingPredictionsVisible.value = false }
                )

                // Loading overlay (darkened background with spinner and message)
                if (qualifyingIsLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(45.dp),
                                color = Color.White,
                                strokeWidth = 5.dp
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            RegularText(
                                text = "Warming up the tyres...",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}