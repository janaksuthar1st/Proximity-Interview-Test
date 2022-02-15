package com.proximity.practicaljanak.models

import com.google.gson.annotations.SerializedName

   
data class AirQualityData (
   @SerializedName("city") var city : String? = null,
   @SerializedName("aqi") var aqi : Double? = null
)