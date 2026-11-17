# PROMPTS · Fase 2 con IA (rama mejora-ia)

El PDF sugiere usar Gemini en Android Studio. Para esta fase usé **Claude (Claude Code)** como asistente de IA. Cada mejora la revisé, la probé en el emulador y corregí lo que no respetaba las reglas del laboratorio: MVVM + Repository, ViewModel sin `Context`, nada de lógica en los Composables y nada de `GlobalScope`, `runBlocking` ni `Thread.sleep`.

## Mejoras realizadas

| # | Mejora | Capas que cambian | Commit |
|---|---|---|---|
| 1 | Progreso desde el Repository con `Flow` | Repository + ViewModel | `feat(progreso)` |
| 2 | Tiempo máximo con `withTimeout` | ViewModel + View | `feat(tiempo)` |
| 3 | Reintento automático con espera creciente | ViewModel + View | `feat(reintento)` |
| 4 | Primos con progreso | Repository + ViewModel + View | `feat(primos)` |
| 5 | Historial con DataStore | Repository + ViewModel + View | `feat(historial)` |
| 6 | Comparación de tiempos | Model + ViewModel + View | `feat(comparacion)` |
| 7 | Rediseño profesional con estilo Azul noche | ViewModel + View + tema | `feat(diseno)` |

## Tabla de prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Mueve la tarea con progreso al Repository y expón el progreso como un Flow, como pide la mejora del PDF» | Propuso `tareaConProgreso(): Flow<Float>` con `flow { emit(i / 100f) }` y `flowOn(Dispatchers.Default)`, y que el ViewModel lo lea con `collect` | Mantuve el `try/finally` del Paso 4 para que `corriendo` vuelva a `false` aunque se cancele, y los `Log.d("HILO", ...)` | El trabajo pesado va en el Repository y el ViewModel solo coordina. Sin el `finally`, cancelar dejaría los botones bloqueados |
| «Agrega un tiempo máximo a las cargas con withTimeout: si tardan más de 2 s, mostrar Tardó demasiado» | Propuso envolver las cargas en `withTimeout(2000)` y atrapar el error | Cambié el límite a **4 s**, lo puse detrás de un interruptor y moví el `catch (TimeoutCancellationException)` **antes** del `catch (CancellationException)` | Con 2 s las dos cargas (5 s y 3 s) fallan siempre y no se aprende nada. Con 4 s, *Una por una* falla y *A la vez* pasa, así se ve la ventaja de `async`. `TimeoutCancellationException` es hija de `CancellationException`; si no va primero, se relanza y nunca se muestra el mensaje |
| «Reintenta la carga hasta 3 veces con una espera creciente antes de mostrar el error» | Propuso una función `conReintento` con `delay` que se duplica (500 ms, 1000 ms) | Relancé la `CancellationException` dentro del reintento, reinicio el contador de intentos al empezar cada carga y solo reintento los pedidos, no el perfil | Si se atrapa la cancelación, la corrutina no se puede detener. Sin reiniciar el contador, la pantalla mostraba "intento 3 de 3" de la carga anterior. El perfil no falla, repetirlo solo gasta tiempo |
| «Muestra el porcentaje del cálculo de primos mientras avanza» | Propuso emitir un avance desde el Repository con un `Flow` | Emití solo **un avance por cada 1 %** y usé `currentCoroutineContext().ensureActive()` dentro del `flow` | Emitir en cada número (500 000 veces) satura la pantalla. Dentro de un `flow` hay que usar `currentCoroutineContext()` para revisar la cancelación |
| «Guarda los últimos 5 resultados con DataStore y muéstralos al abrir la app» | Propuso DataStore Preferences y pasar el `Context` | Creé un `HistorialRepository` aparte; el `Context` solo llega a su implementación desde la Factory. Cambié el separador `\n` por `|` | El PDF prohíbe que el ViewModel reciba `Context`. El separador con salto de línea generó un error de compilación al escribirse literal en el archivo |
| «Agrega una tabla que acumule los tiempos de Una por una y A la vez» | Propuso calcular promedios en la pantalla | Moví los promedios, el ahorro y el porcentaje a la clase `ComparacionTiempos` en `model` | La regla de oro del PDF: la pantalla solo dibuja el estado, no calcula |
| «Rediseña la app para que se vea profesional, sin los colores por defecto de Android» | Con la skill de diseño *ui-ux-pro-max* propuso estilo plano, turquesa `#0D9488` con naranja de acento y una maqueta | Usé turquesa más oscuro `#0F766E` como color principal, agregué modo oscuro, calculé `tareasActivas` con `combine` en el ViewModel y subí el contraste de los interruptores apagados | El texto blanco sobre `#0D9488` tenía poco contraste. Contar tareas activas es lógica, así que va en el ViewModel. Los interruptores apagados casi no se veían |
| «No me gusta el modo oscuro ni el aspecto por defecto de Android; quiero algo claro, profesional, un poco colorido y que se entienda» | Con la skill *ui-ux-pro-max* mostró varias rondas de maquetas claras: Suizo minimalista (blanco, negro y dorado), opciones con más color (Azul eléctrico, Coral, Esmeralda) y neutros con un toque de color (Azul noche, Grafito, Salvia, Terracota) | Probé el estilo suizo en el emulador, pero solo blanco y negro se veía vacío. Elegí **Azul noche**: azul pizarra `#1E293B` en lugar de negro, celeste `#2563EB` para el progreso y los estados, fondo apenas tintado, control segmentado e indicadores de Toques y Activas en la cabecera. La app queda siempre en modo claro | El aspecto "Android" viene de los botones en píldora, los interruptores y el morado de Material, no solo del color. El azul oscuro se lee igual de bien que el negro pero no se ve vacío, y el celeste marca lo que está en movimiento |

## Problemas que aparecieron y cómo se resolvieron

- **Android Studio marcó 54 errores en rojo**, aunque Gradle compilaba sin errores. Era el editor desactualizado tras agregar dependencias nuevas; se resolvió con *Sync Project with Gradle Files*.
- **Unir `main` con `mejora-ia`** dio un conflicto en la guía del Lab 12. Se conservó la versión de `mejora-ia` porque cada rama tiene su propia guía.

## Verificación

- Compila sin errores con `assembleDebug`.
- `git grep` no encuentra `GlobalScope`, `runBlocking` ni `Thread.sleep` en el código final.
- Cada mejora se probó en el emulador: primos hasta 500 000 dan 41 538, *Una por una* ≈ 5 s, *A la vez* ≈ 3 s, el historial se mantiene al cerrar la app y el reintento muestra "intento 2 de 3".
