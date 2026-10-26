package ec.edu.monster.vista;

import ec.edu.monster.modelo.Magnitud;
import ec.edu.monster.modelo.Registro;
import ec.edu.monster.modelo.Unidad;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Vista principal: selección de magnitud, conversión e historial.
 *
 * @author Grupo 1 - NRC 36841
 */
public class VentanaPrincipal extends JFrame {

    private final JLabel lblUsuario = Componentes.etiqueta("", Estilo.NORMAL, Estilo.TEXTO);
    private final JButton btnCerrarSesion = Componentes.boton("Cerrar sesión", Estilo.ROJO);
    private final JPanel panelMagnitudes = new JPanel(new GridLayout(1, 4, 10, 0));
    private final List<JButton> botonesMagnitud = new ArrayList<>();
    private final JComboBox<Unidad> cmbOrigen = new JComboBox<>();
    private final JComboBox<Unidad> cmbDestino = new JComboBox<>();
    private final JButton btnIntercambiar = Componentes.boton("↔", Estilo.PANEL2);
    private final JTextField txtValor = new JTextField();
    private final JButton btnConvertir = Componentes.boton("Convertir", Estilo.AZUL);
    private final Componentes.Tarjeta tarjetaResultado = new Componentes.Tarjeta(new BorderLayout());
    private final JLabel lblResultado = Componentes.etiqueta("—", Estilo.RESULTADO, Estilo.TEXTO);
    private final JLabel lblDetalle = Componentes.etiqueta("Seleccione las unidades e ingrese un valor", Estilo.NORMAL, Estilo.TENUE);
    private final DefaultTableModel modeloHistorial = new DefaultTableModel(new String[]{"Hora", "Magnitud", "Conversión"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JButton btnHistorial = Componentes.boton("Actualizar", Estilo.CIAN);
    private final JLabel lblEstado = Componentes.etiqueta(" ", Estilo.PEQUENA, Estilo.TENUE);
    private List<Magnitud> catalogo = List.of();

    public VentanaPrincipal() {
        super("Conversión de Unidades Monster - Cliente Escritorio");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(1100, 660);
        setMinimumSize(Componentes.dim(980, 600));
        setLocationRelativeTo(null);
        setIconImage(Estilo.monster(64).getImage());

        // Cabecera
        JPanel cabecera = new Componentes.Degradado(new BorderLayout(), new Color(0x1D3561), Estilo.FONDO);
        cabecera.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        marca.setOpaque(false);
        marca.add(new JLabel(Estilo.monster(52)));
        JPanel titulos = new JPanel(new GridLayout(2, 1));
        titulos.setOpaque(false);
        titulos.add(Componentes.etiqueta("Conversión de Unidades Monster", Estilo.TITULO, Estilo.TEXTO));
        titulos.add(Componentes.etiqueta("RESTful Java · Grupo 1 · NRC 36841", Estilo.PEQUENA, Estilo.CIAN));
        marca.add(titulos);
        JPanel usuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        usuario.setOpaque(false);
        usuario.add(lblUsuario);
        usuario.add(btnCerrarSesion);
        cabecera.add(marca, BorderLayout.WEST);
        cabecera.add(usuario, BorderLayout.EAST);

        // Panel de conversión
        panelMagnitudes.setOpaque(false);
        Componentes.Tarjeta conversion = new Componentes.Tarjeta(new GridBagLayout());
        for (JComboBox<Unidad> c : List.of(cmbOrigen, cmbDestino)) {
            c.setFont(Estilo.NORMAL);
        }
        txtValor.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        txtValor.putClientProperty("JTextField.placeholderText", "Valor a convertir");
        btnIntercambiar.setForeground(Estilo.TEXTO);
        btnIntercambiar.setToolTipText("Intercambiar unidades");
        lblResultado.setHorizontalAlignment(SwingConstants.CENTER);
        lblDetalle.setHorizontalAlignment(SwingConstants.CENTER);
        tarjetaResultado.setBackground(Estilo.PANEL2);
        tarjetaResultado.add(lblResultado, BorderLayout.CENTER);
        tarjetaResultado.add(lblDetalle, BorderLayout.SOUTH);
        tarjetaResultado.setPreferredSize(Componentes.dim(400, 130));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 4, 6, 4);
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 3;
        conversion.add(panelMagnitudes, g);
        g.gridy = 1;
        g.gridwidth = 1;
        g.weightx = 1;
        conversion.add(Componentes.etiqueta("De", Estilo.PEQUENA, Estilo.TENUE), g);
        g.gridx = 2;
        conversion.add(Componentes.etiqueta("A", Estilo.PEQUENA, Estilo.TENUE), g);
        g.gridy = 2;
        g.gridx = 0;
        conversion.add(cmbOrigen, g);
        g.gridx = 1;
        g.weightx = 0;
        conversion.add(btnIntercambiar, g);
        g.gridx = 2;
        g.weightx = 1;
        conversion.add(cmbDestino, g);
        g.gridy = 3;
        g.gridx = 0;
        g.gridwidth = 3;
        conversion.add(Componentes.etiqueta("Valor", Estilo.PEQUENA, Estilo.TENUE), g);
        g.gridy = 4;
        conversion.add(txtValor, g);
        g.gridy = 5;
        g.insets = new Insets(14, 4, 6, 4);
        conversion.add(btnConvertir, g);
        g.gridy = 6;
        g.fill = GridBagConstraints.BOTH;
        g.weighty = 1;
        conversion.add(tarjetaResultado, g);

        // Panel de historial
        Componentes.Tarjeta historial = new Componentes.Tarjeta(new BorderLayout(0, 10));
        JPanel tituloHistorial = new JPanel(new BorderLayout());
        tituloHistorial.setOpaque(false);
        tituloHistorial.add(Componentes.etiqueta("Historial de la sesión", Estilo.SUBTITULO, Estilo.TEXTO), BorderLayout.WEST);
        tituloHistorial.add(btnHistorial, BorderLayout.EAST);
        JTable tabla = new JTable(modeloHistorial);
        tabla.setRowHeight(28);
        tabla.setFont(Estilo.PEQUENA);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(95);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(230);
        historial.add(tituloHistorial, BorderLayout.NORTH);
        historial.add(new JScrollPane(tabla), BorderLayout.CENTER);
        historial.setPreferredSize(Componentes.dim(430, 400));

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setBackground(Estilo.FONDO);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        cuerpo.add(conversion, BorderLayout.CENTER);
        cuerpo.add(historial, BorderLayout.EAST);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 6));
        pie.setBackground(Estilo.PANEL);
        pie.add(lblEstado);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(cabecera, BorderLayout.NORTH);
        raiz.add(cuerpo, BorderLayout.CENTER);
        raiz.add(pie, BorderLayout.SOUTH);
        setContentPane(raiz);
        getRootPane().setDefaultButton(btnConvertir);
    }

    /** Crea los botones de magnitud a partir del catálogo del servidor. */
    public void setCatalogo(List<Magnitud> catalogo, java.util.function.Consumer<Magnitud> alSeleccionar) {
        this.catalogo = catalogo;
        panelMagnitudes.removeAll();
        botonesMagnitud.clear();
        for (Magnitud m : catalogo) {
            JButton b = Componentes.boton(m.nombre(), Estilo.PANEL2);
            b.addActionListener(e -> alSeleccionar.accept(m));
            botonesMagnitud.add(b);
            panelMagnitudes.add(b);
        }
        panelMagnitudes.revalidate();
        if (!catalogo.isEmpty()) {
            botonesMagnitud.get(0).doClick();
        }
    }

    public void mostrarMagnitud(Magnitud m) {
        Color c = Estilo.colorMagnitud(m.codigo());
        for (int k = 0; k < botonesMagnitud.size(); k++) {
            JButton b = botonesMagnitud.get(k);
            boolean activo = catalogo.get(k) == m;
            b.setBackground(activo ? c : Estilo.PANEL2);
            b.setForeground(activo ? Estilo.FONDO : Estilo.TEXTO);
        }
        cmbOrigen.removeAllItems();
        cmbDestino.removeAllItems();
        for (Unidad u : m.unidades()) {
            cmbOrigen.addItem(u);
            cmbDestino.addItem(u);
        }
        if (m.unidades().size() > 1) {
            cmbDestino.setSelectedIndex(1);
        }
        btnConvertir.setBackground(c);
        tarjetaResultado.setColorBorde(c);
        lblResultado.setText("—");
        lblResultado.setForeground(Estilo.TEXTO);
        lblDetalle.setText("Convertir " + m.nombre().toLowerCase() + " · unidad base: " + m.unidadBase());
        txtValor.requestFocus();
    }

    public void intercambiar() {
        int o = cmbOrigen.getSelectedIndex();
        cmbOrigen.setSelectedIndex(cmbDestino.getSelectedIndex());
        cmbDestino.setSelectedIndex(o);
    }

    public void mostrarResultado(double resultado, String simbolo, String detalle, Color c) {
        lblResultado.setForeground(c);
        Componentes.contar(lblResultado, resultado, simbolo);
        lblDetalle.setForeground(Estilo.TEXTO);
        lblDetalle.setText(detalle);
        destello(c);
    }

    public void mostrarError(String m) {
        lblResultado.setForeground(Estilo.ROJO);
        lblResultado.setText("×");
        lblDetalle.setForeground(Estilo.ROJO);
        lblDetalle.setText(m);
        tarjetaResultado.setColorBorde(Estilo.ROJO);
        Componentes.sacudir(tarjetaResultado);
    }

    private void destello(Color c) {
        final int[] paso = {0};
        Timer t = new Timer(60, null);
        t.addActionListener(e -> {
            tarjetaResultado.setBackground(paso[0] % 2 == 0 ? c.darker().darker() : Estilo.PANEL2);
            if (++paso[0] > 3) {
                tarjetaResultado.setBackground(Estilo.PANEL2);
                t.stop();
            }
            tarjetaResultado.repaint();
        });
        t.start();
    }

    public void mostrarHistorial(List<Registro> lista) {
        modeloHistorial.setRowCount(0);
        for (Registro r : lista) {
            String hora = r.fecha().length() > 11 ? r.fecha().substring(11) : r.fecha();
            modeloHistorial.addRow(new Object[]{hora, r.conversion().magnitud(), r.conversion().detalle()});
        }
    }

    public void cargando(boolean si) {
        btnConvertir.setEnabled(!si);
        btnConvertir.setText(si ? "Convirtiendo…" : "Convertir");
    }

    public void setUsuario(String u) {
        lblUsuario.setText("Usuario: " + u);
    }

    public void setEstado(String t) {
        lblEstado.setText(t);
    }

    public Unidad getOrigen() {
        return (Unidad) cmbOrigen.getSelectedItem();
    }

    public Unidad getDestino() {
        return (Unidad) cmbDestino.getSelectedItem();
    }

    public String getValorTexto() {
        return txtValor.getText().trim().replace(',', '.');
    }

    public JButton getBtnConvertir() {
        return btnConvertir;
    }

    public JButton getBtnIntercambiar() {
        return btnIntercambiar;
    }

    public JButton getBtnHistorial() {
        return btnHistorial;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }
}
