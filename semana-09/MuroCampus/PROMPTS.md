# PROMPTS · Fase 2 con IA (rama mejora-ia)

El PDF sugiere usar Gemini en Android Studio. Para esta fase usé **Claude (Claude Code)** como asistente de IA, junto con la skill de diseño *ui-ux-pro-max*. Cada mejora la revisé, la probé en el emulador y corregí lo que no respetaba las reglas del laboratorio: MVVM + Repository, nada de Retrofit, `PostApi`, `HttpException` ni `Context` en el ViewModel, la pantalla nunca llama a la API y nada de `GlobalScope` ni `catch` vacíos.

## Mejoras realizadas

| # | Mejora | Capas que cambian | Commit |
|---|---|---|---|
| 1 | Borrar publicación con `@DELETE` y `Response<Unit>` | API + Repository + ViewModel + View | `feat(borrar)` |
| 2 | Editar el título con `@PATCH` | Model + API + Repository + ViewModel + View | `feat(editar)` |
| 3 | Búsqueda por título | ViewModel + View | `feat(busqueda)` |
| 4 | Encabezado `Accept: application/json` con un interceptor | RetrofitClient | `feat(interceptor)` |
| 5 | Caché en Room para ver publicaciones sin internet | Repository + Room + ViewModel + View | `feat(cache)` |
| 6 | Paginación de 20 en 20 con `_start` y `_limit` | API + Repository + ViewModel + View | `feat(paginacion)` |
| 7 | Tema Vino y oro con modo claro y oscuro guardado | Tema + DataStore + View | `feat(diseno)` |
| 8 | Nombres reales de autores y textos sin datos técnicos | API + Room + Repository + ViewModel + View | `feat(experiencia)` |
| 9 | Guardar mis publicaciones para verlas y buscarlas | Model + Room + Repository + ViewModel + View | `feat(publicar)` |
| 10 | Cabecera con olas y menú hamburguesa | ViewModel + View | `feat(diseno)` |

## Tabla de prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un botón Eliminar en el detalle con @DELETE que revise el código de respuesta» | Propuso `@DELETE("posts/{id}")` devolviendo `Response<Unit>` y revisar `isSuccessful` en el ViewModel | Moví la revisión de `isSuccessful` al **Repository**: si no es 2xx lanza `HttpException`, que el mismo `llamar { }` convierte en `Result.failure`. Agregué un diálogo de confirmación | El PDF prohíbe `HttpException` y Retrofit en el ViewModel. Borrar es una acción destructiva y debe confirmarse |
| «Permite modificar el título con PATCH y mostrar la respuesta» | Propuso enviar el `Post` completo | Creé `EdicionPost(title)` con **solo el campo que cambia**, y la respuesta del servidor actualiza el detalle y la lista | Esa es la diferencia entre PATCH (parcial) y PUT (completo). Enviar todo con PATCH no tiene sentido |
| «Agrega un campo de búsqueda que filtre por título lo ya descargado» | Propuso filtrar la lista dentro del Composable | Lo hice en el ViewModel con `combine` entre la lista y el texto buscado, expuesto con `stateIn` | La pantalla solo dibuja el estado, no calcula. Así la búsqueda tampoco vuelve a llamar a la API |
| «Agrega un interceptor que ponga Accept: application/json en todas las peticiones» | Propuso un `Interceptor` de OkHttp en `RetrofitClient` | Lo puse **antes** del `HttpLoggingInterceptor` | Si va después, el encabezado no aparece en Logcat y no se puede comprobar |
| «Guarda las publicaciones en Room y muéstralas sin internet; el Repository decide» | Propuso Room con `@Entity`, `@Dao` y que el ViewModel lea de la base cuando falle la red | La decisión red/caché quedó **dentro del Repository** (`conCache`), que devuelve `Listado(posts, desdeCache)`. El `Context` llega solo a la Factory | El ViewModel no debe conocer Room ni recibir `Context`. Con `desdeCache` la pantalla muestra el aviso «Sin conexión» |
| «Carga de 20 en 20 con _start y _limit y un botón Cargar más» | Propuso usar `actual.size` como siguiente `_start` | Después de agregar mis publicaciones locales, cambié el cálculo a contar **solo las publicaciones del servidor** | Si se cuentan también las propias, `_start` se adelanta y se salta publicaciones |
| «Usa skills y Material 3; quiero un diseño bonito, profesional, con modo claro y oscuro y un botón para cambiarlo» | Con *ui-ux-pro-max* mostró varias maquetas (Azul campus, Coral, Noche teal, Revista, Teal y ámbar, Suizo, Material 3 azul) | Descarté las azules porque se veían como el azul por defecto de Android. Elegí **Vino y oro**, todo con el `colorScheme` de Material 3, y guardé la elección del modo con DataStore a través de un `TemaViewModel` | Un color propio da identidad. Los colores salen del tema, así el modo oscuro funciona solo y no hay colores fijos sueltos |
| «Como cliente no quiero ver ids ni rutas; haz la app más profesional» | Propuso ocultar textos técnicos | Además traje los **nombres reales** con `GET users` (el ejemplo `Usuario` del PDF), guardados en Room, y reescribí los errores para decir qué pasó y qué hacer («No encontramos esta publicación…») | Mensajes como «DELETE posts/1» o «Error del servidor (404)» no le sirven a un usuario. La skill recomienda errores con causa y forma de recuperarse |
| «Publico y dice que se hizo, pero luego no encuentro mi publicación» | Explicó que JSONPlaceholder responde 201 con id 101 pero no guarda nada | Guardé la publicación en Room marcada como **propia**: sale arriba con la etiqueta «Tuya», la búsqueda la encuentra, sigue al reabrir la app, y su detalle, edición y borrado se resuelven sin pedirla al servidor | Sin esto, la app decía «publicado» pero la publicación no existía en ningún lado. Esta es la respuesta práctica a la pregunta 6 de «Para pensar» |
| «Que resalte el rojizo, con olas arriba en claro y oscuro, y una hamburguesa» | Propuso una cabecera con olas dibujadas con `Canvas` y un `ModalNavigationDrawer` | Quité el botón sol/luna de las barras porque ya está en el menú, y en «Nueva publicación» cambié la X por la hamburguesa | Repetir el mismo botón en dos lugares confunde. «Nueva publicación» es una sección del menú, no una ventana que se cierra |

## Problemas que aparecieron y cómo se resolvieron

- **Gradle no compilaba desde la terminal** («Unable to establish loopback connection») por el espacio en la carpeta del usuario de Windows. Se resolvió usando `C:\gradletmp` como carpeta temporal.
- **Las capturas salían tapadas** por la barra del teclado del emulador. Se desactivó el teclado un momento al tomarlas.
- **La primera prueba del 404** mostró «Error desconocido» porque el cambio temporal quitaba `{id}` de la ruta de Retrofit; se rehízo pidiendo el id 9999 desde el Repository.
- **Subir sin mezclar trabajo de otras semanas:** la rama local tenía un commit de semanas anteriores sin subir. Cada mejora se subió por separado desde una copia limpia de `mejora-ia`.

## Verificación

- Compila sin errores con `assembleDebug`.
- `grep` no encuentra Retrofit, `PostApi`, `HttpException` ni `Context` en los ViewModel, ni `GlobalScope`, `runBlocking`, `catch` vacíos o `http://` en el código.
- Probado en el emulador: `DELETE posts/1` → 200, `PATCH posts/1` → 200, búsqueda «dolor», `Accept: application/json` visible en Logcat, lista sin internet desde Room, paginación de 20 a 40, publicar y luego encontrar la publicación con la búsqueda, y modo oscuro que se mantiene al reabrir la app.
