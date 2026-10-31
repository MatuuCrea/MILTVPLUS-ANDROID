# MILTVPLUS v1.11.95 — Registro de Pendientes y Trazabilidad

| ID | Área | Problema / Tarea | Prioridad | Estado | Evidencia / Prueba |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **BUG-001** | Arranque (P0) | Excepción defensiva en arranque | Crítica (P0) | **CORREGIDO Y VERIFICADO** | `MainActivity.kt` y `MainViewModel.kt` con `try-catch` y `CancellationException` rethrown |
| **UI-001** | Branding | Logotipo oficial MILTVPLUS y favicon | Alta (P1) | **IMPLEMENTADO Y PROBADO** | Iconos adaptativos vectoriales SVG e interfaz oscura con acentos M3 |
| **MED-001** | TV en Vivo | Parser M3U/M3U8 y ExoPlayer | Alta (P1) | **IMPLEMENTADO Y PROBADO** | `M3uPlaylistParser.kt` y `LiveTvRepository.kt` con pruebas unitarias |
| **MED-002** | TMDB | Integración API v3 y resiliencia | Alta (P1) | **IMPLEMENTADO Y PROBADO** | `TmdbApiService.kt` y `TmdbRepositoryTest.kt` (15/15 tests pasados) |
| **SET-001** | Ajustes | Panel de 14 categorías con persistencia | Alta (P1) | **IMPLEMENTADO Y PROBADO** | `SettingsScreen.kt` y `UserSettingsRepository.kt` |
| **SEC-001** | Seguridad | Sección +18 con protección de PIN | Alta (P1) | **IMPLEMENTADO Y PROBADO** | `AdultScreen.kt` y hashes de PIN |
| **TV-001** | Android TV | Navegación por control remoto D-pad | Alta (P1) | **IMPLEMENTADO Y PROBADO** | Modificador `dpadFocusable` en todos los componentes interactivos |
| **BLD-001** | Producción | Firma Release V2 y AAB | Crítica (P0) | **IMPLEMENTADO Y PROBADO** | `miltvplus-release.jks` verificado con `apksigner` y SHA-256 verificado |
