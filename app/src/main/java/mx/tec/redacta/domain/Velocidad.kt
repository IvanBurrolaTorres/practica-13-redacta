package mx.tec.redacta.domain

/** Tokens por segundo desde la llegada del primer token; con uno aún no hay intervalo medible. */
fun tokensPorSegundo(tokens: Int, milisDesdeElPrimero: Long): Double? =
    if (tokens < 2 || milisDesdeElPrimero <= 0) null
    else (tokens - 1) * 1000.0 / milisDesdeElPrimero
