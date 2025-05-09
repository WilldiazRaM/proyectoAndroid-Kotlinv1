package com.example.myapplication.Network

import com.example.myapplication.models.Usuario
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    // Operaciones CRUD completas
    @GET("usuarios")
    fun obtenerTodosUsuarios(): Call<List<Usuario>>

    @GET("usuarios/{id}")
    fun obtenerUsuarioPorId(@Path("id") id: Int): Call<Usuario>

    @POST("usuarios")
    fun crearUsuario(@Body usuario: Usuario): Call<Usuario>

    @PUT("usuarios/{id}")
    fun actualizarUsuario(@Path("id") id: Int, @Body usuario: Usuario): Call<Usuario>

    @DELETE("usuarios/{id}")
    fun eliminarUsuario(@Path("id") id: Int): Call<Void>
}
