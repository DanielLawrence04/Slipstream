package com.example.formula1.AppComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.AlternativeStrategy
import com.example.formula1.R
import com.example.formula1.StrategyPredictionClass
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class TrackInformation(
    val averageSpeed: Double,
    val trackType: String,
    val overtakingDifficulty: String,
    val numberOfLaps: Int
)

val trackDataMap = mapOf(
    "Abu Dhabi" to TrackInformation(274.5283157, "high_speed", "medium", 55),
    "Australia" to TrackInformation(259.9952934, "high_speed", "medium", 58),
    "Austria" to TrackInformation(259.9952934, "high_speed", "medium", 71),
    "Azerbaijan" to TrackInformation(259.0738434, "high_speed", "medium", 51),
    "Bahrain" to TrackInformation(263.2997879, "high_speed", "low", 57),
    "Belgium" to TrackInformation(259.9952934, "high_speed", "medium", 44),
    "Brazil" to TrackInformation(259.9952934, "high_speed", "medium", 71),
    "Canada" to TrackInformation(259.9952934, "high_speed", "medium", 70),
    "China" to TrackInformation(259.9952934, "high_speed", "medium", 56),
    "Great Britain" to TrackInformation(259.9952934, "high_speed", "medium", 52),
    "Hungary" to TrackInformation(259.9952934, "high_speed", "medium", 70),
    "Imola" to TrackInformation(259.9952934, "high_speed", "medium", 63),
    "Italy" to TrackInformation(259.9952934, "high_speed", "medium", 53),
    "Japan" to TrackInformation(259.9952934, "high_speed", "medium", 53),
    "Las Vegas" to TrackInformation(239.0699816, "high_speed", "medium", 50),
    "Mexico" to TrackInformation(280.6934188, "high_speed", "medium", 71),
    "Miami" to TrackInformation(235.5888109, "high_speed", "medium", 57),
    "Monaco" to TrackInformation(219.7982475, "medium_speed", "high", 78),
    "Netherlands" to TrackInformation(259.9952934, "high_speed", "medium", 72),
    "Qatar" to TrackInformation(261.351997, "high_speed", "low", 57),
    "Saudi Arabia" to TrackInformation(280.598438, "high_speed", "medium", 50),
    "Singapore" to TrackInformation(271.4267409, "high_speed", "high", 61),
    "Spain" to TrackInformation(259.9952934, "high_speed", "medium", 66),
    "United States" to TrackInformation(213.3495528, "medium_speed", "low", 56)
)

fun getAdvancedDataForTrack(trackName: String): Pair<List<Triple<String, String, String>>, List<Triple<String, String, String>>>? {
    val trackInfo = trackDataMap[trackName] ?: return null

    val advancedDataRow1 = listOf(
        Triple("Average Speed", "${trackInfo.averageSpeed}", "km/h"),
        Triple("Track Type", trackInfo.trackType, "")
    )

    val advancedDataRow2 = listOf(
        Triple("Overtaking Difficulty", trackInfo.overtakingDifficulty, ""),
        Triple("Number of Laps", "${trackInfo.numberOfLaps}", "")
    )

    return advancedDataRow1 to advancedDataRow2
}

@Composable
fun StrategyPredictionsComposable(
    track: String,
    predictions: List<StrategyPredictionClass>,
    onReturnButtonClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Header (Red top bar)
        StrategyPredictionsHeaderComposable(
            track = track,
            onReturnButtonClicked = onReturnButtonClicked
        )

        // 2. Column headers
        StrategyPredictionsSecondaryHeaderComposable()

        // 3. Data rows
        val scrollState = rememberScrollState()
        val rowVisibility = remember(predictions) {
            predictions.map { mutableStateOf(false) }
        }

        val showDividers = remember { mutableStateOf(false) }
        val selectedRowIndex = remember { mutableStateOf<Int?>(null) }

        // staggered animation once predictions are loaded
        LaunchedEffect(predictions) {
            predictions.forEachIndexed { index, _ ->
                delay(100)
                rowVisibility.getOrNull(index)?.value = true
                if (index == predictions.lastIndex) {
                    delay(100)
                    showDividers.value = true
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(Modifier.verticalScroll(scrollState).fillMaxSize().background(Color.White)) {
                predictions.forEachIndexed { index, strat ->
                    val isSelected =
                        selectedRowIndex.value == null || selectedRowIndex.value == index
                    AnimatedVisibility(visible = rowVisibility.getOrNull(index)?.value == true && isSelected) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedRowIndex.value =
                                        if (selectedRowIndex.value == index) null else index
                                }
                                .background(Color.White)
                                .padding(vertical = 16.dp, horizontal = 20.dp)
                        ) {
                            Text(
                                text = (index+1).toString(),
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f, fill = true),
                                fontFamily = FontFamily(Font(R.font.dmsans_medium))
                            )
                            Text(
                                text = strat.numPitStops.toString(),
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f, fill = true),
                                fontFamily = FontFamily(Font(R.font.dmsans_medium))
                            )
                            Text(
                                text = strat.bestStrategy,
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f, fill = true),
                                fontFamily = FontFamily(Font(R.font.dmsans_medium))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f, fill = true),
                                contentAlignment = Alignment.Center
                            ) {
                                val (confidenceLabel, confidenceColor) = when {
                                    strat.bestConfidence < 0.1 -> "Low" to Color(0xFFFFCDD2)
                                    strat.bestConfidence < 0.3 -> "Medium" to Color(0xFFFFF9C4)
                                    else -> "High" to Color(0xFFC8E6C9)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = confidenceColor,
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .padding(
                                            vertical = 6.dp,
                                            horizontal = 12.dp
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = confidenceLabel,
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.dmsans_medium))
                                    )
                                }
                            }
                        }
                        AnimatedVisibility(
                            visible = showDividers.value,
                            enter = fadeIn(animationSpec = tween(durationMillis = 500))
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color(0xFFBFBFBF)
                            )
                        }
                    }
                }
                selectedRowIndex.value?.let { index ->
                    val selectedLap = predictions.getOrNull(index)
                    selectedLap?.let { lap ->
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color(0xFFBFBFBF)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        val allStrategies = buildList {
                            add(AlternativeStrategy(lap.bestStrategy, lap.bestConfidence))
                            addAll(lap.alternatives)
                        }

                        StrategyPredictionBox(allStrategies = allStrategies)
                        val (advancedDataRow1, advancedDataRow2) = getAdvancedDataForTrack(track) ?: (emptyList<Triple<String, String, String>>() to emptyList())

                        Column(Modifier.fillMaxWidth().padding(16.dp)) {
                            MediumText(text = "Additional Track Details", fontSize = 16.sp, color = Color.Black)
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                advancedDataRow1.forEach { (line1, line2, unit) ->
                                    DetailColumnItem2(
                                        line1,
                                        line2,
                                        unit,
                                        Color.Black,
                                        Color(0xFFBFBFBF),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                advancedDataRow2.forEach { (line1, line2, unit) ->
                                    DetailColumnItem2(
                                        line1,
                                        line2,
                                        unit,
                                        Color.Black,
                                        Color(0xFFBFBFBF),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun StrategyPredictionsHeaderComposable(track: String, onReturnButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF1801))
            .padding(vertical = 20.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Chevron Icon
        IconButton(
            onClick = { onReturnButtonClicked () },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Center: Title & Subtitle absolutely centered
        Box(
            modifier = Modifier
                .weight(6f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BoldText(text = "STRATEGY SIMULATIONS", color = Color.White, fontSize = 24.sp)
                BoldText(text = "$track 2025", color = Color.White, fontSize = 16.sp)
            }
        }

        // Right side: spacer to balance the icon
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun StrategyPredictionsSecondaryHeaderComposable() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF575757))
            .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        listOf("POS", "PIT STOPS", "BEST STRATEGY", "CONFIDENCE").forEach { label ->
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f, fill = true),
                fontFamily = FontFamily(Font(R.font.dmsans_medium))
            )
        }
    }
}

@Composable
fun StrategyPredictionBox(allStrategies: List<AlternativeStrategy>) {
    val pagerState = rememberPagerState(pageCount = { allStrategies.size })
    val coroutineScope = rememberCoroutineScope()

    val tyreMap = mapOf(
        "S" to R.drawable.soft_tyre,
        "M" to R.drawable.medium_tyre,
        "H" to R.drawable.hard_tyre,
        "I" to R.drawable.intermediate_tyre,
        "W" to R.drawable.wet_tyre
    )

    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(state = pagerState,pageSpacing = 12.dp,key = { it },modifier = Modifier.fillMaxWidth()) { page ->
                    val currentStrategy = allStrategies[page]
                    Row(
                        modifier = Modifier.fillMaxWidth().width(100.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(150.dp).height(80.dp)) {
                            Text(
                                text = "Strategy (${page + 1})",
                                style = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_bold)),textDecoration = TextDecoration.Underline),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = currentStrategy.strategy,
                                style = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_regular)),),
                                textAlign = TextAlign.Center
                            )

                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(150.dp).height(80.dp)) {
                            Text(
                                text = "Pit Stops",
                                style = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_bold)),textDecoration = TextDecoration.Underline),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = calculatePitStops(currentStrategy.strategy).toString(),
                                style = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_regular))),
                                textAlign = TextAlign.Center
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(150.dp).height(80.dp)) {
                            Text(
                                text = "Confidence",
                                style = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_bold)),textDecoration = TextDecoration.Underline),
                                textAlign = TextAlign.Center
                            )
                            val confidenceLabel = when {
                                currentStrategy.confidence < 0.1 -> "Low"
                                currentStrategy.confidence < 0.3 -> "Medium"
                                else -> "High"
                            }
                            Text(
                                text = confidenceLabel,
                                style = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_regular))),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val currentStrategy = allStrategies[pagerState.currentPage]
                    val compoundCounts = currentStrategy.strategy.groupingBy { it }.eachCount()

                    tyreMap.forEach { (compound, imageRes) ->
                        val count = compoundCounts[compound.first()] ?: 0
                        val isUsed = count > 0

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            if (isUsed) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.Red.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = count.toString(),
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.dmsans_regular)),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                            } else {
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = compound,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(top = 4.dp)
                                    .graphicsLayer {
                                        alpha = if (isUsed) 1f else 0.25f
                                    },
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(allStrategies.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(if (pagerState.currentPage == index) 10.dp else 6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (pagerState.currentPage == index) Color.Black else Color.Gray)
                        )
                    }
                }
            }

            // 👈 Left Chevron
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage - 1).coerceAtLeast(0))
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous",
                    tint = Color.Black
                )
            }

            // 👉 Right Chevron
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage + 1).coerceAtMost(allStrategies.size - 1))
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next",
                    tint = Color.Black
                )
            }
        }
    }
}

private fun calculatePitStops(strategy: String): Int {
    return strategy.count { it == '-' }
}

