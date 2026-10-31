# MILTVPLUS v1.11.95 — Inventario de Módulos y Arquitectura

## 1. Módulos de Datos (`data/`)
- `remote/tmdb/TmdbApiService.kt`: Servicio REST HTTPS para TMDB con manejo de códigos de error (401, 403, 404, 429, 5xx) y enmascaramiento de API Key.
- `remote/tmdb/TmdbRepository.kt`: Repositorio con respaldo local automático en caso de desconexión.
- `repository/LiveTvRepository.kt`: Repositorio de canales de TV en vivo e integración de listas M3U/M3U8.
- `repository/M3uPlaylistParser.kt`: Parser para listas IPTV con metadatos EXTINF (`tvg-id`, `tvg-name`, `tvg-logo`, `group-title`).
- `repository/MoviesRepository.kt`: Catálogo de películas.
- `repository/SeriesRepository.kt`: Catálogo de series, temporadas y episodios.
- `repository/SportsRepository.kt`: Catálogo de eventos deportivos.
- `repository/RadiosRepository.kt`: Directorio de emisoras de radio por país/género.
- `repository/MusicRepository.kt`: Playlists musicales.
- `repository/TheatreRepository.kt`: Obras de teatro y espectáculos.
- `repository/AdultRepository.kt`: Catálogo de adultos protegido por PIN +18.
- `repository/UserSettingsRepository.kt`: DataStore / SharedPreferences para configuración de la app, tema, tamaño de fuente y PIN parental.

## 2. Interfaz y Componentes (`ui/`)
- `ui/screens/HomeScreen.kt`: Pantalla principal con contenido destacado y carruseles de tendencias.
- `ui/screens/LiveTvScreen.kt`: Interfaz de TV en vivo con selector de canales, categorías e IPTV.
- `ui/screens/MoviesSeriesScreen.kt`: Catálogo de películas y series con fichas detalladas.
- `ui/screens/AdultScreen.kt`: Sección restringida con PIN de seguridad.
- `ui/screens/SettingsScreen.kt`: Panel de ajustes con 14 categorías (Cuenta, General, Imagen, Audio, Subtítulos, Idioma, Región, Apariencia, Reproducción, Conectividad, Accesibilidad, Privacidad, Seguridad, Notificaciones).
- `ui/player/PlayerScreen.kt` & `PlayerManager.kt`: Motor de reproducción multimedia Media3/ExoPlayer con soporte HLS/M3U8/MP4 y controles D-pad.

## 3. Dispositivos Compatibles
- Teléfonos Android (Touch)
- Tablets Android (Multi-pane / Layout adaptable)
- Android TV / Google TV / TV Box (D-pad focus halo `dpadFocusable`)
