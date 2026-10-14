package mx.tec.redacta.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.tec.redacta.data.ModeloLocal
import mx.tec.redacta.domain.Tarea
import mx.tec.redacta.domain.limpiar
import mx.tec.redacta.domain.separarOpciones
import mx.tec.redacta.domain.tokensPorSegundo

@HiltViewModel
class RedactaViewModel @Inject constructor(private val modelo: ModeloLocal) : ViewModel() {
    private val _ui = MutableStateFlow(RedactaUiState())
    val ui: StateFlow<RedactaUiState> = _ui.asStateFlow()
    private var redaccion: Job? = null

    init {
        viewModelScope.launch { modelo.estado.collect { estado -> _ui.update { it.copy(modelo = estado) } } }
        buscarModelo()
    }

    fun buscarModelo() { viewModelScope.launch { modelo.cargar() } }
    fun alCambiarTexto(texto: String) = _ui.update { it.copy(texto = texto) }
    fun elegirTarea(tarea: Tarea) = _ui.update { it.copy(tarea = tarea) }
    fun usarRespuesta(texto: String) = _ui.update { it.copy(texto = texto, respuesta = null) }

    fun redactar() {
        val actual = _ui.value
        if (!actual.puedeRedactar) return
        _ui.update { it.copy(respuesta = Respuesta(actual.tarea)) }
        redaccion = viewModelScope.launch {
            var tokens = 0
            var primero = 0L
            try {
                modelo.generar(actual.tarea.pedidoPara(actual.texto)).collect { pedazo ->
                    val ahora = System.nanoTime() / 1_000_000
                    if (tokens == 0) primero = ahora
                    tokens++
                    alResponder { r ->
                        r.copy(texto = r.texto + pedazo, tokens = tokens,
                            tokensPorSegundo = tokensPorSegundo(tokens, ahora - primero))
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                alResponder { it.copy(error = error.message ?: "El modelo no pudo contestar") }
            } finally {
                alResponder { r ->
                    val opciones = if (r.tarea == Tarea.Responder) separarOpciones(r.texto) else emptyList()
                    r.copy(texto = limpiar(r.texto), generando = false, opciones = opciones)
                }
            }
        }
    }

    fun detener() { redaccion?.cancel() }

    private fun alResponder(cambio: (Respuesta) -> Respuesta) =
        _ui.update { estado -> estado.copy(respuesta = estado.respuesta?.let(cambio)) }
}
