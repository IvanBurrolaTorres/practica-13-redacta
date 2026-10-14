# Bitácora — Práctica 13: Redacta

## Ejercicio 0 — ¿Nube o teléfono?

| Caso | Dónde conviene | Por qué |
|---|---|---|
| Sugerir respuestas en un chat privado | Teléfono, si la calidad de un modelo pequeño basta | El texto no sale del dispositivo y funciona sin señal. |
| Resumir un PDF de 80 páginas | Nube o un modelo local con más contexto/memoria | Un modelo de 1B en un teléfono no suele poder manejar todo el documento de una vez; si es sensible, se puede dividir o buscar una solución local más potente. |
| Traducir carteles durante un viaje sin datos | Teléfono | Tiene que funcionar sin conexión y puede procesar el texto localmente. |
| Decir si va a llover hoy | Servicio con datos meteorológicos actuales | Un modelo local sin conexión no conoce el clima en tiempo real. |

## Ejercicio B3 — ¿Qué tan rápido es tu teléfono?

Medición real con **Recado → Más amable** en el AVD `Medium_Phone` (2 GB):

- Tokens mostrados: **41**
- Velocidad al terminar: **32.2 tok/s**
- Primer texto visible: **1.69 s** desde tocar «Redactar», medido con capturas del emulador y OCR local.
- Respuesta observada: «Hola. Me gustaría que laváramos los platos en el fregadero hoy. No me gusta que se atraganten y me da un poco de asco. Gracias por tu cooperación.»
- Añadió contenido que no estaba en el recado («se atraganten», «me da un poco de asco»). La prueba unitaria usa pedazos sintéticos y no mide la velocidad real de Gemma.

## Ejercicio C1 — ¿Por qué no obedeció?

No se capturó la salida con el prompt antiguo antes de implementar C2. En la prueba real con las reglas de C2 aún hubo cambios de sentido y contenido inventado; por eso el prompt reduce la ambigüedad, pero no garantiza obediencia.

| Tarea con C2 | Qué sobró o cambió |
|---|---|
| Más amable, Recado | Añadió que los platos «se atraganten» y que le da asco. |
| Más corto, Aviso | Omitió fechas, hora, salón y duración concreta. |
| 3 respuestas, Mensaje | Produjo tres respuestas breves que el parser separó en tarjetas. |

## Ejercicio C3 — El límite de un modelo de 1B

El emulador recibió la frase sin tilde en «Manana» por la entrada ADB: «Manana no hay clase. No pregunten. Lo que falte lo ven en su casa y el lunes hay examen de todo.»

Gemma contestó: «Les recordamos que mañana no hay clase. No se atrevan y no hagan preguntas. El examen será en su casa y el lunes será para todo. Gracias.» Mostró **36 tokens y 34.8 tok/s**. Cambió «No pregunten» por «No se atrevan», inventó que el examen será en casa y perdió que el lunes hay examen de todo. La respuesta demuestra que hay que revisar el sentido, aunque el pedido esté delimitado y prohíba inventar.

## Prueba real del entorno — 14 oct 2026

- Instalé y abrí `mx.tec.redacta` en el AVD `Medium_Phone` (Android 37.2 beta3). Antes de copiar el archivo mostró «Falta el modelo» y la ruta esperada `/storage/emulated/0/Android/data/mx.tec.redacta/files/gemma3-1b-it-int4.litertlm`.
- Descargué el archivo autorizado `gemma3-1b-it-int4.litertlm` (**584,417,280 bytes**) y lo copié con ADB. Después, la app mostró **«Gemma 3 1B · listo»**.
- El AVD reportó **4.5 GB libres en `/data`** después de instalar la app y antes de copiar el modelo.
- `AndroidManifest.xml` no declara `INTERNET`; el paquete tampoco mostró ese permiso concedido.
- Intenté iniciar el AVD con 4 GB. El host avisó que solo tenía unos 3.2 GB disponibles (5.1 GB requeridos), cambió a renderizado por software y el emulador se desconectó. Con la configuración de 2 GB sí pude cargar Gemma y generar respuestas.
- El experimento D2 está documentado con sus cuadros omitidos en `docs/experimento-hilo.md`.
