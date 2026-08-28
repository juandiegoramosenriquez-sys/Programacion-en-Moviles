package com.ramos.laboratorio02

class ProductoRopa(nombre: String, precio: Double, cantidad: Int) :
    Product(nombre, precio, cantidad) {
    override fun calcularDescuento(): Double = calcularImporte() * 0.10
}