package ec.edu.monster.vista;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 * Vista de inicio de sesión del cliente de escritorio.
 *
 * @author Grupo 1 - NRC 36841
 */
public class VentanaLogin extends JFrame {

    private final JTextField txtUsuario = new JTextField(18);
    private final JPasswordField txtClave = new JPasswordField(18);
    private final JTextField txtServidor = new JTextField(18);
    private final JButton btnIngresar = Componentes.boton("Ingresar", Estilo.AZUL);
    private final JLabel lblMensaje = Componentes.etiqueta(" ", Estilo.PEQUENA, Estilo.ROJO);
    private final Componentes.Tarjeta formulario = new Componentes.Tarjeta(new GridBagLayout());
    private final Timer animacion;

    public VentanaLogin(String urlServidor) {
        super("Conversión de Unidades Monster - Iniciar sesión");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(860, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(Estilo.monster(64).getImage());

        // Lado izquierdo: Monster y título
        JPanel izquierdo = new Componentes.Degradado(new GridBagLayout(), new Color(0x1D3561), Estilo.FONDO);
        JLabel monster = new JLabel(Estilo.monster(190));
        animacion = Componentes.flotar(monster);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        izquierdo.add(monster, g);
        g.gridy = 1;
        g.insets = new Insets(14, 0, 0, 0);
        izquierdo.add(Componentes.etiqueta("Conversión de Unidades", Estilo.TITULO, Estilo.TEXTO), g);
        g.gridy = 2;
        g.insets = new Insets(4, 0, 0, 0);
        izquierdo.add(Componentes.etiqueta("Cliente ESCRITORIO · RESTful Java", Estilo.NORMAL, Estilo.CIAN), g);
        g.gridy = 3;
        izquierdo.add(Componentes.etiqueta("Grupo 1 · NRC 36841 · ESPE", Estilo.PEQUENA, Estilo.TENUE), g);

        // Lado derecho: formulario
        JPanel derecho = new JPanel(new GridBagLayout());
        derecho.setBackground(Estilo.FONDO);
        txtServidor.setText(urlServidor);
        txtUsuario.putClientProperty("JTextField.placeholderText", "Usuario");
        txtClave.putClientProperty("JTextField.placeholderText", "Clave");
        txtClave.putClientProperty("JPasswordField.showRevealButton", true);
        for (var c : new javax.swing.JComponent[]{txtUsuario, txtClave, txtServidor}) {
            c.setFont(Estilo.NORMAL);
        }
        txtServidor.setFont(Estilo.PEQUENA);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.insets = new Insets(4, 0, 4, 0);
        f.gridy = 0;
        formulario.add(Componentes.etiqueta("Iniciar sesión", Estilo.SUBTITULO, Estilo.TEXTO), f);
        f.gridy = 1;
        formulario.add(Componentes.etiqueta("Usuario", Estilo.PEQUENA, Estilo.TENUE), f);
        f.gridy = 2;
        formulario.add(txtUsuario, f);
        f.gridy = 3;
        formulario.add(Componentes.etiqueta("Clave", Estilo.PEQUENA, Estilo.TENUE), f);
        f.gridy = 4;
        formulario.add(txtClave, f);
        f.gridy = 5;
        f.insets = new Insets(14, 0, 4, 0);
        formulario.add(btnIngresar, f);
        f.gridy = 6;
        f.insets = new Insets(4, 0, 4, 0);
        formulario.add(lblMensaje, f);
        f.gridy = 7;
        f.insets = new Insets(10, 0, 2, 0);
        formulario.add(Componentes.etiqueta("Servidor", Estilo.PEQUENA, Estilo.TENUE), f);
        f.gridy = 8;
        f.insets = new Insets(0, 0, 0, 0);
        formulario.add(txtServidor, f);
        derecho.add(formulario);

        JPanel raiz = new JPanel(new java.awt.GridLayout(1, 2));
        raiz.add(izquierdo);
        raiz.add(derecho);
        setContentPane(raiz);
        getRootPane().setDefaultButton(btnIngresar);
        raiz.setBorder(BorderFactory.createEmptyBorder());
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getClave() {
        return new String(txtClave.getPassword());
    }

    public String getServidor() {
        return txtServidor.getText().trim();
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public void cargando(boolean si) {
        btnIngresar.setEnabled(!si);
        btnIngresar.setText(si ? "Validando…" : "Ingresar");
        if (si) {
            mensaje(" ", Estilo.TENUE);
        }
    }

    public void error(String m) {
        mensaje(m, Estilo.ROJO);
        formulario.setColorBorde(Estilo.ROJO);
        Componentes.sacudir(formulario);
        txtClave.setText("");
        txtClave.requestFocus();
    }

    public void mensaje(String m, Color c) {
        lblMensaje.setForeground(c);
        lblMensaje.setText(m);
        formulario.setColorBorde(Estilo.BORDE);
    }

    public void limpiar() {
        txtClave.setText("");
        mensaje(" ", Estilo.TENUE);
    }

}
