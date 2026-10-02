package com.DiegoRamos.semana06.model

data class Producto(
    val nombre: String,
    val precio: Double
)

// Datos de prueba en memoria
val listaProductos = listOf(
    Producto("Audífonos", 89.00),
    Producto("Smartwatch", 199.00),
    Producto("Funda celular", 25.00),
    Producto("Mouse inalámbrico", 59.90),
    Producto("Teclado mecánico", 189.00)
)