package dao;

import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO especializado en la entidad Usuario.
 * Centraliza las operaciones de persistencia base para todos los perfiles de usuario.
 * Sigue el patrón de diseño con constantes SQL y acceso Singleton.
 */
public class UsuarioDAO {

    private static UsuarioDAO instance;
    private Connection connection;

    public static final String CREATE_TABLE_USUARIO = """
    CREATE TABLE IF NOT EXISTS USUARIO (
        id_usuario INT NOT NULL AUTO_INCREMENT,
        correo_electronico VARCHAR(100) NOT NULL,
        contrasena VARCHAR(100) NOT NULL,
        nombre VARCHAR(100) NOT NULL,
        fecha_de_nacimiento DATE,
        tipo_usuario ENUM('Administrador', 'Normal') NOT NULL,
        PRIMARY KEY (id_usuario),
        UNIQUE KEY (correo_electronico)
    );
    """;

    private static final String INSERT_QUERY = "INSERT INTO USUARIO (correo_electronico, contrasena, nombre, fecha_de_nacimiento, tipo_usuario) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM USUARIO";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM USUARIO WHERE id_usuario = ?";
    private static final String UPDATE_QUERY = "UPDATE USUARIO SET correo_electronico = ?, contrasena = ?, nombre = ?, fecha_de_nacimiento = ?, tipo_usuario = ? WHERE id_usuario = ?";
    private static final String DELETE_QUERY = "DELETE FROM USUARIO WHERE id_usuario = ?";
    private static final String TOTAL_USUARIOS_QUERY = "SELECT COUNT(*) FROM USUARIO";
    private static final String SELECT_BY_NOMBRE_QUERY = "SELECT * FROM USUARIO WHERE nombre LIKE ?";
    private static final String SELECT_BY_CORREO_QUERY = "SELECT * FROM USUARIO WHERE correo_electronico = ?";
    private static final String SELECT_BY_TIPO_QUERY = "SELECT * FROM USUARIO WHERE tipo_usuario = ?";

    private UsuarioDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Proporciona la instancia única de UsuarioDAO.
     */
    public static synchronized UsuarioDAO getInstance() {
        if (instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }

    /**
     * Crea la tabla física utilizando la constante DDL.
     */
    public void crearTabla() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_USUARIO);
        }
    }

    public void insertUsuario(Usuario usuario) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
            ps.setString(1, usuario.getCorreo_electronico());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getFecha_de_nacimiento());
            ps.setString(5, usuario.getTipo_usuario());
            ps.executeUpdate();
        }
    }

    public List<Usuario> getAllUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_QUERY)) {
            while (rs.next()) {
                usuarios.add(resultSetToUsuario(rs));
            }
        }
        return usuarios;
    }

    public Usuario getUsuarioById(int idUsuario) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return resultSetToUsuario(rs);
            }
        }
        return null;
    }

    public List<Usuario> getUsuariosByNombre(String nombre) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_NOMBRE_QUERY)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) usuarios.add(resultSetToUsuario(rs));
            }
        }
        return usuarios;
    }

    public Usuario getUsuarioByCorreo(String correo) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CORREO_QUERY)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return resultSetToUsuario(rs);
            }
        }
        return null;
    }

    public void updateUsuario(Usuario usuario) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY)) {
            ps.setString(1, usuario.getCorreo_electronico());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getFecha_de_nacimiento());
            ps.setString(5, usuario.getTipo_usuario());
            ps.setInt(6, usuario.getIdUsuario());
            ps.executeUpdate();
        }
    }

    public boolean deleteUsuarioById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_QUERY)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int totalUsuarios() throws SQLException {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(TOTAL_USUARIOS_QUERY)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public List<Usuario> getUsuariosPorTipo(String tipo) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_TIPO_QUERY)) {
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(resultSetToUsuario(rs));
            }
        }
        return lista;
    }

    /**
     * Mapea un registro del ResultSet a un objeto Usuario.
     */
    private Usuario resultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("correo_electronico"),
                rs.getString("contrasena"),
                rs.getString("nombre"),
                rs.getString("fecha_de_nacimiento"),
                rs.getString("tipo_usuario"));
    }
}