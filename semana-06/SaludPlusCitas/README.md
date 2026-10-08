# Clínica SaludPlus – App Paciente (Tarea Semana 06)

**Alumno:** Juan Diego Ramos Enriquez · **Docente:** Juan León S. · **Guía:** `GLAB-S06-JLEONS-2026-02_CD.pdf`

App de agendamiento de citas médicas en **Kotlin + Jetpack Compose (Material 3)**. El paciente se registra o inicia sesión, elige una especialidad, un médico, el día y la hora, y confirma la cita. Después puede ver sus citas, cancelarlas, revisar resultados, notificaciones y su perfil.
Sin base de datos ni ViewModel: los datos viven en colecciones dentro del `object Repositorio` y se pierden al cerrar la app (intencional según la guía).

> **Para ingresar (rama `mejora-ia`):** `demo@saludplus.com` / `123456`, o crea una cuenta desde **Comenzar**.

---

## 1. Commits

**Fase 1 – Sin IA (rama `main`)**
| # | Commit |
|---|---|
| 1 | Crea estructura base del proyecto SaludPlusCitas |
| 2 | Agrega modelos, rutas, navegación y esqueleto de pantallas |
| 3 | Implementa registro, inicio y cierre de sesión en el repositorio |
| 4 | Agrega pantallas de Splash, Registro y Login con validaciones |
| 5 | Implementa búsqueda de especialidades y filtro de médicos con `filter`, `take` y `sortedByDescending` |
| 6 | Diseña HomeScreen con saludo al usuario, tarjeta de agendar cita y `LazyRow` de especialidades destacadas |
| 7 | Agrega `NavigationBar` en Home con 4 destinos: Inicio, Citas, Resultados y Perfil |
| 8 | Crea EspecialidadesScreen con búsqueda en tiempo real y MedicosScreen que recibe `especialidadId` |
| 9 | Implementa FechaHoraScreen con `LazyVerticalGrid` de horarios disponibles y bloqueo de horas reservadas |
| 10 | Agrega ConfirmarCitaScreen que guarda la cita con `agendarCita` y CitaExitosaScreen con `popUpTo` a Home |
| 11 | Crea MisCitasScreen con `LazyColumn` y lista vacía, y PerfilScreen con cerrar sesión |
| 12 | Agrega retos extra: DetalleCita con `AlertDialog`, Resultados con modelo propio y Notificaciones con `map` |

**Fase 2 – Con IA (rama `mejora-ia`, recibe la Fase 1 con un merge de `main`)**
| # | Commit |
|---|---|
| 13 | Implementa calendario dinámico con `LocalDate`: 5 días hábiles, flechas por semana y mes dinámico |
| 14 | Muestra la fecha en texto en español en ConfirmarCita y demás pantallas de citas |
| 15 | Rediseña pantallas según la Figura 1 de la guía: logo, tema azul, tarjetas de colores e íconos |
| 16 | Pulido final: usuario demo, correo con `trim`, estilo unificado y `PROMPTS.md` |
| 17 | Agrega README con capturas de la Fase 1 y la Fase 2 |

---

## 2. Requerimientos

| Requerimiento | Implementación |
|---|---|
| Repositorio con colecciones | `object Repositorio` con `mutableStateListOf`; usa `any`, `add`, `find`, `filter`, `contains`, `take`, `sortedByDescending`, `map`, `sortedWith` y `removeIf` |
| Registro, login y sesión | Registro con validaciones (campos vacíos, correo, DNI de 8 dígitos, contraseña y términos), login con `find`, saludo con el nombre y cerrar sesión en Perfil |
| `NavigationBar` | `bottomBar` en Inicio con 4 destinos: Inicio, Citas, Resultados y Perfil |
| `LazyRow` / `LazyColumn` | `LazyRow` de especialidades destacadas; `LazyColumn` de especialidades (búsqueda en tiempo real), médicos y mis citas (con mensaje de lista vacía) |
| Horarios reactivos | `LazyVerticalGrid` de horarios; un horario reservado deja de aparecer para ese médico y fecha; Continuar solo se habilita con día y hora |
| Navegación con parámetros | `especialidadId`, `medicoId`, `fecha`, `hora` y `citaId`; `popUpTo(Home)` al confirmar la cita |
| Retos extra | Detalle de cita con `AlertDialog`, Resultados con modelo propio, Notificaciones con `map` y Términos con scroll |
| **Mejora con IA (obligatoria)** | Calendario dinámico con `java.time.LocalDate`: próximos 5 días hábiles, flechas `<` `>` por semana, mes y año dinámico y horarios recalculados al cambiar de día |
| **Fecha en español** | Confirmar cita muestra la fecha como texto: "Viernes 9 de octubre 2026" |
| Mejora extra con IA | Diseño basado en la Figura 1 de la guía: logo propio, tema azul, tarjetas de colores, avatares y chips |

**Flujo:** Splash → Registro / Login → Inicio → Especialidades → Médicos → Fecha y hora → Confirmar → Cita agendada (`popUpTo`) → Mis citas → Detalle

---

## 3. Estructura

```
com.saludplus.citas
├── MainActivity.kt
├── data
│   ├── model          Usuario, Especialidad, Medico, Cita, Resultado
│   └── repository     Repositorio
├── navigation         Rutas, AppNavigation
└── ui
    ├── theme          Color, Theme, Type
    ├── components     Componentes, Fechas
    └── screens
        ├── auth            Splash, Registro, Login, Terminos
        ├── home            Home
        ├── agendamiento    Especialidades, Medicos, FechaHora, ConfirmarCita, CitaExitosa
        ├── citas           MisCitas, DetalleCita
        ├── perfil          Perfil
        ├── resultados      Resultados
        └── notificaciones  Notificaciones
```

---

## 4. Capturas – Fase 1 (sin IA)

| Splash | Iniciar sesión | Registro | Error: campos vacíos |
|:---:|:---:|:---:|:---:|
| <img src="img/fase1/01-splash.png" width="180"> | <img src="img/fase1/02-login.png" width="180"> | <img src="img/fase1/03-registro-vacio.png" width="180"> | <img src="img/fase1/04-registro-error-campos-vacios.png" width="180"> |

| Error: términos | Error: DNI | Error: correo | Inicio + NavigationBar |
|:---:|:---:|:---:|:---:|
| <img src="img/fase1/05-registro-error-terminos.png" width="180"> | <img src="img/fase1/06-registro-error-dni.png" width="180"> | <img src="img/fase1/07-registro-error-correo.png" width="180"> | <img src="img/fase1/08-inicio-navigationbar.png" width="180"> |

| Especialidades | Médicos | Fecha y hora | Confirmar cita |
|:---:|:---:|:---:|:---:|
| <img src="img/fase1/09-especialidades.png" width="180"> | <img src="img/fase1/10-medicos-medicina-general.png" width="180"> | <img src="img/fase1/11-fecha-hora-seleccionada.png" width="180"> | <img src="img/fase1/12-confirmar-cita.png" width="180"> |

| Cita agendada | Mis citas | Detalle de cita | AlertDialog |
|:---:|:---:|:---:|:---:|
| <img src="img/fase1/13-cita-agendada.png" width="180"> | <img src="img/fase1/14-mis-citas-con-cita.png" width="180"> | <img src="img/fase1/15-detalle-cita.png" width="180"> | <img src="img/fase1/16-detalle-cita-alertdialog.png" width="180"> |

| Lista vacía | Perfil | Resultados | Nueva cita |
|:---:|:---:|:---:|:---:|
| <img src="img/fase1/17-mis-citas-vacia-tras-cancelar.png" width="180"> | <img src="img/fase1/18-perfil.png" width="180"> | <img src="img/fase1/19-resultados.png" width="180"> | <img src="img/fase1/20-mis-citas-cardiologia.png" width="180"> |

---

## 5. Capturas – Fase 2 (con IA)

| Splash | Iniciar sesión | Error de credenciales | Campos vacíos |
|:---:|:---:|:---:|:---:|
| <img src="img/fase2/01-splash.png" width="180"> | <img src="img/fase2/02-login.png" width="180"> | <img src="img/fase2/03-login-error-credenciales.png" width="180"> | <img src="img/fase2/04-login-error-campos-vacios.png" width="180"> |

| Términos | Crear cuenta | Error: términos | Error: correo |
|:---:|:---:|:---:|:---:|
| <img src="img/fase2/05-terminos.png" width="180"> | <img src="img/fase2/06-registro-error-campos-vacios.png" width="180"> | <img src="img/fase2/07-registro-error-terminos.png" width="180"> | <img src="img/fase2/08-registro-error-correo.png" width="180"> |

| Error: DNI | Inicio rediseñado | Especialidades | Médicos |
|:---:|:---:|:---:|:---:|
| <img src="img/fase2/09-registro-error-dni.png" width="180"> | <img src="img/fase2/10-inicio-tarjetas-colores.png" width="180"> | <img src="img/fase2/11-especialidades.png" width="180"> | <img src="img/fase2/12-medicos-avatar-disponible.png" width="180"> |

| **Calendario dinámico** | **Fecha en español** | Cita agendada | Mis citas |
|:---:|:---:|:---:|:---:|
| <img src="img/fase2/13-fecha-hora-calendario-dinamico.png" width="180"> | <img src="img/fase2/14-confirmar-cita-fecha-espanol.png" width="180"> | <img src="img/fase2/15-cita-agendada.png" width="180"> | <img src="img/fase2/16-mis-citas-una.png" width="180"> |

| Citas ordenadas | Cancelar cita | Resultados | Perfil |
|:---:|:---:|:---:|:---:|
| <img src="img/fase2/17-mis-citas-varias-ordenadas.png" width="180"> | <img src="img/fase2/18-detalle-cita-alertdialog.png" width="180"> | <img src="img/fase2/19-resultados.png" width="180"> | <img src="img/fase2/20-perfil.png" width="180"> |

---

## 6. Prompts usados (Fase 2)

Los 4 prompts usados con la IA (Claude), con su respuesta resumida y lo que se tuvo que corregir, están en [`PROMPTS.md`](PROMPTS.md):

1. Calendario dinámico con `LocalDate`.
2. Fecha en texto en español.
3. Rediseño según la Figura 1 de la guía.
4. Pulido final del proyecto.
