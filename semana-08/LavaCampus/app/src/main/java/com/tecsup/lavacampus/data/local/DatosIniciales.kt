package com.tecsup.lavacampus.data.local

import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.model.Unidad

object DatosIniciales {
    suspend fun sembrar(db: LavaDatabase) {
        if (db.servicioDao().contar() == 0) {
            db.servicioDao().insertarTodos(servicios())
        }
    }

    private fun servicios() = listOf(
        Servicio(nombre = "Lavado y secado", descripcion = "Lavado, secado y doblado de tu ropa",
            unidad = Unidad.KG.name, precio = 8.00, horasEntrega = 24, icono = "wash"),
        Servicio(nombre = "Planchado", descripcion = "Planchado de camisas, polos y pantalones",
            unidad = Unidad.PRENDA.name, precio = 2.50, horasEntrega = 48, icono = "iron"),
        Servicio(nombre = "Lavado en seco", descripcion = "Para sacos, abrigos y prendas delicadas",
            unidad = Unidad.PRENDA.name, precio = 12.00, horasEntrega = 48, icono = "shirt"),
        Servicio(nombre = "Edredón / frazada", descripcion = "Lavado de edredones y frazadas",
            unidad = Unidad.UNIDAD.name, precio = 20.00, horasEntrega = 48, activo = false, icono = "wash")
    )
}
