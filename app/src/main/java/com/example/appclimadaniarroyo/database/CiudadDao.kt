package com.example.appclimadaniarroyo.database
import androidx.room.*

@Dao
interface CiudadDao {

    @Query("SELECT * FROM Ciudad WHERE user = :user")
    fun getCiudades(user:String): List<Ciudad>

    @Insert
    suspend fun insert(ciudad: Ciudad)

    @Delete
    suspend fun delete(ciudad: Ciudad)

    @Query("UPDATE Ciudad SET ciudad = :newCiudad WHERE idCiudad = :idCiudad")
    suspend fun update(newCiudad: String, idCiudad : Int)
}