# Sistema de Gestión de Inventarios Basado en POO

Este repositorio contiene el proyecto final de la asignatura de Programación Orientada a Objetos (2026-1). El sistema permite gestionar un inventario de productos mediante operaciones CRUD (Crear, Leer, Actualizar, Eliminar) aplicando los principios de POO en Java.

## Integrantes del Equipo
1. Juan Sebastian Guerrero Vanegas
2. Ervin Santiago Ardila Vanegas
3. Luisa Fernanda García Mercado
4. Juan Manuel Caviedes sanchez 

## Requisitos y Dependencias
- **Lenguaje:** Java Development Kit (JDK) 11 o superior.
- **IDE sugerido:** IntelliJ IDEA, Eclipse o Apache NetBeans.
- **Control de versiones:** Git y GitHub Desktop para la colaboración.

## Instalación y Ejecución
1. Clonar este repositorio en tu máquina local
2. Abrir la carpeta del proyecto en tu IDE de preferencia.
3. Compilar los archivos de la carpeta `src`.
4. Ejecutar la clase principal `Main.java`.

También se puede ejecutar desde la terminal:

```bash
javac -d out src/*.java
java -cp out Main
```

## Estructura del Programa

- `Main.java`: contiene el menú por consola y recibe los datos del usuario.
- `Producto.java`: representa cada producto del inventario.
- `Inventario.java`: contiene la lógica para registrar, consultar, actualizar y eliminar productos.
- `Usuario.java`: representa al usuario que usa el sistema.
- `Rol.java`: define los roles `ADMINISTRADOR` y `EMPLEADO`.

## Funcionalidades

- Registrar productos con ID, nombre, categoría, precio, proveedor y cantidad.
- Listar todos los productos registrados.
- Buscar productos por ID o nombre.
- Actualizar información general de un producto.
- Registrar entradas y salidas de stock.
- Validar stock insuficiente.
- Eliminar productos solo con rol de administrador.
- Generar reporte de productos con bajo stock.
