package com.brahma.weather.data.model

import com.brahma.weather.data.model.forecastWeather.FCList

data class ForecastDataReportModel(
    var dateKey: String,
    var timeWeatherDataMap: MutableMap<String, FCList>
)
