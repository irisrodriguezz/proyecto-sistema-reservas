# Sistema de Gestión de Reservas · CRUD en Java con MySQL

Aplicación de **consola** en **Java** para gestionar la reserva de espacios y recursos de una empresa (salas de reuniones, salas de proyecciones, salones de eventos, zonas de descanso…). Permite administrar usuarios, recursos, horarios y reservas con operaciones **CRUD** (crear, consultar, modificar y eliminar) sobre una base de datos **MySQL** mediante **JDBC**.

Es el CRUD desarrollado en el **Proyecto Intermodular** del ciclo de Desarrollo de Aplicaciones Web.

## Funcionalidades

La aplicación se maneja desde un menú en consola con un submenú por módulo:

```
==============================
     SISTEMA DE RESERVAS
==============================
1. Gestión de usuarios
2. Gestión de recursos
3. Gestión de horarios
4. Gestión de reservas
5. Consultas y listados
0. Salir
```

| Módulo | Opciones |
| --- | --- |
| **Usuarios** | Alta (de tipo Administrador o Normal), baja, modificación, listado completo y búsqueda por nombre, por ID y por correo. |
| **Recursos** | Alta, baja, modificación y listado de los espacios reservables. |
| **Horarios** | Listado, creación, modificación y eliminación de franjas horarias por día de la semana. |
| **Reservas** | Nueva reserva, baja, modificación y listado de todas las reservas. |
| **Consultas y listados** | Listado de administradores, listado de usuarios normales, reservas por usuario y reservas por recurso. |

Además, la aplicación valida los datos al introducirlos (formato de hora, fecha de nacimiento, tipo de usuario y números enteros, que se vuelven a pedir si son incorrectos), informa de los errores de la base de datos y cierra la conexión al salir. La búsqueda de usuarios por nombre admite coincidencias parciales.

## Arquitectura

El proyecto sigue una arquitectura por capas, con el patrón **MVC** y el patrón **DAO** para el acceso a datos. Los DAO se obtienen con el patrón **Singleton** (`getInstance()`).

| Paquete | Responsabilidad |
| --- | --- |
| `model` | Entidades (clases POJO con constructores, *getters*/*setters* y un `toString` con formato de tabla para la consola): `Usuario` (superclase), `Administrador` y `UsuarioNormal` (hijas), `Recurso`, `Horario`, `Reserva` y `DisponibleEn`. |
| `dao` | Acceso a la base de datos con JDBC (un DAO por entidad) y la conexión en `DBConnection`. |
| `controller` | Conecta las vistas con los DAO y captura los errores: `UsuarioController`, `RecursoController`, `HorarioController` y `ReservaController`. |
| `view` | Entrada y salida por consola de cada módulo: `UsuarioView`, `RecursoView`, `HorarioView` y `ReservaView`. |
| `app` | Punto de entrada: `Main`, con el menú principal y los submenús. |

## Base de datos

La aplicación **crea las tablas automáticamente** al arrancar (`DBConnection` pide a cada DAO que cree la suya con `CREATE TABLE IF NOT EXISTS`, en el orden que exigen las claves foráneas), pero la base de datos debe existir. El script `database/script_database.sql` crea la base de datos `base_de_datos_mvc` con codificación `utf8mb4`, las mismas tablas y unos datos de ejemplo:

| Tabla | Descripción |
| --- | --- |
| `USUARIO` | Datos comunes: correo (único), contraseña, nombre, fecha de nacimiento y tipo de usuario (`Administrador` o `Normal`). |
| `ADMINISTRADOR` | Especialización de `USUARIO` con el teléfono de guardia. |
| `USUARIONORMAL` | Especialización de `USUARIO` con dirección, teléfono móvil y fotografía. |
| `RECURSO` | Espacios que se pueden reservar. |
| `HORARIO` | Franjas por día de la semana (`hora_fin` debe ser posterior a `hora_inicio`). |
| `DISPONIBLEEN` | Relación N:M entre recursos y horarios. |
| `RESERVA` | Reservas, con clave primaria compuesta (`id_recurso`, `id_reserva_local`). |

Reglas de integridad que aplica la base de datos:

- Claves foráneas con `ON DELETE CASCADE`: al borrar un usuario o un recurso se borran sus datos dependientes (y sus reservas).
- `CHECK` para que la hora de fin de un horario sea posterior a la de inicio.
- Correo electrónico único.

## Tecnologías

- **Java**
- **JDBC** con **MySQL Connector/J 9.6.0**
- **MySQL**
- **IntelliJ IDEA** como entorno de desarrollo
- **Git y GitHub** para el control de versiones

## Estructura del repositorio

```
proyecto-sistema-reservas/
├── src/
│   ├── app/            # Main
│   ├── controller/
│   ├── dao/
│   ├── model/
│   └── view/
├── lib/
│   └── mysql-connector-j-9.6.0.jar
├── database/
│   └── script_database.sql
├── .gitignore
└── README.md
```

## Cómo ejecutarlo

### Requisitos

- JDK instalado
- MySQL en ejecución

### Pasos

1. **Clona el repositorio:**

   ```bash
   git clone https://github.com/irisrodriguezz/proyecto-sistema-reservas.git
   cd proyecto-sistema-reservas
   ```

2. **Crea la base de datos** ejecutando el script en MySQL:

   ```bash
   mysql -u TU_USUARIO -p -P 3307 < database/script_database.sql
   ```

3. **Configura la conexión** en `src/dao/DBConnection.java`. Por defecto usa `localhost`, el puerto **3307**, el usuario `root` sin contraseña y la base de datos `base_de_datos_mvc`; cambia estos valores si tu MySQL es distinto.

4. **Ejecuta la aplicación**, de una de estas dos formas:

   - **Con IntelliJ IDEA:** abre la carpeta del proyecto, añade `lib/mysql-connector-j-9.6.0.jar` como biblioteca (*File → Project Structure → Libraries*) y ejecuta `app.Main`.
   - **Desde la terminal** (en Linux o macOS; en Windows cambia `:` por `;` en el classpath):

     ```bash
     mkdir -p out
     javac -cp lib/mysql-connector-j-9.6.0.jar -d out $(find src -name "*.java")
     java -cp out:lib/mysql-connector-j-9.6.0.jar app.Main
     ```

> Los usuarios del script de ejemplo son ficticios y sus contraseñas son marcadores de posición, no contraseñas reales.

## Decisiones de diseño

- **Herencia de usuarios:** `USUARIO` guarda los datos comunes y `ADMINISTRADOR` y `USUARIONORMAL` los datos propios de cada tipo, compartiendo el mismo identificador. En la aplicación, `Administrador` y `UsuarioNormal` heredan de `Usuario`, y el controlador decide qué DAO usar según el tipo.
- **Dos constructores por entidad:** uno sin ID, para crear objetos nuevos (el ID lo genera MySQL), y otro con ID, para los recuperados de la base de datos.
- **Reservas con clave compuesta:** cada recurso numera sus propias reservas con `id_reserva_local`, así que para borrar una reserva se piden los dos identificadores.
- **Separación en capas:** el controlador no escribe ni lee por consola (lo hace la vista) ni ejecuta SQL (lo hace el DAO), de modo que se puede cambiar una capa sin tocar las demás.
- **Un único punto de conexión:** `DBConnection` (Singleton) abre la conexión al iniciar, crea el esquema y la cierra al salir.
- **Altas transaccionales:** al crear un administrador o un usuario normal se inserta primero en `USUARIO`, se recupera el ID generado y se inserta en la tabla hija dentro de una transacción; si algo falla se hace *rollback*.
- **Consultas preparadas:** todas las consultas usan `PreparedStatement` con parámetros, lo que evita la inyección SQL.

## Autoría

Proyecto realizado por **Iris Rodríguez Ujj** · Ciclo de Desarrollo de Aplicaciones Web · IES Severo Ochoa (Elche).

GitHub: [@irisrodriguezz](https://github.com/irisrodriguezz)
