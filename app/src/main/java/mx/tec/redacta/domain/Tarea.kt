package mx.tec.redacta.domain

/** Prompts probados para un modelo pequeño: contexto delimitado, límites y ejemplo cuando ayuda. */
enum class Tarea(
    val etiqueta: String,
    val pedido: String,
    val rotulo: String = "Texto",
    val ejemplo: Ejemplo? = null,
    val temperatura: Double = 0.2
) {
    Amable(
        "Más amable",
        "Reescribe este texto con un tono amable y respetuoso. Conserva todos los datos y no inventes nada. " +
            "Escribe solo el texto nuevo, en 50 tokens o menos.",
        ejemplo = Ejemplo(
            "Entreguen el proyecto el viernes. No voy a aceptar nada tarde, ni me escriban para pedir prórroga.",
            "Les recuerdo que el proyecto se entrega el viernes. Por favor, planéenlo con tiempo: esta vez " +
                "no podré aceptar entregas tarde ni dar prórrogas. ¡Gracias!"
        )
    ),
    Breve(
        "Más corto",
        "Resume este texto en una sola oración. Usa solo lo que dice el texto. Escribe solo el resumen, en 50 tokens o menos."
    ),
    Responder(
        "3 respuestas",
        "Recibí este mensaje. Escribe tres respuestas cortas que yo podría enviar. " +
            "Numéralas 1., 2. y 3., una por renglón, sin nada más y en 50 tokens o menos en total.",
        rotulo = "Mensaje",
        temperatura = 0.8
    );

    fun enmarcar(texto: String): String = "$pedido\n\n$rotulo: «${texto.trim()}»"

    fun pedidoPara(texto: String): Pedido = Pedido(
        mensaje = enmarcar(texto),
        ejemplo = ejemplo?.let { Ejemplo(enmarcar(it.pregunta), it.respuesta) },
        temperatura = temperatura
    )
}
