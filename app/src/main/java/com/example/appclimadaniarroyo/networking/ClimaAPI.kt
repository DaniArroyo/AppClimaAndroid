package com.example.appclimadaniarroyo.networking

import com.example.appclimadaniarroyo.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaAPI {

    @GET("weather")
    suspend fun getCityWeather(@Query("q") city: String): Response<WeatherResponse>

}