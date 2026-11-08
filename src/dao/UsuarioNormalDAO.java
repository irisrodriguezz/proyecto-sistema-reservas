package dao;

import model.UsuarioNormal;
import java.sql.*;

/**
 * DAO especializado en la entidad UsuarioNormal.
 * Gestiona la persistencia de datos específicos (dirección, teléfono, foto)
 * vinculándolos con la tabla base USUARIO mediante transacciones SQL.
 * Sigue el patrón de diseño con constantes SQL y acceso Singleton.
 */
public class UsuarioNormalDAO {

    private static UsuarioNormalDAO instance;
    private Connection connection;

    public static final String CREATE_TABLE_USUARIO_NORMAL = """
    CREATE TABLE IF NOT EXISTS USUARIONORMAL (
        id_usuario INT PRIMARY KEY,
        direccion VARCHAR(255),
        telefono_movil VARCHAR(20),
        fotografia VARCHAR(255),
        CONSTRAINT fk_normal_usuario FOREIGN KEY (id_usuario)
            REFERENCES USUARIO(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
    );
    """;

    private static final String INSERT_USUARIO_QUERY =
            "INSERT INTO USUARIO (correo_electronico, contrasena, nombre, fecha_de_nacimiento, tipo_usuario) VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_NORMAL_QUERY =
            "INSERT INTO USUARIONORMAL (id_usuario, direccion, telefono_movil, fotografia) VALUES (?, ?, ?, ?)";

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private UsuarioNormalDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Proporciona la instancia única de UsuarioNormalDAO.
     * @return Instancia única del DAO.
     */
    public static synchronized UsuarioNormalDAO getInstance() {
        if (instance == null) {
            instance = new UsuarioNormalDAO();
        }
        return instance;
    }

    /**
     * Ejecuta la sentencia DDL para crear la tabla USUARIONORMAL si no existe.
     */
    public void crearTabla() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_USUARIO_NORMAL);
        }
    }

    /**
     * Realiza una inserción transaccional de un Usuario Normal.
     * El proceso garantiza que los datos se guarden en las tablas USUARIO y USUARIONORMAL
     * de forma atómica. Si falla alguna inserción, se ejecuta un rollback.
     * @param usuarionormal Objeto con los datos completos del usuario.
     * @throws SQLException Si ocurre un error de persistencia.
     */
    public void insertUsuarioNormal(UsuarioNormal usuarionormal) throws SQLException {
        // Desactivamos el autocommit para iniciar el bloque transaccional manual
        connection.setAutoCommit(false);

        try (PreparedStatement psUser = connection.prepareStatement(INSERT_USUARIO_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Datos para la tabla padre (USUARIO)
            psUser.setString(1, usuarionormal.getCorreo_electronico());
            psUser.setString(2, usuarionormal.getContrasena());
            psUser.setString(3, usuarionormal.getNombre());
            psUser.setString(4, usuarionormal.getFecha_de_nacimiento());
            psUser.setString(5, "Normal");
            psUser.executeUpdate();

            // 2. Recuperar ID generaddo
            ResultSet rs = psUser.getGeneratedKeys();
            int generatedId = 0;
            if (rs.next()) {
                generatedId = rs.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID generado para el usuario padre.");
            }

            // 3. Insertar en la tabla hija (USUARIONORMAL) usando la constante
            try (PreparedStatement psNormal = connection.prepareStatement(INSERT_NORMAL_QUERY)) {
                psNormal.setInt(1, generatedId);
                psNormal.setString(2, usuarionormal.getDireccion());
                psNormal.setString(3, usuarionormal.getTelefono_movil());
                psNormal.setString(4, usuarionormal.getFotografia());
                psNormal.executeUpdate();
            }

            // Consolidación de los cambios
            connection.commit();

        } catch (SQLException e) {
            // Reversión total en caso de error
            connection.rollback();
            throw e;
        } finally {
            // Restauración del estado de la conexión
            connection.setAutoCommit(true);
        }
    }
}