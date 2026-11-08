package model;

/**
 * Representa a un usuario con privilegios de gestión.
 * Hereda de la clase base Usuario y añade el teléfono de guardia.
 */
public class Administrador extends Usuario {
    private String telefono_guardia;

    /**
     * Constructor para recuperar un Administrador de la BD (con ID conocido).
     */
    public Administrador(int idUsuario, String correo_electronico, String contrasena, String nombre, String fecha_de_nacimiento, String telefono_guardia) {
        // Pasamos "Administrador" directamente al padre para no tener que escribirlo cada vez
        super(idUsuario, correo_electronico, contrasena, nombre, fecha_de_nacimiento, "Administrador");
        this.telefono_guardia = telefono_guardia;
    }

    /**
     * Constructor para crear un Administrador nuevo (el ID lo generará MySQL).
     */
    public Administrador(String correo_electronico, String contrasena, String nombre, String fecha_de_nacimiento, String telefono_guardia) {
        super(correo_electronico, contrasena, nombre, fecha_de_nacimiento, "Administrador");
        this.telefono_guardia = telefono_guardia;
    }

    // --- Getters y Setters ---

    public String getTelefono_guardia() {
        return telefono_guardia;
    }

    public void setTelefono_guardia(String telefono_guardia) {
        this.telefono_guardia = telefono_guardia;
    }

    @Override
    public String toString() {
        return super.toString() + " | Telf. Guardia: " + telefono_guardia;
    }
}