package mx.tec.redacta.domain

/** Extrae únicamente renglones numerados y limpia los formatos comunes del modelo. */
fun separarOpciones(texto: String): List<String> = texto.lines()
    .mapNotNull { renglon -> NUMERADO.matchEntire(renglon.replace("**", "").trim())?.groupValues?.get(1) }
    .map { it.trim().trim('"', '«', '»', '“', '”').trim() }
    .filter { it.isNotEmpty() }

private val NUMERADO = Regex("""^\d+\s*[.)]\s*[,;:\-]?\s*(.+)$""")
