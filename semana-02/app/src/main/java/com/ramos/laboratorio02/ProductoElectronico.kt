package com.ramos.laboratorio02

class ProductoElectronico(nombre: String, precio: Double, cantidad: Int) :
    Product(nombre, precio, cantidad) {
    override fun calcularDescuento(): Double = calcularImporte() * 0.05
}