import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Inventario {
    private final ArrayList<Producto> productos;

    public Inventario() {
        productos = new ArrayList<>();
        cargarLibrosDemostracion();
    }

    public boolean agregarProducto(Producto producto) {
        if (producto == null || textoVacio(producto.getId())) {
            return false;
        }

        if (buscarPorId(producto.getId()) != null) {
            return false;
        }

        productos.add(producto);
        return true;
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(productos);
    }

    public Producto buscarPorId(String id) {
        if (textoVacio(id)) {
            return null;
        }

        String idNormalizado = id.trim();

        for (Producto producto : productos) {
            if (producto.getId().equalsIgnoreCase(idNormalizado)) {
                return producto;
            }
        }

        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        if (textoVacio(nombre)) {
            return null;
        }

        String consulta = normalizar(nombre);

        for (Producto producto : productos) {
            if (normalizar(producto.getTitulo()).equals(consulta)) {
                return producto;
            }
        }

        return null;
    }

    public List<Producto> buscarPorTitulo(String titulo) {
        ArrayList<Producto> resultados = new ArrayList<>();
        String consulta = normalizar(titulo);

        if (consulta.isEmpty()) {
            return resultados;
        }

        for (Producto producto : productos) {
            if (normalizar(producto.getTitulo()).contains(consulta)) {
                resultados.add(producto);
            }
        }

        return resultados;
    }

    public List<Producto> buscarPorAutor(String autor) {
        ArrayList<Producto> resultados = new ArrayList<>();
        String consulta = normalizar(autor);

        if (consulta.isEmpty()) {
            return resultados;
        }

        for (Producto producto : productos) {
            if (normalizar(producto.getAutor()).contains(consulta)) {
                resultados.add(producto);
            }
        }

        return resultados;
    }

    public List<Producto> buscarPorAreaAcademica(String areaAcademica) {
        ArrayList<Producto> resultados = new ArrayList<>();
        String consulta = normalizar(areaAcademica);

        if (consulta.isEmpty()) {
            return resultados;
        }

        for (Producto producto : productos) {
            if (normalizar(producto.getAreaAcademica()).contains(consulta)) {
                resultados.add(producto);
            }
        }

        return resultados;
    }

    public boolean actualizarProducto(String id, String nombre, String categoria, double precio, String proveedor) {
        Producto producto = buscarPorId(id);

        if (producto == null) {
            return false;
        }

        producto.setTitulo(nombre);
        producto.setAreaAcademica(categoria);
        producto.setValorReferencia(precio);
        producto.setEditorial(proveedor);
        return true;
    }

    public boolean actualizarProducto(String id, String nombre, String categoria, double precio, String proveedor, int cantidad) {
        return actualizarProducto(id, nombre, "No especificado", proveedor, categoria, precio, cantidad);
    }

    public boolean actualizarProducto(
            String id,
            String titulo,
            String autor,
            String editorial,
            String areaAcademica,
            double valorReferencia,
            int cantidadDisponible) {
        Producto producto = buscarPorId(id);

        if (producto == null) {
            return false;
        }

        producto.setTitulo(titulo);
        producto.setAutor(autor);
        producto.setEditorial(editorial);
        producto.setAreaAcademica(areaAcademica);
        producto.setValorReferencia(valorReferencia);
        producto.setCantidadDisponible(cantidadDisponible);
        return true;
    }

    public boolean registrarEntradaStock(String id, int unidades) {
        if (unidades <= 0) {
            return false;
        }

        Producto producto = buscarPorId(id);

        if (producto == null) {
            return false;
        }

        producto.aumentarStock(unidades);
        return true;
    }

    public boolean registrarSalidaStock(String id, int unidades) {
        if (unidades <= 0) {
            return false;
        }

        Producto producto = buscarPorId(id);

        if (producto == null) {
            return false;
        }

        return producto.reducirStock(unidades);
    }

    public boolean eliminarProducto(String id) {
        Producto producto = buscarPorId(id);

        if (producto == null) {
            return false;
        }

        productos.remove(producto);
        return true;
    }

    public List<Producto> obtenerProductosBajoStock(int limite) {
        ArrayList<Producto> productosBajoStock = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto.getCantidadDisponible() <= limite) {
                productosBajoStock.add(producto);
            }
        }

        return productosBajoStock;
    }

    private void cargarLibrosDemostracion() {
        agregarProducto(new Producto(
                "UNAL-BIB-001",
                "Programacion orientada a objetos con Java",
                "Cay S. Horstmann",
                "Pearson",
                "Ingenieria de Sistemas",
                145000,
                18));

        agregarProducto(new Producto(
                "UNAL-BIB-002",
                "Fundamentos de bases de datos",
                "Abraham Silberschatz, Henry F. Korth y S. Sudarshan",
                "McGraw-Hill",
                "Ingenieria de Sistemas",
                168000,
                7));

        agregarProducto(new Producto(
                "UNAL-BIB-003",
                "Calculo de una variable",
                "James Stewart",
                "Cengage Learning",
                "Matematicas",
                132000,
                12));

        agregarProducto(new Producto(
                "UNAL-BIB-004",
                "Fisica universitaria Vol. 1",
                "Sears, Zemansky, Young y Freedman",
                "Pearson",
                "Fisica",
                154000,
                9));

        agregarProducto(new Producto(
                "UNAL-BIB-005",
                "Manual de geologia para ingenieros",
                "Gonzalo Duque-Escobar",
                "Universidad Nacional de Colombia",
                "Geociencias",
                0,
                10));

        agregarProducto(new Producto(
                "UNAL-BIB-006",
                "Colombia: pais fragmentado, sociedad dividida",
                "Marco Palacios y Frank Safford",
                "Universidad de los Andes",
                "Ciencias Sociales",
                98000,
                5));

        agregarProducto(new Producto(
                "UNAL-BIB-007",
                "Analisis numerico",
                "Richard L. Burden y J. Douglas Faires",
                "Cengage Learning",
                "Matematicas Aplicadas",
                126000,
                14));

        agregarProducto(new Producto(
                "UNAL-BIB-008",
                "Introduccion a la teoria general de sistemas",
                "Ludwig von Bertalanffy",
                "Fondo de Cultura Economica",
                "Pensamiento Sistemico",
                72000,
                6));
    }

    private boolean textoVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.trim().toLowerCase(Locale.ROOT);
    }
}
