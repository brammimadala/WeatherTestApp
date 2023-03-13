package com.brahma.weather.data.model.forecastWeather


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)