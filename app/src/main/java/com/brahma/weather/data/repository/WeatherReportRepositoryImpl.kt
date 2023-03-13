package com.brahma.weather.data.repository

import com.brahma.weather.data.model.currentWeather.WeatherCurrentModel
import com.brahma.weather.data.model.forecastWeather.FweatherReport
import com.brahma.weather.data.repository.datasource.WeatherReportRemoteDataSource
import com.brahma.weather.data.util.Resource
import com.brahma.weather.domain.repository.WeatherReportRepository
import javax.inject.Inject

class WeatherReportRepositoryImpl @Inject constructor(private val weatherReportRemoteDataSource: WeatherReportRemoteDataSource) :
    WeatherReportRepository {
    override suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Resource<WeatherCurrentModel> {
        val response = weatherReportRemoteDataSource.getCurrentWeather(latitude, longitude)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getForecastWeather(
        latitude: String,
        longitude: String
    ): Resource<FweatherReport> {
        val response = weatherReportRemoteDataSource.getForecastWeather(latitude, longitude)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

}