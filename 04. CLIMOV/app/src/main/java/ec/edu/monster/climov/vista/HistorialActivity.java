package ec.edu.monster.climov.vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controlador.ControladorConversion;
import ec.edu.monster.climov.modelo.Registro;

/**
 * Vista: historial de conversiones de la sesión.
 *
 * @author Grupo 1 - NRC 36841
 */
public class HistorialActivity extends AppCompatActivity {

    private final ControladorConversion controlador = new ControladorConversion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Pantalla.ajustarTeclado(this);
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
        LinearLayout lista = findViewById(R.id.listaHistorial);
        View progreso = findViewById(R.id.progreso);
        TextView vacio = findViewById(R.id.lblVacio);

        controlador.historial(registros -> {
            progreso.setVisibility(View.GONE);
            if (registros.isEmpty()) {
                vacio.setText("Aún no ha realizado conversiones");
                vacio.setVisibility(View.VISIBLE);
                return;
            }
            LayoutInflater inf = LayoutInflater.from(this);
            int i = 0;
            for (Registro r : registros) {
                View item = inf.inflate(R.layout.item_historial, lista, false);
                item.findViewById(R.id.barra).setBackgroundColor(Colores.de(r.conversion.magnitud));
                ((TextView) item.findViewById(R.id.lblConversion)).setText(r.conversion.detalle);
                ((TextView) item.findViewById(R.id.lblInfo)).setText(Colores.icono(r.conversion.magnitud) + " "
                        + r.conversion.magnitud + " · " + r.fecha + " · " + r.cliente);
                lista.addView(item);
                Animaciones.aparecer(item, 70L * i++);
            }
        }, e -> {
            progreso.setVisibility(View.GONE);
            if (e.sesionExpirada()) {
                Intent in = new Intent(this, LoginActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.putExtra("aviso", "Su sesión expiró. Inicie sesión nuevamente.");
                startActivity(in);
                finish();
                return;
            }
            vacio.setText("✖ " + e.getMessage());
            vacio.setVisibility(View.VISIBLE);
        });
    }
}
