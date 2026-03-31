# Zoo Fantástico - Backend

Este proyecto es el backend para la gestión de un "Zoo de Mascotas Fantásticas", desarrollado en Java utilizando el framework Spring Boot y una base de datos MySQL. Implementa operaciones CRUD básicas enfocadas en las Criaturas.

## Especificaciones y Requisitos Previos

Para ejecutar el proyecto necesitas tener instalado:
- **Java Development Kit (JDK) 8 o superior** (Recomendado JDK 17 según la configuración).
- **Maven** (Aunque el proyecto incluye el *wrapper* `mvnw`).
- **MySQL Server** corriendo localmente en el puerto `3306`.
- **Git** para el control de versiones (flujo Gitflow).

## Configuración de la Base de Datos

1. Abre tu gestor de base de datos MySQL o la consola de comandos de MySQL.
2. Inicia sesión con tus credenciales.
3. Crea la base de datos ejecutando: `CREATE DATABASE zoo_fantastico;`
4. Asegúrate que tus credenciales coincidan con las de `src/main/resources/application.properties`:
    - `spring.datasource.username=root`
    - `spring.datasource.password=1014736507` (o el que dispongas)

## Cómo ejecutar la aplicación

1. Abre una terminal y navega hasta la raíz del proyecto.
2. Compila y levanta la aplicación usando Maven Wrapper:
   ```bash
   # En Windows
   .\mvnw.cmd spring-boot:run
   
   # En Mac/Linux
   ./mvnw spring-boot:run
   ```
3. Una vez se muestre `Started ZooFantasticoApplication`, el servidor estará activo en `http://localhost:8080`.

## Documentación del Código

El código fuente está documentado utilizando **Javadoc**. Para generar un sitio web HTML con la documentación directamente, puedes ejecutar:
```bash
.\mvnw.cmd javadoc:javadoc
```
Luego, abre el archivo generado en `target/site/apidocs/index.html` con cualquier navegador para visualizar todos los componentes del sistema y sus detalles.