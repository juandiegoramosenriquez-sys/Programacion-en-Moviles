# PROMPTS.md – Fase 2 (Mejora con IA)

**Proyecto:** Clínica Salud+ (Opción A) · **Alumno:** Juan Diego Ramos Enriquez
**Rama:** `mejora-ia` (creada a partir de `main`, commit `9b8928b`)
**Asistente de IA:** Claude (Claude Code, dentro del escritorio de Claude)

**Mejora principal:** cancelar una cita desde "Mis citas" con un `AlertDialog` de confirmación y un `Snackbar`.
Además, con ayuda de la IA se completaron partes que faltaban en la Fase 1: selección de fecha y hora, chips de especialidad, calificación, Drawer y estados de las citas.

---

## Prompt 1 – Selección única de fecha y hora + resumen en la confirmación
**Commit:** `2bd2ec2`

**Qué le pedí:**
Que implementara el siguiente paso sobre mi proyecto de la rama `main`: en "Agendar cita" elegir UNA fecha y UNA hora, y que la confirmación muestre el médico, la fecha y la hora elegidos, sin usar ViewModel.

**Qué generó:**
- `model/Doctor.kt`: agregó `calificacion`, `experiencia` y `descripcion` al `Doctor`; creó `Cita` y `EstadoCita`; listas de 3 fechas y 3 horas.
- `navigation/Screen.kt`: ruta `confirmacion/{doctorId}/{fecha}/{hora}` con `createRoute` de 3 parámetros.
- `navigation/AppNavigation.kt`: lista de citas con `remember { mutableStateListOf() }` y 3 `navArgument` (`IntType` + `StringType`).
- `screens/AgendarScreen.kt`: dos `LazyRow` con `FilterChip` de selección única.
- `screens/ConfirmacionScreen.kt`: `Scaffold`, check verde y resumen de la cita.

**Qué tuve que corregir / revisar:**
- Mi ruta de confirmación solo recibía `doctorId`; se tuvo que cambiar para recibir también fecha y hora.
- La fecha ("Vie 27") tiene espacio y la hora ("10:30") tiene dos puntos: se usó `Uri.encode` para que la ruta no se rompa.
- Mi `ConfirmacionScreen` no tenía `Scaffold` (lo pide la rúbrica en todas las pantallas).
- Había un archivo vacío duplicado `data/Doctor.kt` que se eliminó.
- (Completa aquí si al compilar o probar tuviste que corregir algo más.)

---

## Prompt 2 – Chips de especialidad, calificación y menú lateral
**Commit:** `19bc624`

**Qué le pedí:**
Que el Inicio y el Perfil quedaran como la Figura 1 del enunciado (barra morada, chips de especialidad, tarjetas con calificación) y que agregara el menú lateral de la Figura 2 con Inicio, Mis citas e Historial médico.

**Qué generó:**
- `screens/HomeScreen.kt`: `ModalNavigationDrawer` envolviendo el `Scaffold`, ícono ☰ que abre el menú, `LazyRow` de `FilterChip` que filtra la lista, tarjetas con ⭐ calificación.
- `screens/DoctorScreen.kt`: perfil con años de experiencia, calificación y descripción.
- `screens/AgendarScreen.kt`: chips y botón con el mismo color morado.

**Qué tuve que corregir / revisar:**
- Mi Inicio no tenía `LazyRow` ni calificación, y la navegación a Mis citas / Historial era con botones sueltos en vez de un Drawer.
- En el encabezado del menú se usó mi nombre en lugar de "Juan Pérez" del ejemplo del enunciado.
- (Completa aquí si tuviste que corregir algo más al probar.)

---

## Prompt 3 – Mis citas con estados y cancelación (mejora principal)
**Commit:** `18381e4`

**Qué le pedí:**
Que "Mis citas" mostrara las citas agendadas con su estado (Confirmada / Completada) diferenciado por colores, y que se pudiera cancelar una cita con un `AlertDialog` de confirmación.

**Qué generó:**
- `screens/MisCitasScreen.kt`: `LazyColumn` de citas, etiqueta de estado con color (verde, gris, rojo), borde morado en las confirmadas, botón "Cancelar" solo en las confirmadas, `AlertDialog` con "Sí, cancelar" / "No" y `Snackbar` "Cita cancelada".
- `navigation/AppNavigation.kt`: función `onCancelar` que reemplaza la cita por `cita.copy(estado = CANCELADA)`.

**Qué tuve que corregir / revisar:**
- Mi pantalla "Mis citas" solo mostraba un texto fijo; no recibía las citas.
- Se agregó un tercer estado `CANCELADA` que no estaba en el enunciado, para que la cancelación se vea en la lista.
- (Completa aquí si tuviste que corregir algo más al probar.)

---

## Conclusión
La IA ayudó a completar rápido las partes más nuevas (Drawer, chips con selección única, varios argumentos en la navegación y el `AlertDialog`), pero hubo que revisar cada cambio: adaptar las rutas que yo ya tenía, cuidar los caracteres especiales en la navegación, respetar la regla de no usar ViewModel y comprobar en el emulador que cada flujo funcionara.
