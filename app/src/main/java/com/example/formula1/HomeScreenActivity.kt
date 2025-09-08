package com.example.formula1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.AppComponents.BoldText
import com.example.formula1.AppComponents.DailyTipComposable
import com.example.formula1.AppComponents.HomeHeaderComposable
import com.example.formula1.AppComponents.NextRaceComposable
import com.example.formula1.AppComponents.RussoOneText
import com.example.formula1.AppComponents.SelectStandingsComposable
import com.example.formula1.AppComponents.StandingsItem
import java.util.Calendar


class HomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}

@Composable
fun HomeScreen(nextRace: RaceList?, driverStandingsViewModel: List<DriverStanding>?, constructorsStandingsViewModel: List<ConstructorStanding>?) {
        // declaring all images
        val f1LogoWhitePainter = painterResource(R.drawable.f1_logo_white)
        val f1DailyTipPainter = painterResource(R.drawable.f1_daily_tip_image)
        // variable for switching between standings
        val selectedStandings = remember { mutableStateOf("Drivers") }
        val driversList = remember { driverStandingsViewModel ?: emptyList() }
        val constructorsList = remember { constructorsStandingsViewModel ?: emptyList() }

        val flagMap = mapOf(
            "British" to painterResource(R.drawable.flag_great_britain),
            "Dutch" to painterResource(R.drawable.flag_netherlands),
            "Monegasque" to painterResource(R.drawable.flag_monaco),
            "Australian" to painterResource(R.drawable.flag_australia),
            "Spanish" to painterResource(R.drawable.flag_spain),
            "Mexican" to painterResource(R.drawable.flag_mexico),
            "German" to painterResource(R.drawable.flag_germany),
            "Canadian" to painterResource(R.drawable.flag_canada),
            "Japanese" to painterResource(R.drawable.flag_japan),
            "Danish" to painterResource(R.drawable.flag_denmark),
            "Thai" to painterResource(R.drawable.flag_thailand),
            "French" to painterResource(R.drawable.flag_france),
            "Argentinian" to painterResource(R.drawable.flag_argentina),
            "New Zealander" to painterResource(R.drawable.flag_new_zealand),
            "Chinese" to painterResource(R.drawable.flag_china),
            "American" to painterResource(R.drawable.flag_united_states),
            "Finnish" to painterResource(R.drawable.flag_finland),
            "Italian" to painterResource(R.drawable.flag_italy),
            "Brazilian" to painterResource(R.drawable.flag_brazil),
        )

        val defaultFlag = ColorPainter(Color.Transparent)

    Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp).padding(top = 32.dp).padding(bottom = 0.dp).verticalScroll(rememberScrollState())
        ) {
                HomeHeaderComposable(painter = f1LogoWhitePainter)
                RussoOneText(text = "Daily Tip", fontSize = 24.sp, color = Color.White)
                Spacer(Modifier.height(15.dp))
                val tipOfTheDay = getTipOfTheDay()
                DailyTipComposable(painter = f1DailyTipPainter, tip = tipOfTheDay)
                Spacer(Modifier.height(20.dp))
                RussoOneText(text = "Next Race", fontSize = 24.sp, color = Color.White)
                Spacer(Modifier.height(15.dp))
                if (nextRace != null) {
                    NextRaceComposable(nextRace)
                }
                Spacer(Modifier.height(20.dp))
                SelectStandingsComposable(selectedOption = selectedStandings)
                Spacer(Modifier.height(15.dp))
            if (selectedStandings.value == "Drivers") {
                driversList.forEach { driver ->
                    val nationality = driver.Driver.nationality.trim()
                    val flag = flagMap[nationality] ?: defaultFlag
                    StandingsItem(
                        position = driver.position,
                        nationality = flag,
                        firstName = driver.Driver.givenName,
                        lastName = driver.Driver.familyName,
                        points = driver.points
                    )
                }
            } else {
                constructorsList.forEach { team ->
                    StandingsItem(
                        position = team.position,
                        nationality = ColorPainter(Color.Transparent),
                        firstName = team.Constructor.name,
                        lastName = "",
                        points = team.points
                    )
                }
            }
        }
}


@Composable
fun getTipOfTheDay() : String {
    val currentDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    // Use modulus to cycle back to the start of the list if the day exceeds the list size
    val tipIndex = (currentDayOfMonth - 1) % 15 + 1
    // Map the index to the corresponding resource ID
    val tipResourceId = when (tipIndex) {
        1 -> R.string.tip_of_the_day_1
        2 -> R.string.tip_of_the_day_2
        3 -> R.string.tip_of_the_day_3
        4 -> R.string.tip_of_the_day_4
        5 -> R.string.tip_of_the_day_5
        6 -> R.string.tip_of_the_day_6
        7 -> R.string.tip_of_the_day_7
        8 -> R.string.tip_of_the_day_8
        9 -> R.string.tip_of_the_day_9
        10 -> R.string.tip_of_the_day_10
        11 -> R.string.tip_of_the_day_11
        12 -> R.string.tip_of_the_day_12
        13 -> R.string.tip_of_the_day_13
        14 -> R.string.tip_of_the_day_14
        else -> R.string.tip_of_the_day_15
    }
    return stringResource(id = tipResourceId)
}



