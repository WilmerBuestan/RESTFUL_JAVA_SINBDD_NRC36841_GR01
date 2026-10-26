package ec.edu.monster.servicio;

import ec.edu.monster.modelo.EventoServidor;
import ec.edu.monster.modelo.RegistroConversion;
import ec.edu.monster.modelo.ResultadoConversion;
import ec.edu.monster.modelo.Sesion;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Historial de conversiones por sesión y registro de eventos para el panel de
 * monitoreo. Todo en memoria (sin base de datos). Patrón Singleton.
 *
 * @author Grupo 1 - NRC 36841
 */
public final class ServicioHistorial {

    private static final ServicioHistorial INSTANCIA = new ServicioHistorial();
    private static final int MAX_EVENTOS = 40;
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Map<String, List<RegistroConversion>> historial = new ConcurrentHashMap<>();
    private final Deque<EventoServidor> eventos = new ConcurrentLinkedDeque<>();
    private final Map<String, AtomicLong> porMagnitud = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> porCliente = new ConcurrentHashMap<>();
    private final AtomicLong totalConversiones = new AtomicLong();
    private final AtomicLong totalPeticiones = new AtomicLong();
    private final LocalDateTime inicio = LocalDateTime.now();

    private ServicioHistorial() {
    }

    public static ServicioHistorial getInstancia() {
        return INSTANCIA;
    }

    public void registrar(Sesion sesion, ResultadoConversion r) {
        RegistroConversion reg = new RegistroConversion();
        reg.setFecha(LocalDateTime.now().format(FECHA));
        reg.setUsuario(sesion.getUsuario());
        reg.setCliente(sesion.getCliente());
        reg.setConversion(r);
        historial.computeIfAbsent(sesion.getToken(), k -> Collections.synchronizedList(new ArrayList<>())).add(reg);
        totalConversiones.incrementAndGet();
        porMagnitud.computeIfAbsent(r.getMagnitud(), k -> new AtomicLong()).incrementAndGet();
    }

    /** Historial de la sesión, del más reciente al más antiguo. */
    public List<RegistroConversion> listar(String token) {
        List<RegistroConversion> lista = historial.get(token);
        if (lista == null) {
            return new ArrayList<>();
        }
        List<RegistroConversion> copia;
        synchronized (lista) {
            copia = new ArrayList<>(lista);
        }
        Collections.reverse(copia);
        return copia;
    }

    public void eliminarSesion(String token) {
        historial.remove(token);
    }

    public void registrarEvento(String tipo, String cliente, String detalle, boolean exito) {
        EventoServidor e = new EventoServidor();
        e.setHora(LocalDateTime.now().format(HORA));
        e.setTipo(tipo);
        e.setCliente(ServicioAutenticacion.normalizarCliente(cliente));
        e.setDetalle(detalle);
        e.setExito(exito);
        eventos.addFirst(e);
        while (eventos.size() > MAX_EVENTOS) {
            eventos.pollLast();
        }
        totalPeticiones.incrementAndGet();
        porCliente.computeIfAbsent(e.getCliente(), k -> new AtomicLong()).incrementAndGet();
    }

    public List<EventoServidor> ultimosEventos(int n) {
        List<EventoServidor> lista = new ArrayList<>();
        Iterator<EventoServidor> it = eventos.iterator();
        while (it.hasNext() && lista.size() < n) {
            lista.add(it.next());
        }
        return lista;
    }

    public Map<String, Long> conversionesPorMagnitud() {
        Map<String, Long> m = new LinkedHashMap<>();
        for (String k : List.of("LONGITUD", "MASA", "TEMPERATURA", "TIEMPO")) {
            AtomicLong v = porMagnitud.get(k);
            m.put(k, v == null ? 0L : v.get());
        }
        return m;
    }

    public Map<String, Long> peticionesPorCliente() {
        Map<String, Long> m = new LinkedHashMap<>();
        porCliente.forEach((k, v) -> m.put(k, v.get()));
        return m;
    }

    public long getTotalConversiones() {
        return totalConversiones.get();
    }

    public long getTotalPeticiones() {
        return totalPeticiones.get();
    }

    public LocalDateTime getInicio() {
        return inicio;
    }
}
