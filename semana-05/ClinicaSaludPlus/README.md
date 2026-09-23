# Clínica Salud+ – Tarea integradora (Semanas 1 a 6)

**Alumno:** Juan Diego Ramos Enriquez · **Docente:** Juan León S. · **Opción A:** reserva de citas médicas con menú lateral (Drawer)
Sin ViewModel: todo el estado se maneja con `remember` / `mutableStateOf`.

---

## 1. Commits

**Fase 1 – Sin IA (rama `main`)**
| # | Commit |
|---|---|
| 1 | Crea proyecto ClinicaSaludPlus |
| 2 | Crea estructura de paquetes y archivos de pantallas |
| 3 | Agrega modelo Doctor y lista de médicos de ejemplo |
| 4 | Agrega navegación y pantallas de la clínica |

**Fase 2 – Con IA (rama `mejora-ia`, creada desde `main`)**
| # | Commit |
|---|---|
| 5 | Agrega selección única de fecha y hora y resumen en la confirmación |
| 6 | Agrega chips de especialidad, calificación y menú lateral en el inicio |
| 7 | Agrega mis citas con estados y cancelación con AlertDialog de confirmación |
| 8 | Agrega PROMPTS.md con los prompts usados y correcciones |

---

## 2. Requerimientos (Opción A)

| Requerimiento | Implementación |
|---|---|
| Inicio | `LazyRow` con chips de especialidad + `LazyColumn` de médicos (nombre, especialidad, ⭐ calificación) |
| Perfil del médico | Recibe el `doctorId` por navegación (`NavType.IntType`) y botón "Agendar cita" |
| Agendar cita | 3 fechas y 3 horas con `FilterChip` de selección única |
| Confirmación | Resumen con médico, fecha y hora (3 argumentos en la ruta) |
| Menú lateral | `ModalNavigationDrawer` con Inicio, Mis citas e Historial médico |
| Mis citas | `LazyColumn` con estados Confirmada / Completada diferenciados por color |
| **Mejora con IA** | Cancelar una cita con `AlertDialog` de confirmación + `Snackbar` |

**Flujo:** Inicio → Perfil del médico → Agendar cita → Confirmación → Mis citas

---

## 3. Prompts usados (Fase 2)

| Prompt | Qué se pidió |
|---|---|
| 1 | Elegir una sola fecha y hora al agendar y mostrar el resumen en la confirmación |
| 2 | Dejar el Inicio y el Perfil como la Figura 1 (chips, calificación) y agregar el menú lateral de la Figura 2 |
| 3 | Mostrar las citas con su estado y permitir cancelarlas con un `AlertDialog` de confirmación |

El detalle de cada prompt (qué generó la IA y qué se corrigió) está en [PROMPTS.md](PROMPTS.md).
