package com.brahma.weather.data.model

import com.google.gson.annotations.SerializedName

data class AddressModel(
    @SerializedName("addressLine")
    val addressLine: String? = null,

    @SerializedName("feature")
    val feature: String? = null,

    @SerializedName("admin")
    val admin: String? = null,

    @SerializedName("locality")
    val locality: String? = null,

    @SerializedName("postalCode")
    val postalCode: String? = null,

    @SerializedName("countryName")
    val countryName: String? = null,

    @SerializedName("latitude")
    val latitude: String? = null,

    @SerializedName("longitude")
    val longitude: String? = null,

    @SerializedName("sid")
    val sid: Int = 0

)
