# 🧭 Guía del proyecto · Catálogo de Smartphones

> Mapa completo del proyecto `Catalogo`: **dónde está cada archivo, qué hace cada función y por qué está en ese lugar**.
> Todos los nombres de archivo son **enlaces**: haz clic para abrirlos.
>
> 🌿 Esta es la versión de la rama **`main`** (Fase 1 · sin IA · hitos H1 a H16).
> La rama **`mejora-ia`** tiene su propia versión de esta guía, que incluye la Fase 2 (pruebas, buscador, rediseño y `PROMPTS.md`).

---

## 📑 Índice

1. [Cómo leer esta guía](#1-cómo-leer-esta-guía)
2. [Mapa rápido de carpetas](#2-mapa-rápido-de-carpetas)
3. [Árbol completo del proyecto](#3-árbol-completo-del-proyecto)
4. [Cómo viajan los datos (flujos)](#4-cómo-viajan-los-datos-flujos)
5. [Archivo por archivo](#5-archivo-por-archivo)
6. [Recursos y configuración](#6-recursos-y-configuración)
7. [Firebase](#7-firebase)
8. [Si quiero cambiar X, ¿dónde voy?](#8-si-quiero-cambiar-x-dónde-voy)
9. [Hitos: qué archivo nació en cada commit](#9-hitos-qué-archivo-nació-en-cada-commit)
10. [Diferencias entre ramas](#10-diferencias-entre-ramas)
11. [Preguntas de sustentación y dónde está la respuesta](#11-preguntas-de-sustentación-y-dónde-está-la-respuesta)

---

## 1. Cómo leer esta guía

| Símbolo | Significa |
|---|---|
| 🖼️ | Pantalla (lo que el usuario ve) |
| 🧠 | ViewModel (decide qué mostrar) |
| 📦 | Repositorio (de dónde salen los datos) |
| 🌐 | Internet (Retrofit) |
| 💾 | Base de datos del celular (Room) |
| 🔥 | Firebase (login y roles) |
| 🔔 | Notificación |

**La regla que ordena todo el proyecto (MVVM):**

```
🖼️ Pantalla  ──observa──►  🧠 ViewModel  ──pide──►  📦 Repository (interface)
                                                        │
                                   ┌────────────────────┼────────────────────┐
                                   ▼                    ▼                    ▼
                              🌐 Retrofit          💾 Room              🔥 Firebase
                              (descarga)       (guarda y muestra)    (login y rol)
```

- Las **pantallas** solo dibujan y avisan los clics. Nunca hablan con internet, Room ni Firebase.
- Los **ViewModel** guardan el estado (`UiState`) y deciden qué hacer. No reciben `Context`.
- Los **repositorios** son **interfaces**: el ViewModel no sabe si los datos vienen de internet o del celular.

---

## 2. Mapa rápido de carpetas

Ruta base del código: [`Catalogo/app/src/main/java/com/tecsup/catalogo/`](Catalogo/app/src/main/java/com/tecsup/catalogo)

| Carpeta | Capa | ¿Qué hay? | ¿Por qué está separada? |
|---|---|---|---|
| [`/`](Catalogo/app/src/main/java/com/tecsup/catalogo) (raíz) | Arranque | `MainActivity`, `AppContainer` | Es el punto de entrada y donde se crean todas las piezas |
| [`data/remote/`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/remote) | 🌐 Datos | Retrofit y el DTO | Todo lo de internet en un solo lugar |
| [`data/local/`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/local) | 💾 Datos | Room: tabla, consultas y base | Todo lo del celular en un solo lugar |
| [`data/auth/`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/auth) | 🔥 Datos | Login y rol con Firebase | Separar la autenticación del catálogo |
| [`data/repository/`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/repository) | 📦 Datos | Interface + implementación | Une internet y Room; el ViewModel solo ve la interface |
| [`data/mapper/`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/mapper) | Datos | Conversores entre modelos | Los 3 modelos (Dto, Entity, Item) no se mezclan |
| [`domain/model/`](Catalogo/app/src/main/java/com/tecsup/catalogo/domain/model) | Dominio | `Item`, `Sesion`, `Rol` | Los modelos "limpios" que usa la app |
| [`notification/`](Catalogo/app/src/main/java/com/tecsup/catalogo/notification) | 🔔 | Interface + implementación Android | El ViewModel pide notificar sin usar `Context` |
| [`ui/`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui) | 🖼️🧠 Presentación | Pantallas, ViewModels, navegación, tema | Todo lo visual |
| [`ui/components/`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/components) | 🖼️ | Piezas reutilizables | Se usan en varias pantallas |
| [`ui/theme/`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/theme) | 🎨 | Colores y tipografía | Cambiar el diseño en un solo lugar |

---

## 3. Árbol completo del proyecto

```
semana-12/
├── README.md                     ← presentación del lab y capturas
├── GUIA_PROYECTO.md              ← esta guía
├── capturas/                     ← imágenes del README
└── Catalogo/                     ← proyecto Android
    ├── build.gradle.kts          ← plugins del proyecto
    ├── settings.gradle.kts       ← nombre del proyecto y módulos
    ├── gradle/libs.versions.toml ← versiones de todas las librerías
    └── app/
        ├── build.gradle.kts      ← dependencias de la app
        ├── google-services.json  ← (NO está en Git) conexión a Firebase
        └── src/main/
            ├── AndroidManifest.xml
            └── java/com/tecsup/catalogo/
                ├── MainActivity.kt
                ├── AppContainer.kt
                ├── data/
                │   ├── auth/        AuthRepository · FirebaseAuthRepository
                │   ├── local/       ItemEntity · ItemDao · AppDatabase
                │   ├── mapper/      Mappers
                │   ├── remote/      ApiService · RetrofitClient · dto/ItemDto
                │   └── repository/  ItemRepository · ItemRepositoryImpl
                ├── domain/model/    Item · Sesion
                ├── notification/    Notificador · NotificadorAndroid
                └── ui/
                    ├── UiState.kt
                    ├── navigation/  AppNav
                    ├── login/       LoginScreen · LoginViewModel
                    ├── lista/       ListaScreen · ListaViewModel
                    ├── detalle/     DetalleScreen · DetalleViewModel
                    ├── favoritos/   FavoritosScreen · FavoritosViewModel
                    ├── admin/       AdminScreen · AdminViewModel
                    ├── components/  Cargando · ErrorView · BotonFavorito
                    └── theme/       Color · Theme · Type
```

---

## 4. Cómo viajan los datos (flujos)

### 4.1 Al abrir la app

```
MainActivity.onCreate()
  ├─ pedirPermisoNotificaciones()            → pide POST_NOTIFICATIONS (Android 13+)
  ├─ AppContainer(applicationContext)         → crea API, base de datos, repositorios y notificador
  └─ AppNav(container)                        → empieza en la ruta "login"
        └─ LoginViewModel (init)
              └─ authRepository.sesionActual()
                    ├─ hay sesión  → navega a "lista"          (R11)
                    └─ no hay      → muestra el formulario
```

### 4.2 Iniciar sesión y leer el rol (R01, R02)

```
🖼️ LoginScreen  ─ Ingresar ─►  🧠 LoginViewModel.iniciarSesion(correo, clave)
                                   ├─ campos vacíos → UiState.Error("Completa…")   (no llama a Firebase)
                                   └─ 🔥 FirebaseAuthRepository.iniciarSesion()
                                         ├─ signInWithEmailAndPassword(...).await()
                                         └─ leerSesion(uid) → Firestore usuarios/{uid}.rol
                                               └─ Sesion(uid, correo, Rol.ADMIN | Rol.USUARIO)
AppNav observa la sesión → esAdmin = rol == ADMIN → muestra u oculta el botón Admin
```

### 4.3 Cargar la lista (R03, R06, R08, R09)

```
🧠 ListaViewModel (init)
  ├─ escucha repo.items  (Flow que sale de 💾 Room)  → UiState.Exito(lista)
  └─ refrescar()
        └─ 📦 ItemRepositoryImpl.refrescar()
              ├─ dao.idsFavoritos()                      ← guarda qué era favorito
              ├─ 🌐 api.listar()  GET products/category/smartphones
              ├─ ItemDto.aEntity(esFavorito)             ← vuelve a marcar los favoritos
              └─ 💾 dao.guardarTodos(...)                → Room avisa → la lista se actualiza sola
        ├─ éxito  → 🔔 notificador.mostrar("Datos actualizados", "Se guardaron N elementos")
        └─ fallo
              ├─ Room vacío   → UiState.Error("No hay conexión a internet")  + Reintentar
              └─ Room con datos → aviso "Sin conexión: mostrando datos guardados"
```

> 💡 **Room es la fuente única de verdad**: la pantalla siempre lee de Room. Retrofit solo la actualiza.

### 4.4 Ver el detalle (R04)

```
Tocar una tarjeta → AppNav navega a "detalle/{id}"
  └─ 🧠 DetalleViewModel(id).cargar()
        └─ 📦 repo.obtenerDetalle(id) → 🌐 GET products/{id} → Item
```

### 4.5 Marcar un favorito (R07)

```
Tocar ♥ → ViewModel.alternarFavorito(id) → repo.alternarFavorito(id)
  → 💾 UPDATE items SET esFavorito = NOT esFavorito WHERE id = :id
  → Room avisa por Flow → Lista y Favoritos se actualizan solos
```

### 4.6 Panel Admin (R10)

```
🧠 AdminViewModel
  ├─ estado = combine(repo.items, repo.favoritos) → Totales(guardados, favoritos)  (en vivo)
  ├─ sincronizar()   → repo.refrescar() → mensaje + 🔔 notificación
  └─ limpiarCache()  → repo.limpiarCache() → 💾 DELETE FROM items
```

---

## 5. Archivo por archivo

### 5.1 Arranque

#### [`MainActivity.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/MainActivity.kt)
**Qué es:** la única Activity; es lo primero que corre al abrir la app.
**Por qué aquí:** Android necesita una Activity de entrada (declarada en el Manifest).

| Función | Qué hace |
|---|---|
| `onCreate()` | Pide el permiso de notificaciones y dibuja `CatalogoTheme { AppNav(container) }` |
| `pedirPermisoNotificaciones()` | Si es Android 13+ y no hay permiso, muestra el cuadro "¿Permitir notificaciones?" |
| `container` | Crea **una sola vez** el `AppContainer` |

#### [`AppContainer.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/AppContainer.kt)
**Qué es:** la "fábrica" que crea las piezas de la app.
**Por qué aquí:** el PDF pide que la API, la base y los repositorios se creen en un solo lugar y se pasen a los ViewModel por su `Factory`.

| Propiedad | Qué crea |
|---|---|
| `itemRepository` | `ItemRepositoryImpl(RetrofitClient.api, baseDeDatos.itemDao())` |
| `authRepository` | `FirebaseAuthRepository()` |
| `notificador` | `NotificadorAndroid(contexto)` |

---

### 5.2 🌐 Internet · `data/remote/`

#### [`ApiService.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/remote/ApiService.kt)
**Qué es:** la interface de Retrofit con los **dos endpoints** del PDF.

| Función | Petición | Devuelve |
|---|---|---|
| `listar()` | `GET products/category/smartphones` | `ProductosRespuesta` (los 16 smartphones) |
| `detalle(id)` | `GET products/{id}` | `ItemDto` de un producto |

#### [`RetrofitClient.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/remote/RetrofitClient.kt)
**Qué es:** crea Retrofit **una sola vez** (`object` + `by lazy`).

| Parte | Para qué |
|---|---|
| `BASE_URL = "https://dummyjson.com/"` | Dirección base (termina en `/`, como pide el PDF) |
| `Json { ignoreUnknownKeys = true }` | Ignora los campos del JSON que no usamos (reviews, meta…) |
| `HttpLoggingInterceptor` | Muestra las peticiones en Logcat (buscar `OkHttp`) |
| `api` | La instancia de `ApiService` lista para usar |

#### [`dto/ItemDto.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/remote/dto/ItemDto.kt)
**Qué es:** las clases con **la misma forma que el JSON** de dummyjson.

| Clase | Campos |
|---|---|
| `ProductosRespuesta` | `products: List<ItemDto>` (la lista viene "envuelta") |
| `ItemDto` | `id, title, description, brand, price, rating, stock, thumbnail` |

---

### 5.3 💾 Celular · `data/local/`

#### [`ItemEntity.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/local/ItemEntity.kt)
**Qué es:** la **tabla `items`** de Room. Cada fila es un smartphone guardado, con `esFavorito`.

#### [`ItemDao.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/local/ItemDao.kt)
**Qué es:** las **consultas SQL** a la tabla.

| Función | SQL / acción | Uso |
|---|---|---|
| `todos()` | `SELECT * … ORDER BY titulo` → `Flow` | La lista (se actualiza sola) |
| `favoritos()` | `… WHERE esFavorito = 1` → `Flow` | Pantalla Favoritos |
| `idsFavoritos()` | Ids marcados | Conservar favoritos al sincronizar |
| `contar()` | `COUNT(*)` | Total guardado (Admin) |
| `contarFavoritos()` | `COUNT(*) … esFavorito = 1` | Total favoritos |
| `guardarTodos()` | `@Upsert` | Guardar lo descargado |
| `alternarFavorito(id)` | `esFavorito = NOT esFavorito` | Corazón ♥ |
| `borrarTodo()` | `DELETE FROM items` | Limpiar caché |

#### [`AppDatabase.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/local/AppDatabase.kt)
**Qué es:** la base `catalogo.db`.

| Función | Qué hace |
|---|---|
| `itemDao()` | Entrega el DAO |
| `obtener(context)` | Crea la base **una sola vez** (patrón singleton con `synchronized`) |

---

### 5.4 🔥 Login · `data/auth/`

#### [`AuthRepository.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/auth/AuthRepository.kt)
**Qué es:** el **contrato** (interface) del login, tal cual lo da el PDF.

| Función | Qué promete |
|---|---|
| `iniciarSesion(correo, clave)` | `Result<Sesion>` |
| `sesionActual()` | La sesión guardada o `null` |
| `cerrarSesion()` | Salir |

#### [`FirebaseAuthRepository.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/auth/FirebaseAuthRepository.kt)
**Qué es:** la implementación real con Firebase.

| Función | Qué hace |
|---|---|
| `iniciarSesion()` | `signInWithEmailAndPassword(...).await()` y luego `leerSesion()` |
| `sesionActual()` | Si `auth.currentUser` existe, lee su rol |
| `cerrarSesion()` | `auth.signOut()` |
| `leerSesion(uid, correo)` | Lee **Firestore `usuarios/{uid}.rol`** → `"admin"` = `ADMIN`, si no `USUARIO` |

---

### 5.5 📦 Repositorio · `data/repository/`

#### [`ItemRepository.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/repository/ItemRepository.kt)
**Qué es:** el **contrato** del catálogo (del PDF). Los ViewModel dependen de **esta interface**.

#### [`ItemRepositoryImpl.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/repository/ItemRepositoryImpl.kt)
**Qué es:** une 🌐 Retrofit y 💾 Room.

| Miembro | Qué hace |
|---|---|
| `items` | `dao.todos()` convertido a `Item` |
| `favoritos` | `dao.favoritos()` convertido a `Item` |
| `refrescar()` | Lee ids favoritos → descarga → guarda conservando favoritos. Atrapa `IOException` (sin internet) y `HttpException` (404, 500…) |
| `obtenerDetalle(id)` | `api.detalle(id)` + si era favorito |
| `alternarFavorito(id)` | `dao.alternarFavorito(id)` |
| `cantidadGuardada()` / `cantidadFavoritos()` | Conteos de Room |
| `limpiarCache()` | `dao.borrarTodo()` |

#### [`mapper/Mappers.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/mapper/Mappers.kt)
**Qué es:** convierte entre los **3 modelos** que pide el PDF.

```
ItemDto (API) ──aEntity()──► ItemEntity (Room) ──aModelo()──► Item (pantalla)
ItemDto (API) ──────────────aModelo()─────────────────────────► Item (detalle)
```

---

### 5.6 Dominio · `domain/model/`

#### [`Item.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/domain/model/Item.kt)
**Qué es:** el producto que ven las pantallas.

| Campo | Ejemplo |
|---|---|
| `id, titulo, subtitulo, imagenUrl` | `123`, `"iPhone 13 Pro"`, `"Apple · $1099.99"`, url |
| `esFavorito, descripcion, rating, stock` | `true`, texto, `4.12`, `56` |

#### [`Sesion.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/domain/model/Sesion.kt)
`enum class Rol { ADMIN, USUARIO }` y `data class Sesion(uid, correo, rol)`.

---

### 5.7 🔔 Notificaciones · `notification/`

#### [`Notificador.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/notification/Notificador.kt)
**Qué es:** interface con `mostrar(titulo, texto)`.
**Por qué existe:** el ViewModel **no puede recibir `Context`**; pide la notificación a través de esta interface.

#### [`NotificadorAndroid.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/notification/NotificadorAndroid.kt)

| Función | Qué hace |
|---|---|
| `crearCanal()` | Crea el canal "Sincronización" (obligatorio desde Android 8) |
| `tienePermiso()` | Revisa `POST_NOTIFICATIONS`; si está negado, no se notifica (PA-14) |
| `mostrar()` | `NotificationCompat` con título, texto e ícono; al tocarla abre `MainActivity` |

---

### 5.8 🖼️🧠 Presentación · `ui/`

#### [`UiState.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/UiState.kt)
`sealed interface` con los 3 estados de cualquier pantalla: `Cargando`, `Exito(datos)`, `Error(mensaje)`.

#### [`navigation/AppNav.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/navigation/AppNav.kt)
**Qué es:** el mapa de pantallas (`NavHost`).

| Ruta | Pantalla | Nota |
|---|---|---|
| `login` | `LoginScreen` | Ruta inicial. Al entrar, se borra del historial |
| `lista` | `ListaScreen` | Recibe `esAdmin` para mostrar el botón Admin |
| `favoritos` | `FavoritosScreen` | |
| `detalle/{id}` | `DetalleScreen` | El `id` es `Int` |
| `admin` | `AdminScreen` | Solo se dibuja si `esAdmin` |

`esAdmin` se calcula aquí a partir de la `Sesion` del `LoginViewModel` (que se crea una sola vez para toda la app).

#### Login · [`LoginScreen.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/login/LoginScreen.kt) · [`LoginViewModel.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/login/LoginViewModel.kt)

| Función | Archivo | Qué hace |
|---|---|---|
| `LoginScreen()` | Screen | Título, correo, contraseña, error y botón Ingresar |
| `init` | ViewModel | Revisa `sesionActual()` para entrar directo |
| `iniciarSesion()` | ViewModel | Valida vacíos (no llama a Firebase) y luego inicia sesión |
| `mensajeDe()` | ViewModel | Traduce el error: credenciales, sin internet u otro |
| `cerrarSesion()` | ViewModel | `signOut` y vuelve al formulario |

#### Lista · [`ListaScreen.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/lista/ListaScreen.kt) · [`ListaViewModel.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/lista/ListaViewModel.kt)

| Función | Archivo | Qué hace |
|---|---|---|
| `ListaScreen()` | Screen | Barra con ♥ Favoritos, Admin (solo admin) y Salir; aviso y la lista |
| `AvisoSinConexion()` | Screen | Franja con el aviso y Reintentar |
| `ItemFila()` | Screen | Tarjeta: foto, nombre, "marca · precio" y ♥ (se reutiliza en Favoritos) |
| `estado` / `aviso` | ViewModel | Lo que observa la pantalla |
| `refrescar()` | ViewModel | Descarga, notifica o muestra error/aviso |
| `alternarFavorito()` | ViewModel | Marca o desmarca |
| `mensajeDe()` | ViewModel | `IOException` → "No hay conexión a internet" |
| `factory()` | ViewModel | Crea el ViewModel con el `AppContainer` |

#### Detalle · [`DetalleScreen.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/detalle/DetalleScreen.kt) · [`DetalleViewModel.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/detalle/DetalleViewModel.kt)

| Función | Qué hace |
|---|---|
| `DetalleScreen()` | Barra con ←, y Cargando / Error / contenido |
| `DetalleContenido()` | Foto grande, nombre con ♥, marca · precio, rating, stock y descripción |
| `Dato()` | Tarjeta pequeña con un valor y su título (Rating, Stock) |
| `cargar()` | Pide `obtenerDetalle(id)` |
| `alternarFavorito()` | Guarda en Room y actualiza el ♥ en pantalla |

#### Favoritos · [`FavoritosScreen.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/favoritos/FavoritosScreen.kt) · [`FavoritosViewModel.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/favoritos/FavoritosViewModel.kt)

| Función | Qué hace |
|---|---|
| `FavoritosScreen()` | Lista de favoritos o el texto "Aún no tienes favoritos" |
| `estado` | `repo.favoritos` convertido a `UiState` con `stateIn` |
| `alternarFavorito()` | Quitar de favoritos |

#### Admin · [`AdminScreen.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/admin/AdminScreen.kt) · [`AdminViewModel.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/admin/AdminViewModel.kt)

| Función | Qué hace |
|---|---|
| `AdminScreen()` | Totales, botones Sincronizar y Limpiar caché, y el mensaje |
| `Total()` | Tarjeta con el número y su título |
| `Totales` | `data class(guardados, favoritos)` |
| `estado` | `combine(items, favoritos)` → totales en vivo |
| `sincronizar()` / `limpiarCache()` | Acciones del admin, con mensaje y notificación |

#### Componentes · [`ui/components/`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/components)

| Componente | Qué dibuja | Dónde se usa |
|---|---|---|
| [`Cargando`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/components/Cargando.kt) | Ruedita + "Cargando..." | Todas las pantallas |
| [`ErrorView`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/components/ErrorView.kt) | Mensaje en rojo y Reintentar | Lista, Detalle, Admin |
| [`BotonFavorito`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/components/BotonFavorito.kt) | ♥ o ♡ | Lista, Detalle, Favoritos |

#### Tema · [`ui/theme/`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/theme)

| Archivo | Qué define |
|---|---|
| [`Color.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/theme/Color.kt) | Colores por defecto de la plantilla de Android Studio |
| [`Theme.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/theme/Theme.kt) | `CatalogoTheme` (Material 3, colores dinámicos del sistema) |
| [`Type.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/theme/Type.kt) | Tipografía |

---

## 6. Recursos y configuración

| Archivo | Qué hay | Cuándo tocarlo |
|---|---|---|
| [`AndroidManifest.xml`](Catalogo/app/src/main/AndroidManifest.xml) | Permisos `INTERNET` y `POST_NOTIFICATIONS`, la `MainActivity` | Nuevos permisos o Activities |
| [`strings.xml`](Catalogo/app/src/main/res/values/strings.xml) | El nombre de la app | Cambiar el nombre visible |
| [`app/build.gradle.kts`](Catalogo/app/build.gradle.kts) | Dependencias: Retrofit, Room, Firebase, Coil, Navigation | Agregar una librería |
| [`gradle/libs.versions.toml`](Catalogo/gradle/libs.versions.toml) | **Versiones** de todas las librerías | Cambiar una versión |
| [`build.gradle.kts`](Catalogo/build.gradle.kts) | Plugins: Android, Compose, serialización, KSP, google-services | Rara vez |

---

## 7. Firebase

| Qué | Dónde |
|---|---|
| Archivo de conexión | `Catalogo/app/google-services.json` (no se sube a Git) |
| Usuarios | Consola → **Authentication → Usuarios** |
| Roles | Consola → **Firestore → `usuarios/{uid}`** con el campo `rol` = `admin` o `usuario` |
| Reglas | Cada usuario solo puede **leer** su propio documento; nadie puede escribir |
| Código que lo usa | [`FirebaseAuthRepository.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/auth/FirebaseAuthRepository.kt) |

---

## 8. Si quiero cambiar X, ¿dónde voy?

| Quiero… | Archivo |
|---|---|
| Cambiar un texto de una pantalla | La pantalla correspondiente en `ui/` (por ejemplo, [`LoginScreen.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/login/LoginScreen.kt)) |
| Cambiar los colores | [`Color.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/theme/Color.kt) y [`Theme.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/theme/Theme.kt) |
| Usar otra categoría de la API (por ejemplo, laptops) | [`ApiService.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/remote/ApiService.kt) → ruta de `listar()` |
| Mostrar otro dato del JSON | [`ItemDto.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/remote/dto/ItemDto.kt) → [`Mappers.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/mapper/Mappers.kt) → [`Item.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/domain/model/Item.kt) → la pantalla |
| Cambiar cómo se ve una tarjeta | `ItemFila()` en [`ListaScreen.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/lista/ListaScreen.kt) |
| Cambiar el texto de la notificación | `refrescar()` en [`ListaViewModel.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/lista/ListaViewModel.kt) y `sincronizar()` en [`AdminViewModel.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/admin/AdminViewModel.kt) |
| Agregar una pantalla nueva | Crear `ui/nueva/` (Screen + ViewModel) y su ruta en [`AppNav.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/navigation/AppNav.kt) |
| Cambiar quién es admin | Consola de Firebase → Firestore → `usuarios/{uid}.rol` (no el código) |
| Agregar una consulta a la base | [`ItemDao.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/local/ItemDao.kt) |

---

## 9. Hitos: qué archivo nació en cada commit

| Hito | Archivos principales |
|---|---|
| H1 | Proyecto base: `MainActivity`, `ui/theme/` |
| H2 | `Sesion`, `Item`, `AuthRepository`, `ItemRepository`, `Notificador` |
| H3 | `UiState`, `Cargando`, `ErrorView` |
| H4 | `ListaScreen`, `ListaViewModel` (con datos de prueba) |
| H5 | `ApiService`, `RetrofitClient`, `ItemDto` |
| H6 | `ItemRepositoryImpl`, `Mappers`, `AppContainer` |
| H7 | `DetalleScreen`, `DetalleViewModel`, `AppNav` |
| H8 | `ItemEntity`, `ItemDao`, `AppDatabase` |
| H9 | `ItemRepositoryImpl` pasa a usar Room |
| H10 | `BotonFavorito`, `FavoritosScreen`, `FavoritosViewModel` |
| H11 | Aviso sin conexión en `ListaViewModel` y `ListaScreen` |
| H12 | Firebase en Gradle |
| H13 | `FirebaseAuthRepository`, `LoginScreen`, `LoginViewModel` |
| H14 | `AdminScreen`, `AdminViewModel`, rol en `AppNav` |
| H15 | `NotificadorAndroid`, permiso en `MainActivity` |
| H16 | `README.md` y `capturas/` |

---

## 10. Diferencias entre ramas

| | `main` (Fase 1 · sin IA) | `mejora-ia` (Fase 2 · con IA) |
|---|---|---|
| Hitos H1 a H16 | ✅ | ✅ (heredados de `main`) |
| Pruebas unitarias (R12) | ❌ | ✅ `ListaViewModelTest` |
| Buscador | ❌ | ✅ |
| Diseño | Tema por defecto de Android | Tonos claros, íconos y estados vacíos |
| Textos | En cada pantalla | Centralizados en `strings.xml` |
| `PROMPTS.md` | ❌ | ✅ |

---

## 11. Preguntas de sustentación y dónde está la respuesta

| Pregunta | Respuesta corta | Míralo en |
|---|---|---|
| ¿Por qué el repository es una interface? | El ViewModel no depende de la clase concreta; se puede cambiar por uno falso en pruebas | [`ItemRepository.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/repository/ItemRepository.kt) |
| ¿Por qué la lista sale de Room y no de Retrofit? | Room es la fuente única de verdad: funciona sin internet y conserva favoritos | `items` y `refrescar()` en [`ItemRepositoryImpl.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/repository/ItemRepositoryImpl.kt) |
| ¿Qué pasa con los favoritos al sincronizar? | Se leen los ids favoritos antes y se vuelven a marcar al guardar | `refrescar()` en [`ItemRepositoryImpl.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/repository/ItemRepositoryImpl.kt) |
| ¿Dónde se decide el rol y por qué no en la pantalla? | En Firestore; lo lee `leerSesion()`. Las reglas impiden cambiarlo desde la app | [`FirebaseAuthRepository.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/data/auth/FirebaseAuthRepository.kt) · [`AppNav.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/navigation/AppNav.kt) |
| ¿Qué pasa si el permiso de notificaciones está negado? | `tienePermiso()` devuelve falso y no se notifica; la app sigue normal | [`NotificadorAndroid.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/notification/NotificadorAndroid.kt) |
| ¿Por qué el ViewModel no recibe Context? | Regla MVVM: usa la interface `Notificador` | [`Notificador.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/notification/Notificador.kt) |
| ¿Cómo se manejan los errores de red? | `IOException` = sin internet, `HttpException` = error del servidor | `refrescar()` y `mensajeDe()` |
| ¿Qué pasa al girar la pantalla? | El ViewModel sobrevive y no vuelve a descargar | `viewModel(factory = …)` en [`AppNav.kt`](Catalogo/app/src/main/java/com/tecsup/catalogo/ui/navigation/AppNav.kt) |
