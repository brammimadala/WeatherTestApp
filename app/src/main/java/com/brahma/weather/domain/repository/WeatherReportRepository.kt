package com.brahma.weather.domain.repository

import com.brahma.weather.data.model.currentWeather.WeatherCurrentModel
import com.brahma.weather.data.model.forecastWeather.FweatherReport
import com.brahma.weather.data.util.Resource

interface WeatherReportRepository {

    suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Resource<WeatherCurrentModel>

    suspend fun getForecastWeather(
        latitude: String,
        longitude: String
    ): Resource<FweatherReport>
}