package com.tecsup.lavacampus.data.repository

import androidx.room.withTransaction
import com.tecsup.lavacampus.data.local.LavaDatabase
import com.tecsup.lavacampus.data.local.entity.DetallePedido
import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.ClienteResumen
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.IngresoServicio
import com.tecsup.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class LavaRepositoryRoom(private val db: LavaDatabase) : LavaRepository {
    private val servicioDao = db.servicioDao()
    private val usuarioDao = db.usuarioDao()
    private val pedidoDao = db.pedidoDao()
    private val detalleDao = db.detalleDao()

    override suspend fun login(correo: String, clave: String): Usuario? = usuarioDao.login(correo, clave)

    override suspend fun obtenerUsuario(id: Int): Usuario? = usuarioDao.obtener(id)

    override suspend fun obtenerUsuarioPorRol(rol: String): Usuario? = usuarioDao.obtenerPorRol(rol)

    override fun observarClientes(): Flow<List<ClienteResumen>> = usuarioDao.observarClientes()

    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) servicioDao.insertar(s) else servicioDao.actualizar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) {
        servicioDao.eliminar(s)
    }

    override suspend fun cambiarActivo(id: Int, activo: Boolean) {
        servicioDao.cambiarActivo(id, activo)
    }

    override suspend fun servicioTieneDetalles(id: Int): Boolean = detalleDao.contarPorServicio(id) > 0

    override suspend fun crearPedido(
        usuarioId: Int,
        lineas: List<LineaPedido>,
        fechaEntrega: String,
        nota: String
    ): Long = db.withTransaction {
        val detalles = lineas.map { linea ->
            val servicio = servicioDao.obtener(linea.servicioId) ?: error("Servicio no encontrado")
            DetallePedido(
                pedidoId = 0,
                servicioId = servicio.id,
                cantidad = linea.cantidad,
                precioUnitario = servicio.precio,
                subtotal = linea.cantidad * servicio.precio
            )
        }
        val pedidoId = pedidoDao.insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = LocalDate.now().toString(),
                fechaEntrega = fechaEntrega,
                estado = EstadoPedido.RECIBIDO.name,
                total = detalles.sumOf { it.subtotal },
                pagado = false,
                nota = nota
            )
        )
        detalleDao.insertarTodos(detalles.map { it.copy(pedidoId = pedidoId.toInt()) })
        pedidoId
    }

    override suspend fun contarActivos(usuarioId: Int): Int = pedidoDao.contarActivos(usuarioId)

    override fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>> =
        pedidoDao.observarPorUsuario(usuarioId)

    override fun observarPedidos(): Flow<List<PedidoConDetalle>> = pedidoDao.observarTodos()

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> = pedidoDao.observarUno(id)

    override suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?) {
        pedidoDao.actualizarEstado(id, estado.name, casillero)
    }

    override suspend fun marcarPagado(id: Int, pagado: Boolean) {
        pedidoDao.actualizarPagado(id, pagado)
    }

    override fun observarCasillerosOcupados(): Flow<List<String>> = pedidoDao.observarCasillerosOcupados()

    override fun observarPedidosDeFecha(fecha: String): Flow<Int> = pedidoDao.contarPorFecha(fecha)

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> = pedidoDao.contarPorEstado(estado.name)

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> = pedidoDao.ingresosDesde(fechaInicio)

    override fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>> =
        detalleDao.observarIngresosPorServicio(fechaInicio)
}
