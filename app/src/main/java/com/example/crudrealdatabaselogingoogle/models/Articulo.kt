package com.example.crudrealdatabaselogingoogle.models

import java.io.Serializable

data class Articulo(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0
): Serializable
