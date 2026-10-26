package ec.edu.monster.climov.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controlador.ControladorConversion;
import ec.edu.monster.climov.modelo.ServicioRest;

/**
 * Vista: inicio de sesión.
 *
 * @author Grupo 1 - NRC 36841
 */
public class LoginActivity extends AppCompatActivity {

    private static final String PREFS = "conversor";
    private final ControladorConversion controlador = new ControladorConversion();
    private EditText txtUsuario, txtClave, txtServidor;
    private Button btnIngresar;
    private ProgressBar progreso;
    private TextView lblMensaje;
    private LinearLayout tarjeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Pantalla.ajustarTeclado(this);
        ImageView monster = findViewById(R.id.imgMonster);
        monster.setClipToOutline(true);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtClave = findViewById(R.id.txtClave);
        txtServidor = findViewById(R.id.txtServidor);
        btnIngresar = findViewById(R.id.btnIngresar);
        progreso = findViewById(R.id.progreso);
        lblMensaje = findViewById(R.id.lblMensaje);
        tarjeta = findViewById(R.id.tarjetaLogin);

        SharedPreferences p = getSharedPreferences(PREFS, MODE_PRIVATE);
        txtServidor.setText(p.getString("servidor", ServicioRest.URL_POR_DEFECTO));
        txtUsuario.setText(p.getString("usuario", ""));

        Animaciones.flotar(monster);
        Animaciones.aparecer(tarjeta, 150);
        btnIngresar.setOnClickListener(v -> ingresar());

        String aviso = getIntent().getStringExtra("aviso");
        if (aviso != null) {
            mensaje(aviso, Color.parseColor("#22C55E"));
        }
    }

    private void ingresar() {
        String usuario = txtUsuario.getText().toString().trim();
        String clave = txtClave.getText().toString();
        String servidor = txtServidor.getText().toString().trim();
        if (usuario.isEmpty() || clave.isEmpty()) {
            error("Ingrese usuario y clave");
            return;
        }
        cargando(true);
        controlador.iniciarSesion(servidor, usuario, clave, mensaje -> {
            cargando(false);
            getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                    .putString("servidor", servidor).putString("usuario", usuario).apply();
            txtClave.setText("");
            Intent i = new Intent(this, ConversionActivity.class);
            i.putExtra("bienvenida", mensaje);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, e -> {
            cargando(false);
            error(e.getMessage());
        });
    }

    private void cargando(boolean si) {
        btnIngresar.setEnabled(!si);
        btnIngresar.setText(si ? "Validando…" : "Ingresar");
        progreso.setVisibility(si ? View.VISIBLE : View.GONE);
        if (si) {
            lblMensaje.setText("");
        }
    }

    private void error(String m) {
        mensaje("✖ " + m, Color.parseColor("#F43F5E"));
        Animaciones.sacudir(tarjeta);
        txtClave.setText("");
    }

    private void mensaje(String m, int color) {
        lblMensaje.setTextColor(color);
        lblMensaje.setText(m);
    }
}
