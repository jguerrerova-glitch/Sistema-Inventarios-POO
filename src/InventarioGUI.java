import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

@SuppressWarnings({"serial", "this-escape"})
public class InventarioGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String LOGO_PATH = "Universidad_Nacional_de_Colombia"
            + File.separator + "Universidad_Nacional_de_Colombia_idGHINTe5f_0.png";

    private static final Color VERDE_UNAL = new Color(46, 106, 65);
    private static final Color ROJO_UNAL = new Color(166, 27, 48);
    private static final Color NEGRO = new Color(24, 24, 24);
    private static final Color BLANCO = Color.WHITE;
    private static final Color FONDO = new Color(245, 246, 244);
    private static final Color GRIS_TEXTO = new Color(74, 79, 83);
    private static final Color GRIS_BORDE = new Color(184, 188, 190);
    private static final Color GRIS_CLARO = new Color(231, 234, 232);

    private final Inventario inventario;
    private final CardLayout vistas;
    private final JPanel raiz;

    private Usuario usuario;
    private ModeloTablaProductos modeloTabla;
    private JTable tablaProductos;

    private JTextField txtLoginNombre;
    private JComboBox<String> cmbLoginRol;

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtEditorial;
    private JTextField txtAreaAcademica;
    private JTextField txtValorReferencia;
    private JTextField txtCantidadDisponible;

    private JComboBox<String> cmbBusqueda;
    private JTextField txtBusqueda;
    private JTextField txtStockId;
    private JTextField txtStockUnidades;
    private JTextField txtLimiteStock;

    private JLabel lblUsuario;
    private JLabel lblEstado;
    private JButton btnEliminar;

    public InventarioGUI() {
        configurarEstiloGlobal();
        inventario = new Inventario();
        vistas = new CardLayout();
        raiz = new JPanel(vistas);

        setTitle("Sistema de Inventarios de Biblioteca - UNAL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 720));
        setLocationByPlatform(true);

        ImageIcon icono = cargarLogo(48, 64);
        if (icono != null) {
            setIconImage(icono.getImage());
        }

        raiz.add(crearPanelLogin(), "login");
        setContentPane(raiz);
        pack();
        setLocationRelativeTo(null);
    }

    private void configurarEstiloGlobal() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception error) {
            // La interfaz sigue funcionando con el look and feel disponible.
        }

        UIManager.put("Button.arc", Integer.valueOf(0));
        UIManager.put("Component.arc", Integer.valueOf(0));
        UIManager.put("TextComponent.arc", Integer.valueOf(0));
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.add(crearEncabezado(false), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(FONDO);
        centro.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(BLANCO);
        formulario.setBorder(new CompoundBorder(
                new LineBorder(GRIS_BORDE, 1),
                new EmptyBorder(24, 28, 24, 28)));

        JLabel titulo = new JLabel("Ingreso al sistema bibliotecario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(NEGRO);

        txtLoginNombre = crearCampoTexto();
        txtLoginNombre.addActionListener(evento -> iniciarSesion());
        cmbLoginRol = crearCombo(new String[] {"Administrador", "Empleado"});
        JButton btnIngresar = crearBoton("Ingresar", VERDE_UNAL, BLANCO);
        btnIngresar.addActionListener(evento -> iniciarSesion());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 18, 0);
        formulario.add(titulo, gbc);

        agregarFilaFormulario(formulario, "Nombre", txtLoginNombre, 1);
        agregarFilaFormulario(formulario, "Rol", cmbLoginRol, 2);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(16, 0, 0, 0);
        formulario.add(btnIngresar, gbc);

        centro.add(formulario);
        panel.add(centro, BorderLayout.CENTER);

        panel.add(crearBarraEstado(), BorderLayout.SOUTH);
        return panel;
    }

    private void iniciarSesion() {
        String nombre = txtLoginNombre.getText().trim();

        if (nombre.isEmpty()) {
            mostrarEstado("Error: el nombre de usuario es obligatorio.", true);
            txtLoginNombre.requestFocus();
            return;
        }

        Rol rol = cmbLoginRol.getSelectedIndex() == 0 ? Rol.ADMINISTRADOR : Rol.EMPLEADO;
        usuario = new Usuario(nombre, rol);

        JPanel principal = crearPanelPrincipal();
        raiz.add(principal, "principal");
        vistas.show(raiz, "principal");
        mostrarEstado("Sesion iniciada correctamente. Bienvenido a la coleccion academica UNAL.", false);
    }

    private void cerrarSesion() {
        usuario = null;
        raiz.removeAll();
        raiz.add(crearPanelLogin(), "login");
        vistas.show(raiz, "login");
        raiz.revalidate();
        raiz.repaint();
        txtLoginNombre.requestFocus();
        mostrarEstado("Sesion cerrada. Inventario academico listo para nuevo ingreso.", false);
    }

    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO);
        panel.add(crearEncabezado(true), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(12, 12));
        cuerpo.setBackground(FONDO);
        cuerpo.setBorder(new EmptyBorder(14, 14, 14, 14));

        JSplitPane division = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                crearPanelListado(),
                crearPanelAcciones());
        division.setResizeWeight(0.68);
        division.setDividerSize(8);
        division.setBorder(new LineBorder(GRIS_BORDE, 1));
        division.setBackground(FONDO);
        cuerpo.add(division, BorderLayout.CENTER);

        panel.add(cuerpo, BorderLayout.CENTER);
        panel.add(crearBarraEstado(), BorderLayout.SOUTH);

        actualizarVistaCompleta();
        return panel;
    }

    private JPanel crearEncabezado(boolean mostrarUsuario) {
        JPanel encabezado = new JPanel(new BorderLayout(16, 0));
        encabezado.setBackground(BLANCO);
        encabezado.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 3, 0, VERDE_UNAL),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setPreferredSize(new Dimension(76, 82));
        ImageIcon imagenLogo = cargarLogo(62, 78);
        if (imagenLogo != null) {
            logo.setIcon(imagenLogo);
        } else {
            logo.setText("UNAL");
            logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            logo.setForeground(VERDE_UNAL);
            logo.setBorder(new LineBorder(NEGRO, 1));
        }
        encabezado.add(logo, BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel titulo = new JLabel("Inventario de Biblioteca Academica");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(NEGRO);

        JLabel subtitulo = new JLabel("Universidad Nacional de Colombia - Sede Bogota");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(GRIS_TEXTO);

        JLabel sello = new JLabel("UNAL 1867 | Busca la verdad en las aulas");
        sello.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sello.setForeground(VERDE_UNAL);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);
        textos.add(Box.createVerticalStrut(2));
        textos.add(sello);
        encabezado.add(textos, BorderLayout.CENTER);

        if (mostrarUsuario) {
            lblUsuario = new JLabel(usuario.getNombre() + " | " + usuario.getRol());
            lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblUsuario.setForeground(NEGRO);
            lblUsuario.setBorder(new CompoundBorder(
                    new LineBorder(GRIS_BORDE, 1),
                    new EmptyBorder(8, 12, 8, 12)));

            JButton btnCerrarSesion = crearBoton("Cerrar sesion", GRIS_CLARO, NEGRO);
            btnCerrarSesion.addActionListener(evento -> cerrarSesion());

            JPanel sesion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            sesion.setOpaque(false);
            sesion.add(lblUsuario);
            sesion.add(btnCerrarSesion);
            encabezado.add(sesion, BorderLayout.EAST);
        }

        return encabezado;
    }

    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BLANCO);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel busqueda = new JPanel(new GridBagLayout());
        busqueda.setBackground(BLANCO);
        busqueda.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(GRIS_BORDE, 1), "Consulta de inventario"),
                new EmptyBorder(8, 8, 8, 8)));

        cmbBusqueda = crearCombo(new String[] {"ID", "Titulo", "Autor", "Area academica"});
        txtBusqueda = crearCampoTexto();
        JButton btnBuscar = crearBoton("Buscar", NEGRO, BLANCO);
        JButton btnTodos = crearBoton("Mostrar todos", VERDE_UNAL, BLANCO);

        txtBusqueda.addActionListener(evento -> consultarProductos());
        btnBuscar.addActionListener(evento -> consultarProductos());
        btnTodos.addActionListener(evento -> {
            txtBusqueda.setText("");
            limpiarSeleccionSinEstado();
            actualizarVistaCompleta();
            mostrarEstado("Listado completo actualizado.", false);
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.weightx = 0;
        busqueda.add(cmbBusqueda, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        busqueda.add(txtBusqueda, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        busqueda.add(btnBuscar, gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        busqueda.add(btnTodos, gbc);

        modeloTabla = new ModeloTablaProductos();
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFillsViewportHeight(true);
        tablaProductos.setRowHeight(30);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setGridColor(GRIS_CLARO);
        tablaProductos.setShowGrid(true);
        tablaProductos.setAutoCreateRowSorter(true);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setSelectionBackground(new Color(214, 230, 218));
        tablaProductos.setSelectionForeground(NEGRO);

        JTableHeader header = tablaProductos.getTableHeader();
        header.setBackground(NEGRO);
        header.setForeground(BLANCO);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(new LineBorder(NEGRO, 1));

        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tablaProductos.getColumnModel().getColumn(5).setCellRenderer(derecha);

        DefaultTableCellRenderer stock = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tabla,
                    Object valor,
                    boolean seleccionado,
                    boolean enfocado,
                    int fila,
                    int columna) {
                Component componente = super.getTableCellRendererComponent(
                        tabla, valor, seleccionado, enfocado, fila, columna);
                setHorizontalAlignment(SwingConstants.RIGHT);

                if (!seleccionado && valor instanceof Integer && ((Integer) valor).intValue() <= 10) {
                    componente.setForeground(ROJO_UNAL);
                } else if (!seleccionado) {
                    componente.setForeground(NEGRO);
                }

                return componente;
            }
        };
        tablaProductos.getColumnModel().getColumn(6).setCellRenderer(stock);

        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(105);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(230);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(210);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(170);
        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(95);
        tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(85);

        tablaProductos.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                cargarProductoSeleccionado();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.setBorder(new LineBorder(GRIS_BORDE, 1));

        panel.add(busqueda, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane crearPanelAcciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BLANCO);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel producto = crearPanelProducto();
        JPanel stock = crearPanelStock();
        JPanel reporte = crearPanelReporte();

        producto.setAlignmentX(Component.LEFT_ALIGNMENT);
        stock.setAlignmentX(Component.LEFT_ALIGNMENT);
        reporte.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(producto);
        panel.add(Box.createVerticalStrut(12));
        panel.add(stock);
        panel.add(Box.createVerticalStrut(12));
        panel.add(reporte);
        panel.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel crearPanelProducto() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BLANCO);
        panel.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(GRIS_BORDE, 1), "Libro o material academico"),
                new EmptyBorder(8, 10, 10, 10)));

        txtId = crearCampoTexto();
        txtTitulo = crearCampoTexto();
        txtAutor = crearCampoTexto();
        txtEditorial = crearCampoTexto();
        txtAreaAcademica = crearCampoTexto();
        txtValorReferencia = crearCampoTexto();
        txtCantidadDisponible = crearCampoTexto();

        agregarFilaFormulario(panel, "ID", txtId, 0);
        agregarFilaFormulario(panel, "Titulo", txtTitulo, 1);
        agregarFilaFormulario(panel, "Autor", txtAutor, 2);
        agregarFilaFormulario(panel, "Editorial", txtEditorial, 3);
        agregarFilaFormulario(panel, "Area academica", txtAreaAcademica, 4);
        agregarFilaFormulario(panel, "Valor ref.", txtValorReferencia, 5);
        agregarFilaFormulario(panel, "Disponibles", txtCantidadDisponible, 6);

        JButton btnRegistrar = crearBoton("Registrar libro", VERDE_UNAL, BLANCO);
        JButton btnActualizar = crearBoton("Actualizar", NEGRO, BLANCO);
        JButton btnLimpiar = crearBoton("Limpiar", GRIS_CLARO, NEGRO);
        btnEliminar = crearBoton("Eliminar", ROJO_UNAL, BLANCO);

        btnRegistrar.addActionListener(evento -> registrarProducto());
        btnActualizar.addActionListener(evento -> actualizarProducto());
        btnLimpiar.addActionListener(evento -> limpiarFormulario());
        btnEliminar.addActionListener(evento -> eliminarProducto());

        if (usuario == null || !usuario.esAdministrador()) {
            btnEliminar.setEnabled(false);
            btnEliminar.setToolTipText("Solo disponible para administradores");
        }

        JPanel botones = new JPanel(new GridLayout(2, 2, 8, 8));
        botones.setBackground(BLANCO);
        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnLimpiar);
        botones.add(btnEliminar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 0, 0);
        gbc.weightx = 1;
        panel.add(botones, gbc);

        return panel;
    }

    private JPanel crearPanelStock() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BLANCO);
        panel.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(GRIS_BORDE, 1), "Movimientos de stock"),
                new EmptyBorder(8, 10, 10, 10)));

        txtStockId = crearCampoTexto();
        txtStockUnidades = crearCampoTexto();

        agregarFilaFormulario(panel, "ID libro", txtStockId, 0);
        agregarFilaFormulario(panel, "Ejemplares", txtStockUnidades, 1);

        JButton btnEntrada = crearBoton("Entrada", VERDE_UNAL, BLANCO);
        JButton btnSalida = crearBoton("Prestamo / salida", ROJO_UNAL, BLANCO);

        btnEntrada.addActionListener(evento -> registrarEntradaStock());
        btnSalida.addActionListener(evento -> registrarSalidaStock());

        JPanel botones = new JPanel(new GridLayout(1, 2, 8, 0));
        botones.setBackground(BLANCO);
        botones.add(btnEntrada);
        botones.add(btnSalida);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 0, 0);
        gbc.weightx = 1;
        panel.add(botones, gbc);

        return panel;
    }

    private JPanel crearPanelReporte() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BLANCO);
        panel.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(GRIS_BORDE, 1), "Reporte de bajo stock"),
                new EmptyBorder(8, 10, 10, 10)));

        txtLimiteStock = crearCampoTexto();
        txtLimiteStock.setText("10");
        agregarFilaFormulario(panel, "Limite", txtLimiteStock, 0);

        JButton btnReporte = crearBoton("Generar reporte", NEGRO, BLANCO);
        btnReporte.addActionListener(evento -> mostrarReporteBajoStock());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 0, 0);
        gbc.weightx = 1;
        panel.add(btnReporte, gbc);

        return panel;
    }

    private JPanel crearBarraEstado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BLANCO);
        panel.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, GRIS_BORDE),
                new EmptyBorder(8, 14, 8, 14)));

        lblEstado = crearEtiquetaEstado();

        JLabel marca = new JLabel("UNAL 1867");
        marca.setFont(new Font("Segoe UI", Font.BOLD, 12));
        marca.setForeground(VERDE_UNAL);

        panel.add(lblEstado, BorderLayout.CENTER);
        panel.add(marca, BorderLayout.EAST);
        return panel;
    }

    private JLabel crearEtiquetaEstado() {
        JLabel label = new JLabel("Listo.");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(GRIS_TEXTO);
        return label;
    }

    private void registrarProducto() {
        try {
            Producto producto = leerProductoDesdeFormulario();

            if (inventario.buscarPorId(producto.getId()) != null) {
                mostrarEstado("Error: el ID ingresado ya existe en el inventario.", true);
                txtId.requestFocus();
                return;
            }

            inventario.agregarProducto(producto);
            actualizarVistaCompleta();
            seleccionarFilaPorId(producto.getId());
            mostrarEstado("Libro registrado correctamente.", false);
        } catch (IllegalArgumentException error) {
            mostrarEstado(error.getMessage(), true);
        }
    }

    private void actualizarProducto() {
        try {
            Producto producto = leerProductoDesdeFormulario();

            if (inventario.buscarPorId(producto.getId()) == null) {
                mostrarEstado("Error: el libro buscado no existe en el inventario.", true);
                txtId.requestFocus();
                return;
            }

            inventario.actualizarProducto(
                    producto.getId(),
                    producto.getTitulo(),
                    producto.getAutor(),
                    producto.getEditorial(),
                    producto.getAreaAcademica(),
                    producto.getValorReferencia(),
                    producto.getCantidadDisponible());

            actualizarVistaCompleta();
            seleccionarFilaPorId(producto.getId());
            mostrarEstado("Libro actualizado correctamente.", false);
        } catch (IllegalArgumentException error) {
            mostrarEstado(error.getMessage(), true);
        }
    }

    private void eliminarProducto() {
        if (usuario == null || !usuario.esAdministrador()) {
            mostrarEstado("Error: solo un administrador puede eliminar libros.", true);
            return;
        }

        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            mostrarEstado("Error: ingrese el ID del libro a eliminar.", true);
            txtId.requestFocus();
            return;
        }

        Producto producto = inventario.buscarPorId(id);
        if (producto == null) {
            mostrarEstado("Error: el libro buscado no existe en el inventario.", true);
            return;
        }

        boolean confirmado = confirmarAccion(
                "Confirmar eliminacion",
                "Se eliminara el libro:\n\n" + producto.mostrarInformacion());

        if (!confirmado) {
            mostrarEstado("Eliminacion cancelada.", false);
            return;
        }

        inventario.eliminarProducto(id);
        limpiarFormulario();
        actualizarVistaCompleta();
        mostrarEstado("Libro eliminado correctamente.", false);
    }

    private void consultarProductos() {
        String consulta = txtBusqueda.getText().trim();
        if (consulta.isEmpty()) {
            mostrarEstado("Error: ingrese un dato de busqueda.", true);
            txtBusqueda.requestFocus();
            return;
        }

        List<Producto> resultados = new ArrayList<>();

        switch (cmbBusqueda.getSelectedIndex()) {
            case 0:
                Producto producto = inventario.buscarPorId(consulta);
                if (producto != null) {
                    resultados.add(producto);
                }
                break;
            case 1:
                resultados = inventario.buscarPorTitulo(consulta);
                break;
            case 2:
                resultados = inventario.buscarPorAutor(consulta);
                break;
            case 3:
                resultados = inventario.buscarPorAreaAcademica(consulta);
                break;
            default:
                resultados = inventario.buscarPorTitulo(consulta);
                break;
        }

        if (resultados.isEmpty()) {
            actualizarTabla(new ArrayList<Producto>());
            limpiarCamposFormulario();
            mostrarEstado("Error: no se encontraron libros con ese criterio.", true);
            return;
        }

        actualizarTabla(resultados);

        if (resultados.size() == 1) {
            seleccionarFilaPorId(resultados.get(0).getId());
        } else {
            tablaProductos.clearSelection();
            limpiarCamposFormulario();
        }

        mostrarEstado("Consulta finalizada: " + resultados.size() + " libro(s) encontrado(s).", false);
    }

    private void registrarEntradaStock() {
        try {
            String id = leerTextoObligatorio(txtStockId, "ID");
            int unidades = leerEnteroPositivo(txtStockUnidades, "Unidades");

            if (inventario.registrarEntradaStock(id, unidades)) {
                actualizarVistaCompleta();
                seleccionarFilaPorId(id);
                txtStockUnidades.setText("");
                mostrarEstado("Entrada de stock registrada correctamente.", false);
            } else {
                mostrarEstado("Error: el libro buscado no existe en el inventario.", true);
            }
        } catch (IllegalArgumentException error) {
            mostrarEstado(error.getMessage(), true);
        }
    }

    private void registrarSalidaStock() {
        try {
            String id = leerTextoObligatorio(txtStockId, "ID");
            int unidades = leerEnteroPositivo(txtStockUnidades, "Unidades");
            Producto producto = inventario.buscarPorId(id);

            if (producto == null) {
                mostrarEstado("Error: el libro buscado no existe en el inventario.", true);
                return;
            }

            if (inventario.registrarSalidaStock(id, unidades)) {
                actualizarVistaCompleta();
                seleccionarFilaPorId(id);
                txtStockUnidades.setText("");
                mostrarEstado("Salida de stock registrada correctamente.", false);
            } else {
                mostrarEstado("Stock insuficiente para registrar la salida.", true);
            }
        } catch (IllegalArgumentException error) {
            mostrarEstado(error.getMessage(), true);
        }
    }

    private void mostrarReporteBajoStock() {
        try {
            int limite = leerEnteroNoNegativo(txtLimiteStock, "Limite");
            List<Producto> productos = inventario.obtenerProductosBajoStock(limite);

            actualizarTabla(productos);

            if (productos.isEmpty()) {
                mostrarEstado("El inventario se encuentra excelente.", false);
            } else {
                mostrarEstado("Reporte generado con " + productos.size() + " libro(s).", false);
            }
        } catch (IllegalArgumentException error) {
            mostrarEstado(error.getMessage(), true);
        }
    }

    private Producto leerProductoDesdeFormulario() {
        String id = leerTextoObligatorio(txtId, "ID");
        String titulo = leerTextoObligatorio(txtTitulo, "Titulo");
        String autor = leerTextoObligatorio(txtAutor, "Autor");
        String editorial = leerTextoObligatorio(txtEditorial, "Editorial");
        String areaAcademica = leerTextoObligatorio(txtAreaAcademica, "Area academica");
        double valorReferencia = leerDoubleNoNegativo(txtValorReferencia, "Valor de referencia");
        int cantidadDisponible = leerEnteroNoNegativo(txtCantidadDisponible, "Disponibles");

        return new Producto(
                id,
                titulo,
                autor,
                editorial,
                areaAcademica,
                valorReferencia,
                cantidadDisponible);
    }

    private String leerTextoObligatorio(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim();

        if (texto.isEmpty()) {
            campo.requestFocus();
            throw new IllegalArgumentException("Error: " + nombreCampo + " es obligatorio.");
        }

        return texto;
    }

    private int leerEnteroNoNegativo(JTextField campo, String nombreCampo) {
        int numero = leerEntero(campo, nombreCampo);

        if (numero < 0) {
            campo.requestFocus();
            throw new IllegalArgumentException("Error: " + nombreCampo + " no puede ser negativo.");
        }

        return numero;
    }

    private int leerEnteroPositivo(JTextField campo, String nombreCampo) {
        int numero = leerEntero(campo, nombreCampo);

        if (numero <= 0) {
            campo.requestFocus();
            throw new IllegalArgumentException("Error: " + nombreCampo + " debe ser mayor a cero.");
        }

        return numero;
    }

    private int leerEntero(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim();

        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException error) {
            campo.requestFocus();
            throw new IllegalArgumentException("Error: " + nombreCampo + " debe ser un numero entero valido.");
        }
    }

    private double leerDoubleNoNegativo(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim().replace(',', '.');

        try {
            double numero = Double.parseDouble(texto);

            if (numero < 0) {
                campo.requestFocus();
                throw new IllegalArgumentException("Error: " + nombreCampo + " no puede ser negativo.");
            }

            return numero;
        } catch (NumberFormatException error) {
            campo.requestFocus();
            throw new IllegalArgumentException("Error: " + nombreCampo + " debe ser un numero valido.");
        }
    }

    private void cargarProductoSeleccionado() {
        int filaVista = tablaProductos.getSelectedRow();

        if (filaVista < 0) {
            return;
        }

        int filaModelo = tablaProductos.convertRowIndexToModel(filaVista);
        Producto producto = modeloTabla.getProducto(filaModelo);

        txtId.setText(producto.getId());
        txtTitulo.setText(producto.getTitulo());
        txtAutor.setText(producto.getAutor());
        txtEditorial.setText(producto.getEditorial());
        txtAreaAcademica.setText(producto.getAreaAcademica());
        txtValorReferencia.setText(String.format(Locale.US, "%.2f", producto.getValorReferencia()));
        txtCantidadDisponible.setText(String.valueOf(producto.getCantidadDisponible()));
        txtStockId.setText(producto.getId());
    }

    private void limpiarFormulario() {
        limpiarCamposFormulario();
        txtId.requestFocus();
        mostrarEstado("Formulario limpio.", false);
    }

    private void limpiarCamposFormulario() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtAreaAcademica.setText("");
        txtValorReferencia.setText("");
        txtCantidadDisponible.setText("");
        txtStockId.setText("");
        txtStockUnidades.setText("");

        if (tablaProductos != null) {
            tablaProductos.clearSelection();
        }
    }

    private void limpiarSeleccionSinEstado() {
        limpiarCamposFormulario();
    }

    private void actualizarVistaCompleta() {
        actualizarTabla(inventario.listarProductos());
    }

    private void actualizarTabla(List<Producto> productos) {
        if (modeloTabla != null) {
            modeloTabla.setProductos(productos);
        }
    }

    private void seleccionarFilaPorId(String id) {
        if (tablaProductos == null || modeloTabla == null) {
            return;
        }

        for (int filaModelo = 0; filaModelo < modeloTabla.getRowCount(); filaModelo++) {
            Producto producto = modeloTabla.getProducto(filaModelo);

            if (producto.getId().equalsIgnoreCase(id)) {
                int filaVista = tablaProductos.convertRowIndexToView(filaModelo);
                if (filaVista >= 0) {
                    tablaProductos.getSelectionModel().setSelectionInterval(filaVista, filaVista);
                    tablaProductos.scrollRectToVisible(tablaProductos.getCellRect(filaVista, 0, true));
                }
                return;
            }
        }
    }

    private boolean confirmarAccion(String titulo, String mensaje) {
        final boolean[] confirmado = new boolean[] {false};

        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(BLANCO);
        panel.setBorder(new CompoundBorder(
                new LineBorder(NEGRO, 1),
                new EmptyBorder(18, 18, 18, 18)));

        JTextArea texto = new JTextArea(mensaje);
        texto.setEditable(false);
        texto.setOpaque(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        texto.setForeground(NEGRO);
        texto.setBorder(BorderFactory.createEmptyBorder());
        texto.setRows(8);
        texto.setColumns(36);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setBackground(BLANCO);

        JButton cancelar = crearBoton("Cancelar", GRIS_CLARO, NEGRO);
        JButton confirmar = crearBoton("Eliminar", ROJO_UNAL, BLANCO);

        cancelar.addActionListener(evento -> dialogo.dispose());
        confirmar.addActionListener(evento -> {
            confirmado[0] = true;
            dialogo.dispose();
        });

        botones.add(cancelar);
        botones.add(confirmar);

        panel.add(texto, BorderLayout.CENTER);
        panel.add(new JSeparator(), BorderLayout.NORTH);
        panel.add(botones, BorderLayout.SOUTH);

        dialogo.setContentPane(panel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);

        return confirmado[0];
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(NEGRO);
        campo.setBackground(BLANCO);
        campo.setBorder(new CompoundBorder(
                new LineBorder(GRIS_BORDE, 1),
                new EmptyBorder(7, 8, 7, 8)));
        return campo;
    }

    private JComboBox<String> crearCombo(String[] opciones) {
        JComboBox<String> combo = new JComboBox<>(opciones);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setForeground(NEGRO);
        combo.setBackground(BLANCO);
        combo.setBorder(new LineBorder(GRIS_BORDE, 1));
        return combo;
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(textoColor);
        boton.setBackground(fondo);
        boton.setOpaque(true);
        boton.setFocusPainted(false);
        boton.setBorderPainted(true);
        boton.setContentAreaFilled(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(new CompoundBorder(
                new LineBorder(fondo.darker(), 1),
                new EmptyBorder(8, 12, 8, 12)));
        return boton;
    }

    private void agregarFilaFormulario(JPanel panel, String etiqueta, Component campo, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 0, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(GRIS_TEXTO);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(campo, gbc);
    }

    private void mostrarEstado(String mensaje, boolean error) {
        if (lblEstado != null) {
            lblEstado.setText(mensaje);
            lblEstado.setForeground(error ? ROJO_UNAL : VERDE_UNAL);
        }
    }

    private ImageIcon cargarLogo(int anchoMaximo, int altoMaximo) {
        File archivo = resolverArchivoLogo();

        if (!archivo.exists()) {
            return null;
        }

        ImageIcon original = new ImageIcon(archivo.getAbsolutePath());
        int anchoOriginal = original.getIconWidth();
        int altoOriginal = original.getIconHeight();

        if (anchoOriginal <= 0 || altoOriginal <= 0) {
            return null;
        }

        double escala = Math.min(
                (double) anchoMaximo / anchoOriginal,
                (double) altoMaximo / altoOriginal);
        int ancho = Math.max(1, (int) Math.round(anchoOriginal * escala));
        int alto = Math.max(1, (int) Math.round(altoOriginal * escala));

        Image imagen = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    private File resolverArchivoLogo() {
        File archivoDesdeTrabajo = buscarLogoDesde(new File(System.getProperty("user.dir")));
        if (archivoDesdeTrabajo != null) {
            return archivoDesdeTrabajo;
        }

        File archivoDesdeClases = buscarLogoDesde(obtenerDirectorioClases());
        if (archivoDesdeClases != null) {
            return archivoDesdeClases;
        }

        return new File(LOGO_PATH);
    }

    private File buscarLogoDesde(File directorioBase) {
        File directorio = directorioBase;

        for (int nivel = 0; nivel < 6 && directorio != null; nivel++) {
            File archivo = new File(directorio, LOGO_PATH);
            if (archivo.exists()) {
                return archivo;
            }

            directorio = directorio.getParentFile();
        }

        return null;
    }

    private File obtenerDirectorioClases() {
        try {
            File ubicacion = new File(InventarioGUI.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());
            return ubicacion.isFile() ? ubicacion.getParentFile() : ubicacion;
        } catch (Exception error) {
            return null;
        }
    }

    @SuppressWarnings("serial")
    private static class ModeloTablaProductos extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private static final String[] COLUMNAS = {
            "ID", "Titulo", "Autor", "Area academica", "Editorial", "Valor ref.", "Disponibles"
        };

        private List<Producto> productos = new ArrayList<>();

        public void setProductos(List<Producto> productos) {
            this.productos = new ArrayList<>(productos);
            fireTableDataChanged();
        }

        public Producto getProducto(int fila) {
            return productos.get(fila);
        }

        @Override
        public int getRowCount() {
            return productos.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNAS.length;
        }

        @Override
        public String getColumnName(int columna) {
            return COLUMNAS[columna];
        }

        @Override
        public Object getValueAt(int fila, int columna) {
            Producto producto = productos.get(fila);

            switch (columna) {
                case 0:
                    return producto.getId();
                case 1:
                    return producto.getTitulo();
                case 2:
                    return producto.getAutor();
                case 3:
                    return producto.getAreaAcademica();
                case 4:
                    return producto.getEditorial();
                case 5:
                    return String.format(Locale.US, "$%,.2f", producto.getValorReferencia());
                case 6:
                    return Integer.valueOf(producto.getCantidadDisponible());
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columna) {
            if (columna == 6) {
                return Integer.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    }
}
