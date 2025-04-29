package com.example.myapplication.Network

import com.example.myapplication.models.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("usuarios")
    fun obtenerUsuarios(): Call<List<Usuario>>

    @POST("usuarios")
    fun registrarUsuario(@Body usuario: Usuario): Call<Usuario>
}
