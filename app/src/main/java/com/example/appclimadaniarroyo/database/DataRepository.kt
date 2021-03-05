package com.example.appclimadaniarroyo.database

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import kotlinx.coroutines.*
import java.lang.Exception

class DataRepository(context: Context) {
    private val ciudadDao: CiudadDao? = AppDatabase.getInstance(context)?.ciudadDao()
    private val usuarioDao: UsuarioDao? = AppDatabase.getInstance(context)?.usuarioDao()

    fun existeUsuario(usuario: String, password: String): Boolean {
        var job: Job

        job = CoroutineScope(Dispatchers.IO).async {
            usuarioDao!!.countUsuarioByUsuarioPassword(usuario, password)!!
        }

        return runBlocking {
            job.await() == 1
        }
    }

    fun countUsuario(): Int {

        var job: Job

        job = CoroutineScope(Dispatchers.IO).async {
            usuarioDao!!.countUsuario()
        }

        return runBlocking {
            job.await()
        }
    }

    fun insert(usuario: Usuario): Int {
        if (usuarioDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                usuarioDao.insert(usuario)
            }
            return 0
        }
        return -1
    }


    /******************************CIUDADES **********************************/

    fun insert(ciudad: Ciudad): Int {
        var job: Job

        job = CoroutineScope(Dispatchers.IO).async {
            ciudadDao!!.insert(ciudad)
        }
        try {
            runBlocking {
                job.await()
            }
            return 0
        } catch (e: Exception) {
            return -1
        }
    }

    fun update(newCiudad: String, idCiudad : Int): Int {
        var job: Job
        job = CoroutineScope(Dispatchers.IO).async {
            ciudadDao!!.update(newCiudad, idCiudad)
        }
        try {
            runBlocking {
                job.await()
            }
            return 0
        } catch (e: Exception) {
            return -1
        }
    }

    fun getCiudades(user: String): MutableList<Ciudad> {
        var job: Job

        job = CoroutineScope(Dispatchers.IO).async {
            ciudadDao!!.getCiudades(user)
        }

        return runBlocking {
            job.await().toMutableList()
        }
    }

    fun deleteCiudad(ciudad: Ciudad): Int {
        if (ciudadDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                ciudadDao.delete(ciudad)
            }
            return 0
        }
        return -1
    }
}