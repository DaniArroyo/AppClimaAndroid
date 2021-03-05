package com.example.appclimadaniarroyo.model

data class WeatherAPI(val main : String, val description : String, val icon : String)

data class TemperatureAPI(val temp : Double, val pressure : Int, val humidity : Int, val temp_min : Double, val temp_max : Double)


data class WeatherResponse(
    val weather: List<WeatherAPI>,
    val main: TemperatureAPI,
    val cod: Int
)



