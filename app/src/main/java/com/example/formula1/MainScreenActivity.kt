package com.example.formula1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formula1.AppComponents.NavigationBar
import com.google.android.filament.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import java.time.LocalDate


class MainScreenActivity : ComponentActivity() {
    companion object {
        init { Utils.init() } // Initialise Filament (used for 3D rendering)
    }

    // Declare ViewModels
    private val driverStandingsViewModel: DriverStandingsViewModel by viewModels()
    private val raceListViewModel: RaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                MainScreen(raceListViewModel, driverStandingsViewModel)
            }
        }

        // Initialise Python runtime using Chaquopy
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
    }
}

// Information displayed per track
data class TrackDetailsClass(
    val name: String,
    val trackFlag: Int,
    val trackLayout: Int,
    val firstGrandPrix: String,
    val numberOfLaps: String,
    val fullThrottle: String,
    val circuitLength: String,
    val raceDistance: String,
    val longestFlatOutSection: String,
    val lapRecordTime: String,
    val lapRecordDriver: String,
    val qualifyingLapRecordTime: String,
    val qualifyingLapRecordDriver: String
)

// Data class for Country
data class Country(val name: String, val flagRes: Int)

data class CarStatisticsClass(
    val competitiveness: Float
)

class DriverStandingsViewModel : ViewModel() {
    private val driverStandingsService: DriverStandingsService
    private val constructorStandingsService: ConstructorStandingsService

    private val _driverStandingsList = MutableStateFlow<List<DriverStanding>?>(null)
    val driverStandingsList: StateFlow<List<DriverStanding>?> get() = _driverStandingsList

    private val _constructorStandingsList = MutableStateFlow<List<ConstructorStanding>?>(null)
    val constructorStandingsList: StateFlow<List<ConstructorStanding>?> get() = _constructorStandingsList

    init {
        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jolpi.ca/ergast/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        driverStandingsService = retrofit.create(DriverStandingsService::class.java)
        constructorStandingsService = retrofit.create(ConstructorStandingsService::class.java)

        // Fetch standings when the ViewModel is created
        fetchDriverStandings()
        fetchConstructorStandings()
    }


    private fun fetchDriverStandings() {
        // year used by API
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = driverStandingsService.getDriverStandings(currentYear).execute()
                if (response.isSuccessful) {
                    val body = response.body()

                    // Log the entire JSON response
                    Log.d("API_RESPONSE", "Full Response: ${body.toString()}")

                    // Additional debugging: Log individual fields
                    Log.d("API_DEBUG", "MRData: ${body?.MRData}")
                    Log.d("API_DEBUG", "StandingsTable: ${body?.MRData?.StandingsTable}")
                    Log.d("API_DEBUG", "StandingsLists: ${body?.MRData?.StandingsTable?.StandingsLists}")
                    Log.d("API_DEBUG", "First Driver Standing: ${body?.MRData?.StandingsTable?.StandingsLists?.firstOrNull()?.DriverStandings}")
                    val standings = response.body()?.MRData?.StandingsTable?.StandingsLists
                        ?.firstOrNull()
                        ?.DriverStandings
                        ?.mapIndexed { index, driverStanding ->
                            if (driverStanding.position == null) {
                                driverStanding.copy(position = (index + 1).toString())
                            } else {
                                driverStanding
                            }
                        } ?: emptyList()

                    _driverStandingsList.value = standings
                } else {
                    Log.e("API_ERROR", "Driver Standings API Error: ${response.errorBody()?.string()}")
                    _driverStandingsList.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", "Error fetching driver standings", e)
                _driverStandingsList.value = emptyList()
            }
        }
    }

    private fun fetchConstructorStandings() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = constructorStandingsService.getConstructorStandings(currentYear).execute()
                if (response.isSuccessful) {
                    val standings = response.body()?.MRData?.StandingsTable?.StandingsLists
                        ?.firstOrNull()
                        ?.ConstructorStandings
                        ?.mapIndexed { index, driverStanding ->
                            if (driverStanding.position == null) {
                                driverStanding.copy(position = (index + 1).toString())
                            } else {
                                driverStanding
                            }
                        } ?: emptyList()

                    _constructorStandingsList.value = standings
                } else {
                    _constructorStandingsList.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _constructorStandingsList.value = emptyList()
            }
        }
    }
}

class RaceViewModel : ViewModel() {
    private val raceService: RaceService

    private val _nextRace = MutableStateFlow<RaceList?>(null) // Holds next race data
    val nextRace: StateFlow<RaceList?> get() = _nextRace

    init {
        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jolpi.ca/ergast/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        raceService = retrofit.create(RaceService::class.java)

        fetchNextRace()
    }

    private fun fetchNextRace() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = raceService.getRaceSchedule(currentYear).execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("API_RESPONSE", "Race Schedule: $body")

                    // Extract race list
                    val races = body?.MRData?.RaceTable?.Races ?: emptyList()

                    // Get today's date
                    val today = LocalDate.now()

                    // Find the first race that hasn't happened yet
                    val upcomingRace = races.firstOrNull { race ->
                        LocalDate.parse(race.date) >= today
                    }

                    Log.d("NEXT_RACE", "Next Race: $upcomingRace")

                    _nextRace.value = upcomingRace  // Update state
                } else {
                    Log.e("API_ERROR", "Race Schedule API Error: ${response.errorBody()?.string()}")
                    _nextRace.value = null
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", "Error fetching race schedule", e)
                _nextRace.value = null
            }
        }
    }
}




@Composable
fun MainScreen(
    raceViewModel: RaceViewModel,
    viewModel: DriverStandingsViewModel
) {
    var selectedPage by remember { mutableStateOf("Home") }
    var isQualifyingActive by remember { mutableStateOf(false) }

    // Observe live data
    val driverStandingsList by viewModel.driverStandingsList.collectAsState()
    val constructorsStandingsList by viewModel.constructorStandingsList.collectAsState()
    val nextRace by raceViewModel.nextRace.collectAsState()

    // Scaffold manages screen layout and optional bottom navigation bar
    Scaffold(
        bottomBar = {
            if (!isQualifyingActive) {
                NavigationBar(
                    selectedItem = selectedPage,
                    onClick = { selectedPage = it }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.50f to Color(0xFF290202),
                            0.95f to Color(0xFF402625)
                        )
                    )
                )
        ) {
            when (selectedPage) {
                "Home" -> {
                    if (driverStandingsList == null) {
                        // Optionally show loading spinner
                    } else {
                        HomeScreen(nextRace, driverStandingsList, constructorsStandingsList)
                    }
                }
                "Simulate" -> {
                    SimulateHomeScreen(setQualifyingActive = { isQualifyingActive = it })
                }
                "Settings" -> {
                    SettingsScreen()
                }
            }
        }
    }
}