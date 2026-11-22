package com.example.doki_doki.network

import com.example.doki_doki.model.Investigador
import com.example.doki_doki.model.Proyecto
import com.example.doki_doki.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface XanoApiService {

    // --- INVESTIGADORES ---
    @GET("investigador")
    suspend fun getInvestigadores(): List<Investigador>

    @POST("investigador")
    suspend fun crearInvestigador(@Body investigador: Investigador): Investigador

    @PATCH("investigador/{id}")
    suspend fun actualizarInvestigador(@Path("id") id: Int, @Body investigador: Investigador): Investigador

    @DELETE("investigador/{id}")
    suspend fun eliminarInvestigador(@Path("id") id: Int): Response<Unit>

    // --- PROYECTOS ---
    @GET("proyecto")
    suspend fun getProyectos(): List<Proyecto>

    @POST("proyecto")
    suspend fun crearProyecto(@Body proyecto: Proyecto): Proyecto

    @PATCH("proyecto/{id}")
    suspend fun actualizarProyecto(@Path("id") id: Int, @Body proyecto: Proyecto): Proyecto

    @DELETE("proyecto/{id}")
    suspend fun eliminarProyecto(@Path("id") id: Int): Response<Unit>

    // --- USUARIOS ---
    @GET("usuario")
    suspend fun getUsuarios(): List<Usuario>

    @POST("usuario")
    suspend fun registrarUsuario(@Body usuario: Usuario): Usuario
}