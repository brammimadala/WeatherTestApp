package com.brahma.weather.data.model.currentWeather


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)