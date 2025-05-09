package com.example.myapplication.models

data class Usuario(
    val id: Int? = null,  // Nullable para cuando se crea nuevos usuarios
    val nombre: String,
    val correo: String,
    val contrasena: String
)