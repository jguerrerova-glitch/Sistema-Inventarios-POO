public class Producto {
    private final String id;
    private String titulo;
    private String autor;
    private String editorial;
    private String areaAcademica;
    private double valorReferencia;
    private int cantidadDisponible;

    public Producto(
            String id,
            String titulo,
            String autor,
            String editorial,
            String areaAcademica,
            double valorReferencia,
            int cantidadDisponible) {
        this.id = validarTexto(id, "ID");
        setTitulo(titulo);
        setAutor(autor);
        setEditorial(editorial);
        setAreaAcademica(areaAcademica);
        setValorReferencia(valorReferencia);
        setCantidadDisponible(cantidadDisponible);
    }

    public Producto(String id, String nombre, String categoria, double precio, String proveedor, int cantidad) {
        this(id, nombre, "No especificado", proveedor, categoria, precio, cantidad);
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = validarTexto(titulo, "Titulo");
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = validarTexto(autor, "Autor");
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = validarTexto(editorial, "Editorial");
    }

    public String getAreaAcademica() {
        return areaAcademica;
    }

    public void setAreaAcademica(String areaAcademica) {
        this.areaAcademica = validarTexto(areaAcademica, "Area academica");
    }

    public double getValorReferencia() {
        return valorReferencia;
    }

    public void setValorReferencia(double valorReferencia) {
        if (valorReferencia < 0) {
            throw new IllegalArgumentException("El valor de referencia no puede ser negativo.");
        }

        this.valorReferencia = valorReferencia;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        if (cantidadDisponible < 0) {
            throw new IllegalArgumentException("La cantidad disponible no puede ser negativa.");
        }

        this.cantidadDisponible = cantidadDisponible;
    }

    public void aumentarStock(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades deben ser mayores a cero.");
        }

        cantidadDisponible += unidades;
    }

    public boolean reducirStock(int unidades) {
        if (unidades <= 0 || unidades > cantidadDisponible) {
            return false;
        }

        cantidadDisponible -= unidades;
        return true;
    }

    public String mostrarInformacion() {
        return "ID: " + id
                + "\nTitulo: " + titulo
                + "\nAutor: " + autor
                + "\nEditorial: " + editorial
                + "\nArea academica: " + areaAcademica
                + "\nValor de referencia: $" + valorReferencia
                + "\nCantidad disponible: " + cantidadDisponible;
    }

    public String getNombre() {
        return getTitulo();
    }

    public void setNombre(String nombre) {
        setTitulo(nombre);
    }

    public String getCategoria() {
        return getAreaAcademica();
    }

    public void setCategoria(String categoria) {
        setAreaAcademica(categoria);
    }

    public double getPrecio() {
        return getValorReferencia();
    }

    public void setPrecio(double precio) {
        setValorReferencia(precio);
    }

    public String getProveedor() {
        return getEditorial();
    }

    public void setProveedor(String proveedor) {
        setEditorial(proveedor);
    }

    public int getCantidad() {
        return getCantidadDisponible();
    }

    public void setCantidad(int cantidad) {
        setCantidadDisponible(cantidad);
    }

    private String validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }

        return texto.trim();
    }
}
