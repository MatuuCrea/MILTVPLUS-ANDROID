# Documentación de GitHub Actions y Secretos — MILTVPLUS

## 1. Workflows Disponibles
- **Android CI (`android-ci.yml`)**: Ejecuta pruebas unitarias (`:app:testDebugUnitTest`) y compila el APK de depuración en cada push o pull request hacia `main`.
- **Android Release (`android-release.yml`)**: Compila y firma la versión de producción (APK y AAB) utilizando los secretos de firma cuando se ejecuta manualmente (`workflow_dispatch`).

## 2. Secretos Requeridos en GitHub (Repository Secrets)
| Nombre del Secret | Finalidad | Obligatorio |
| :--- | :--- | :--- |
| `ANDROID_KEYSTORE_BASE64` | Almacén de claves (`miltvplus-release.jks`) codificado en Base64. | Opcional (para firma en la nube) |
| `ANDROID_KEYSTORE_PASSWORD` | Contraseña del almacén de claves (keystore). | Opcional |
| `ANDROID_KEY_ALIAS` | Alias de la clave de firma (`miltvplus-key`). | Opcional |
| `ANDROID_KEY_PASSWORD` | Contraseña de la clave individual. | Opcional |
| `TMDB_API_KEY` | Clave opcional para consultas en vivo a la API de TMDB. | Opcional |

## 3. Instrucciones de Configuración
1. En tu repositorio privado de GitHub (`MatuuCrea/MILTVPLUS-ANDROID`), ve a **Settings > Secrets and variables > Actions**.
2. Hacé clic en **New repository secret** y agregá los nombres exactos indicados en la tabla anterior según corresponda.
3. Para ejecutar compilaciones y despliegues automáticos, ve a la pestaña **Actions** y seleccioná el workflow deseado.
