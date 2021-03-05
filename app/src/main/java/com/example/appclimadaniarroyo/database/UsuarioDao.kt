package com.example.appclimadaniarroyo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT count(*) FROM usuario where usuario = :usuarioInput and password = :passwordInput")
    suspend fun countUsuarioByUsuarioPassword(usuarioInput:String, passwordInput: String): Int

    @Query("SELECT count(*) FROM usuario")
    suspend fun countUsuario(): Int

    @Insert
    suspend fun insert(usuario: Usuario)
}