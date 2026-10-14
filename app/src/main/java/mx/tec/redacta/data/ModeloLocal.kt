package mx.tec.redacta.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.redacta.domain.EstadoModelo
import mx.tec.redacta.domain.Pedido

/** Interfaz del modelo; permite sustituir Gemma por un modelo falso en pruebas. */
interface ModeloLocal {
    val estado: StateFlow<EstadoModelo>
    suspend fun cargar()
    fun generar(pedido: Pedido): Flow<String>
}
