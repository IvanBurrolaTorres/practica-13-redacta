# Experimento D2 — carga en el hilo principal

Esta rama elimina `withContext(Dispatchers.Default)` de `GemmaLocal.cargar()` a propósito.
Al abrir la app con el modelo presente, la carga nativa ocupa el hilo que dibuja la UI:
se esperan cuadros omitidos, pantalla congelada y riesgo de ANR. Las 24 pruebas con
`ModeloFalso` no demuestran el bloqueo porque su `cargar()` es instantáneo.

**Observación real en emulador: pendiente.** No se conectó un emulador en el entorno
donde se preparó este repositorio. Para completar el experimento: abrir la app, filtrar
Logcat por `Skipped`, anotar cuadros omitidos/tiempo congelado y volver a `main`.
