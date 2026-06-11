# Sistema de Gestión de Inventarios Basado en POO

Proyecto final de Programación Orientada a Objetos (2026-1). El sistema permite gestionar un inventario bibliográfico con libros y materiales académicos mediante operaciones CRUD (Crear, Leer, Actualizar, Eliminar) aplicando principios de POO en Java.

## Integrantes del Equipo
1. Juan Sebastian Guerrero Vanegas
2. Ervin Santiago Ardila Vanegas
<<<<<<< Updated upstream
3. Juan Manuel Caviedes sanchez 
=======
3. Luisa Fernanda García Mercado
4. Juan Manuel Caviedes Sánchez
>>>>>>> Stashed changes

## Requisitos y Dependencias
- **Lenguaje:** Java Development Kit (JDK) 11 o superior.
- **IDE sugerido:** IntelliJ IDEA, Eclipse o Apache NetBeans.
- **Dependencias externas:** ninguna. El proyecto usa Java Swing y clases estándar de Java.
- **Sistema operativo:** Windows, Linux o macOS. El script `ejecutar.ps1` es opcional y está pensado para PowerShell en Windows.

## Instalación y Ejecución
1. Clonar este repositorio en tu máquina local.
2. Abrir la carpeta del proyecto en tu IDE de preferencia.
3. Compilar los archivos de la carpeta `src`.
4. Ejecutar la clase principal `Principal.java`.

Desde una terminal ubicada en la raíz del proyecto:

```bash
javac -d out src/*.java
java -cp out Principal
```

En PowerShell para Windows también puedes usar el script incluido:

```powershell
cd "Sistema-Inventarios-POO"
powershell -ExecutionPolicy Bypass -File ".\ejecutar.ps1"
```

Para compilar sin abrir la interfaz gráfica:

```powershell
powershell -ExecutionPolicy Bypass -File ".\ejecutar.ps1" -SoloCompilar
```

Al ejecutar `Principal.java` se abre la interfaz gráfica del sistema. Desde la ventana se puede iniciar sesión como administrador o empleado. El nombre de usuario es libre y el rol define permisos: solo el administrador puede eliminar registros.

## Datos de Demostración

El inventario carga libros de demostración con enfoque universitario, incluyendo títulos de programación, bases de datos, cálculo, física, geociencias, ciencias sociales y pensamiento sistémico. Los datos incluyen ID, título, autor, editorial, área académica, valor de referencia y cantidad disponible.

## Recursos Visuales

El logo institucional se carga desde la carpeta `Universidad_Nacional_de_Colombia`. Esa carpeta debe conservarse en la raíz del proyecto para que la interfaz muestre correctamente la identidad visual de la Universidad Nacional de Colombia.

## Estructura del Programa

- `Principal.java`: inicia la interfaz gráfica del sistema.
- `Main.java`: delega en `Principal.java` para mantener compatibilidad.
- `InventarioGUI.java`: contiene las pantallas, formularios, tabla y acciones de la interfaz gráfica.
- `Producto.java`: representa cada libro o material académico del inventario.
- `Inventario.java`: contiene la lógica para registrar, consultar, actualizar y eliminar libros.
- `Usuario.java`: representa al usuario que usa el sistema.
- `Rol.java`: define los roles `ADMINISTRADOR` y `EMPLEADO`.

## Funcionalidades

- Registrar libros con ID, título, autor, editorial, área académica, valor de referencia y cantidad disponible.
- Listar todos los libros registrados.
- Buscar libros por ID, título, autor o área académica.
- Actualizar información general de un libro.
- Registrar entradas y salidas de ejemplares.
- Validar stock insuficiente.
- Eliminar libros solo con rol de administrador.
- Generar reporte de libros con bajo stock.

## Notas para GitHub

- Sube la carpeta `src`.
- Sube la carpeta `Universidad_Nacional_de_Colombia` porque contiene el logo usado por la interfaz.
- Sube `README.md`, `ejecutar.ps1` y `.gitignore`.
- No es necesario subir la carpeta `out`, archivos `.class` ni configuraciones locales del IDE.
