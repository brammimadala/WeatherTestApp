package com.brahma.weather.domain.usecase

import com.brahma.weather.data.model.currentWeather.WeatherCurrentModel
import com.brahma.weather.data.util.Resource
import com.brahma.weather.domain.repository.WeatherReportRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(private val weatherReportRepository: WeatherReportRepository) {

    suspend fun execute(latitude: String, longitude: String): Resource<WeatherCurrentModel> {
        return weatherReportRepository.getCurrentWeather(latitude, longitude)
    }
}