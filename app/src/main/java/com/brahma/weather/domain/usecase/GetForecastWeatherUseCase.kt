package com.brahma.weather.domain.usecase

import com.brahma.weather.data.model.forecastWeather.FweatherReport
import com.brahma.weather.data.util.Resource
import com.brahma.weather.domain.repository.WeatherReportRepository
import javax.inject.Inject

class GetForecastWeatherUseCase @Inject constructor(private val weatherReportRepository: WeatherReportRepository) {
    suspend fun execute(latitude: String, longitude: String): Resource<FweatherReport> {
        return weatherReportRepository.getForecastWeather(latitude, longitude)
    }
}