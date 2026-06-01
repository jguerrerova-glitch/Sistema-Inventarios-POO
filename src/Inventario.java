import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private ArrayList<Producto> productos;

    public Inventario() {
        productos = new ArrayList<>();
    }

    public boolean agregarProducto(Producto producto) {
        if (buscarPorId(producto.getId()) != null) {
            return false;
        }

        productos.add(producto);
        return true;
    }

    public List<Producto> listarProductos() {
        return productos;
    }

    public Producto buscarPorId(String id) {
        for (Producto producto : productos) {
            if (producto.getId().equalsIgnoreCase(id)) {
                return producto;
            }
        }

        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }

        return null;
    }

    public boolean actualizarProducto(String id, String nombre, String categoria, double precio, String proveedor) {
        Producto producto = buscarPorId(id);

        if (producto == null) {
            return false;
        }

        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setPrecio(precio);
        producto.setProveedor(proveedor);
        return true;
    }

    public boolean registrarEntradaStock(String id, int unidades) {
        Producto producto = buscarPorId(id);

        if (producto == null) {
            return false;
        }

        producto.aumentarStock(unidades);
        return true;
    }

    public boolean registrarSalidaStock(String id, int unidades) {
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
            if (producto.getCantidad() <= limite) {
                productosBajoStock.add(producto);
            }
        }

        return productosBajoStock;
    }
}
