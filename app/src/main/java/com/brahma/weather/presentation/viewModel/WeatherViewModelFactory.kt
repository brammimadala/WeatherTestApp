package com.brahma.weather.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brahma.weather.domain.usecase.GetCurrentWeatherUseCase
import com.brahma.weather.domain.usecase.GetForecastWeatherUseCase

class WeatherViewModelFactory(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getForecastWeatherUseCase: GetForecastWeatherUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherHomeViewModel(
            getCurrentWeatherUseCase,
            getForecastWeatherUseCase
        ) as T
    }

}