import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventario inventario = new Inventario();

    public static void main(String[] args) {
        Usuario usuario = iniciarSesion();
        int opcion;

        do {
            mostrarMenu(usuario);
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    registrarProducto();
                    break;
                case 2:
                    listarProductos();
                    break;
                case 3:
                    buscarProducto();
                    break;
                case 4:
                    actualizarProducto();
                    break;
                case 5:
                    registrarEntradaStock();
                    break;
                case 6:
                    registrarSalidaStock();
                    break;
                case 7:
                    eliminarProducto(usuario);
                    break;
                case 8:
                    mostrarReporteBajoStock();
                    break;
                case 9:
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Error: opcion no valida.");
                    break;
            }
        } while (opcion != 9);
    }

    private static Usuario iniciarSesion() {
        System.out.println("=== Sistema de Gestion de Inventarios ===");
        String nombre = leerTextoObligatorio("Ingrese su nombre: ");

        System.out.println("Seleccione su rol:");
        System.out.println("1. Administrador");
        System.out.println("2. Empleado");

        int opcionRol = leerEntero("Opcion: ");
        Rol rol = opcionRol == 1 ? Rol.ADMINISTRADOR : Rol.EMPLEADO;

        return new Usuario(nombre, rol);
    }

    private static void mostrarMenu(Usuario usuario) {
        System.out.println();
        System.out.println("Usuario: " + usuario.getNombre() + " | Rol: " + usuario.getRol());
        System.out.println("=== Menu Principal ===");
        System.out.println("1. Registrar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto");
        System.out.println("4. Actualizar producto");
        System.out.println("5. Registrar entrada de stock");
        System.out.println("6. Registrar salida de stock");
        System.out.println("7. Eliminar producto");
        System.out.println("8. Reporte de bajo stock");
        System.out.println("9. Salir");
    }

    private static void registrarProducto() {
        System.out.println();
        System.out.println("=== Registrar Producto ===");

        String id = leerTextoObligatorio("ID: ");
        if (inventario.buscarPorId(id) != null) {
            System.out.println("Error: el ID ingresado ya existe en el inventario.");
            return;
        }

        String nombre = leerTextoObligatorio("Nombre: ");
        String categoria = leerTextoObligatorio("Categoria: ");
        double precio = leerDoubleNoNegativo("Precio: ");
        String proveedor = leerTextoObligatorio("Proveedor: ");
        int cantidad = leerEnteroNoNegativo("Cantidad inicial: ");

        Producto producto = new Producto(id, nombre, categoria, precio, proveedor, cantidad);

        if (inventario.agregarProducto(producto)) {
            System.out.println("Producto registrado correctamente.");
        } else {
            System.out.println("Error: no fue posible registrar el producto.");
        }
    }

    private static void listarProductos() {
        System.out.println();
        System.out.println("=== Lista de Productos ===");

        List<Producto> productos = inventario.listarProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto producto : productos) {
            System.out.println("-------------------------");
            System.out.println(producto.mostrarInformacion());
        }
    }

    private static void buscarProducto() {
        System.out.println();
        System.out.println("=== Buscar Producto ===");
        System.out.println("1. Buscar por ID");
        System.out.println("2. Buscar por nombre");

        int opcion = leerEntero("Opcion: ");
        Producto producto;

        if (opcion == 1) {
            String id = leerTextoObligatorio("ID: ");
            producto = inventario.buscarPorId(id);
        } else if (opcion == 2) {
            String nombre = leerTextoObligatorio("Nombre: ");
            producto = inventario.buscarPorNombre(nombre);
        } else {
            System.out.println("Error: opcion no valida.");
            return;
        }

        if (producto == null) {
            System.out.println("Error: El producto buscado no existe en el inventario.");
            return;
        }

        System.out.println(producto.mostrarInformacion());
    }

    private static void actualizarProducto() {
        System.out.println();
        System.out.println("=== Actualizar Producto ===");

        String id = leerTextoObligatorio("ID del producto a actualizar: ");
        Producto productoActual = inventario.buscarPorId(id);

        if (productoActual == null) {
            System.out.println("Error: El producto buscado no existe en el inventario.");
            return;
        }

        System.out.println("Datos actuales:");
        System.out.println(productoActual.mostrarInformacion());

        String nombre = leerTextoObligatorio("Nuevo nombre: ");
        String categoria = leerTextoObligatorio("Nueva categoria: ");
        double precio = leerDoubleNoNegativo("Nuevo precio: ");
        String proveedor = leerTextoObligatorio("Nuevo proveedor: ");

        if (inventario.actualizarProducto(id, nombre, categoria, precio, proveedor)) {
            System.out.println("Producto actualizado correctamente.");
        } else {
            System.out.println("Error: no fue posible actualizar el producto.");
        }
    }

    private static void registrarEntradaStock() {
        System.out.println();
        System.out.println("=== Registrar Entrada de Stock ===");

        String id = leerTextoObligatorio("ID del producto: ");
        int unidades = leerEnteroPositivo("Unidades a ingresar: ");

        if (inventario.registrarEntradaStock(id, unidades)) {
            System.out.println("Entrada de stock registrada correctamente.");
        } else {
            System.out.println("Error: El producto buscado no existe en el inventario.");
        }
    }

    private static void registrarSalidaStock() {
        System.out.println();
        System.out.println("=== Registrar Salida de Stock ===");

        String id = leerTextoObligatorio("ID del producto: ");
        Producto producto = inventario.buscarPorId(id);

        if (producto == null) {
            System.out.println("Error: El producto buscado no existe en el inventario.");
            return;
        }

        int unidades = leerEnteroPositivo("Unidades a retirar: ");

        if (inventario.registrarSalidaStock(id, unidades)) {
            System.out.println("Salida de stock registrada correctamente.");
        } else {
            System.out.println("Stock insuficiente.");
        }
    }

    private static void eliminarProducto(Usuario usuario) {
        System.out.println();
        System.out.println("=== Eliminar Producto ===");

        if (!usuario.esAdministrador()) {
            System.out.println("Error: solo un administrador puede eliminar productos.");
            return;
        }

        String id = leerTextoObligatorio("ID del producto a eliminar: ");
        Producto producto = inventario.buscarPorId(id);

        if (producto == null) {
            System.out.println("Error: El producto buscado no existe en el inventario.");
            return;
        }

        System.out.println(producto.mostrarInformacion());
        String confirmacion = leerTextoObligatorio("Escriba SI para confirmar la eliminacion: ");

        if (!confirmacion.equalsIgnoreCase("SI")) {
            System.out.println("Eliminacion cancelada.");
            return;
        }

        if (inventario.eliminarProducto(id)) {
            System.out.println("Producto eliminado correctamente.");
        } else {
            System.out.println("Error: no fue posible eliminar el producto.");
        }
    }

    private static void mostrarReporteBajoStock() {
        System.out.println();
        System.out.println("=== Reporte de Bajo Stock ===");

        int limite = leerEnteroNoNegativo("Ingrese el limite de stock: ");
        List<Producto> productosBajoStock = inventario.obtenerProductosBajoStock(limite);

        if (productosBajoStock.isEmpty()) {
            System.out.println("El inventario se encuentra excelente.");
            return;
        }

        for (Producto producto : productosBajoStock) {
            System.out.println("-------------------------");
            System.out.println(producto.mostrarInformacion());
        }
    }

    private static String leerTextoObligatorio(String mensaje) {
        String texto;

        do {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("Error: este campo es obligatorio.");
            }
        } while (texto.isEmpty());

        return texto;
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException error) {
                System.out.println("Error: ingrese un numero entero valido.");
            }
        }
    }

    private static int leerEnteroNoNegativo(String mensaje) {
        int numero;

        do {
            numero = leerEntero(mensaje);

            if (numero < 0) {
                System.out.println("Error: el numero no puede ser negativo.");
            }
        } while (numero < 0);

        return numero;
    }

    private static int leerEnteroPositivo(String mensaje) {
        int numero;

        do {
            numero = leerEntero(mensaje);

            if (numero <= 0) {
                System.out.println("Error: el numero debe ser mayor a cero.");
            }
        } while (numero <= 0);

        return numero;
    }

    private static double leerDoubleNoNegativo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            try {
                double numero = Double.parseDouble(entrada);

                if (numero < 0) {
                    System.out.println("Error: el numero no puede ser negativo.");
                } else {
                    return numero;
                }
            } catch (NumberFormatException error) {
                System.out.println("Error: ingrese un numero valido.");
            }
        }
    }
}
