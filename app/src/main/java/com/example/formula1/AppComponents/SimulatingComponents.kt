package com.example.formula1.AppComponents

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.Country
import com.example.formula1.QualifyingPredictionClass
import com.example.formula1.R
import com.example.formula1.TrackDetailsClass
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.floor

@Composable
fun SelectTrackHeaderComposable(text: String, fontSize: TextUnit, trackFlag: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Chevron inside a circle on the left side
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colorResource(id = R.color.light_red), shape = CircleShape)
                .clip(CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                tint = Color.Red,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (trackFlag != 0) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White)
                        .padding(2.dp)
                        .shadow(4.dp, RoundedCornerShape(5.dp))
                ) {
                    Image(
                        painter = painterResource(id = trackFlag),
                        contentDescription = "flag",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp)), 
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
            Spacer(Modifier.width(5.dp).height(65.dp))
            MediumText(text = text, fontSize = fontSize, color = Color.Black)
        }
    }
    Spacer(Modifier.height(20.dp))
    HorizontalDivider(
        thickness = 1.dp,
        color = colorResource(R.color.secondary_text)
    )
}

@Composable
fun SelectTrack(enabled: Boolean, onTrackSelected: (String) -> Unit, onDismiss: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val countries = remember {
                listOf(
                    Country("Australia", R.drawable.flag_australia),
                    Country("China", R.drawable.flag_china),
                    Country("Japan", R.drawable.flag_japan),
                    Country("Bahrain", R.drawable.flag_bahrain),
                    Country("Saudi Arabia", R.drawable.flag_saudi),
                    Country("Miami", R.drawable.flag_united_states),
                    Country("Imola", R.drawable.flag_italy),
                    Country("Monaco", R.drawable.flag_monaco),
                    Country("Spain", R.drawable.flag_spain),
                    Country("Canada", R.drawable.flag_canada),
                    Country("Austria", R.drawable.flag_austria),
                    Country("Great Britain", R.drawable.flag_great_britain),
                    Country("Belgium", R.drawable.flag_belgium),
                    Country("Hungary", R.drawable.flag_hungary),
                    Country("Netherlands", R.drawable.flag_netherlands),
                    Country("Italy", R.drawable.flag_italy),
                    Country("Azerbaijan", R.drawable.flag_azerbaijan),
                    Country("Singapore", R.drawable.flag_singapore),
                    Country("United States", R.drawable.flag_united_states),
                    Country("Mexico", R.drawable.flag_mexico),
                    Country("Brazil", R.drawable.flag_brazil),
                    Country("Las Vegas", R.drawable.flag_united_states),
                    Country("Qatar", R.drawable.flag_qatar),
                    Country("Abu Dhabi", R.drawable.flag_united_arab_emirates)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp, vertical = 32.dp)
                    .background(Color.White)
            ) {
                SelectTrackHeaderComposable(
                    text = "Select Track",
                    fontSize = 48.sp,
                    trackFlag = 0,
                    onClick = { onDismiss() }
                )
                Spacer(Modifier.height(20.dp))

                val numberOfColumns = 4
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp -
                        WindowInsets.statusBars.asPaddingValues().calculateTopPadding() -
                        WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                val availableHeight = screenHeight - 160.dp

                val rowsNeeded = countries.size / numberOfColumns
                val flagHeight = availableHeight / (rowsNeeded + 3)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(numberOfColumns),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(countries) { country ->
                        CountryItem(
                            country = country,
                            flagHeight = flagHeight,
                            onClick = { if (enabled) onTrackSelected(country.name) }
                        )
                    }
                }
            }
        }

}

@Composable
fun CountryItem(
    country: Country,
    flagHeight: Dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(flagHeight * 1.5f)
                .height(flagHeight)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(2.dp)
                .shadow(8.dp, RoundedCornerShape(10.dp))
                .clickable { onClick() }
        ) {
            Image(
                painter = painterResource(id = country.flagRes),
                contentDescription = "${country.name} flag",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds
            )
        }
        Spacer(Modifier.height(8.dp))
        RegularText(
            text = country.name,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}



@Composable
fun TrackDetails(track: String, onReturnButtonClicked: () -> Unit, onContinueButtonClicked: () -> Unit) {
    val trackDetails = remember(track) {
        getTrackDetails(track)
    }
    val trackName = trackDetails.name
    val trackFlag = trackDetails.trackFlag
    val trackLayout = trackDetails.trackLayout
    val firstGrandPrixYear = trackDetails.firstGrandPrix
    val numberOfLaps = trackDetails.numberOfLaps
    val fullThrottle = trackDetails.fullThrottle
    val circuitLength = trackDetails.circuitLength
    val raceDistance = trackDetails.raceDistance
    val longestFlatOutSection = trackDetails.longestFlatOutSection
    val lapRecordTime = trackDetails.lapRecordTime
    val lapRecordDriver = trackDetails.lapRecordDriver
    val qualifyingLapRecordTime = trackDetails.qualifyingLapRecordTime
    val qualifyingLapRecordDriver = trackDetails.qualifyingLapRecordDriver


    val trackDetailsRow1 = listOf(
        Pair("First Grand Prix", firstGrandPrixYear),
        Pair("Number of Laps", numberOfLaps),
        Pair("Full Throttle %", fullThrottle)
    )
    val trackDetailsRow2 = listOf(
        Triple("Circuit Length", circuitLength, "km"),
        Triple("Race Distance", raceDistance, "km"),
        Triple("Longest Flat-Out Section", longestFlatOutSection, "m")
    )
    val trackDetailsRow3 = listOf(
        Triple("Lap Record", lapRecordTime, lapRecordDriver),
        Triple("Qualifying Lap Record", qualifyingLapRecordTime, qualifyingLapRecordDriver),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val borderColor = colorResource(R.color.secondary_text)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 32.dp)
                .background(Color.White)
        ) {
            SelectTrackHeaderComposable(
                text = trackName!!,
                fontSize = 24.sp,
                trackFlag = trackFlag!!,
                onClick = { onReturnButtonClicked() })
            Spacer(Modifier.height(20.dp))
            Image(
                painter = painterResource(id = trackLayout!!),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(Modifier.height(20.dp))
            // Display row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                trackDetailsRow1.forEach { (line1, line2) ->
                    DetailColumnItem(
                        line1,
                        line2,
                        null,
                        borderColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            // Display row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                trackDetailsRow2.forEach { (line1, line2, unit) ->
                    DetailColumnItem(
                        line1,
                        line2,
                        unit,
                        borderColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            // Display row 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                trackDetailsRow3.forEach { (line1, line2, driver) ->
                    DetailColumnItem(
                        line1,
                        line2,
                        driver,
                        borderColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(40.dp))
            LoginAndRegisterButton(
                text = "Continue",
                cornerRadius = 10.dp,
                opacity = 1f,
                onContinueClick = { onContinueButtonClicked() })
        }
    }
}

@Composable
fun DetailColumnItem(line1: String, line2: String?, line2Extra: String?, borderColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 2.dp, end = 2.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 10.dp
                )
            )
            .drawBehind {
                // Drawing borders with rounded corner
                drawRoundedCornerBorder(borderColor)
            }
            .padding(8.dp)
    ) {
        RegularText(text = line1, fontSize = 16.sp, color = Color.Black)
        line2?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BoldText(text = it, fontSize = 16.sp, color = Color.Black)
                // If content in line 2 extra
                line2Extra?.takeIf { it.isNotEmpty() }?.let { text ->
                    Spacer(modifier = Modifier.width(4.dp))
                    RegularText(text = text, fontSize = 14.sp, color = colorResource(R.color.secondary_text))
                }
            }
        }
    }
}

@Composable
fun DetailColumnItem2(line1: String, line2: String?, line2Extra: String?, textColor: Color, borderColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 2.dp, end = 2.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 10.dp
                )
            )
            .drawBehind {
                drawRoundedCornerBorder(borderColor)
            }
            .padding(8.dp)
    ) {
        RegularText(text = line1, fontSize = 16.sp, color = Color.Black)
        line2?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BoldText(text = it, fontSize = 16.sp, color = textColor)
                line2Extra?.takeIf { it.isNotEmpty() }?.let { text ->
                    Spacer(modifier = Modifier.width(4.dp))
                    RegularText(text = text, fontSize = 14.sp, color = colorResource(R.color.secondary_text))
                }
            }
        }
    }
}


fun DrawScope.drawRoundedCornerBorder(borderColor: Color) {
    val cornerRadius = 10.dp.toPx()
    val borderRadius = 2.dp.toPx()

    // Right border
    drawLine(
        color = borderColor,
        start = Offset(this.size.width, 0f),
        end = Offset(this.size.width, this.size.height - cornerRadius),
        strokeWidth = borderRadius
    )
    // Bottom border
    drawLine(
        color = borderColor,
        start = Offset(0f, this.size.height),
        end = Offset(this.size.width - cornerRadius, this.size.height),
        strokeWidth = borderRadius
    )
    // Rounded corner arc at bottom right
    drawArc(
        color = borderColor,
        startAngle = 0f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset(this.size.width - 2 * cornerRadius, this.size.height - 2 * cornerRadius),
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = borderRadius)
    )
}

fun getTrackDetails(track: String): TrackDetailsClass {
    val trackMap = mapOf(
        "Bahrain" to TrackDetailsClass("Bahrain International Circuit", R.drawable.flag_bahrain, R.drawable.circuit_bahrain, firstGrandPrix = "2004",numberOfLaps = "57",fullThrottle = "72",circuitLength = "5.412",raceDistance = "308.484",longestFlatOutSection = "1205",lapRecordTime = "1:31.447",lapRecordDriver = "Pedro de la Rosa (2005)",qualifyingLapRecordTime = "1:27.264",qualifyingLapRecordDriver = "Lewis Hamilton (2020)"),
        "Saudi Arabia" to TrackDetailsClass("Jeddah Corniche Circuit", R.drawable.flag_saudi, R.drawable.circuit_saudi_arabia, firstGrandPrix = "2021",numberOfLaps = "50",fullThrottle = "79",circuitLength = "6.175",raceDistance = "308.75",longestFlatOutSection = "1550",lapRecordTime = "1:30.734",lapRecordDriver = "Lewis Hamilton (2021)",qualifyingLapRecordTime = "1:27.472",qualifyingLapRecordDriver = "Max Verstappen (2024)"),
        "Australia" to TrackDetailsClass("Albert Park Grand Prix Circuit", R.drawable.flag_australia, R.drawable.circuit_australia, firstGrandPrix = "1928", numberOfLaps = "58", fullThrottle = "77", circuitLength = "5.303", raceDistance = "307.574", longestFlatOutSection = "843", lapRecordTime = "1:19.813", lapRecordDriver = "Charles Leclerc (2024)", qualifyingLapRecordTime = "1:15.915", qualifyingLapRecordDriver = "Max Verstappen (2024)"),
        "Japan" to TrackDetailsClass("Suzuka Circuit", R.drawable.flag_japan, R.drawable.circuit_japan, firstGrandPrix = "1963", numberOfLaps = "53", fullThrottle = "65", circuitLength = "5.807", raceDistance = "307.771", longestFlatOutSection = "1315", lapRecordTime = "1:30.983", lapRecordDriver = "Lewis Hamilton (2019)", qualifyingLapRecordTime = "1:27.064", qualifyingLapRecordDriver = "Sebastian Vettel (2019)"),
        "China" to TrackDetailsClass("Shanghai International Circuit", R.drawable.flag_china, R.drawable.circuit_china, firstGrandPrix = "2004", numberOfLaps = "56", fullThrottle = "70", circuitLength = "5.451", raceDistance = "305.256", longestFlatOutSection = "1202", lapRecordTime = "1:32.238", lapRecordDriver = "Michael Schumacher (2004)", qualifyingLapRecordTime = "1:31.095", qualifyingLapRecordDriver = "Valtteri Bottas (2018)"),
        "Miami" to TrackDetailsClass("Miami International Autodrome", R.drawable.flag_united_states, R.drawable.circuit_usa, firstGrandPrix = "2022", numberOfLaps = "57", fullThrottle = "76", circuitLength = "5.412", raceDistance = "308.484", longestFlatOutSection = "1255", lapRecordTime = "1:29.708", lapRecordDriver = "Max Verstappen (2023)", qualifyingLapRecordTime = "1:26.841", qualifyingLapRecordDriver = "Sergio Perez (2023)"),
        "Imola" to TrackDetailsClass("Autodromo Internazionale Enzo e Dino Ferrari", R.drawable.flag_italy, R.drawable.circuit_imola, firstGrandPrix = "1980", numberOfLaps = "63", fullThrottle = "67", circuitLength = "4.909", raceDistance = "309.267", longestFlatOutSection = "877", lapRecordTime = "1:15.484", lapRecordDriver = "Lewis Hamilton (2020)", qualifyingLapRecordTime = "1:13.609", qualifyingLapRecordDriver = "Valtteri Bottas (2020)"),
        "Monaco" to TrackDetailsClass("Circuit de Monaco", R.drawable.flag_monaco, R.drawable.circuit_monaco, firstGrandPrix = "1929", numberOfLaps = "78", fullThrottle = "34", circuitLength = "3.337", raceDistance = "260.286", longestFlatOutSection = "660", lapRecordTime = "1:12.909", lapRecordDriver = "Lewis Hamilton (2021)", qualifyingLapRecordTime = "1:10.166", qualifyingLapRecordDriver = "Lewis Hamilton (2019)"),
        "Canada" to TrackDetailsClass("Circuit Gilles Villeneuve", R.drawable.flag_canada, R.drawable.circuit_canada, firstGrandPrix = "1967", numberOfLaps = "70", fullThrottle = "76", circuitLength = "4.361", raceDistance = "305.27", longestFlatOutSection = "1190", lapRecordTime = "1:13.078", lapRecordDriver = "Valtteri Bottas (2019)", qualifyingLapRecordTime = "1:10.240", qualifyingLapRecordDriver = "Sebastian Vettel (2019)"),
        "Spain" to TrackDetailsClass("Circuit de Barcelona-Catalunya", R.drawable.flag_spain, R.drawable.circuit_spain, firstGrandPrix = "1951", numberOfLaps = "66", fullThrottle = "61", circuitLength = "4.657", raceDistance = "307.362", longestFlatOutSection = "1192", lapRecordTime = "1:16.330", lapRecordDriver = "Max Verstappen (2023)", qualifyingLapRecordTime = "1:11.383", qualifyingLapRecordDriver = "Lando Norris (2024)"),
        "Austria" to TrackDetailsClass("Red Bull Ring", R.drawable.flag_austria, R.drawable.circuit_austria, firstGrandPrix = "1963", numberOfLaps = "71", fullThrottle = "79", circuitLength = "4.318", raceDistance = "306.578", longestFlatOutSection = "868", lapRecordTime = "1:05.619", lapRecordDriver = "Carlos Sainz (2020)", qualifyingLapRecordTime = "1:02.939", qualifyingLapRecordDriver = "Valtteri Bottas (2020)"),
        "Great Britain" to TrackDetailsClass("Silverstone Circuit", R.drawable.flag_great_britain, R.drawable.circuit_great_britain, firstGrandPrix = "1950", numberOfLaps = "52", fullThrottle = "70", circuitLength = "5.891", raceDistance = "306.332", longestFlatOutSection = "1034", lapRecordTime = "1:27.097", lapRecordDriver = "Max Verstappen (2020)", qualifyingLapRecordTime = "1:24.303", qualifyingLapRecordDriver = "Lewis Hamilton (2020)"),
        "Hungary" to TrackDetailsClass("Hungaroring", R.drawable.flag_hungary, R.drawable.circuit_hungary, firstGrandPrix = "1986", numberOfLaps = "70", fullThrottle = "50", circuitLength = "4.381", raceDistance = "306.67", longestFlatOutSection = "908", lapRecordTime = "1:16.627", lapRecordDriver = "Lewis Hamilton (2020)", qualifyingLapRecordTime = "1:13.447", qualifyingLapRecordDriver = "Lewis Hamilton (2020)"),
        "Belgium" to TrackDetailsClass("Circuit de Spa-Francorchamps", R.drawable.flag_belgium, R.drawable.circuit_belgium, firstGrandPrix = "1925", numberOfLaps = "44", fullThrottle = "75", circuitLength = "7.004", raceDistance = "308.176", longestFlatOutSection = "2015", lapRecordTime = "1:44.701", lapRecordDriver = "Sergio Perez (2024)", qualifyingLapRecordTime = "1:41.252", qualifyingLapRecordDriver = "Lewis Hamilton (2020)"),
        "Netherlands" to TrackDetailsClass("Circuit Zandvoort", R.drawable.flag_netherlands, R.drawable.circuit_netherlands, firstGrandPrix = "1952", numberOfLaps = "72", fullThrottle = "59", circuitLength = "4.259", raceDistance = "306.648", longestFlatOutSection = "678", lapRecordTime = "1:11.097", lapRecordDriver = "Lewis Hamilton (2021)", qualifyingLapRecordTime = "1:08.885", qualifyingLapRecordDriver = "Max Verstappen (2021)"),
        "Italy" to TrackDetailsClass("Autodromo Nazionale Monza", R.drawable.flag_italy, R.drawable.circuit_italy, firstGrandPrix = "1950", numberOfLaps = "53", fullThrottle = "84", circuitLength = "5.793", raceDistance = "307.029", longestFlatOutSection = "1520", lapRecordTime = "1:21.046", lapRecordDriver = "Rubens Barrichello (2004)", qualifyingLapRecordTime = "1:18.887", qualifyingLapRecordDriver = "Lewis Hamilton (2020)"),
        "Azerbaijan" to TrackDetailsClass("Baku City Circuit", R.drawable.flag_azerbaijan, R.drawable.circuit_baku, firstGrandPrix = "2016", numberOfLaps = "51", fullThrottle = "77", circuitLength = "6.003", raceDistance = "306.153", longestFlatOutSection = "2010", lapRecordTime = "1:43.009", lapRecordDriver = "Charles Leclerc (2019)", qualifyingLapRecordTime = "1:40.203", qualifyingLapRecordDriver = "Charles Leclerc (2019)"),
        "Singapore" to TrackDetailsClass("Marina Bay Street Circuit", R.drawable.flag_singapore, R.drawable.circuit_singapore, firstGrandPrix = "2008", numberOfLaps = "61", fullThrottle = "49", circuitLength = "4.94", raceDistance = "301.34", longestFlatOutSection = "832", lapRecordTime = "1:34.486", lapRecordDriver = "Daniel Ricciardo (2024)", qualifyingLapRecordTime = "1:29.525", qualifyingLapRecordDriver = "Lando Norris (2024)"),
        "United States" to TrackDetailsClass("Circuit of the Americas", R.drawable.flag_united_states, R.drawable.circuit_usa, firstGrandPrix = "2012", numberOfLaps = "56", fullThrottle = "59", circuitLength = "5.513", raceDistance = "308.728", longestFlatOutSection = "1090", lapRecordTime = "1:36.169", lapRecordDriver = "Charles Leclerc (2019)", qualifyingLapRecordTime = "1:32.029", qualifyingLapRecordDriver = "Valtteri Bottas (2019)"),
        "Mexico" to TrackDetailsClass("Autodromo Hermanos Rodriguez", R.drawable.flag_mexico, R.drawable.circuit_mexico, firstGrandPrix = "1963", numberOfLaps = "71", fullThrottle = "45", circuitLength = "4.304", raceDistance = "305.584", longestFlatOutSection = "1200", lapRecordTime = "1:17.774", lapRecordDriver = "Valtteri Bottas (2021)", qualifyingLapRecordTime = "1:14.758", qualifyingLapRecordDriver = "Max Verstappen (2019)"),
        "Brazil" to TrackDetailsClass("Autodromo Jose Carlos Pace (Interlagos)", R.drawable.flag_brazil, R.drawable.circuit_brazil, firstGrandPrix = "1973", numberOfLaps = "71", fullThrottle = "64", circuitLength = "4.309", raceDistance = "305.939", longestFlatOutSection = "1394", lapRecordTime = "1:10.540", lapRecordDriver = "Valtteri Bottas (2018)", qualifyingLapRecordTime = "1:07.281", qualifyingLapRecordDriver = "Lewis Hamilton (2018)"),
        "Las Vegas" to TrackDetailsClass("Las Vegas Street Circuit", R.drawable.flag_united_states, R.drawable.circuit_las_vegas, firstGrandPrix = "2023", numberOfLaps = "50", fullThrottle = "74", circuitLength = "6.201", raceDistance = "310.05", longestFlatOutSection = "1800", lapRecordTime = "1:35.490", lapRecordDriver = "Oscar Piastri (2023)", qualifyingLapRecordTime = "1:32.726", qualifyingLapRecordDriver = "Charles Leclerc (2023)"),
        "Qatar" to TrackDetailsClass("Lusail International Circuit", R.drawable.flag_qatar, R.drawable.circuit_qatar, firstGrandPrix = "2021", numberOfLaps = "57", fullThrottle = "68", circuitLength = "5.419", raceDistance = "308.883", longestFlatOutSection = "1180", lapRecordTime = "1:24.319", lapRecordDriver = "Max Verstappen (2023)", qualifyingLapRecordTime = "1:20.827", qualifyingLapRecordDriver = "Lewis Hamilton (2021)"),
        "Abu Dhabi" to TrackDetailsClass("Yas Marina Circuit", R.drawable.flag_united_arab_emirates, R.drawable.circuit_abu_dhabi, firstGrandPrix = "2009", numberOfLaps = "58", fullThrottle = "63", circuitLength = "5.281", raceDistance = "306.298", longestFlatOutSection = "1233", lapRecordTime = "1:26.103", lapRecordDriver = "Max Verstappen (2021)", qualifyingLapRecordTime = "1:22.109", qualifyingLapRecordDriver = "Max Verstappen (2021)"),
    )
    return trackMap[track] ?: error("Track not found")
}

fun getTrackNumber(track: String): Int = when (track) {
    "Abu Dhabi"     -> 1
    "Australia"     -> 2
    "Austria"       -> 3
    "Azerbaijan"    -> 4
    "Bahrain"       -> 5
    "Belgium"       -> 6
    "Brazil"        -> 7
    "Canada"        -> 8
    "China"         -> 9
    "Great Britain" -> 10
    "Hungary"       -> 11
    "Imola"         -> 12
    "Italy"         -> 13
    "Japan"         -> 14
    "Las Vegas"     -> 15
    "Mexico"        -> 16
    "Miami"         -> 17
    "Monaco"        -> 18
    "Netherlands"   -> 19
    "Qatar"         -> 20
    "Saudi Arabia"  -> 21
    "Singapore"     -> 22
    "Spain"         -> 23
    "United States" -> 24
    else -> throw IllegalArgumentException("Unknown track: $track")
}

val recommendedTemperatures = mapOf(
    "Abu Dhabi" to (28f to 33f),
    "Australia" to (22f to 36f),
    "Austria" to (24f to 37f),
    "Azerbaijan" to (26f to 44f),
    "Bahrain" to (23f to 27f),
    "Belgium" to (20f to 36f),
    "Great Britain" to (19f to 30f),
    "Canada" to (19f to 34f),
    "China" to (19f to 30f),
    "Netherlands" to (20f to 31f),
    "Imola" to (19f to 30f),
    "Hungary" to (26f to 40f),
    "Italy" to (30f to 45f),
    "Japan" to (22f to 33f),
    "Las Vegas" to (18f to 18f),
    "Mexico" to (23f to 41f),
    "Miami" to (29f to 41f),
    "Monaco" to (22f to 43f),
    "Qatar" to (26f to 31f),
    "Saudi Arabia" to (26f to 31f),
    "Singapore" to (29f to 34f),
    "Spain" to (27f to 41f),
    "Brazil" to (22f to 39f),
    "United States" to (29f to 41f)
)


@Composable
fun TelemetryOptions(track: String, onReturnButtonClicked: () -> Unit, onContinueButtonClicked: (Float, Float, Boolean) -> Unit) {
    val (recommendedAirTemp, recommendedTrackTemp) = recommendedTemperatures[track] ?: (26f to 26f)

    var airTemperature by remember { mutableFloatStateOf(recommendedAirTemp) }
    var trackTemperature by remember { mutableFloatStateOf(recommendedTrackTemp) }
    var selectedCondition by remember { mutableStateOf("Dry") }

    val trackDetails = remember(track) {
        getTrackDetails(track)
    }
    val trackName = trackDetails.name
    val trackFlag = trackDetails.trackFlag
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 32.dp)
                .background(Color.White)
        ) {
            SelectTrackHeaderComposable(
                text = trackName,
                fontSize = 24.sp,
                trackFlag = trackFlag,
                onClick = { onReturnButtonClicked() })
            Spacer(Modifier.height(32.dp))
            BoldText(text = "Race Conditions", fontSize = 24.sp, color = Color.Black)
            Spacer(Modifier.height(16.dp))
            TemperatureSliderBox(
                label = "Air Temperature",
                temperature = airTemperature,
                onTemperatureChange = { airTemperature = it },
                initialTemperature = recommendedAirTemp,
                valueRange = 15f..35f
            )

            Spacer(modifier = Modifier.height(16.dp))

            TemperatureSliderBox(
                label = "Track Temperature",
                temperature = trackTemperature,
                onTemperatureChange = { trackTemperature = it },
                initialTemperature = recommendedTrackTemp,
                valueRange = 15f..50f
            )

            Spacer(modifier = Modifier.height(16.dp))

            TrackConditionsBox(
                selectedCondition = selectedCondition,
                onConditionSelected = { selectedCondition = it }
            )

            Spacer(Modifier.height(16.dp))
            LoginAndRegisterButton(
                text = "Simulate",
                cornerRadius = 10.dp,
                opacity = 1f,
                onContinueClick = {
                    val isWet = selectedCondition == "Wet"
                    onContinueButtonClicked(airTemperature, trackTemperature, isWet)
                })
        }
    }
}

@Composable
fun TemperatureSliderBox(
    label: String,
    temperature: Float,
    onTemperatureChange: (Float) -> Unit,
    initialTemperature: Float,
    valueRange: ClosedFloatingPointRange<Float> = 15f..35f
) {
    // Calculate middle of the range
    val midPoint = (valueRange.start + valueRange.endInclusive) / 2f

    // Switch color based on cold/hot
    val isCold = temperature < midPoint
    val activeColor = if (isCold) Color(0xFF2196F3) else Color.Red // Blue or Red
    val inactiveColor = if (isCold) Color(0xFF90CAF9) else Color(0xFFFFCDD2) // Light Blue or Light Red

    val showRecommended = temperature.toInt() == initialTemperature.toInt()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFBFBFBF),
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.dmsans_regular))
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp, start = 8.dp)
            )
            Slider(
                value = temperature,
                onValueChange = { onTemperatureChange(it) },
                valueRange = valueRange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(
                    thumbColor = activeColor,
                    activeTrackColor = activeColor,
                    inactiveTrackColor = inactiveColor
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                    Text(text = "${temperature.toInt()}°C",style = TextStyle(fontSize = 20.sp,color = activeColor,fontFamily = FontFamily(Font(R.font.dmsans_medium))))

                    if (showRecommended) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp), // optional fine-tuning
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(Color(0xFF4CAF50), shape = CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                MediumText(text = "Recommended",fontSize = 20.sp,color = Color(0xFF4CAF50))
                            }
                        }
                    }
            }
        }
    }
}

@Composable
fun TrackConditionsBox(selectedCondition: String, onConditionSelected: (String) -> Unit   ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFBFBFBF),
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Track Conditions",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.dmsans_regular))
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp, start = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // Dry on Left
                TrackConditionImage(
                    modifier = Modifier.weight(1f),
                    label = "Dry",
                    isSelected = selectedCondition == "Dry",
                    imageResId = R.drawable.dry_conditions_image,
                    cornerShape = RoundedCornerShape(
                        topStart = 10.dp,
                        bottomStart = 10.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    onClick = { onConditionSelected("Dry") }
                )
                // Wet on Right
                TrackConditionImage(
                    modifier = Modifier.weight(1f),
                    label = "Wet",
                    isSelected = selectedCondition == "Wet",
                    imageResId = R.drawable.wet_conditions_image,
                    cornerShape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 10.dp,
                        bottomEnd = 10.dp
                    ),
                    onClick = { onConditionSelected("Wet") }
                )
            }
            Spacer(Modifier.height(8.dp))
            if (selectedCondition == "Dry") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFF4CAF50), shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        MediumText(text = "Recommended",fontSize = 20.sp,color = Color(0xFF4CAF50))
                    }
                }
            }
        }
    }
}

@Composable
fun TrackConditionImage(
    modifier: Modifier,
    label: String,
    isSelected: Boolean,
    @DrawableRes imageResId: Int,
    cornerShape: RoundedCornerShape,
    onClick: () -> Unit
) {
    val colorMatrix = ColorMatrix().apply {
        if (!isSelected) {
            setToSaturation(0f)
        }
    }
    val painter = painterResource(id = imageResId)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() }
            .clip(cornerShape)
            .background(Color.LightGray.copy(alpha = if (isSelected) 1f else 0.5f))
    ) {
        Image(
            painter = painter,
            contentDescription = label,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(), colorFilter = (ColorFilter.colorMatrix(colorMatrix))
        )
    }
}





@Composable
fun QualifyingPredictionsComposable(
    track: String,
    predictions: List<QualifyingPredictionClass>,
    onReturnButtonClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Header (Red top bar)
        QualifyingPredictionsHeaderComposable(track = track, onReturnButtonClicked = onReturnButtonClicked)

        // 2. Column headers
        QualifyingPredictionsSecondaryHeaderComposable()

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
                // getting the fastest sector times
                val fastestSector1Time = predictions.minOfOrNull { it.sector1Time }
                val fastestSector2Time = predictions.minOfOrNull { it.sector2Time }
                val fastestSector3Time = predictions.minOfOrNull { it.sector3Time }
                predictions.forEachIndexed { index, lap ->
                    val isSelected = selectedRowIndex.value == null || selectedRowIndex.value == index
                    AnimatedVisibility(visible = rowVisibility.getOrNull(index)?.value == true && isSelected) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedRowIndex.value = if (selectedRowIndex.value == index) null else index
                                }
                                .background(Color.White)
                                .padding(vertical = 16.dp, horizontal = 20.dp)
                        ) {
                            Text(
                                text = lap.position.toString(),
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f, fill = true),
                                fontFamily = FontFamily(Font(R.font.dmsans_medium))
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(5.dp)
                                    .background(teamColor(lap.teamName))
                            )
                            Text(
                                text = lap.driverName,
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f, fill = true),
                                fontFamily = FontFamily(Font(R.font.dmsans_medium))
                            )
                            Text(
                                text = lap.teamName,
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
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color(0xFFD9D9D9).copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(30.dp)
                                                )
                                                .padding(
                                                    vertical = 6.dp,
                                                    horizontal = 12.dp
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = formatLapTime(lap.lapTime),
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
                        // Driver Image
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(teamColor(lap.teamName)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = getDriverImageRes(lap.driverName)),
                                contentDescription = "Driver image",
                                modifier = Modifier
                                    .width(250.dp),
                                contentScale = ContentScale.FillWidth
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = getDriverFullName(lap.driverName),
                                style = TextStyle(fontSize = 40.sp,color = Color.White,fontFamily = FontFamily(Font(R.font.russo_one_regular))),
                                textAlign = TextAlign.Start
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp)
                        ) {
                            val advancedDataRow1 = listOf(
                                Triple("Sector 1", lap.sector1Time, "s"),
                                Triple("Sector 2", lap.sector2Time, "s"),
                                Triple("Sector 3", lap.sector3Time, "s")
                            )

                            val advancedDataRow2 = listOf(
                                Triple("RMP", lap.sector1RPMAvg, ""),
                                Triple("Throttle", lap.sector1ThrottleAvg, "%"),
                                Triple("Brake", lap.sector1BrakeAvg * 100, "%")
                            )

                            val advancedDataRow3 = listOf(
                                Triple("RMP", lap.sector2RPMAvg, ""),
                                Triple("Throttle", lap.sector2ThrottleAvg, "%"),
                                Triple("Brake", lap.sector2BrakeAvg * 100, "%")
                            )

                            val advancedDataRow4 = listOf(
                                Triple("RMP", lap.sector3RPMAvg, ""),
                                Triple("Throttle", lap.sector3ThrottleAvg, "%"),
                                Triple("Brake", lap.sector3BrakeAvg * 100, "%")
                            )

                            val advancedDataRow5 = listOf(
                                Triple("Sector 1", lap.speedI1, "km/h"),
                                Triple("Sector 2", lap.speedI2, "km/h"),
                            )

                            val advancedDataRow6 = listOf(
                                Triple("Finish Line", lap.speedFL, "km/h"),
                                Triple("Longest Straight", lap.speedST, "km/h"),
                            )

                            // Sector Breakdown
                            MediumText(text = "Sector Breakdown", fontSize = 16.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                advancedDataRow1.forEach { (line1, line2, unit) ->
                                    DetailColumnItem2(
                                        line1,
                                        String.format(Locale.US, "%.3f", line2.toString().toDouble()),
                                        unit,
                                        if (line2 == fastestSector1Time || line2 == fastestSector2Time || line2 == fastestSector3Time) Color(0xFF9D00FF) else Color.Black,
                                        Color(0xFFBFBFBF),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            // Space between
                            Spacer(modifier = Modifier.height(12.dp))
                            // Sector 1 Averages
                            MediumText(text = "Sector 1 Averages", fontSize = 16.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                advancedDataRow2.forEach { (line1, line2, unit) ->
                                    DetailColumnItem(
                                        line1,
                                        String.format(Locale.US, "%.3f", line2.toString().toDouble()),
                                        unit,
                                        Color(0xFFBFBFBF),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            // Space between
                            Spacer(modifier = Modifier.height(12.dp))
                            // Sector 2 Averages
                            MediumText(text = "Sector 2 Averages", fontSize = 16.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                advancedDataRow3.forEach { (line1, line2, unit) ->
                                    DetailColumnItem(
                                        line1,
                                        String.format(Locale.US, "%.3f", line2.toString().toDouble()),
                                        unit,
                                        Color(0xFFBFBFBF),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            // Space between
                            Spacer(modifier = Modifier.height(12.dp))
                            // Sector 3 Averages
                            MediumText(text = "Sector 3 Averages", fontSize = 16.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                advancedDataRow4.forEach { (line1, line2, unit) ->
                                    DetailColumnItem(
                                        line1,
                                        String.format(Locale.US, "%.3f", line2.toString().toDouble()),
                                        unit,
                                        Color(0xFFBFBFBF),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            // Space between
                            Spacer(modifier = Modifier.height(12.dp))
                            // Speed Traps
                            MediumText(text = "Speed Traps", fontSize = 16.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                advancedDataRow5.forEach { (line1, line2, unit) ->
                                    DetailColumnItem(
                                        line1,
                                        String.format(Locale.US, "%.3f", line2.toString().toDouble()),
                                        unit,
                                        Color(0xFFBFBFBF),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                advancedDataRow6.forEach { (line1, line2, unit) ->
                                    DetailColumnItem(
                                        line1,
                                        String.format(Locale.US, "%.3f", line2.toString().toDouble()),
                                        unit,
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
fun QualifyingPredictionsHeaderComposable(track: String, onReturnButtonClicked: () -> Unit) {
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
        
        Box(
            modifier = Modifier
                .weight(6f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BoldText(text = "QUALIFYING PREDICTIONS", color = Color.White, fontSize = 24.sp)
                BoldText(text = "$track 2025", color = Color.White, fontSize = 16.sp)
            }
        }

        // Right side: spacer to balance the icon
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun QualifyingPredictionsSecondaryHeaderComposable() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF575757))
            .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        listOf("POS", "DRIVER", "CONSTRUCTOR", "LAP TIME").forEach { label ->
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f, fill = true) ,
                fontFamily = FontFamily(Font(R.font.dmsans_medium))
            )
        }
    }
}

fun getDriverImageRes(driverName: String): Int {
    val driverImageMap = mapOf(
        "ALB" to R.drawable.albon,
        "ALO" to R.drawable.alonso,
        "ANT" to R.drawable.antonelli,
        "BOR" to R.drawable.bortoleto,
        "DOO" to R.drawable.doohan,
        "GAS" to R.drawable.gasly,
        "HAD" to R.drawable.hadjar,
        "HAM" to R.drawable.hamilton,
        "HUL" to R.drawable.hulkenberg,
        "LAW" to R.drawable.lawson,
        "LEC" to R.drawable.leclerc,
        "NOR" to R.drawable.norris,
        "OCO" to R.drawable.ocon,
        "PIA" to R.drawable.piastri,
        "RUS" to R.drawable.russell,
        "SAI" to R.drawable.sainz,
        "STR" to R.drawable.stroll,
        "TSU" to R.drawable.tsunoda,
        "VER" to R.drawable.verstappen,
        "BEA" to R.drawable.bearman,
    )
    return driverImageMap[driverName] ?: R.drawable.hamilton
}

fun getDriverFullName(driverName: String): String {
    val driverNameMap = mapOf(
        "ALB" to "Alexander Albon",
        "ALO" to "Fernando Alonso",
        "ANT" to "Kimi Antonelli",
        "BOR" to "Gabriel Bortoleto",
        "DOO" to "Jack Doohan",
        "GAS" to "Pierre Gasly",
        "HAD" to "Isack Hadjar",
        "HAM" to "Lewis Hamilton",
        "HUL" to "Nico Hulkenberg",
        "LAW" to "Liam Lawson",
        "LEC" to "Charles Leclerc",
        "NOR" to "Lando Norris",
        "OCO" to "Esteban Ocon",
        "PIA" to "Oscar Piastri",
        "RUS" to "George Russell",
        "SAI" to "Carlos Sainz",
        "STR" to "Lance Stroll",
        "TSU" to "Yuki Tsunoda",
        "VER" to "Max Verstappen",
        "BEA" to "Oliver Bearman",
    )
    return driverNameMap[driverName] ?: "Lewis Hamilton"
}

fun formatLapTime(seconds: Double): String {
    val minutes = floor(seconds / 60).toInt()
    val remainingSeconds = seconds % 60
    return String.format(Locale.US, "%d:%06.3f", minutes, remainingSeconds)
}

fun teamColor(team: String): Color = when (team) {
    "RED BULL"   -> Color(0xFF3671C6)
    "FERRARI"    -> Color(0xFFE8002D)
    "MERCEDES"   -> Color(0xFF27F4D2)
    "MCLAREN"    -> Color(0xFFFF8000)
    "ASTON MARTIN" -> Color(0xFF229971)
    "WILLIAMS" -> Color(0xFF64C4FF)
    "ALPINE" -> Color(0xFFFF87BC)
    "KICK" -> Color(0xFF52E252)
    "HAAS" -> Color(0xFFB6BABD)
    "RACING BULL" -> Color(0xFF6692FF)
    else         -> Color.Gray            // fallback
}
