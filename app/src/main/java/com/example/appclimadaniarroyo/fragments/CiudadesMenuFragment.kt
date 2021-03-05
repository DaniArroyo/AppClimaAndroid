package com.example.appclimadaniarroyo.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appclimadaniarroyo.R
import com.example.appclimadaniarroyo.database.Ciudad
import com.example.appclimadaniarroyo.database.DataRepository
import com.example.appclimadaniarroyo.networking.CityWeatherRepository
import com.example.appclimadaniarroyo.networking.ClimaAPI
import com.example.appclimadaniarroyo.networking.FactoryAPI
import com.example.customadapterlistviewexample.adapters.CiudadesListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CiudadesMenuFragment : Fragment() , CiudadesListAdapter.ICiudadRecycler {

    lateinit var preferences: SharedPreferences
    lateinit var user: String
    lateinit var editTextCiudad: EditText
    lateinit var listaCiudades: MutableList<Ciudad>
    lateinit var dataRepository: DataRepository
    lateinit var adapter: CiudadesListAdapter
    var thiscontext: Context? = null
    lateinit var cityWeatherRepository: CityWeatherRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_ciudades_menu, container, false)
        var buttonLogout = v.findViewById<Button>(R.id.btnLogout)
        var buttonAgregar = v.findViewById<Button>(R.id.btnAgregar)
        var buttonComparar = v.findViewById<Button>(R.id.btnComparar)
        var textViewUser = v.findViewById<TextView>(R.id.textViewUser)
        editTextCiudad = v.findViewById(R.id.editTextCiudad)

        //SHAREDPREFERENCES

        preferences = this.requireActivity().getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        user = preferences.getString("USER", "").toString()
        textViewUser.text =  String.format(getString(R.string.welcome_user), user)

        //API

        var climaAPI = FactoryAPI.OpenWeatherAPI
        cityWeatherRepository = CityWeatherRepository(climaAPI)

        //ADAPTER

        val recyclerViewLista: RecyclerView = v.findViewById<View>(R.id.recyclerViewCiudades) as RecyclerView
        thiscontext = container?.getContext();
        dataRepository = DataRepository(thiscontext!!)

        listaCiudades = dataRepository.getCiudades(user)
        adapter = CiudadesListAdapter(listaCiudades, this)

        recyclerViewLista.setAdapter(adapter)
        recyclerViewLista.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))

        buttonLogout.setOnClickListener {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()
            findNavController().navigate(R.id.action_ciudadesMenuFragment_to_loginFragment)
        }

        buttonAgregar.setOnClickListener {
            comprobarCiudad()
            editTextCiudad.text.clear()
        }

        buttonComparar.setOnClickListener {
            compararCiudades()
        }

        return v
    }

    private fun agregarCiudades(ciudad: String) {
        if (dataRepository.insert(Ciudad(ciudad = ciudad.capitalize(), user = user)) == -1
        ) {
            showToasts("Ciudad repetida")
        } else {
            showToasts("Ciudad agregada con exito")
            listaCiudades = dataRepository.getCiudades(user)
            adapter.setData(listaCiudades)
        }
    }

    private fun comprobarCiudad() {
        var ciudad = editTextCiudad.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val response = cityWeatherRepository.getCityWeather(ciudad)
            if (response?.cod == 200) {
                withContext(Dispatchers.Main) {
                agregarCiudades(ciudad)
                }
            } else {
                withContext(Dispatchers.Main) {
                    showToasts("Introduce una Ciudad valida para OPENWEATHER")
                }
            }
        }
    }

    override fun borraCiudad(item: Ciudad, position: Int) {
        dataRepository.deleteCiudad(item)
        listaCiudades.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    override fun onCiudadClick(item: Ciudad) {
        var bundle = bundleOf("ciudad" to item.ciudad, "user" to item.user)
        findNavController().navigate(R.id.action_ciudadesMenuFragment_to_ciudadElegidaFragment, bundle)
    }

    override fun editarCiudad(item: Ciudad, position: Int) {
        val editView = layoutInflater.inflate(R.layout.alert_dialog, null)
        val builder = AlertDialog.Builder(thiscontext).setTitle("Modificar Ciudad").create()
        builder.setView(editView)

        val textViewCiudad = editView.findViewById<EditText>(R.id.editTextUpdateCiudad)
        val btnAlertDialogCancel = editView.findViewById<Button>(R.id.btnAlertDialogCancel)
        val btnAlertDialogUpdate = editView.findViewById<Button>(R.id.btnAlertDialogUpdate)
        textViewCiudad.setText(item.ciudad)

        btnAlertDialogCancel.setOnClickListener{
            builder.dismiss()
        }

        btnAlertDialogUpdate.setOnClickListener{
            var newCiudad = textViewCiudad.text.toString()
            comprobarCiudadAlertDialog(newCiudad, item.idCiudad)
            builder.dismiss()
        }
        builder.show()
    }

    private fun compararCiudades()  {
        var bundle = bundleOf("user" to user)
        findNavController().navigate(R.id.action_ciudadesMenuFragment_to_compararClimaFragment, bundle)
    }

    private fun comprobarCiudadAlertDialog(newCiudad: String, idCiudad : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = cityWeatherRepository.getCityWeather(newCiudad)
            if (response?.cod == 200) {
                withContext(Dispatchers.Main) {
                    editarCiudades(newCiudad, idCiudad)
                }
            } else {
                withContext(Dispatchers.Main) {
                    showToasts("Introduce una Ciudad valida para OPENWEATHER")
                }
            }
        }
    }

    private fun editarCiudades(newCiudad: String, idCiudad : Int) {
        if (dataRepository.update(newCiudad.capitalize(), idCiudad) == -1
        ) {
            showToasts("Ciudad repetida")
        } else {
            showToasts("Ciudad Modificada con exito")
            listaCiudades = dataRepository.getCiudades(user)
            adapter.setData(listaCiudades)
        }
    }

    private fun showToasts(mensaje : String){
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}
