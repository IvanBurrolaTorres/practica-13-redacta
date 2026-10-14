# Experimento D2 — carga en el hilo principal

Esta rama elimina `withContext(Dispatchers.Default)` de `GemmaLocal.cargar()` a propósito.
Al abrir la app con el modelo presente, la carga nativa ocupa el hilo que dibuja la UI:
se esperan cuadros omitidos, pantalla congelada y riesgo de ANR. Las 24 pruebas con
`ModeloFalso` no demuestran el bloqueo porque su `cargar()` es instantáneo.

## Observación real (14 oct 2026)

- AVD `Medium_Phone`, 2 GB de RAM, modelo ya copiado y cargado desde almacenamiento local.
- Logcat: `Skipped 402 frames! The application may be doing too much work on its main thread.`
  A 60 Hz son aproximadamente **6.7 s** sin dibujar cuadros.
- Durante la carga la captura de pantalla mostró solo el splash en blanco con el icono; al
  terminar volvió la pantalla principal y el chip indicó «Gemma 3 1B · listo».
- Hubo también un aviso de 45 cuadros omitidos en el primer arranque, antes del bloqueo largo.

Las pruebas con `ModeloFalso` no detectan esto porque su `cargar()` regresa inmediatamente.
La implementación correcta en `main` vuelve a ejecutar `initialize()` en
`Dispatchers.Default`.
