package com.example.appclimadaniarroyo.networking

import com.google.gson.Gson
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FactoryAPI {

    //Creating Auth Interceptor to add api_key query in front of all the requests.
    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("appid", "d1dbfa4968c5ea214064150d5a9ed93f")
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }


    //OkhttpClient for building http request url
    private val OPWClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .build()

    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(OPWClient)
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val OpenWeatherAPI: ClimaAPI = retrofit().create(ClimaAPI::class.java)

}