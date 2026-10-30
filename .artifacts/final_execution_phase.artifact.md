# MILTVPLUS — Informe de Evaluación Técnica y Estado Final

## 1. Prioridad 1: Firma Release de Producción
- **Configuración Gradle**: Verificada en [app/build.gradle.kts](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/app/build.gradle.kts). `signingConfigs` y `buildTypes` leen de forma segura `keystore.properties`. Si este archivo no existe al intentar un build `release`, se lanza una excepción explícita impidiendo el uso de la clave debug.
- **Exclusiones en Git**: Verificado en [.gitignore](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/.gitignore) (`keystore.properties`, `*.jks`, `*.keystore`).
- **Estado**: **BLOQUEADO**. No se generaron contraseñas ni claves arbitrarias.
- **Acción requerida del usuario**: Ejecutar el comando local de creación de keystore:
  ```cmd
  keytool -genkeypair -v -keystore miltvplus-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias miltvplus-key
  ```
  Y guardar `keystore.properties` en la raíz del proyecto.

## 2. Prioridad 2: Integración Real de TMDB
- **Arquitectura de Credenciales**: [TmdbApiService.kt](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/app/src/main/java/com/milt/miltvplus/data/remote/tmdb/TmdbApiService.kt) busca la clave `TMDB_API_KEY` en las variables de entorno (`System.getenv("TMDB_API_KEY")`).
- **Resiliencia y Fallback**: Comprobado en [TmdbRepositoryTest.kt](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/app/src/test/java/com/milt/miltvplus/data/remote/tmdb/TmdbRepositoryTest.kt). La aplicación conmuta al catálogo local sin bloquearse ante errores de red o falta de API Key.
- **Estado de Pruebas**: **APROBADO** (en tests unitarios y fallback). **PENDIENTE** (en peticiones reales en vivo por falta de la variable de entorno `TMDB_API_KEY`).

## 3. Prioridad 3: Verificación de Compatibilidad
- **Parámetros Android**:
  - `applicationId`: `com.milt.miltvplus`
  - `minSdk`: `24` (Android 7.0 Nougat)
  - `targetSdk`: `37` (Android 15)
  - `versionCode`: `1` / `versionName`: `"1.0"`
- **Coincidencia con la Web**: Verificado en `website/download.html` y `website/index.html`.
- **Estado**: **APROBADO**.

## 4. Prioridad 4: Pruebas Finales
- `testDebugUnitTest` ➔ **APROBADO** (Todas las pruebas pasaron exitosamente).
- `assembleDebug` ➔ **APROBADO** (APK debug generado y verificado).
- `assembleRelease` ➔ **BLOQUEADO** (Capturada la falla de seguridad esperada por la falta de `keystore.properties`).

## 5. Prioridad 5: Publicación y Entrega
- **Sitio Web y Descarga Directa**: Publicado en [https://matuucrea.github.io/miltvplus-website/](https://matuucrea.github.io/miltvplus-website/).
- **APK Verificado**: Copiado a `website/downloads/miltvplus-v1.0.0.apk`.
- **SHA-256 Calculado**: `D96BB8F0430ED37527FAD10A1462702420FD8B08D444AA2AC095C9CCEEC7E928`
- **Estado**: **APROBADO** (Sitio web y descarga debug QA). **BLOQUEADO** (Compilación release de producción).
