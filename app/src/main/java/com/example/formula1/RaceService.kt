package com.example.formula1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RaceService {
    @GET("f1/{year}.json")
    fun getRaceSchedule(@Path("year") year: Int): Call<RaceScheduleResponse>
}

data class RaceScheduleResponse(
    val MRData: RaceData
)

data class RaceData(
    val RaceTable: RaceTable
)

data class RaceTable(
    val season: String,
    val Races: List<RaceList>
)

data class RaceList(
    val round: String,
    val raceName: String,
    val date: String,
    val Circuit: Circuit
)

data class Circuit(
    val circuitName: String,
    val Location: Location
)

data class Location(
    val locality: String,
    val country: String
)