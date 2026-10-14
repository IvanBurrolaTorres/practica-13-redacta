package mx.tec.redacta.data

import android.content.Context
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Content
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.Message
import com.google.ai.edge.litertlm.SamplerConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import mx.tec.redacta.domain.EstadoModelo
import mx.tec.redacta.domain.Pedido

/** Gemma 3 1B corriendo localmente en el CPU del dispositivo. */
@Singleton
class GemmaLocal @Inject constructor(
    @ApplicationContext private val contexto: Context
) : ModeloLocal {

    private val _estado = MutableStateFlow<EstadoModelo>(EstadoModelo.Cargando)
    override val estado: StateFlow<EstadoModelo> = _estado.asStateFlow()
    private var motor: Engine? = null
    private val candado = Mutex()

    override suspend fun cargar() = candado.withLock {
        if (motor != null) return@withLock
        val archivo = File(contexto.getExternalFilesDir(null), ARCHIVO)
        if (!archivo.exists()) {
            _estado.value = EstadoModelo.Falta(archivo.absolutePath)
            return@withLock
        }
        _estado.value = EstadoModelo.Cargando
        val nuevo = Engine(
            EngineConfig(
                modelPath = archivo.absolutePath,
                backend = Backend.CPU(),
                cacheDir = contexto.cacheDir.absolutePath
            )
        )
        // Experimento D2: deliberadamente en el hilo principal para observar el bloqueo.
        runCatching { nuevo.initialize() }
            .onSuccess {
                motor = nuevo
                _estado.value = EstadoModelo.Listo
            }
            .onFailure { error ->
                if (nuevo.isInitialized()) nuevo.close()
                if (error is CancellationException) throw error
                _estado.value = EstadoModelo.Error(error.message ?: "No se pudo cargar el modelo")
            }
    }

    override fun generar(pedido: Pedido): Flow<String> = flow {
        val motor = checkNotNull(motor) { "El modelo todavía no está cargado" }
        val config = ConversationConfig(
            systemInstruction = pedido.instruccion?.let { Contents.of(it) },
            initialMessages = pedido.ejemplo?.let {
                listOf(Message.user(it.pregunta), Message.model(it.respuesta))
            }.orEmpty(),
            samplerConfig = pedido.temperatura?.let {
                SamplerConfig(topK = 40, topP = 0.95, temperature = it)
            },
            maxOutputToken = MAX_TOKENS
        )
        motor.createConversation(config).use { conversacion ->
            try {
                conversacion.sendMessageAsync(pedido.mensaje).collect { mensaje ->
                    emit(mensaje.contents.contents.filterIsInstance<Content.Text>().joinToString("") { it.text })
                }
            } catch (error: CancellationException) {
                conversacion.cancelProcess()
                throw error
            }
        }
    }.flowOn(Dispatchers.Default)

    companion object {
        const val ARCHIVO = "gemma3-1b-it-int4.litertlm"
        const val MAX_TOKENS = 256
    }
}
