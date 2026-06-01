public class Producto {
    private String id;
    private String nombre;
    private String categoria;
    private double precio;
    private String proveedor;
    private int cantidad;

    public Producto(String id, String nombre, String categoria, double precio, String proveedor, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.proveedor = proveedor;
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void aumentarStock(int unidades) {
        cantidad += unidades;
    }

    public boolean reducirStock(int unidades) {
        if (unidades > cantidad) {
            return false;
        }

        cantidad -= unidades;
        return true;
    }

    public String mostrarInformacion() {
        return "ID: " + id
                + "\nNombre: " + nombre
                + "\nCategoria: " + categoria
                + "\nPrecio: $" + precio
                + "\nProveedor: " + proveedor
                + "\nCantidad en stock: " + cantidad;
    }
}
