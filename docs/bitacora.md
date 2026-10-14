# Bitácora — Práctica 13: Redacta

> Registro inicial preparado con la implementación. Las observaciones que requieren ejecutar Gemma en un dispositivo se dejan explícitamente pendientes; no se inventaron mediciones.

## Ejercicio 0 — ¿Nube o teléfono?

| Caso | Dónde conviene | Por qué |
|---|---|---|
| Sugerir respuestas en un chat privado | Teléfono, si la calidad de un modelo pequeño basta | La conversación no sale del dispositivo y puede funcionar sin señal. |
| Resumir un PDF de 80 páginas | Nube | El contexto y la memoria necesarios suelen superar lo práctico para un modelo de 1B en un teléfono. Para documentos sensibles, una opción local más potente o dividir el documento sería preferible. |
| Traducir carteles durante un viaje sin datos | Teléfono | Debe funcionar sin conexión y puede procesar el texto localmente. |
| Decir si va a llover hoy | Nube/servicio con datos meteorológicos | Requiere información actualizada; un modelo local sin conexión no conoce el clima en tiempo real. |

## Ejercicio B3 — ¿Qué tan rápido es tu teléfono?

Pendiente de medir con el modelo real y la tarea **Recado → Más amable**:

- Tokens mostrados: ______
- Tokens por segundo mostrados: ______ tok/s
- Tiempo hasta la primera palabra (cronómetro): ______ s
- Dispositivo/emulador y RAM: ______
- Comparación con la tabla de la práctica/otro equipo: ______

La prueba unitaria usa pedazos sintéticos y no representa la velocidad del motor Gemma.

## Ejercicio C1 — ¿Por qué no obedeció?

Hipótesis para comprobar comparando la respuesta antes y después de C2: si solo se le presenta el texto con una instrucción genérica, Gemma puede interpretar el texto como un mensaje dirigido al modelo, agregar una introducción/opciones o extenderse. Enmarcar el texto, prohibir explícitamente contenido adicional y dar un ejemplo de salida concreta reduce esa ambigüedad. Las respuestas reales observadas antes de aplicar C2: **pendientes de anotar en el dispositivo**.

| Tarea | Qué sobró en la respuesta antes de C2 |
|---|---|
| Más amable | Pendiente de observación |
| Más corto | Pendiente de observación |
| 3 respuestas | Pendiente de observación |

## Ejercicio C3 — El límite de un modelo de 1B

Probar el aviso «Mañana no hay clase. No pregunten. Lo que falte lo ven en su casa y el lunes hay examen de todo.» con **Más amable** y comprobar que no cambien los datos. Después, usar un texto de Aviso para buscar una frase añadida que no venga del original.

Resultado real y frase inventada: **pendientes de observar con Gemma**. Un modelo de 1B puede omitir o añadir detalles pese a las instrucciones; por eso se advierte al usuario que revise la respuesta y se prueba el formato, pero no se puede garantizar semánticamente que cada reescritura conserve todo el sentido.
