package com.example.appclimadaniarroyo.fragments

import android.content.Context
import android.graphics.Paint
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.appclimadaniarroyo.R
import com.example.appclimadaniarroyo.database.DataRepository
import com.example.appclimadaniarroyo.model.WeatherResponse
import com.example.appclimadaniarroyo.networking.CityWeatherRepository
import com.example.appclimadaniarroyo.networking.FactoryAPI
import com.example.appclimadaniarroyo.utils.round
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*


class CompararClimaFragment : Fragment() {

    lateinit var dataRepository: DataRepository
    var thiscontext: Context? = null
    var response: WeatherResponse? = null
    lateinit var textViewWeatherCompare: TextView
    lateinit var textViewCompare: TextView
    lateinit var textViewTemperatureCompare: TextView
    lateinit var textViewMainStatusCompare1: TextView
    lateinit var textViewDescriptionCompare1: TextView
    lateinit var textViewMainStatusCompare2: TextView
    lateinit var textViewDescriptionCompare2: TextView
    lateinit var imageViewWeatherCompare1: ImageView
    lateinit var imageViewWeatherCompare2: ImageView
    lateinit var textViewTemperatureValueCompare1: TextView
    lateinit var textViewTemperatureValueCompare2: TextView
    lateinit var textViewPressureCompare1: TextView
    lateinit var textViewPressureCompare2: TextView
    lateinit var textViewHumidityCompare1: TextView
    lateinit var textViewHumidityCompare2: TextView
    lateinit var textViewMaxTemperatureCompare1: TextView
    lateinit var textViewMaxTemperatureCompare2: TextView
    lateinit var textViewMinTemperatureCompare1: TextView
    lateinit var textViewMinTemperatureCompare2: TextView

    lateinit var cityWeatherRepository: CityWeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_comparar_clima, container, false)
        val nombreUser = arguments?.getString("user")
        thiscontext = container?.getContext();

        textViewWeatherCompare = v.findViewById<View>(R.id.textViewWeatherCompare) as TextView
        textViewCompare = v.findViewById<View>(R.id.textViewCompare) as TextView
        textViewTemperatureCompare =
            v.findViewById<View>(R.id.textViewTemperatureCompare) as TextView
        textViewMainStatusCompare1 =
            v.findViewById<View>(R.id.textViewMainStatusCompare1) as TextView
        textViewDescriptionCompare1 =
            v.findViewById<View>(R.id.textViewDescriptionCompare1) as TextView
        textViewMainStatusCompare2 =
            v.findViewById<View>(R.id.textViewMainStatusCompare2) as TextView
        textViewDescriptionCompare2 =
            v.findViewById<View>(R.id.textViewDescriptionCompare2) as TextView
        imageViewWeatherCompare1 = v.findViewById<View>(R.id.imageViewWeatherCompare1) as ImageView
        imageViewWeatherCompare2 = v.findViewById<View>(R.id.imageViewWeatherCompare2) as ImageView
        textViewTemperatureValueCompare1 =
            v.findViewById<View>(R.id.textViewTemperatureValueCompare1) as TextView
        textViewTemperatureValueCompare2 =
            v.findViewById<View>(R.id.textViewTemperatureValueCompare2) as TextView
        textViewPressureCompare1 = v.findViewById<View>(R.id.textViewPressureCompare1) as TextView
        textViewPressureCompare2 = v.findViewById<View>(R.id.textViewPressureCompare2) as TextView
        textViewHumidityCompare1 = v.findViewById<View>(R.id.textViewHumidityCompare1) as TextView
        textViewHumidityCompare2 = v.findViewById<View>(R.id.textViewHumidityCompare2) as TextView
        textViewMaxTemperatureCompare1 =
            v.findViewById<View>(R.id.textViewMaxTemperatureCompare1) as TextView
        textViewMaxTemperatureCompare2 =
            v.findViewById<View>(R.id.textViewMaxTemperatureCompare2) as TextView
        textViewMinTemperatureCompare1 =
            v.findViewById<View>(R.id.textViewMinTemperatureCompare1) as TextView
        textViewMinTemperatureCompare2 =
            v.findViewById<View>(R.id.textViewMinTemperatureCompare2) as TextView

        textViewCompare.text = String.format(getString(R.string.comparar_climas))
        textViewWeatherCompare.text = getString(R.string.weather)
        textViewWeatherCompare.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        textViewTemperatureCompare.text = getString(R.string.temperature)
        textViewTemperatureCompare.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        dataRepository = DataRepository(thiscontext!!)
        val spinnerClima1 = v.findViewById<Spinner>(R.id.spinnerClima1)
        val spinnerClima2 = v.findViewById<Spinner>(R.id.spinnerClima2)
        var ciudadesArray = dataRepository.getCiudades(nombreUser.toString())
        var spinnerArray = ArrayList<String>()
        for (items in ciudadesArray!!) {
            if (!spinnerArray.contains(items)) {
                spinnerArray.add(items.ciudad)
            }
        }
        spinnerClima1.adapter =
            ArrayAdapter(thiscontext!!, android.R.layout.simple_list_item_1, spinnerArray)
        spinnerClima2.adapter =
            ArrayAdapter(thiscontext!!, android.R.layout.simple_list_item_1, spinnerArray)
        if (spinnerClima1 != null) {
            spinnerClima1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    response = weatherCiudadRespone(spinnerClima1.selectedItem.toString())
                    response?.let {
                        textViewMainStatusCompare1.text = it.weather[0].main
                        textViewDescriptionCompare1.text = it.weather[0].description
                        var tempCelsius = (it.main.temp - 273).round(2)
                        textViewTemperatureValueCompare1.text =
                            String.format(
                                getString(R.string.temperatura_ciudad),
                                tempCelsius.toString()
                            )
                        textViewPressureCompare1.text =
                            String.format(
                                getString(R.string.pressure_ciudad),
                                it.main.pressure.toString()
                            )
                        textViewHumidityCompare1.text =
                            String.format(
                                getString(R.string.humidity_ciudad),
                                it.main.humidity.toString()
                            )
                        var maxTempCelsius = (it.main.temp_max - 273).round(0)
                        var minTempCelsius = (it.main.temp_min - 273).round(0)
                        textViewMaxTemperatureCompare1.text =
                            String.format(getString(R.string.max_temp_ciudad), maxTempCelsius)
                        textViewMinTemperatureCompare1.text =
                            String.format(getString(R.string.min_temp_ciudad), minTempCelsius)
                        Picasso.get().load(getString(R.string.weather_icon, it.weather[0].icon))
                            .into(imageViewWeatherCompare1);
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        if (spinnerClima2 != null) {
            spinnerClima2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    response = weatherCiudadRespone(spinnerClima2.selectedItem.toString())
                    response?.let {
                        textViewMainStatusCompare2.text = it.weather[0].main
                        textViewDescriptionCompare2.text = it.weather[0].description
                        var tempCelsius = (it.main.temp - 273).round(2)
                        textViewTemperatureValueCompare2.text =
                            String.format(
                                getString(R.string.temperatura_ciudad),
                                tempCelsius.toString()
                            )
                        textViewPressureCompare2.text =
                            String.format(
                                getString(R.string.pressure_ciudad),
                                it.main.pressure.toString()
                            )
                        textViewHumidityCompare2.text =
                            String.format(
                                getString(R.string.humidity_ciudad),
                                it.main.humidity.toString()
                            )
                        var maxTempCelsius = (it.main.temp_max - 273).round(0)
                        var minTempCelsius = (it.main.temp_min - 273).round(0)
                        textViewMaxTemperatureCompare2.text =
                            String.format(getString(R.string.max_temp_ciudad), maxTempCelsius)
                        textViewMinTemperatureCompare2.text =
                            String.format(getString(R.string.min_temp_ciudad), minTempCelsius)
                        Picasso.get().load(getString(R.string.weather_icon, it.weather[0].icon))
                            .into(imageViewWeatherCompare2);
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        //API CALL

        var climaAPI = FactoryAPI.OpenWeatherAPI
        cityWeatherRepository = CityWeatherRepository(climaAPI)

        return v
    }

    fun weatherCiudadRespone(nombreCiudadElegida: String) = runBlocking {
        cityWeatherRepository.getCityWeather(nombreCiudadElegida)
    }

}