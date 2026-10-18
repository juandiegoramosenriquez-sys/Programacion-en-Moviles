package com.tecsup.lavacampus.data.local

import com.tecsup.lavacampus.data.local.entity.DetallePedido
import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.model.Unidad
import java.time.LocalDate

object DatosIniciales {

    suspend fun sembrar(db: LavaDatabase) {
        if (db.usuarioDao().contar() > 0) return

        val lavado = guardar(db, Servicio(nombre = "Lavado y secado", descripcion = "Lavado, secado y doblado de tu ropa",
            unidad = Unidad.KG.name, precio = 8.00, horasEntrega = 24, icono = "wash"))
        val planchado = guardar(db, Servicio(nombre = "Planchado", descripcion = "Planchado de camisas, polos y pantalones",
            unidad = Unidad.PRENDA.name, precio = 2.50, horasEntrega = 48, icono = "iron"))
        val seco = guardar(db, Servicio(nombre = "Lavado en seco", descripcion = "Para sacos, abrigos y prendas delicadas",
            unidad = Unidad.PRENDA.name, precio = 12.00, horasEntrega = 48, icono = "shirt"))
        guardar(db, Servicio(nombre = "Edredón / frazada", descripcion = "Lavado de edredones y frazadas",
            unidad = Unidad.UNIDAD.name, precio = 20.00, horasEntrega = 48, activo = false, icono = "wash"))

        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(nombre = "Ana Torres", codigo = "U202310234", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Luis Quispe", codigo = "U202311587", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Marta Rojas", codigo = "U202312946", correo = "marta@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Administrador", correo = "admin@lavacampus.test", clave = "admin", rol = Rol.ADMIN.name)
            )
        )
        val ana = db.usuarioDao().login("ana@correo.test", "1234")!!.id
        val luis = db.usuarioDao().login("luis@correo.test", "1234")!!.id
        val marta = db.usuarioDao().login("marta@correo.test", "1234")!!.id

        pedido(db, ana, 6, EstadoPedido.CANCELADO, listOf(lavado to 1.0))
        pedido(db, marta, 4, EstadoPedido.ENTREGADO, listOf(lavado to 1.5, planchado to 2.0), pagado = true)
        pedido(db, luis, 3, EstadoPedido.ENTREGADO, listOf(lavado to 2.5, seco to 2.0), pagado = true)
        pedido(db, marta, 2, EstadoPedido.EN_PROCESO, listOf(lavado to 5.0))
        pedido(db, ana, 2, EstadoPedido.EN_PROCESO, listOf(lavado to 2.0, planchado to 4.0))
        pedido(db, ana, 1, EstadoPedido.ENTREGADO, listOf(planchado to 6.0), pagado = true)
        pedido(db, luis, 1, EstadoPedido.LISTO, listOf(lavado to 4.0), casillero = "C-05")
        pedido(db, marta, 1, EstadoPedido.LISTO, listOf(planchado to 8.0), casillero = "C-07")
        pedido(db, luis, 0, EstadoPedido.RECIBIDO, listOf(seco to 1.0))
        pedido(db, ana, 0, EstadoPedido.RECIBIDO, listOf(lavado to 3.0))
    }

    private suspend fun guardar(db: LavaDatabase, s: Servicio): Servicio =
        s.copy(id = db.servicioDao().insertar(s).toInt())

    private suspend fun pedido(
        db: LavaDatabase,
        usuarioId: Int,
        haceDias: Long,
        estado: EstadoPedido,
        lineas: List<Pair<Servicio, Double>>,
        casillero: String? = null,
        pagado: Boolean = false
    ) {
        val registro = LocalDate.now().minusDays(haceDias)
        val dias = lineas.maxOf { it.first.horasEntrega } / 24
        val detalles = lineas.map { (s, cantidad) ->
            DetallePedido(
                pedidoId = 0,
                servicioId = s.id,
                cantidad = cantidad,
                precioUnitario = s.precio,
                subtotal = cantidad * s.precio
            )
        }
        val pedidoId = db.pedidoDao().insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = registro.toString(),
                fechaEntrega = registro.plusDays(dias.toLong()).toString(),
                estado = estado.name,
                total = detalles.sumOf { it.subtotal },
                casillero = casillero,
                pagado = pagado
            )
        ).toInt()
        db.detalleDao().insertarTodos(detalles.map { it.copy(pedidoId = pedidoId) })
    }
}
