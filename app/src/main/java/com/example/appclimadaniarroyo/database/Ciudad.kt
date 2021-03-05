package com.example.appclimadaniarroyo.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ciudad", "user"], unique = true)])
data class Ciudad (
    @PrimaryKey(autoGenerate = true) val idCiudad: Int = 0,
    val ciudad: String,
    val user: String?
)