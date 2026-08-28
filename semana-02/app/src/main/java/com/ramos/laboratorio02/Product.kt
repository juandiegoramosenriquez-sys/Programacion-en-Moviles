package com.ramos.laboratorio02

abstract class Producto(
    private val nombre: String,
    private val precio: Double,
    private var cantidad: Int
) {
    fun getNombre(): String = nombre
    fun getPrecio(): Double = precio
    fun getCantidad(): Int = cantidad
    fun setCantidad(nueva: Int) { cantidad = nueva }

    fun calcularImporte(): Double = precio * cantidad

    fun descripcion(): String = "$nombre - S/ $precio x $cantidad"

    abstract fun calcularDescuento(): Double
}