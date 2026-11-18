# PROMPTS · Fase 2 con IA (rama mejora-ia)

El PDF sugiere usar Gemini en Android Studio. Para esta fase usé **Claude (Claude Code)** como asistente de IA. Revisé cada mejora, la probé en el emulador y en mi celular, y corregí lo que no respetaba las reglas del laboratorio:

- MVVM + Repository.
- ViewModel sin `Context`.
- Nada de lógica en los Composables.
- La clave de API nunca va en un prompt ni en el repositorio.

## Mejoras realizadas

Hice las 6 mejoras de la tabla del PDF, más el rediseño.

| # | Mejora | Capas que cambian | Commit |
|---|---|---|---|
| 1 | Filtrar por categoría (chips) | ViewModel + View | `feat(filtro)` |
| 2 | Radio de cercanía de 100, 200 o 500 m, con `Circle` | ViewModel + View | `feat(radio)` |
| 3 | Punto azul nativo (`isMyLocationEnabled`) | View | `feat(mapa)` |
| 4 | Seguimiento en vivo con `callbackFlow` | Repository + ViewModel + View | `feat(seguimiento)` |
| 5 | Último lugar visitado con DataStore | Repository + ViewModel | `feat(memoria)` |
| 6 | Mapa en modo oscuro con `MapStyleOptions` | View | `feat(mapa)` |
| 7 | Rediseño Azul noche, con botón de modo claro u oscuro | Repository + ViewModel + View + tema | `feat(diseno)` |

## Tabla de prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega chips Estudio, Comida, Eventos y Servicios que muestren solo esos lugares en el mapa y en la lista» | Propuso filtrar la lista dentro del Composable con `lugares.filter { ... }` | Moví el filtro al ViewModel. `lugaresVisibles` se arma con `combine(_lugares, _categoria)`, y ahí también se calcula `masCercano`, para que el verde sea el más cercano **de esa categoría**. También quité la selección si el lugar elegido queda oculto | El PDF prohíbe la lógica en el Composable. Si el más cercano se calculaba sobre todos los lugares, el marcador verde podía apuntar a un lugar que no se veía |
| «Agrega un selector de 100, 200 o 500 m, dibuja un Circle alrededor de mi posición y marca los lugares dentro del radio» | Propuso comparar `metros <= radio` en cada tarjeta de la lista | El ViewModel expone `dentroDelRadio: StateFlow<Set<Int>>` con `combine`. La lista solo recibe los ids que tiene que marcar | La comparación es lógica y va en el ViewModel. Además, así el conteo de "En el radio 3 de 5" usa el mismo dato |
| «Activa el punto azul y el botón de mi ubicación del mapa, solo si el permiso fue concedido» | Propuso `MapProperties(isMyLocationEnabled = true)` siempre activo | Lo condicioné a `permisoConcedido`, que se actualiza al responder el cuadro de permiso y al pulsar el botón | Con el permiso negado, `isMyLocationEnabled = true` lanza `SecurityException` y la app se cierra |
| «Actualiza la ubicación mientras camino con requestLocationUpdates convertido en un Flow, con botón Iniciar/Detener» | Propuso un `callbackFlow` con `requestLocationUpdates` dentro del ViewModel | Moví el `callbackFlow` al Repository (`seguirPosicion(): Flow<Posicion>`). Agregué `awaitClose { removeLocationUpdates(callback) }` y `close(it)` si falla la solicitud. El ViewModel guarda un `Job` para poder detenerlo | El cliente de ubicación necesita `Context` y no puede estar en el ViewModel. Sin `awaitClose`, el GPS seguía encendido después de pulsar Detener |
| «Guarda con DataStore el último lugar seleccionado y recupéralo al abrir la app» | Propuso crear el DataStore dentro del ViewModel con `context.dataStore` | Creé la interface `PreferenciasRepository` y su implementación `PreferenciasRepositoryDataStore(context)`. El `Context` llega solo desde la Factory | El PDF dice que el ViewModel no recibe `Context`. Con la interface, el ViewModel se puede probar con un repositorio falso |
| «Pon el mapa en estilo oscuro con MapStyleOptions cuando el tema de la app sea oscuro» | Propuso cargar el JSON del estilo dentro de `GoogleMap` en cada recomposición | Cargué el estilo una sola vez con `remember { MapStyleOptions.loadRawResourceStyle(...) }` desde `res/raw/mapa_oscuro.json` | Leer el archivo en cada recomposición es trabajo repetido e innecesario |
| «Rediseña la app con skills para que se vea profesional, usando Material 3, sin perder lo que pide el PDF» | Con las skills *ui-ux-pro-max* y *mobile-app* mostró maquetas: panel de control, mapa con hoja inferior y navegación a pantalla completa | Elegí el **panel de control**: cabecera con indicadores (Más cercano, En el radio, Último), el radio como control segmentado, chips con íconos y botones de 48 dp | Muestra las 6 mejoras de un vistazo y mantiene los elementos que exigen las pruebas: Obtener mi ubicación, Ir al más cercano, marcadores rojo, azul y verde, y los mensajes de error |
| «El mapa se ve muy chico en mi celular, tiene que ser más grande» | Propuso hacer toda la pantalla desplazable | Descarté el desplazamiento y cambié la lista vertical por un **carrusel horizontal** de tarjetas. También compacté la cabecera y junté las coordenadas con "Ir al más cercano" en una sola fila | Un mapa dentro de una pantalla desplazable pelea con los gestos de arrastre. Con el carrusel, el mapa ocupa casi la mitad de la pantalla |
| «En el modo oscuro el azul no me gusta, quiero color carbón; y en el claro que la cabecera también sea carbón para que combine» | Propuso varias paletas con la skill *ui-ux-pro-max* (Azul Material, Azul pizarra y grafito, Azul océano) | Dejé el modo oscuro en carbón (`#121212`, `#1E1E1E`, `#2A2A2A`) y la cabecera de los dos modos en `#1F1F1F`. El azul `#2563EB` queda solo en lo que se toca y en lo importante | El azul sobre azul cansaba la vista. Con el fondo neutro, el azul se reconoce como "acción" y los colores de los marcadores (rojo, azul y verde) siguen siendo los del PDF |
| «Agrega un botón dentro de la app para cambiar entre modo claro y oscuro» | Propuso guardar el modo en una variable `remember` dentro del Composable | Lo guardé en DataStore (`modoOscuro` en `PreferenciasRepository`). El ViewModel lo expone y `MainActivity` se lo pasa a `CampusGPSTheme` y al mapa. Si nunca se toca, sigue el modo del celular | Con `remember`, el modo se perdía al cerrar la app. Además, el mapa oscuro de la mejora 6 ahora sigue el tema de la app y no solo el del celular |

## Problemas que aparecieron y cómo se resolvieron

- **El mapa salía gris al inicio.** La restricción de la clave en Google Cloud tarda unos minutos en activarse. Se resolvió esperando, sin cambiar código.
- **En modo oscuro la app salía morada.** El tema base tenía los colores morados por defecto de Android Studio. Se reemplazaron por las paletas propias de `Color.kt` y `Theme.kt`.
- **Dos dispositivos conectados** (el emulador y mi celular por cable). Los comandos `adb` se apuntaron con `-s emulator-5554` para no instalar en el celular sin querer.
- **La clave de API** nunca se escribió en un prompt. Vive solo en `local.properties`, que está en `.gitignore`.

## Lo que aprendí

- La IA tiende a poner la lógica donde se usa (en el Composable o en el ViewModel con `Context`). Hay que moverla a su capa: cálculos al ViewModel y Android al Repository.
- Un `callbackFlow` siempre necesita `awaitClose`; si no, el recurso (el GPS) sigue encendido.
- Diseñar no es solo cambiar colores: también importan el tamaño de los botones, el contraste, el orden de la pantalla y que cada color tenga un significado.
