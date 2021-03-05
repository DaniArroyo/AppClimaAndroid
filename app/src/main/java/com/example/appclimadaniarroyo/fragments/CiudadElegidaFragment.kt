package com.example.appclimadaniarroyo.fragments

import android.content.Context
import android.graphics.Paint
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.appclimadaniarroyo.R
import com.example.appclimadaniarroyo.model.WeatherResponse
import com.example.appclimadaniarroyo.networking.CityWeatherRepository
import com.example.appclimadaniarroyo.networking.FactoryAPI
import com.example.appclimadaniarroyo.utils.round
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*


class CiudadElegidaFragment : Fragment() {

    var thiscontext: Context? = null
    var response: WeatherResponse? = null
    lateinit var textViewCiudad: TextView
    lateinit var textViewWeather: TextView
    lateinit var textViewMainStatus: TextView
    lateinit var textViewDescription: TextView
    lateinit var textViewTemperature: TextView
    lateinit var textViewTemperatureValue: TextView
    lateinit var textViewPressure: TextView
    lateinit var textViewHumidity: TextView
    lateinit var textViewMaxTemperature: TextView
    lateinit var textViewMinTemperature: TextView
    lateinit var imageViewWeather : ImageView
    lateinit var cityWeatherRepository: CityWeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_ciudad_elegida, container, false)
        val nombreCiudadElegida = arguments?.getString("ciudad")

        thiscontext = container?.getContext();
        textViewCiudad = v.findViewById<View>(R.id.textViewCiudad) as TextView
        textViewWeather = v.findViewById<View>(R.id.textViewWeather) as TextView
        textViewMainStatus = v.findViewById<View>(R.id.textViewMainStatus) as TextView
        textViewDescription = v.findViewById<View>(R.id.textViewDescription) as TextView
        textViewTemperature = v.findViewById<View>(R.id.textViewTemperature) as TextView
        textViewTemperatureValue = v.findViewById<View>(R.id.textViewTemperatureValue) as TextView
        textViewPressure = v.findViewById<View>(R.id.textViewPressure) as TextView
        textViewHumidity = v.findViewById<View>(R.id.textViewHumidity) as TextView
        textViewMaxTemperature = v.findViewById<View>(R.id.textViewMaxTemperature) as TextView
        textViewMinTemperature = v.findViewById<View>(R.id.textViewMinTemperature) as TextView
        imageViewWeather = v.findViewById<View>(R.id.imageViewWeather) as ImageView

        textViewCiudad.text = String.format(getString(R.string.clima_ciudad), nombreCiudadElegida.toString())
        textViewWeather.text = getString(R.string.weather)
        textViewWeather.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        textViewTemperature.text = getString(R.string.temperature)
        textViewTemperature.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        //API CALL

        var climaAPI = FactoryAPI.OpenWeatherAPI
        cityWeatherRepository = CityWeatherRepository(climaAPI)

        if (nombreCiudadElegida != null) {
            response = weatherCiudadRespone(nombreCiudadElegida)
            response?.let {
                textViewMainStatus.text = it.weather[0].main
                textViewDescription.text = it.weather[0].description
                var tempCelsius = (it.main.temp - 273).round(2)
                textViewTemperatureValue.text =
                    String.format(getString(R.string.temperatura_ciudad), tempCelsius.toString())
                textViewPressure.text =
                    String.format(getString(R.string.pressure_ciudad), it.main.pressure.toString())
                textViewHumidity.text =
                    String.format(getString(R.string.humidity_ciudad), it.main.humidity.toString())
                var maxTempCelsius = (it.main.temp_max - 273).round(0)
                var minTempCelsius = (it.main.temp_min - 273).round(0)
                textViewMaxTemperature.text =
                    String.format(getString(R.string.max_temp_ciudad), maxTempCelsius)
                textViewMinTemperature.text =
                    String.format(getString(R.string.min_temp_ciudad), minTempCelsius)
                Picasso.get().load(getString(R.string.weather_icon, it.weather[0].icon)).into(imageViewWeather);
            }
        }
        return v
    }

    fun weatherCiudadRespone(nombreCiudadElegida: String) = runBlocking {
        cityWeatherRepository.getCityWeather(nombreCiudadElegida)
    }

}