package com.ramos.laboratorio02

class ProductoAlimento(nombre: String, precio: Double, cantidad: Int) :
    Product(nombre, precio, cantidad) {
    override fun calcularDescuento(): Double = 0.0
}