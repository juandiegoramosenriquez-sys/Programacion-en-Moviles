package com.ramos.laboratorio02

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d S/ %8.2f",
            i, p.nombre, p.cantidad, importe))
        i++
    }
    println("---------------------------------------")
}

fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}


fun main() {
    println("=========================================")
    println("      CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    val nombreCliente = "Juan Ramos"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Laptop ASUS", 3000.0, 1))
    carrito.add(Producto("MousePad", 25.0, 2))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }


    println()
    println("Cantidad de productos: ${carrito.size}")
    println()

    mostrarDetalle(carrito)

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    val descuento = calcularDescuento(total)
    val totalFinal = total - descuento

    println(String.format("%-18s: S/ %8.2f", "Subtotal", subtotal))
    println(String.format("%-18s: S/ %8.2f", "IGV (18%)", igv))
    println(String.format("%-18s: S/ %8.2f", "TOTAL A PAGAR", total))

    if (descuento > 0) {
        val pct = if (total > 5000) "10%" else "5%"
        println(String.format("%-18s: S/ %8.2f", "Descuento ($pct)", descuento))
        println(String.format("%-18s: S/ %8.2f", "TOTAL CON DESC.", totalFinal))
    }

    println()
    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/ %.2f)", masCaro.precio))
    }

    println()
    println("--- ELIMINANDO 'MousePad' DEL CARRITO ---")
    carrito.removeIf { it.nombre.equals("MousePad", ignoreCase = true) }

    mostrarDetalle(carrito)
    val nuevoSubtotal = calcularSubtotal(carrito)
    val nuevoIgv = calcularIGV(nuevoSubtotal)
    val nuevoTotal = calcularTotal(nuevoSubtotal, nuevoIgv)
    println(String.format("%-18s: S/ %8.2f", "NUEVO TOTAL", nuevoTotal))
}



