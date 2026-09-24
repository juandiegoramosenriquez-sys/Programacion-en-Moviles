# PROMPTS.md – Fase 2 (Mejora con IA)

**Proyecto:** TECSUP Fit (Opción B) · **Alumno:** Juan Diego Ramos Enriquez
**Rama:** `mejora-ia` (recibe los proyectos de `main` con `git merge main`)
**Asistente de IA:** Claude (Claude Code, dentro del escritorio de Claude)

**Mejoras implementadas:**
1. Cancelar una reserva con un `AlertDialog` de confirmación.
2. Control de cupos y bloqueo de reservas repetidas en el mismo horario.

---

## Prompt 1 – Cancelar reserva con AlertDialog
**Commit:** `9bc6509`

**Qué le pedí:**
Implementar la mejora que sugiere el enunciado: poder cancelar una reserva desde "Mis reservas" con un diálogo de confirmación, sin usar ViewModel.

**Qué generó:**
- `screens/ReservasScreen.kt`: botón "Cancelar reserva" solo en las reservas confirmadas, `AlertDialog` con "Sí, cancelar" / "No", `Snackbar` "Reserva cancelada" y un tercer estado "Cancelada" en rojo en `EstadoEtiqueta`.
- `navigation/AppNavigation.kt`: función `onCancelar` que reemplaza la reserva por `reserva.copy(estado = "Cancelada")` dentro de la lista con `remember { mutableStateListOf() }`.

**Qué tuve que corregir / revisar:**
- Mi `ReservasScreen` solo recibía la lista; se le agregó el parámetro `onCancelar` y se actualizó la llamada en `AppNavigation`.
- Mi etiqueta de estado solo distinguía Confirmada y Completada; se agregó el color para "Cancelada".
- (Completa aquí si al compilar o probar tuviste que corregir algo más.)

---

## Prompt 2 – Control de cupos y reservas repetidas
**Commit:** `946cac5`

**Qué le pedí:**
Que los cupos de cada clase bajen al reservar y vuelvan a subir al cancelar, y que no se pueda reservar dos veces la misma clase en el mismo horario.

**Qué generó:**
- `screens/DetalleClaseScreen.kt`: calcula los cupos disponibles con las reservas confirmadas de la clase, muestra "Cupos disponibles: X de Y", marca con ✓ los horarios ya reservados y desactiva el botón con los textos "Ya reservaste este horario" o "Sin cupos disponibles".
- `navigation/AppNavigation.kt`: la pantalla de detalle recibe la lista de reservas.

**Qué tuve que corregir / revisar:**
- En mi versión los cupos eran un número fijo del modelo (`cupos`) que nunca cambiaba.
- Se decidió no modificar el modelo `Clase`, sino calcular los cupos a partir de las reservas, para que al cancelar se liberen solos.
- (Completa aquí si al compilar o probar tuviste que corregir algo más.)

---

## Conclusión
La IA ayudó a agregar lógica que conecta varias pantallas (reservas, detalle y cupos) sin ViewModel, pero hubo que revisar cada cambio: adaptar los parámetros de mis pantallas, mantener los estados como texto igual que en mi modelo y probar en el emulador que los cupos suban y bajen correctamente.
