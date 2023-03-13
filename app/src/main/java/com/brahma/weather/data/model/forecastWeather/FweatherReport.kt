package com.brahma.weather.data.model.forecastWeather


import com.google.gson.annotations.SerializedName

data class FweatherReport(
    @SerializedName("cod")
    val cod: String,
    @SerializedName("message")
    val message: Int,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("city")
    val city: City,
    @SerializedName("list")
    val list: List<FCList>,

    )