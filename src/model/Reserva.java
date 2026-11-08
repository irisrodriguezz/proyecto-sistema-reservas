package model;

import java.time.LocalTime;

/**
 * Representa una reserva de un recurso realizada por un usuario.
 * Utiliza una clave primaria compuesta (idRecurso + idReservaLocal).
 */
public class Reserva {
    private int idRecurso;
    private int idReservaLocal;
    private int idUsuario;
    private String fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double coste;
    private int numeroPlazas;
    private String motivo;
    private String observaciones;

    /**
     * Constructor vacío por defecto
     */
    public Reserva() {}

    /**
     * Constructor completo para persistencia (recuperar de BD)
     */
    public Reserva(int idRecurso, int idReservaLocal, int idUsuario, String fecha,
                   LocalTime horaInicio, LocalTime horaFin, double coste,
                   int numeroPlazas, String motivo, String observaciones) {
        this.idRecurso = idRecurso;
        this.idReservaLocal = idReservaLocal;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.coste = coste;
        this.numeroPlazas = numeroPlazas;
        this.motivo = motivo;
        this.observaciones = observaciones;
    }

    // --- Getters y Setters ---

    public int getIdRecurso() { return idRecurso; }
    public void setIdRecurso(int idRecurso) { this.idRecurso = idRecurso; }

    public int getIdReservaLocal() { return idReservaLocal; }
    public void setIdReservaLocal(int idReservaLocal) { this.idReservaLocal = idReservaLocal; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public double getCoste() { return coste; }
    public void setCoste(double coste) { this.coste = coste; }

    public int getNumeroPlazas() { return numeroPlazas; }
    public void setNumeroPlazas(int numeroPlazas) { this.numeroPlazas = numeroPlazas; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return String.format("Rec: %-3d Loc: %-3d | Usu: %-3d | Fecha: %s | Horario: %s - %s | Coste: %.2f€ | Motivo: %s",
                idRecurso, idReservaLocal, idUsuario, fecha, horaInicio, horaFin, coste, motivo);
    }
}