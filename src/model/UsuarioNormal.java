package model;

/**
 * Representa a un usuario normal del sistema.
 * Hereda de Usuario y añade información de contacto y perfil.
 */
public class UsuarioNormal extends Usuario {
    private String direccion;
    private String telefono_movil;
    private String fotografia;

    /**
     * Constructor vacío por defecto
     */
    public UsuarioNormal() {}

    /**
     * Constructor para recuperar un Usuario Normal de la BD (con ID)
     */
    public UsuarioNormal(int id, String correo, String pass, String nombre, String fecha, String direccion, String telefono_movil, String fotografia) {
        super(id, correo, pass, nombre, fecha, "Normal");
        this.direccion = direccion;
        this.telefono_movil = telefono_movil;
        this.fotografia = fotografia;
    }

    /**
     * Constructor para crear un Usuario Normal nuevo (sin ID)
     */
    public UsuarioNormal(String correo, String pass, String nombre, String fecha, String direccion, String telefono_movil, String fotografia) {
        super(correo, pass, nombre, fecha, "Normal");
        this.direccion = direccion;
        this.telefono_movil = telefono_movil;
        this.fotografia = fotografia;
    }

    // --- Getters y Setters ---

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono_movil() { return telefono_movil; }
    public void setTelefono_movil(String telefono_movil) { this.telefono_movil = telefono_movil; }

    public String getFotografia() { return fotografia; }
    public void setFotografia(String fotografia) { this.fotografia = fotografia; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Dir: %-15s | Tel: %-10s", direccion, telefono_movil);
    }
}