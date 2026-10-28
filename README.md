# Directorio CENAMAD

Aplicación móvil Android que funciona como directorio de investigadores del
Centro Nacional de Excelencia para la Industria de la Madera (CENAMAD).
Permite buscar investigadores, filtrarlos por línea de investigación y ver
en qué proyectos participa cada uno.

Proyecto de la asignatura DSY1105 – Desarrollo de Aplicaciones Móviles, Duoc UC.
Los datos de investigadores y proyectos son ficticios.

## Integrantes
- Diego Silva
- Cristian Retamales
- Joaquín Rodriguez
## Funcionalidades
- Inicio de sesión con validación de campos y contraseña guardada como hash (SHA-256)
- Registro de usuarios con validación de cada campo y modal de confirmación
- Listado de investigadores con buscador por nombre y filtro por línea de investigación
- Modal con el detalle de cada investigador y sus proyectos
- Perfil de usuario con foto tomada desde la cámara
- Barra de navegación inferior

## Tecnologías
- Kotlin + Jetpack Compose
- Arquitectura MVVM
- Room (persistencia local)
- Navigation Compose
- KSP

## Estructura del proyecto
| Carpeta | Contenido |
|---|---|
| `model` | Tablas de Room, DAO, base de datos y estados de las pantallas |
| `repository` | Acceso a los datos y validaciones de formularios |
| `viewmodel` | Lógica y estado de cada pantalla |
| `ui` | Pantallas en Compose y navegación |
| `ui/theme` | Colores y tipografía |

## Cómo ejecutar
1. Clonar el repositorio y abrirlo en Android Studio.
2. Esperar a que termine la sincronización de Gradle.
3. Ejecutar en un emulador o dispositivo con Android 7.0 o superior.

**Usuario de prueba:** `demo@cenamad.cl` / `Demo1234`

## Flujo de trabajo en Git
Cada integrante trabaja en su rama (`dev/nombre`) y los cambios se integran
a `develop` mediante Pull Requests. La versión final se lleva a `main`.