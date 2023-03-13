package com.brahma.weather.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brahma.weather.data.model.ForecastDataReportModel
import com.brahma.weather.data.model.currentWeather.WeatherCurrentModel
import com.brahma.weather.data.model.forecastWeather.FweatherReport
import com.brahma.weather.data.util.Resource
import com.brahma.weather.domain.usecase.GetCurrentWeatherUseCase
import com.brahma.weather.domain.usecase.GetForecastWeatherUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherHomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getForecastWeatherUseCase: GetForecastWeatherUseCase
) : ViewModel() {


    val currentWeather: MutableLiveData<Resource<WeatherCurrentModel>> = MutableLiveData()
    val forecastWeather: MutableLiveData<Resource<FweatherReport>> = MutableLiveData()

    val forecastDataReportModel: MutableLiveData<ForecastDataReportModel> = MutableLiveData()


    fun getCurrentWeather(latitude: String, longitude: String) =
        viewModelScope.launch(Dispatchers.IO) {
            currentWeather.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable()) {
                    val apiResult = getCurrentWeatherUseCase.execute(latitude, longitude)
                    currentWeather.postValue(apiResult)
                } else {
                    currentWeather.postValue(Resource.Error("Internet is not available"))
                }
            } catch (e: Exception) {
                currentWeather.postValue(Resource.Error(e.message.toString()))
            }
        }

    fun getForecastWeather(latitude: String, longitude: String) =
        viewModelScope.launch(Dispatchers.IO) {
            forecastWeather.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable()) {
                    val apiResult = getForecastWeatherUseCase.execute(latitude, longitude)
                    forecastWeather.postValue(apiResult)
                } else {
                    forecastWeather.postValue(Resource.Error("Internet is not available"))
                }
            } catch (e: Exception) {
                forecastWeather.postValue(Resource.Error(e.message.toString()))
            }
        }


    //TODO
    private fun isNetworkAvailable(): Boolean {
        return true
    }

    fun getWindDirection(windDegree: Double): String {
        if (windDegree >= 348.76 || windDegree <= 11.25) {
            return "NORTH"
        }
        if (windDegree in 11.26..78.75) {
            return "NORTH-EAST"
        }
        if (windDegree in 78.76..101.25) {
            return "EAST"
        }
        if (windDegree in 101.26..146.25) {
            return "SOUTH-EAST"
        }
        if (windDegree in 146.26..191.25) {
            return "SOUTH"
        }
        if (windDegree in 191.26..236.25) {
            return "SOUTH-WEST"
        }
        if (windDegree in 236.26..281.25) {
            return "WEST"
        }
        if (windDegree in 281.26..348.75) {
            return "NORTH-WEST"
        }
        return ""

    }

    fun setDataForDetailsFrag(data: ForecastDataReportModel) {
        forecastDataReportModel.postValue(data)
    }
}
