# PROMPTS · Fase 2 con IA (rama mejora-ia)

El PDF sugiere usar Gemini en Android Studio. Para esta fase usé **Claude (Claude Code)** como asistente de IA. Cada mejora la revisé, la probé en el emulador y corregí lo que no respetaba las reglas del laboratorio: MVVM + Repository, nada de `Context`, `NotificationManager` ni `BroadcastReceiver` dentro del ViewModel, todo receptor registrado se quita en `onDispose` y los `PendingIntent` son inmutables.

## Mejoras realizadas

| # | Mejora | Capas que cambian | Commit |
|---|---|---|---|
| 1 | Dos canales: Urgentes (`IMPORTANCE_HIGH`) y Avisos; el recordatorio usa el urgente | Model + Notificador + ViewModel | `feat(canales)` |
| 2 | Texto largo al expandir la notificación con `BigTextStyle` | Notificador | `feat(texto-largo)` |
| 3 | Acción **Posponer 10 s** con `addAction` y un `BroadcastReceiver` propio | Notificador + Receiver + ViewModel + Factory | `feat(posponer)` |
| 4 | Receptor del cargador (`ACTION_POWER_CONNECTED` / `DISCONNECTED`) con su propio switch | Receiver + ViewModel + View | `feat(cargador)` |
| 5 | Historial persistente con Room | Repository + ViewModel + Factory | `feat(historial)` |
| 6 | Rediseño editorial blanco, negro y arena con bienvenida, barra inferior y modo oscuro | View + tema | `feat(diseno)` |

La mejora **Push de prueba con Firebase** no se hizo porque necesita crear un proyecto en la consola de Firebase y su archivo `google-services.json`.

## Tabla de prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un canal Urgentes con IMPORTANCE_HIGH y que el recordatorio lo use» | Propuso crear el segundo canal y elegirlo en el Notificador según el origen del aviso | Agregué el campo `urgente` al modelo `Aviso` y es el **ViewModel** el que marca el recordatorio como urgente; creé los dos canales juntos con `createNotificationChannels` | Decidir qué es urgente es lógica de la app y va en el ViewModel; el Notificador solo obedece. Así la regla se puede cambiar sin tocar el código de Android |
| «Muestra el texto completo al expandir la notificación con BigTextStyle» | Propuso `setStyle(BigTextStyle().bigText(...))` repitiendo el mismo texto corto | Escribí un `textoLargo` distinto para cada origen (botón, recordatorio, modo avión y cargador) | Si el texto largo es igual al corto, al expandir no se ve ninguna diferencia y la mejora no aporta nada |
| «Agrega la acción Posponer 10 s con addAction y un BroadcastReceiver propio» | Propuso un receptor que, al tocar Posponer, llamara al ViewModel o mostrara la notificación desde ahí con un `PendingIntent` mutable | Descarté llamar al ViewModel desde el receptor. El receptor solo quita la notificación y emite en un `SharedFlow` (`SolicitudesPosponer`); el ViewModel lo recibe como un `Flow<Unit>` por la Factory y vuelve a llamar a `recordarEn(10)`. Usé `FLAG_IMMUTABLE` y el id del aviso como `requestCode` | El receptor no puede tener una referencia al ViewModel, y el ViewModel no debe conocer el `BroadcastReceiver`. Un `PendingIntent` mutable no hace falta porque el extra no cambia. Usar el id evita que dos acciones se pisen |
| «Escucha ACTION_POWER_CONNECTED y ACTION_POWER_DISCONNECTED con su propio switch» | Propuso registrar el receptor en `onCreate` de la Activity | Lo registré con `DisposableEffect(escuchandoCargador)` igual que el del modo avión, con `RECEIVER_NOT_EXPORTED` y `unregisterReceiver` en `onDispose` | Si se registra en `onCreate` y no se quita, sigue escuchando con el switch apagado y se filtra memoria. Con `DisposableEffect` se activa y se quita solo cuando cambia el switch |
| «Guarda el historial con Room para que no se pierda al cerrar la app» | Propuso pasar el `Context` al ViewModel para crear la base de datos | El `Context` solo llega a la **Factory**, que crea `AvisosDatabase` y le da al ViewModel un `AvisosRepositoryRoom`. Cambié la interface a `Flow<List<Aviso>>` y `registrar` a `suspend`, y en el ViewModel lo convertí con `stateIn` | El PDF prohíbe `Context` en el ViewModel. Room no permite escribir en el hilo principal, por eso `registrar` es `suspend`. Con Room el id lo genera la base, así las notificaciones no se pisan después de reiniciar |
| «Rediseña la app con estilo editorial: base blanca, negra y arena, acentos morado, verde y rojo, bienvenida, barra inferior y modo oscuro» (prompt completo armado primero para Figma) | Propuso la paleta como colores sueltos de Material, la bienvenida con un `HorizontalPager` y el modo oscuro solo según el sistema | Creé una `Paleta` propia con `CompositionLocal` para claro y oscuro, la bienvenida en una sola pantalla que solo aparece la primera vez, y un switch de modo oscuro en Ajustes guardado con `SharedPreferences`. Mantuve todos los efectos (permiso, receptores, eventos) arriba de las pestañas y corregí los íconos de la barra de estado en modo oscuro | Con colores sueltos las etiquetas pastel no cambiaban bien en oscuro. Si los receptores quedaban dentro de una pestaña, al cambiar de pestaña se quitaban y dejaban de escuchar. En oscuro los íconos de la hora y la batería no se veían |

## Problemas que aparecieron y cómo se resolvieron

- **Tu rama `mejora-ia` local tiene un commit viejo ("cambios") que no se debe subir.** Para no arrastrarlo, la Fase 2 se trabajó en una copia limpia desde `origin/mejora-ia`, con un merge de `main` al inicio.
- **El celular por cable y el emulador estaban conectados a la vez.** Las pruebas se hicieron solo en el emulador (`emulator-5554`).
- **La fuente Inter del prompt no se incluyó.** Se usó la tipografía del sistema con pesos SemiBold para mantener el estilo sin agregar archivos de fuente.

## Verificación

- Compila sin errores con `assembleDebug`.
- `grep` no encuentra `Context`, `NotificationManager` ni `BroadcastReceiver` en `viewmodel/`, y cada `registerReceiver` tiene su `unregisterReceiver` en `onDispose`.
- Cada mejora se probó en el emulador: el canal Avisos tiene importancia 3 y Urgentes 4; el recordatorio llega por Urgentes y al expandirlo muestra el texto largo; **Posponer 10 s** quita la notificación y vuelve a llegar a los 10 s; el cargador avisa Conectado y Desconectado solo con su switch encendido; el historial sigue después de cerrar la app; y el diseño funciona en claro, en oscuro y con el permiso denegado.
