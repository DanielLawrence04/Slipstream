package com.example.formula1
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DriverStandingsService {
    @GET("f1/{year}/driverstandings/?format=json")
    fun getDriverStandings(@Path("year") year: Int): Call<JolpicaDriverResponse>
}

data class JolpicaDriverResponse(
    val MRData: MRData
)

data class MRData(
    val StandingsTable: StandingsTable
)

data class StandingsTable(
    val StandingsLists: List<StandingsList>
)

data class StandingsList(
    val DriverStandings: List<DriverStanding>
)

data class DriverStanding(
    val position: String,
    val points: String,
    val Driver: Driver,
    val Constructors: List<Constructor>
)

data class Driver(
    val givenName: String,
    val familyName: String,
    val nationality: String
)

data class Constructor(
    val name: String
)
