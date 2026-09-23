# Semana 05 – Navegación en Jetpack Compose

**Curso:** Programación en Móviles  
**Alumno:** Juan Diego Ramos Enriquez  
**Docente:** Juan León S.  
**Guía:** `GLAB-S05-JLEONS-2026-2.pdf`

---

## Parte 1 – Navegación con la guía (sin IA)

App con 4 pantallas conectadas con **Navigation Compose**: `NavHost`, rutas con `sealed class` y paso de argumentos tipados (`NavType.IntType`).

### Flujo de navegación

```
HomeScreen ("home")
 ├── navigate("list")    → ListScreen ("list")
 │                          └── navigate("detail/{itemId}") → DetailScreen (recibe itemId: Int)
 └── navigate("profile") → ProfileScreen ("profile")
                            └── navigate("home") { popUpTo("home") { inclusive = true } }
```

### Estructura del proyecto

```
com.tecsup.semanaapp
├── navigation/
│   ├── Screen.kt          → rutas con sealed class
│   └── AppNavigation.kt   → NavHost con las 4 pantallas
├── screens/
│   ├── HomeScreen.kt
│   ├── ListScreen.kt      → Scaffold + TopAppBar + LazyColumn
│   ├── DetailScreen.kt    → Scaffold + TopAppBar + Card
│   └── ProfileScreen.kt   → popUpTo para limpiar el back stack
├── ui/theme/
└── MainActivity.kt        → SemanaAppTheme { AppNavigation() }
```

### Dependencias agregadas

```kotlin
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("androidx.compose.material:material-icons-core:1.7.8")
```

> **Nota:** la guía solo menciona `navigation-compose`, pero `material-icons-core` es necesaria para el ícono `Icons.AutoMirrored.Filled.ArrowBack` de la barra superior. En las versiones actuales de Compose, `material3` ya no la incluye.

### Capturas – Parte 1

<!-- Guarda las capturas en la carpeta semana-05/img/ con estos nombres -->

| Inicio | Lista | Detalle | Perfil |
|:---:|:---:|:---:|:---:|
| ![Inicio](img/parte1-home.png) | ![Lista](img/parte1-lista.png) | ![Detalle](img/parte1-detalle.png) | ![Perfil](img/parte1-perfil.png) |

---

## Parte 2 – Implementación con prompt (con IA)

Usando **Gemini** en Android Studio, se mejoró la presentación de la primera parte y se convirtió en un **Portal Académico** de 5 pantallas:

- **Login** con validación (correo con formato válido y contraseña de mínimo 6 caracteres)
- **Bienvenida** con degradado y accesos rápidos
- **Directorio de Alumnos** (lista con paso de `id` al detalle)
- **Expediente Académico** del alumno elegido
- **Configuración de Perfil** con **cambio de idioma** Español / English

> **Para ingresar:** cualquier correo con formato válido y una contraseña de 6 caracteres o más.  
> Ejemplo: `diego.ramos@tecsup.edu.pe` / `123456`

### Prompt utilizado

```text
# ROL
Actúa como un desarrollador Android senior experto en Kotlin, Jetpack Compose y Material 3. Escribe código limpio, que compile a la primera y listo para copiar y pegar.

# CONTEXTO DEL PROYECTO
- Paquete: com.tecsup.semanaapp
- Carpetas actuales: navigation/ (Screen.kt, AppNavigation.kt), screens/ (HomeScreen.kt, ListScreen.kt, DetailScreen.kt, ProfileScreen.kt) y ui/theme/ (Color.kt, Theme.kt, Type.kt)
- Rutas con sealed class Screen(val route: String) y NavHost en AppNavigation.kt
- MainActivity usa enableEdgeToEdge() y llama a AppNavigation() dentro de SemanaAppTheme { }
- Dependencias instaladas: navigation-compose 2.7.7 y material-icons-core 1.7.8
- Usa rutas String. NO uses navegación type-safe con @Serializable.
- NO tengo material-icons-extended. Usa SOLO estos íconos:
  • Icons.Default: Email, Lock, Person, Phone, Home, Info, Star, Face, DateRange, AccountBox, AccountCircle
  • Icons.AutoMirrored.Filled: ArrowBack, ExitToApp, KeyboardArrowRight, List
- No agregues librerías nuevas (nada de Coil ni imágenes de internet). Las fotos se reemplazan por un círculo con las iniciales del nombre.

# OBJETIVO
Rediseñar la app como un "Portal Académico" de 5 pantallas, con el siguiente estilo visual y contenido.

# ESTILO VISUAL
- Morado primario #6750A4, lavanda claro #EADDFF, fondo #F7F2FA, texto oscuro #1D1B20, gris #625B71, rojo #B3261E.
- Tarjetas con esquinas redondeadas de 16.dp y sombra suave (elevation 2-4.dp).
- Avatares: círculo con las iniciales (ej. "MG" para Maria Garcia) sobre fondo lavanda #EADDFF y texto morado, con borde blanco cuando va sobre un banner.
- Títulos en negrita, subtítulos y carreras en morado, etiquetas pequeñas en gris.
- En ui/theme/Theme.kt pon dynamicColor = false y primary = #6750A4, para que los colores no cambien según el fondo de pantalla del celular.

# PANTALLAS

P1 – LoginScreen ("Portal Académico"):
- Fondo con degradado vertical lavanda (#E8DEF8 arriba a #F7F2FA abajo) que cubra toda la pantalla.
- Card centrada con título "Portal Académico" (morado, negrita, grande) y subtítulo "Accede a tu cuenta".
- OutlinedTextField "Correo Institucional" con ícono Email.
- OutlinedTextField "Contraseña" con ícono Lock, PasswordVisualTransformation y botón de texto "Ver"/"Ocultar" al final.
- Botón ancho morado "INICIAR SESIÓN" y debajo el texto "¿Olvidaste tu contraseña?".
- VALIDACIÓN OBLIGATORIA (no dejar entrar sin datos):
  • Correo vacío → error "Ingresa tu correo".
  • Correo con formato inválido (usa android.util.Patterns.EMAIL_ADDRESS) → error "Correo no válido".
  • Contraseña vacía → error "Ingresa tu contraseña".
  • Contraseña con menos de 6 caracteres → error "Mínimo 6 caracteres".
  • Muestra cada error debajo de su campo con isError = true y supportingText en rojo. Los errores se muestran al tocar el botón y se limpian cuando el usuario corrige el campo.
  • Solo si todo es válido, navega a Home con popUpTo(Screen.Login.route) { inclusive = true }.

P2 – HomeScreen (Bienvenida):
- Fondo con degradado vertical de #6750A4 (arriba) a #F7F2FA (abajo) que cubra TODA la pantalla, incluida la barra de estado.
- Texto centrado, blanco, negrita y grande en dos líneas: "Bienvenido," / "Diego Ramos".
- Debajo: "¿Qué deseas gestionar hoy?" en blanco semitransparente.
- Dos tarjetas blancas anchas, cada una con un ícono dentro de un cuadrado lavanda redondeado, título en negrita y subtítulo gris:
  • "Directorio de Alumnos" – "Ver y gestionar estudiantes" (ícono AutoMirrored List) → navega a StudentList.
  • "Mi Perfil Académico" – "Datos personales y progreso" (ícono Person) → navega a Profile.
- Abajo del todo, centrado: botón de texto rojo "Cerrar Sesión Segura" con ícono AutoMirrored ExitToApp → vuelve al Login con popUpTo(0) { inclusive = true }.

P3 – StudentListScreen ("Directorio de Alumnos"):
- TopAppBar con fondo lavanda #EADDFF, título "Directorio de Alumnos" en morado oscuro y flecha atrás.
- LazyColumn con una Card por alumno: avatar con iniciales, nombre en negrita, carrera en morado debajo, y flecha AutoMirrored KeyboardArrowRight a la derecha.
- Alumnos de ejemplo (en este orden):
  1. Diego Ramos – Ingeniería de Sistemas
  2. Maria Garcia – Arquitectura
  3. Carlos Perez – Medicina
  4. Ana Lopez – Derecho
  5. Luis Ramirez – Administración
- Al tocar un alumno navega a su expediente pasando su id como Int (NavType.IntType) con Screen.StudentDetail.createRoute(id).

P4 – StudentDetailScreen ("Expediente Académico"):
- TopAppBar con título "Expediente Académico" y flecha atrás.
- Banner morado (degradado de #6750A4 a #4F378B) con esquinas inferiores redondeadas de 32.dp.
- Avatar grande (≈110.dp) con borde blanco, centrado y SOBRESALIENDO del banner (la mitad dentro del banner y la otra mitad fuera).
- Debajo: nombre en negrita grande y carrera en morado.
- Card lavanda claro con filas (ícono morado + etiqueta pequeña gris + valor en negrita):
  • ID Estudiante (ícono AccountBox) – ej. 2024-0001
  • Correo Electrónico (ícono Email) – ej. diego.ramos@example.com
  • Facultad (ícono Home) – ej. Ingeniería y Tecnología
  • Un divisor y luego "Biografía" en negrita con un texto corto, ej. "Estudiante destacado con interés en desarrollo Android."
- Cada alumno tiene sus propios datos. Si el id no existe, mostrar "Alumno no encontrado".

P5 – ProfileScreen ("Configuración de Perfil"):
- TopAppBar con título "Configuración de Perfil" y flecha atrás.
- Banner con degradado horizontal de #6750A4 a #7D5260, con avatar circular con borde blanco y debajo "Juan Diego Ramos Enriquez" en blanco negrita.
- Sección "INFORMACIÓN PERSONAL" (título pequeño, morado, mayúsculas) con filas (ícono gris dentro de un cuadrado lavanda + etiqueta pequeña + valor):
  • Nombre Completo – Juan Diego Ramos Enriquez (ícono Person)
  • Correo – diego.ramos@tecsup.edu.pe (ícono Email)
  • Teléfono – +51 987 654 321 (ícono Phone)
- Sección "ACADÉMICO":
  • Carrera – Diseño y Desarrollo de Software (ícono Star)
  • Ciclo Actual – IV Ciclo (ícono DateRange)
- Sección "PREFERENCIAS":
  • Idioma: dos RadioButton "Español" / "English". Al elegir uno, TODA la app cambia de idioma al instante (ver CAMBIO DE IDIOMA).
- Al final, botón ancho "Cerrar Sesión" con fondo rojo claro #F9DEDC, texto e ícono AutoMirrored ExitToApp en rojo #B3261E → vuelve al Login con popUpTo(0) { inclusive = true }.

# CAMBIO DE IDIOMA (debe funcionar de verdad)
- Crea ui/Strings.kt con un data class AppStrings que contenga TODOS los textos visibles de la app (títulos, botones, etiquetas, subtítulos y mensajes de error), y dos instancias: SpanishStrings y EnglishStrings.
- En AppNavigation.kt guarda el idioma elegido con rememberSaveable y provéelo a toda la app con un CompositionLocal (LocalAppStrings).
- Todas las pantallas usan LocalAppStrings.current en lugar de textos fijos.
- No traducir nombres propios ni datos de ejemplo (nombres, correos, teléfonos, carreras e IDs).
- No uses AppCompatDelegate ni agregues librerías.

# REGLAS TÉCNICAS
1. Usa rememberSaveable { mutableStateOf() } para campos de texto, errores, idioma y visibilidad de contraseña.
2. Usa Scaffold en cada pantalla. El color o degradado de fondo debe aplicarse con containerColor del Scaffold o ANTES de .padding(innerPadding), para que no queden franjas blancas arriba ni abajo.
3. Agrega @OptIn(ExperimentalMaterial3Api::class) donde uses TopAppBar.
4. Comentarios breves solo en las partes clave (navegación, paso del argumento, validación, idioma).
5. No cambies MainActivity salvo que sea necesario; si lo cambias, mantén SemanaAppTheme { AppNavigation() }.

# FORMATO DE RESPUESTA
Entrega archivos completos, uno por bloque de código con el nombre del archivo arriba:
- navigation/Screen.kt (Login, Home, StudentList, StudentDetail con createRoute(id: Int), Profile)
- navigation/AppNavigation.kt (startDestination = Login, con el CompositionLocal del idioma)
- data/Student.kt (data class Student y la lista de 5 alumnos de ejemplo)
- ui/Strings.kt (AppStrings, SpanishStrings, EnglishStrings y LocalAppStrings)
- components/StudentComponents.kt (StudentAvatar con iniciales e InfoRow reutilizables)
- screens/LoginScreen.kt, HomeScreen.kt, StudentListScreen.kt, StudentDetailScreen.kt, ProfileScreen.kt
- ui/theme/Theme.kt (dynamicColor = false, primary #6750A4)
Al final, una lista corta de archivos a CREAR, REEMPLAZAR y ELIMINAR (ListScreen.kt y DetailScreen.kt deben eliminarse, no dejarse vacíos).
```

### Capturas – Parte 2

**Login**

| Login vacío | Login con datos | Correo sin @ |
|:---:|:---:|:---:|
| <img src="img/img_7.png" width="230"> | <img src="img/img.png" width="230"> | <img src="img/img_1.png" width="230"> |

**Navegación principal**

| Bienvenida | Directorio de Alumnos | Expediente Académico |
|:---:|:---:|:---:|
| <img src="img/img_2.png" width="230"> | <img src="img/img_3.png" width="230"> | <img src="img/img_4.png" width="230"> |

**Configuración de Perfil**

| Información personal y académica | Preferencias: idioma y cerrar sesión |
|:---:|:---:|
| <img src="img/img_5.png" width="230"> | <img src="img/img_6.png" width="230"> |

---

## Ramas en GitHub

| Rama | Contenido |
|---|---|
| `main` | Parte 1 – navegación según la guía |
| `semana05/Rama-manual-pdf` | Parte 1 – desarrollo sin IA (unida a `main`) |
| `semana05/Rama-implementacion-prompt` | Parte 2 – Portal Académico con Gemini |
