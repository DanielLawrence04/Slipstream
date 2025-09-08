package com.example.formula1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ConstructorStandingsService {
    @GET("f1/{year}/constructorstandings/?format=json")
    fun getConstructorStandings(@Path("year") year: Int): Call<JolpicaConstructorResponse>
}

data class JolpicaConstructorResponse(
    val MRData: ConstructorMRData
)

data class ConstructorMRData(
    val StandingsTable: ConstructorStandingsTable
)

data class ConstructorStandingsTable(
    val StandingsLists: List<ConstructorStandingsList>
)

data class ConstructorStandingsList(
    val ConstructorStandings: List<ConstructorStanding>
)

data class ConstructorStanding(
    val position: String,
    val points: String,
    val wins: String,
    val Constructor: Constructor
)