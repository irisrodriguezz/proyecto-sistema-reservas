package model;

/**
 * Representa un recurso del sistema (sala, material, equipo, etc.)
 * que puede ser reservado por los usuarios.
 */
public class Recurso {
    private int idRecurso;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private int capacidad;

    /**
     * Constructor vacío por defecto
     */
    public Recurso() {}

    /**
     * Constructor para recuperar un recurso de la BD (con ID)
     */
    public Recurso(int idRecurso, String nombre, String descripcion, String ubicacion, int capacidad) {
        this.idRecurso = idRecurso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
    }

    /**
     * Constructor para crear un nuevo recurso (sin ID, gestionado por AUTO_INCREMENT)
     */
    public Recurso(String nombre, String descripcion, String ubicacion, int capacidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
    }

    // --- Getters y Setters ---
    
    public int getIdRecurso() { return idRecurso; }
    public void setIdRecurso(int idRecurso) { this.idRecurso = idRecurso; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    @Override
    public String toString() {
        // Formateado para que las listas en consola se vean como una tabla real
        return String.format("ID: %-4d | %-20s | Ubic: %-15s | Cap: %-4d | Desc: %s",
                idRecurso, nombre, ubicacion, capacidad, descripcion);
    }
}