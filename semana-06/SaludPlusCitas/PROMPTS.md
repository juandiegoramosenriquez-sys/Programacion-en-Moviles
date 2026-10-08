# PROMPTS.md — Fase 2 (rama `mejora-ia`)

**Proyecto:** Clínica SaludPlus — App Paciente (`semana-06/SaludPlusCitas`)
**Alumno:** Juan Diego Ramos Enriquez
**Asistente de IA usado:** Claude (Anthropic)

En la Fase 1 (rama `main`) la app se desarrolló sin IA. En esta fase se usó un asistente de IA para la mejora obligatoria (calendario dinámico) y para mejoras adicionales. A continuación se documenta cada prompt, la respuesta resumida y lo que se tuvo que corregir.

---

## Prompt 1 — Calendario dinámico con LocalDate

**Prompt:**
> Genera un calendario dinámico en Jetpack Compose para la pantalla FechaHoraScreen usando java.time.LocalDate. Debe mostrar los próximos 5 días hábiles a partir de hoy (sin sábados, domingos ni días pasados), tener flechas < y > para avanzar o retroceder una semana sin poder retroceder antes de la semana actual, mostrar el mes y año en español ("Octubre 2026") según la semana mostrada, y al cambiar de día reiniciar la hora seleccionada y recalcular los horarios disponibles con Repositorio.horariosDisponibles(medicoId, fecha).

**Respuesta resumida:**
- Creó `ui/components/Fechas.kt` con `diasHabiles(desde, cantidad)` (generateSequence + filter de sábado y domingo + take), `mesYAnio(fecha)` y `diaCorto(fecha)`.
- Reescribió `FechaHoraScreen` con un estado `semana` (0 = semana actual), los días calculados con `hoy.plusWeeks(semana)`, flechas con `IconButton` (la de atrás con `enabled = semana > 0`) y el título del mes dinámico.
- Al tocar un día o cambiar de semana se reinician `fecha` y `hora`; la grilla `LazyVerticalGrid` se recalcula sola con `horariosDisponibles`.

**Qué tuve que corregir:**
1. `LocalDate` (java.time) requiere API 26 y el proyecto tenía `minSdk = 24`, por lo que Android Studio marcaba "Call requires API level 26". Se subió `minSdk` a 26 en `app/build.gradle.kts`.
2. El formateador de Java en español escribe "septiembre", pero en Perú (y en la guía) se usa "setiembre". Se usó una lista propia de meses en español.
3. La fecha se sigue guardando como texto `"yyyy-MM-dd"` (`dia.toString()`) para no romper la ruta `confirmarCita/{medicoId}/{fecha}/{hora}` ni el bloqueo de horarios ya reservados del Repositorio.
4. Se quitó un import inexistente (`androidx.compose.foundation.layout.weight`): `weight` es parte de `RowScope`/`ColumnScope` y no se importa.

---

## Prompt 2 — Fecha en texto en español

**Prompt:**
> Crea una función en Kotlin que convierta una fecha con formato "yyyy-MM-dd" a texto en español como "Martes 16 de setiembre 2026" y úsala en ConfirmarCitaScreen. Aplícala también en las demás pantallas donde se muestra la fecha de una cita, sin cambiar cómo se guarda la fecha en el Repositorio.

**Respuesta resumida:**
- Agregó `fechaEnTexto(fecha: String)` en `ui/components/Fechas.kt`: hace `LocalDate.parse(fecha)` y arma el texto con el nombre del día, el número, el mes y el año.
- La usó en ConfirmarCitaScreen (obligatorio) y también en CitaExitosaScreen, DetalleCitaScreen, MisCitasScreen, la tarjeta "Próxima cita" de HomeScreen y los recordatorios de NotificacionesScreen.
- Internamente la fecha sigue guardándose como "yyyy-MM-dd": solo cambia cómo se muestra.

**Qué tuve que corregir:**
1. Con `DateTimeFormatter.ofPattern("EEEE d 'de' MMMM yyyy", Locale("es"))` Java devuelve "martes 16 de septiembre 2026": el día en minúscula y "septiembre". Por eso no se usó el formateador y se armaron listas propias de días y meses para obtener "Martes" con mayúscula y "setiembre", como pide la guía.
2. Se protegió `LocalDate.parse` con `runCatching` para que, si llega una fecha con otro formato, se muestre tal cual en vez de cerrar la app.
3. En Mis citas la fecha larga no entraba en una sola línea junto a la hora, así que se separaron en dos líneas (📅 fecha y 🕐 hora).

---

## Prompt 3 — Rediseño según la Figura 1 de la guía

**Prompt:**
> Rediseña las pantallas de mi app de citas en Jetpack Compose para que se parezcan a la Figura 1 de la guía (App Paciente - Clínica SaludPlus): tema azul con fondo claro, tarjetas blancas redondeadas, logo de cruz azul con corazón en la Splash, botón "Comenzar" y enlace "Ya tengo una cuenta", campos del registro con íconos, Inicio con 4 tarjetas de colores (Agendar cita, Mis citas, Mis datos, Resultados), especialidades con ícono en círculo de color y flecha, médicos con avatar, estrellas y etiqueta de disponibilidad, y Confirmar cita con filas de Fecha, Hora, Tipo de atención y Dirección. No cambies la lógica, el Repositorio, ni los nombres o parámetros de las pantallas.

**Respuesta resumida:**
- Nuevo tema (`Color.kt`, `Theme.kt`) con azul `#2563EB`, fondo `#F5F7FB` y tarjetas blancas.
- Logo vectorial propio `res/drawable/logo_saludplus.xml` (cruz azul con corazón blanco), usado en Splash y Login.
- Componentes reutilizables nuevos en `Componentes.kt`: `CirculoEmoji`, `Avatar` (iniciales), `TarjetaMedico`, `FilaInfo`, `ChipSeleccion`; `CampoTexto` acepta un ícono y `BarraSuperior` usa título centrado.
- Pantallas rediseñadas: Splash, Registro, Login, Inicio, Especialidades, Médicos, Fecha y hora y Confirmar cita.

**Qué tuve que corregir:**
1. El tema original usaba "dynamic color" (Android 12+), que reemplazaba el azul por los colores del fondo de pantalla del celular; se quitó para que la app siempre use la paleta de la clínica.
2. Las tarjetas (`Card`) de Material 3 salían grises por defecto; se ajustó `surfaceContainerHighest` a blanco en el esquema de colores.
3. El diseño de la guía muestra etiquetas como "Disponible hoy"; en vez de ponerlas fijas (serían datos inventados) se calculan: aparece "Disponible hoy" solo si hoy es día hábil y el médico aún tiene horarios libres hoy (`horariosDisponibles`); si no, "Disponible esta semana".
4. No hay fotos de médicos en el proyecto: se reemplazaron por un avatar con las iniciales (quitando el "Dr."/"Dra.").
5. `material-icons-core` no tiene ícono de reloj ni de hospital, así que las filas de Confirmar cita usan emojis (📅 🕐 🏥 📍) dentro de un círculo azul claro.
6. El campo "Motivo de consulta" del diseño no se agregó porque el modelo `Cita` no lo guarda (se evitó un campo que no hace nada).
7. Los íconos `ArrowBack` y `KeyboardArrowLeft/Right` estaban deprecados; se cambiaron a sus versiones `Icons.AutoMirrored.Filled`.

---

## Prompt 4 — Pulido final del proyecto

**Prompt:**
> Revisa mi proyecto completo y haz un pulido final sin cambiar la lógica principal: elimina el código que ya no se usa, haz que el login no falle por espacios o mayúsculas en el correo, agrega un usuario de prueba precargado para poder entrar sin registrarse cada vez (sin usar base de datos), y aplica el mismo estilo del rediseño a las pantallas de Cita agendada y Detalle de cita.

**Respuesta resumida:**
- Eliminó `PantallaEnConstruccion` de `Componentes.kt`, porque ninguna pantalla la usa ya.
- `LoginScreen` y `RegistroScreen` usan `correo.trim()`, y el Repositorio compara correos con `equals(..., ignoreCase = true)`.
- Agregó el usuario de prueba `demo@saludplus.com` / `123456` dentro de la lista `usuarios` del Repositorio (en memoria, igual que especialidades y médicos).
- Rediseñó `CitaExitosaScreen` y `DetalleCitaScreen` con `TarjetaMedico` y `FilaInfo`, para que sigan el estilo de la Figura 1.

**Qué tuve que corregir:**
1. Se consultó si hacía falta guardar los usuarios registrados en una base de datos para que no se pierdan al cerrar la app; la guía lo prohíbe ("sin Room, SQLite ni Firebase" y "se pierden al cerrar la app, esto es intencional"), así que solo se agregó un usuario de prueba en la lista en memoria.
2. Solo se aplicó `trim()` al correo y no a la contraseña, porque un espacio en la contraseña puede ser intencional.
3. Se mantuvieron sin cambios los nombres y parámetros de las funciones del Repositorio y de las pantallas, como exige la guía.

---

## Resumen de commits en `mejora-ia`

1. Implementa calendario dinámico con LocalDate: 5 días hábiles, flechas por semana y mes dinámico.
2. Muestra la fecha en texto en español en ConfirmarCita, CitaExitosa, MisCitas, DetalleCita, Home y Notificaciones.
3. Rediseña pantallas según la Figura referencial del PDF: logo, tema azul, tarjetas de colores e íconos.
4. Pulido final: usuario demo, correo con trim, estilo en Cita agendada y Detalle, y PROMPTS.md.
