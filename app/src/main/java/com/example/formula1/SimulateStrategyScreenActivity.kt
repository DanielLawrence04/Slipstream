package com.example.formula1

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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.formula1.AppComponents.StrategyPredictionsComposable
import com.example.formula1.AppComponents.TelemetryOptions
import com.example.formula1.AppComponents.TrackDetails
import com.example.formula1.AppComponents.getTrackNumber
import com.opencsv.CSVReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.StringReader

class SimulateStrategyScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}

// Data class for one driver's strategy prediction
data class StrategyPredictionClass(
    val bestStrategy: String,
    val bestConfidence: Double,
    val numPitStops: Int,
    val alternatives: List<AlternativeStrategy>
)

// Alternative strategies (top 3)
data class AlternativeStrategy(
    val strategy: String,
    val confidence: Double
)


@Composable
fun SimulateStrategy(onExit: () -> Unit) {
    // Temperature values chosen by the user
    val userSelectedAirTemp = remember { mutableIntStateOf(26) }
    val userSelectedTrackTemp = remember { mutableIntStateOf(26) }

    Box(modifier = Modifier.fillMaxSize()) {
        // UI page state management
        val isSelectTrackVisible = remember { mutableStateOf(true) }
        val isTrackDetailsVisible = remember { mutableStateOf(false) }
        val isTelemetryOptionsVisible = remember { mutableStateOf(false) }
        val isStrategyPredictionsVisible = remember { mutableStateOf(false) }
        val trackSelected = remember { mutableStateOf("") }

        val context = LocalContext.current // Needed for Python backend

        val strategyIsLoading = remember { mutableStateOf(false) } // Loading overlay flag
        val predictions = remember { mutableStateOf<List<StrategyPredictionClass>>(emptyList()) } // Prediction results

        // Step 1: Track selection screen
        SelectTrack(
            enabled = isSelectTrackVisible.value &&
                    !isTrackDetailsVisible.value &&
                    !isTelemetryOptionsVisible.value &&
                    !isStrategyPredictionsVisible.value,
            onTrackSelected = { trackName ->
                trackSelected.value = trackName
                isTrackDetailsVisible.value = true
            },
            onDismiss = {
                isSelectTrackVisible.value = false
                onExit()
            }
        )

        // Step 2: Show details of the selected track
        AnimatedVisibility(
            visible = isTrackDetailsVisible.value,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
        ) {
            TrackDetails(
                track = trackSelected.value,
                onReturnButtonClicked = {
                    isTrackDetailsVisible.value = false
                },
                onContinueButtonClicked = {
                    isTelemetryOptionsVisible.value = true
                }
            )
        }

        // Step 3: Let user customise simulation settings (telemetry inputs)
        AnimatedVisibility(
            visible = isTelemetryOptionsVisible.value,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
        ) {
            TelemetryOptions(
                track = trackSelected.value,
                onReturnButtonClicked = {
                    isTelemetryOptionsVisible.value = false
                },
                onContinueButtonClicked = { selectedAirTemp, selectedTrackTemp, isWet ->

                    // Save the user input values
                    userSelectedAirTemp.intValue = selectedAirTemp.toInt()
                    userSelectedTrackTemp.intValue = selectedTrackTemp.toInt()

                    // Reset state and show loading screen
                    predictions.value = emptyList()
                    isStrategyPredictionsVisible.value = true
                    strategyIsLoading.value = true

                    val trackNumber = getTrackNumber(trackSelected.value)

                    // Launch background task to run Python simulation
                    CoroutineScope(Dispatchers.IO).launch {
                        val startTime = System.currentTimeMillis()
                        try {
                            // Start Python interpreter if needed
                            if (!Python.isStarted()) {
                                Python.start(AndroidPlatform(context))
                            }

                            val python = Python.getInstance()
                            val allResults = mutableListOf<StrategyPredictionClass>()

                            // Simulate strategy for each starting position (1 to 20)
                            for (position in 1..20) {
                                val pyIsWet = if (isWet) python.builtins["True"] else python.builtins["False"]
                                Log.d("INPUTS", userSelectedAirTemp.intValue.toString() + userSelectedTrackTemp.intValue.toString() + pyIsWet.toString())

                                val pyObjectResult = python.getModule("strategy")
                                    .callAttr(
                                        "main",
                                        trackNumber,
                                        position,
                                        pyIsWet,
                                        userSelectedAirTemp.intValue,
                                        userSelectedTrackTemp.intValue
                                    )
                                val rawCsv = pyObjectResult.toString()

                                // Parse the returned CSV result from Python
                                val reader = CSVReader(StringReader(rawCsv))
                                val rows = reader.readAll()

                                val parsedResultsForPosition = rows.drop(1).mapNotNull { parts ->
                                    try {
                                        StrategyPredictionClass(
                                            bestStrategy = parts[0],
                                            bestConfidence = parts[1].toDouble(),
                                            numPitStops = parts[2].toInt(),
                                            alternatives = listOf(
                                                AlternativeStrategy(parts[3], parts[4].toDouble()),
                                                AlternativeStrategy(parts[5], parts[6].toDouble()),
                                                AlternativeStrategy(parts[7], parts[8].toDouble())
                                            )
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        null
                                    }
                                }

                                allResults.addAll(parsedResultsForPosition)
                            }

                            // Delay added to smooth UI transition
                            val elapsedTime = System.currentTimeMillis() - startTime
                            val delayTime = 1500 - elapsedTime
                            if (delayTime > 0) delay(delayTime)

                            // Show results in UI (main thread)
                            withContext(Dispatchers.Main) {
                                predictions.value = allResults
                                strategyIsLoading.value = false
                                Log.d("Parsed Result", allResults.toString())
                            }
                        } catch (e: Exception) {
                            Log.d("Python Error", e.toString())
                            withContext(Dispatchers.Main) {
                                strategyIsLoading.value = false
                            }
                        }
                    }
                }
            )
        }

        // Step 4: Display final strategy predictions or loading screen
        AnimatedVisibility(
            visible = isStrategyPredictionsVisible.value,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                // Main results screen (even if empty initially)
                StrategyPredictionsComposable(
                    trackSelected.value,
                    predictions = predictions.value,
                    onReturnButtonClicked = {
                        isStrategyPredictionsVisible.value = false
                    }
                )

                // Loading overlay while simulation runs
                if (strategyIsLoading.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f)), // Semi-transparent overlay
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

