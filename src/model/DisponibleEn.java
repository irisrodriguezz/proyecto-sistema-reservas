package model;

/* NOTA: He creado el modelo para mantener la integridad de la arquitectura MVC,
aunque sepamos que actualmente el DAO gestiona la relación mediante tipos primitivos
por eficiencia, la clase está lista para escalar si la relación N:M necesitase atributos adicionales.
 */

/**
 * Clase que representa la relación de disponibilidad entre un Recurso y un Horario.
 * Actúa como modelo para la tabla intermedia DISPONIBLEEN.
 */
public class DisponibleEn {
    private int idRecurso;
    private int idHorario;

    /**
     * Constructor vacío por defecto (Buena práctica POJO).
     */
    public DisponibleEn() {}

    /**
     * Constructor completo para representar la unión.
     * @param idRecurso Identificador del recurso.
     * @param idHorario Identificador del horario asociado.
     */
    public DisponibleEn(int idRecurso, int idHorario) {
        this.idRecurso = idRecurso;
        this.idHorario = idHorario;
    }

    // --- Getters y Setters ---

    public int getIdRecurso() { return idRecurso; }
    public void setIdRecurso(int idRecurso) { this.idRecurso = idRecurso; }

    public int getIdHorario() { return idHorario; }
    public void setIdHorario(int idHorario) { this.idHorario = idHorario; }

    @Override
    public String toString() {
        return "Disponibilidad [Recurso ID=" + idRecurso + ", Horario ID=" + idHorario + "]";
    }
}