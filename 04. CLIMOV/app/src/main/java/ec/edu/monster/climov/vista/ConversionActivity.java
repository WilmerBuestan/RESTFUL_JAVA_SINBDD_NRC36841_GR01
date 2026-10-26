package ec.edu.monster.climov.vista;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controlador.ControladorConversion;
import ec.edu.monster.climov.modelo.ExcepcionCliente;
import ec.edu.monster.climov.modelo.Magnitud;
import ec.edu.monster.climov.modelo.Unidad;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista: selección de magnitud y conversión.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ConversionActivity extends AppCompatActivity {

    private final ControladorConversion controlador = new ControladorConversion();
    private final List<MaterialButton> botones = new ArrayList<>();
    private Spinner spnOrigen, spnDestino;
    private EditText txtValor;
    private MaterialButton btnConvertir;
    private LinearLayout tarjetaResultado;
    private TextView lblResultado, lblDetalle;
    private Magnitud actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (controlador.catalogo() == null) { // la app se reinició: volver al login
            irAlLogin(null);
            return;
        }
        setContentView(R.layout.activity_conversion);
        Pantalla.ajustarTeclado(this);
        findViewById(R.id.imgMini).setClipToOutline(true);
        spnOrigen = findViewById(R.id.spnOrigen);
        spnDestino = findViewById(R.id.spnDestino);
        txtValor = findViewById(R.id.txtValor);
        btnConvertir = findViewById(R.id.btnConvertir);
        tarjetaResultado = findViewById(R.id.tarjetaResultado);
        lblResultado = findViewById(R.id.lblResultado);
        lblDetalle = findViewById(R.id.lblDetalle);
        ((TextView) findViewById(R.id.lblUsuario)).setText("👤 " + controlador.usuario() + " · sesión 30 min");
        ((TextView) findViewById(R.id.lblServidor)).setText("Servidor: " + controlador.urlServidor());

        crearBotonesMagnitud();
        View btnIntercambiar = findViewById(R.id.btnIntercambiar);
        btnIntercambiar.setOnClickListener(v -> {
            Animaciones.girar(v);
            int o = spnOrigen.getSelectedItemPosition();
            spnOrigen.setSelection(spnDestino.getSelectedItemPosition());
            spnDestino.setSelection(o);
        });
        btnConvertir.setOnClickListener(v -> convertir());
        findViewById(R.id.btnHistorial).setOnClickListener(v ->
                startActivity(new Intent(this, HistorialActivity.class)));
        findViewById(R.id.btnSalir).setOnClickListener(v -> confirmarSalida());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmarSalida();
            }
        });

        String bienvenida = getIntent().getStringExtra("bienvenida");
        if (bienvenida != null) {
            Toast.makeText(this, bienvenida, Toast.LENGTH_SHORT).show();
        }
    }

    private void crearBotonesMagnitud() {
        LinearLayout panel = findViewById(R.id.panelMagnitudes);
        float dp = getResources().getDisplayMetrics().density;
        for (Magnitud m : controlador.catalogo()) {
            MaterialButton b = new MaterialButton(this);
            b.setText(Colores.icono(m.codigo) + "\n" + m.nombre);
            b.setTextSize(11);
            b.setAllCaps(false);
            b.setCornerRadius((int) (12 * dp));
            b.setInsetTop(0);
            b.setInsetBottom(0);
            b.setPadding(0, (int) (8 * dp), 0, (int) (8 * dp));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            lp.setMarginEnd((int) (6 * dp));
            b.setLayoutParams(lp);
            b.setOnClickListener(v -> seleccionar(m));
            botones.add(b);
            panel.addView(b);
        }
        if (!controlador.catalogo().isEmpty()) {
            seleccionar(controlador.catalogo().get(0));
        }
    }

    private void seleccionar(Magnitud m) {
        actual = m;
        int color = Colores.de(m.codigo);
        List<Magnitud> cat = controlador.catalogo();
        for (int i = 0; i < botones.size(); i++) {
            boolean activo = cat.get(i) == m;
            MaterialButton b = botones.get(i);
            b.setBackgroundTintList(ColorStateList.valueOf(activo ? color : Color.parseColor("#1A2B4B")));
            b.setTextColor(activo ? Color.parseColor("#06101F") : Color.parseColor("#E8EEFB"));
            b.animate().scaleX(activo ? 1.05f : 1f).scaleY(activo ? 1.05f : 1f).setDuration(200).start();
        }
        ArrayAdapter<Unidad> ad = new ArrayAdapter<>(this, R.layout.item_spinner, m.unidades);
        spnOrigen.setAdapter(ad);
        spnDestino.setAdapter(ad);
        if (m.unidades.size() > 1) {
            spnDestino.setSelection(1);
        }
        btnConvertir.setBackgroundTintList(ColorStateList.valueOf(color));
        bordeResultado(Color.parseColor("#24395F"));
        lblResultado.setTextColor(Color.parseColor("#E8EEFB"));
        lblResultado.setText("—");
        lblDetalle.setText("Unidad base: " + m.unidadBase);
    }

    private void convertir() {
        String texto = txtValor.getText().toString().trim().replace(',', '.');
        double valor;
        try {
            valor = Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            mostrarError("Ingrese un valor numérico válido (ej. 12.5)");
            return;
        }
        Unidad o = (Unidad) spnOrigen.getSelectedItem();
        Unidad d = (Unidad) spnDestino.getSelectedItem();
        btnConvertir.setEnabled(false);
        btnConvertir.setText("Convirtiendo…");
        controlador.convertir(actual, o.codigo, d.codigo, valor, r -> {
            restaurarBoton();
            int color = Colores.de(r.magnitud);
            bordeResultado(color);
            lblResultado.setTextColor(color);
            Animaciones.contar(lblResultado, r.resultado, r.simboloDestino);
            lblDetalle.setTextColor(Color.parseColor("#E8EEFB"));
            lblDetalle.setText(r.detalle);
            Animaciones.latido(tarjetaResultado);
        }, e -> {
            restaurarBoton();
            manejarError(e);
        });
    }

    private void manejarError(ExcepcionCliente e) {
        if (e.sesionExpirada()) {
            irAlLogin("Su sesión expiró. Inicie sesión nuevamente.");
        } else {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarError(String m) {
        int rojo = Color.parseColor("#F43F5E");
        bordeResultado(rojo);
        lblResultado.setTextColor(rojo);
        lblResultado.setText("✖");
        lblDetalle.setTextColor(rojo);
        lblDetalle.setText(m);
        Animaciones.sacudir(tarjetaResultado);
    }

    private void restaurarBoton() {
        btnConvertir.setEnabled(true);
        btnConvertir.setText("Convertir");
    }

    private void bordeResultado(int color) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.parseColor("#1A2B4B"));
        g.setCornerRadius(20 * getResources().getDisplayMetrics().density);
        g.setStroke((int) (2 * getResources().getDisplayMetrics().density), color);
        tarjetaResultado.setBackground(g);
    }

    private void confirmarSalida() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.monster)
                .setTitle("Cerrar sesión")
                .setMessage("¿Desea cerrar la sesión?")
                .setPositiveButton("Sí", (d, w) -> controlador.cerrarSesion(ok -> irAlLogin("Sesión cerrada correctamente")))
                .setNegativeButton("No", null)
                .show();
    }

    private void irAlLogin(String aviso) {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (aviso != null) {
            i.putExtra("aviso", aviso);
        }
        startActivity(i);
        finish();
    }
}
