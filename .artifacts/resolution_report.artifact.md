# MILTVPLUS — Resolución Definitiva de Bloqueos y Estado Operativo

## 1. Bloqueo 1: APK Release Firmado
- **Estado de Comprobación**: No existe un archivo keystore (`.jks`) ni `keystore.properties` en el repositorio (por diseño de seguridad, para evitar exponer claves privadas).
- **Configuración de Gradle**: Verificada en [app/build.gradle.kts](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/app/build.gradle.kts). Se encuentra configurada para leer `keystore.properties` y rechazar cualquier intento de compilación release sin firma de producción.
- **Acción Requerida del Usuario**:
  1. Generar la clave de producción localmente:
     ```cmd
     keytool -genkeypair -v -keystore miltvplus-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias miltvplus-key
     ```
  2. Crear `keystore.properties` en la raíz del proyecto.
  3. Ejecutar: `.\gradlew.bat :app:assembleRelease`

## 2. Bloqueo 2: Publicación del Sitio Web
- **Estado de Contenido**: [website/](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/website) contiene todas las páginas oficiales (`index.html`, `download.html`, `privacy.html`, `support.html`, `terms.html`, `tmdb.html`) y estilos con enlaces y metadatos verificados.
- **Configuración de Despliegue**: [netlify.toml](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/netlify.toml) y [.github/workflows/deploy-web.yml](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/.github/workflows/deploy-web.yml) listos.
- **Estado de Autenticación**: GitHub CLI no está autenticado localmente (`gh auth status` indica que no hay sesión activa).
- **Acción Requerida del Usuario**:
  1. Ejecutar `gh auth login` en la terminal para autenticarse.
  2. Ejecutar `gh repo create miltvplus --public --source=. --push` para desplegar automáticamente el sitio en la nube.

## 3. Bloqueo 3: TMDB Real
- **Estado de Configuración**: [TmdbApiService.kt](file:///C:/Users/El...Matuu250/AndroidStudioProjects/MILTVPLUS/app/src/main/java/com/milt/miltvplus/data/remote/tmdb/TmdbApiService.kt) está preparado para leer `System.getenv("TMDB_API_KEY")` o `System.getProperty("tmdb.api.key")`.
- **Estado de Pruebas**: Las pruebas unitarias de integración y fallback (`TmdbRepositoryTest`) se ejecutan y aprueban al 100%.
- **Acción Requerida del Usuario**:
  1. Definir la variable de entorno de forma segura en Windows antes de ejecutar las peticiones reales:
     ```cmd
     set TMDB_API_KEY=tu_api_key_real
     ```
  2. El sistema cambiará automáticamente del modo de respaldo local a peticiones en vivo a la API de TMDB.
