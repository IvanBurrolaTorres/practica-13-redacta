package mx.tec.redacta

import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import mx.tec.redacta.data.ModeloLocal
import mx.tec.redacta.domain.EstadoModelo
import mx.tec.redacta.domain.Pedido

/** Sustituto instantáneo del modelo nativo para pruebas de JVM. */
class ModeloFalso(
    private val alCargar: EstadoModelo = EstadoModelo.Listo
) : ModeloLocal {
    private val _estado = MutableStateFlow<EstadoModelo>(EstadoModelo.Cargando)
    override val estado: StateFlow<EstadoModelo> = _estado
    var pedazos: List<String> = listOf("Hola", ", ", "mundo")
    var seQuedaGenerando = false
    var falla: Exception? = null
    val pedidos = mutableListOf<Pedido>()

    override suspend fun cargar() { _estado.value = alCargar }

    override fun generar(pedido: Pedido): Flow<String> = flow {
        pedidos += pedido
        pedazos.forEach { emit(it) }
        falla?.let { throw it }
        if (seQuedaGenerando) awaitCancellation()
    }
}
