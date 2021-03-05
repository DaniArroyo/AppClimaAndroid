package com.example.appclimadaniarroyo.networking

import com.example.appclimadaniarroyo.model.WeatherResponse

private const val API_KEY = "d1dbfa4968c5ea214064150d5a9ed93f"

class CityWeatherRepository(private val api: ClimaAPI) : BaseRepository() {
    suspend fun getCityWeather(city: String): WeatherResponse? {
        val cityResponse = safeApiCall(
            call = { api.getCityWeather(city) },
            errorMessage = "Error Fetching City Weather"
        )

        return cityResponse

    }
}