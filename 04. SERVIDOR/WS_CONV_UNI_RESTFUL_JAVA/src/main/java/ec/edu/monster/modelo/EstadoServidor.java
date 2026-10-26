package ec.edu.monster.modelo;

import java.util.List;
import java.util.Map;

/**
 * Estado del servidor para el panel de monitoreo.
 *
 * @author Grupo 1 - NRC 36841
 */
public class EstadoServidor {

    private String estado;
    private String inicio;
    private long segundosActivo;
    private int sesionesActivas;
    private long totalConversiones;
    private long totalPeticiones;
    private Map<String, Long> conversionesPorMagnitud;
    private Map<String, Long> peticionesPorCliente;
    private List<EventoServidor> ultimosEventos;

    public EstadoServidor() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public long getSegundosActivo() {
        return segundosActivo;
    }

    public void setSegundosActivo(long segundosActivo) {
        this.segundosActivo = segundosActivo;
    }

    public int getSesionesActivas() {
        return sesionesActivas;
    }

    public void setSesionesActivas(int sesionesActivas) {
        this.sesionesActivas = sesionesActivas;
    }

    public long getTotalConversiones() {
        return totalConversiones;
    }

    public void setTotalConversiones(long totalConversiones) {
        this.totalConversiones = totalConversiones;
    }

    public long getTotalPeticiones() {
        return totalPeticiones;
    }

    public void setTotalPeticiones(long totalPeticiones) {
        this.totalPeticiones = totalPeticiones;
    }

    public Map<String, Long> getConversionesPorMagnitud() {
        return conversionesPorMagnitud;
    }

    public void setConversionesPorMagnitud(Map<String, Long> conversionesPorMagnitud) {
        this.conversionesPorMagnitud = conversionesPorMagnitud;
    }

    public Map<String, Long> getPeticionesPorCliente() {
        return peticionesPorCliente;
    }

    public void setPeticionesPorCliente(Map<String, Long> peticionesPorCliente) {
        this.peticionesPorCliente = peticionesPorCliente;
    }

    public List<EventoServidor> getUltimosEventos() {
        return ultimosEventos;
    }

    public void setUltimosEventos(List<EventoServidor> ultimosEventos) {
        this.ultimosEventos = ultimosEventos;
    }

}
