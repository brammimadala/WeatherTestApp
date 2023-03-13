package com.brahma.weather.data.repository.datasource

import com.brahma.weather.data.model.currentWeather.WeatherCurrentModel
import com.brahma.weather.data.model.forecastWeather.FweatherReport
import retrofit2.Response

interface WeatherReportRemoteDataSource {

    suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Response<WeatherCurrentModel>

    suspend fun getForecastWeather(
        latitude: String,
        longitude: String
    ): Response<FweatherReport>
}