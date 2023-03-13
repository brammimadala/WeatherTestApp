package com.brahma.weather.data.api

import com.brahma.weather.data.model.currentWeather.WeatherCurrentModel
import com.brahma.weather.data.model.forecastWeather.FweatherReport
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("units")
        units: String = "Metric",
        @Query("APPID")
        appid: String = "7b47bed1e9e281c15dba959f983b5de9"
    ): Response<WeatherCurrentModel>

    @GET("/data/2.5/forecast")
    suspend fun getForeCastWeather(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("units")
        units: String = "Metric",
        @Query("APPID")
        appid: String = "7b47bed1e9e281c15dba959f983b5de9"
    ): Response<FweatherReport>
}