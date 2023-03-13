package com.brahma.weather.data.model.forecastWeather


import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    val deg: Double,
    @SerializedName("gust")
    val gust: Double,
    @SerializedName("speed")
    val speed: Double
)