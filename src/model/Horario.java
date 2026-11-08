package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una franja horaria de disponibilidad en el sistema.
 */
public class Horario {
    private int idHorario;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    /**
     * Constructor vacío por defecto
     */
    public Horario() {}

    /**
     * Constructor para crear un horario nuevo (viene bien si manejamos el ID manualmente
     * antes de insertar o para recuperarlo de la BD)
     */
    public Horario(int idHorario, String diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.idHorario = idHorario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * Constructor sin ID (útil para la lógica de negocio antes de la persistencia).
     */
    public Horario(String diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // --- Getters y Setters ---
    
    public int getIdHorario() { return idHorario; }
    public void setIdHorario(int idHorario) { this.idHorario = idHorario; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    @Override
    public String toString() {
        // Formato limpio para la consola
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");

        return String.format("ID: %-4d | Día: %-10s | %s - %s",
                idHorario,
                diaSemana,
                horaInicio.format(formato),
                horaFin.format(formato));
    }
}