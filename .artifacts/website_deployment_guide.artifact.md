# Guía de Publicación Web para MatuuCrea/miltvplus-website

## 1. Verificación de Archivos Web
Se ha inspeccionado la carpeta `website/` del proyecto, la cual contiene la web completa y optimizada:
- `index.html` (Página principal con hero, características y llamadas a la acción)
- `download.html` (Página de descarga del APK con verificación SHA-256 e instrucciones)
- `privacy.html` (Política de privacidad)
- `terms.html` (Términos de servicio)
- `support.html` (Soporte técnico y contacto)
- `tmdb.html` (Documentación e integración con la API de TMDB)
- `assets/css/style.css` (Diseño responsivo con tema oscuro y acentos Material 3 M3)

## 2. Estado de Autenticación
GitHub CLI (`gh`) no se encuentra autenticado en esta sesión de terminal (`gh auth status` requiere login). Por motivos de seguridad y privacidad, no es posible realizar el push remoto sin credenciales activas.

## 3. Acción Mínima Requerida del Usuario
Para actualizar el repositorio público `MatuuCrea/miltvplus-website` con la web completa y reemplazar el README predeterminado, ejecutá los siguientes comandos en tu terminal local:

```cmd
:: 1. Autenticar GitHub CLI
gh auth login

:: 2. Clonar el repositorio web existente
git clone https://github.com/MatuuCrea/miltvplus-website.git temp-website

:: 3. Copiar los archivos web a la raíz del repositorio clonado
xcopy /E /I /Y website temp-website

:: 4. Entrar al directorio, confirmar cambios y subir a GitHub
cd temp-website
git add .
git commit -m "Deploy complete MILTVPLUS website files"
git push origin main

:: 5. Limpiar directorio temporal
cd ..
rmdir /S /Q temp-website
```

Una vez completado, la URL oficial `https://MatuuCrea.github.io/miltvplus-website/` cargará la aplicación web completa de MILTVPLUS en lugar del README.
