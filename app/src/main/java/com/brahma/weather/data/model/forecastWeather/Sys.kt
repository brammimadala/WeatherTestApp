package com.brahma.weather.data.model.forecastWeather


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String
)