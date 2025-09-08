package com.example.formula1.AppComponents

import F1TeamAdditionalInformation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.formula1.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun ModellingHeaderComposable(onReturnButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
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
                .weight(6f) // More weight to center it better
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            RussoOneText(text = "MODELLING SIMULATION", color = Color.White, fontSize = 24.sp)
        }

        // Right side: spacer to balance the icon
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ModellingTeamHeaderComposable(team: String) {
    Row(
        modifier = Modifier
            .padding(24.dp)
            .height(35.dp) // Adjust height as needed
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(Color.Red)
        )

        // Black label box
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .background(Color.Black, shape = RoundedCornerShape(2.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            RussoOneText(text = team, fontSize = 20.sp, color = Color.White)
        }
    }
}

@Composable
fun ModellingTeamSelectionComposable(
    teamLogos: List<Int>,
    teamColours: List<Color>,
    onTeamSelected: (Int) -> Unit     // index callback
) {
    // remember which team is selected
    var selectedIndex by remember { mutableIntStateOf(0) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp),
        contentPadding = PaddingValues(
            end   = 24.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(teamLogos) { index, logoRes ->
            val boxColor = teamColours.getOrNull(index) ?: Color.Gray
            // each item is a clickable box
            Box(
                modifier = Modifier
                    .size(width = 275.dp, height = 140.dp)
                    .alpha(if (index == selectedIndex) 1f else 0.5f)
                    .border(
                        width = if (index == selectedIndex) 4.dp else 0.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(bottomEnd = 20.dp)
                    )
                    .clip(RoundedCornerShape(bottomEnd = 20.dp))
                    .background(boxColor)
                    .clickable {
                        selectedIndex = index
                        onTeamSelected(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

private enum class SheetState {
    HIDDEN,
    VISIBLE
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeableModal(
    showModal: Boolean,
    team: F1TeamAdditionalInformation,
    teamColor: Color,
    driverPairs: Pair<String, String>,
    onDismiss: () -> Unit,
    sheetHeight: Dp = 850.dp
) {
    if (!showModal) return

    val sheetHeightPx = with(LocalDensity.current) { sheetHeight.toPx() }
    val swipeableState = rememberSwipeableState(initialValue = SheetState.HIDDEN)
    val scope = rememberCoroutineScope()

    var shownOnce by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        swipeableState.snapTo(SheetState.HIDDEN)
        swipeableState.animateTo(
            SheetState.VISIBLE,
            anim = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        shownOnce = true
    }

    LaunchedEffect(swipeableState.currentValue, swipeableState.isAnimationRunning) {
        if (
            shownOnce &&
            !swipeableState.isAnimationRunning &&
            swipeableState.currentValue == SheetState.HIDDEN
        ) {
            onDismiss()
        }
    }


    val anchors = mapOf(
        0f to SheetState.VISIBLE,
        sheetHeightPx to SheetState.HIDDEN
    )

    Box(Modifier.fillMaxSize()) {
        // Dimmed background
        Box(Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable {
                // optional: tap outside to dismiss
                scope.launch { swipeableState.animateTo(SheetState.HIDDEN) }
            }
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(x = 0, y = swipeableState.offset.value.roundToInt()) }
                .fillMaxWidth()
                .height(sheetHeight)
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Vertical,
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .align(Alignment.BottomCenter)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.drag_icon),
                    contentDescription = "Drag to dismiss",
                    modifier = Modifier
                        .size(64.dp)
                )
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                color = teamColor,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = team.logoRes),
                            contentDescription = "ogo",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                    RussoOneText(text = team.name, fontSize = 24.sp, color = Color.Black)
                }
                Spacer(Modifier.height(24.dp))
                LabelValueRow(label = "Base:", value = team.base)
                LabelValueRow(label = "Team Chief:", value = team.teamChief)
                LabelValueRow(label = "Technical Chief:", value = team.techChief)
                LabelValueRow(label = "Chassis:", value = team.chassis)
                LabelValueRow(label = "Power Unit:", value = team.powerUnit)
                LabelValueRow(label = "World Championships:", value = team.worldChampionships)
                LabelValueRow(label = "Highest Race Finish:", value = team.highestRaceFinish)
                LabelValueRow(label = "Pole Positions:", value = team.polePositions)
                LabelValueRow(label = "Fastest Laps:", value = team.fastestLaps)
                Spacer(Modifier.height(24.dp))
                // Driver Image
                Column(modifier = Modifier.fillMaxWidth()) {
                        DriverRow(driverName = driverPairs.first, teamColor = teamColor)
                        Spacer(modifier = Modifier.height(12.dp))
                        DriverRow(driverName = driverPairs.second, teamColor = teamColor)
                        Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun LabelValueRow(label: String,value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MediumText(text = label, fontSize = 20.sp, color = Color.Black)
        RegularText(text = value, fontSize = 20.sp, color = Color(0xFFBFBFBF))
    }
}

@Composable
fun DriverRow(driverName: String, teamColor: Color, modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxWidth() .clip(RoundedCornerShape(10.dp))
        .border(1.dp, Color(0xFFBFBFBF), RoundedCornerShape(10.dp))
        .background(Color.White),) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(teamColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = getDriverImageRes(driverName)),
                    contentDescription = "Driver image",
                    modifier = Modifier
                        .size(110.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = getDriverFullName(driverName),
                style = TextStyle(
                    fontSize = 40.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.russo_one_regular))
                ),
                textAlign = TextAlign.Start
            )
        }
    }
}
