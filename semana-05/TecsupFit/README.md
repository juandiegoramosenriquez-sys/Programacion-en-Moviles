# TECSUP Fit – Tarea integradora (Semanas 1 a 6)

**Alumno:** Juan Diego Ramos Enriquez · **Docente:** Juan León S. · **Opción B:** reserva de clases de gimnasio con barra inferior (bottomBar)
Sin ViewModel: todo el estado se maneja con `remember` / `mutableStateOf`.

---

## 1. Commits

**Fase 1 – Sin IA (rama `main`)**
| # | Commit |
|---|---|
| 1 | Crea proyecto TecsupFit |
| 2 | Agrega modelo Clase, reservas y lista de clases de ejemplo |
| 3 | Agrega navegación con bottomBar y pantallas de la app |

**Fase 2 – Con IA (rama `mejora-ia`, recibe los proyectos de `main`)**
| # | Commit |
|---|---|
| 4 | Agrega cancelación de reservas con AlertDialog de confirmación |
| 5 | Agrega control de cupos y evita reservar el mismo horario dos veces |
| 6 | Agrega PROMPTS.md y README con los prompts usados |

---

## 2. Requerimientos (Opción B)

| Requerimiento | Implementación |
|---|---|
| Inicio | `LazyRow` con chips "Hoy" / "Esta semana" + `LazyColumn` de clases (nombre y horario) |
| Detalle de clase | Recibe el `claseId` por navegación (`NavType.IntType`), horario de selección única y botón "Reservar cupo" |
| Confirmación | Resumen de la reserva (clase y horario) y botón "Ver reservas" |
| bottomBar | `NavigationBar` con 4 pestañas (Inicio, Reservas, Rutinas, Perfil); el ícono activo se resalta con `currentBackStackEntryAsState()` |
| Reservas | `LazyColumn` con estados Confirmada / Completada diferenciados por color |
| Perfil | Datos del usuario y estadísticas (clases tomadas, racha) |
| **Mejora con IA 1** | Cancelar una reserva con `AlertDialog` de confirmación + `Snackbar` |
| **Mejora con IA 2** | Cupos que bajan al reservar y suben al cancelar; no permite reservar el mismo horario dos veces |

**Flujo:** Inicio → Detalle de clase → Confirmación → Reservas

---

## 3. Prompts usados (Fase 2)

| Prompt | Qué se pidió |
|---|---|
| 1 | Cancelar una reserva desde "Mis reservas" con un diálogo de confirmación |
| 2 | Que los cupos bajen al reservar y suban al cancelar, sin permitir reservas repetidas |

El detalle de cada prompt (qué generó la IA y qué se corrigió) está en [PROMPTS.md](PROMPTS.md).
