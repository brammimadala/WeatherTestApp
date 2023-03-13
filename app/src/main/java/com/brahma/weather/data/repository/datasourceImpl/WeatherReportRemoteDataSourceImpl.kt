package com.brahma.weather.data.repository.datasourceImpl

import com.brahma.weather.data.api.WeatherApiService
import com.brahma.weather.data.model.currentWeather.WeatherCurrentModel
import com.brahma.weather.data.model.forecastWeather.FweatherReport
import com.brahma.weather.data.repository.datasource.WeatherReportRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class WeatherReportRemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : WeatherReportRemoteDataSource {
    override suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Response<WeatherCurrentModel> {
        return weatherApiService.getCurrentWeather(latitude, longitude)
    }

    override suspend fun getForecastWeather(
        latitude: String,
        longitude: String
    ): Response<FweatherReport> {
        return weatherApiService.getForeCastWeather(latitude, longitude)
    }
}