package model;

/**
 * Clase que representa a un usuario en el sistema.
 * Sirve como superclase para Administrador y UsuarioNormal.
 */
public class Usuario {
    private int idUsuario;
    private String correo_electronico;
    private String contrasena;
    private String nombre;
    private String fecha_de_nacimiento;
    private String tipo_usuario;

    /**
     * Constructor vacío por defecto
     */
    public Usuario() {}

    /**
     * Constructor para recuperar un usuario de la BD (con ID)
     */
    public Usuario(int idUsuario, String correo_electronico, String contrasena, String nombre, String fecha_de_nacimiento, String tipo_usuario) {
        this.idUsuario = idUsuario;
        this.correo_electronico = correo_electronico;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.fecha_de_nacimiento = fecha_de_nacimiento;
        this.tipo_usuario = tipo_usuario;
    }

    /**
     * Constructor para crear un nuevo usuario (sin ID, gestionado por MySQL)
     */
    public Usuario(String correo_electronico, String contrasena, String nombre, String fecha_de_nacimiento, String tipo_usuario) {
        this.correo_electronico = correo_electronico;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.fecha_de_nacimiento = fecha_de_nacimiento;
        this.tipo_usuario = tipo_usuario;
    }

    // --- Getters y Setters ---

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getCorreo_electronico() { return correo_electronico; }
    public void setCorreo_electronico(String correo_electronico) { this.correo_electronico = correo_electronico; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFecha_de_nacimiento() { return fecha_de_nacimiento; }
    public void setFecha_de_nacimiento(String fecha_de_nacimiento) { this.fecha_de_nacimiento = fecha_de_nacimiento; }

    public String getTipo_usuario() { return tipo_usuario; }
    public void setTipo_usuario(String tipo_usuario) { this.tipo_usuario = tipo_usuario; }

    @Override
    public String toString() {
        // Formato tabular para visualización clara en consola
        return String.format("ID: %-4d | Email: %-25s | Nom: %-15s | F.Nac: %-12s | Rol: %-12s",
                idUsuario, correo_electronico, nombre, fecha_de_nacimiento, tipo_usuario);
    }
}