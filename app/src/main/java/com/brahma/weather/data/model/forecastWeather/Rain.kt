package com.brahma.weather.data.model.forecastWeather


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)