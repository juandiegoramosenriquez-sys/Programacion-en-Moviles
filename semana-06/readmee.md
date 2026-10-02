# TECSUP Store — Laboratorio 06: Menú y Navegación

**Curso:** Programación en Móviles — 4to ciclo
**Alumno:** Juan Diego Ramos Enriquez
**Tecnología:** Kotlin + Jetpack Compose (Material 3)

## Descripción
Aplicación de tienda que implementa dos tipos de menú:
- **DropdownMenu contextual** en cada tarjeta de producto (⋮).
- **NavigationDrawer** como navegación principal de la app (☰).

## Funcionalidades

### DropdownMenu (por producto)
- Opciones: Favoritos, Compartir y Reportar, cada una con su ícono.
- Divisor antes de "Reportar".
- La opción Favoritos cambia a "Quitar de favoritos" si el producto ya está marcado.

### NavigationDrawer (toda la app)
- Encabezado con iniciales, nombre y correo del usuario.
- 4 destinos: Inicio, Mis pedidos, Favoritos y Perfil.
- El destino activo se resalta con otro color de fondo.
- **Badge con contador** en Favoritos (mejora con IA).

## Estructura
```
com.DiegoRamos.semana06
├── MainActivity.kt
├── navigaton
│   ├── Screen.kt          → rutas de la app
│   └── AppNavigation.kt   → ModalNavigationDrawer + NavHost + estado de favoritos
└── screens
    └── InicioScreen.kt    → ProductCard con DropdownMenu
```

## Ramas
| Rama | Contenido |
|---|---|
| `main` | Fase 1: DropdownMenu y NavigationDrawer (desarrollo propio) |
| `mejora-ia` | Fase 2: badge de favoritos que conecta el DropdownMenu con el Drawer |

## Cómo funciona la mejora
La lista de favoritos vive en `AppNavigation`, el nivel común entre las tarjetas y el drawer.
- La tarjeta **modifica** la lista mediante `onFavoritoClick`.
- El drawer **lee** el tamaño de la lista y lo muestra en un `Badge`.

Como la lista es un `mutableStateListOf`, cualquier cambio se refleja automáticamente en la pantalla.

## Cómo ejecutar
1. Clonar el repositorio y abrir la carpeta `semana-06` en Android Studio.
2. Esperar la sincronización de Gradle.
3. Ejecutar en un emulador o dispositivo con ▶ Run.

---

## Prompts usados en la Fase 2 (mejora con IA)

### Prompt 1 — Conectar favoritos
**Prompt:** "Tengo un ProductCard con DropdownMenu y un ModalNavigationDrawer en AppNavigation. ¿Cómo hago que la opción Favoritos guarde el producto en una lista que el drawer pueda leer?"
**Respuesta resumida:** Mover la lista a AppNavigation con `mutableStateListOf` y pasar a ProductCard `esFavorito` y `onFavoritoClick`. Además, cambiar ícono y texto según si ya es favorito.
**Qué corregí:** [completa: ej. actualizar la llamada a ProductCard porque cambió su firma y no compilaba]

### Prompt 2 — Badge en el drawer
**Prompt:** "¿Cómo agrego un badge con el número de favoritos en el NavigationDrawerItem?"
**Respuesta resumida:** Usar el parámetro `badge` con `Badge { Text(...) }`, visible solo si la lista no está vacía.
**Qué corregí:** [completa]

### Prompt 3 — Pantalla Favoritos
**Prompt:** "Haz que la pantalla Favoritos muestre los productos marcados y un mensaje si está vacía."
**Respuesta resumida:** Un `if` con mensaje para lista vacía y, si no, una LazyColumn reutilizando ProductCard.
**Qué corregí:** [completa]

### Prompt 4 — Integrar ramas
**Prompt:** "Mi rama mejora-ia no tiene el avance de main, ¿cómo lo paso?"
**Respuesta resumida:** Cambiarse a `mejora-ia` y ejecutar `git merge main`.
**Qué corregí:** [completa: ej. el cambio del Commit 1 quedó dentro del commit del merge]

---

## Capturas
<!-- Agrega aquí: DropdownMenu abierto y Drawer con el badge -->