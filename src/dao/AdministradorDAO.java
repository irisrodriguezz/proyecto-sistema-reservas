package dao;

import model.Administrador;
import java.sql.*;

/**
 * DAO especializado en la entidad Administrador.
 * Gestiona la persistencia entre las tablas USUARIO (padre) y
 * ADMINISTRADOR (hija), implementando una lógica de herencia en la base de datos.
 * * Utiliza el patrón Singleton y garantiza la integridad mediante transacciones SQL.
 */
public class AdministradorDAO {

    private static AdministradorDAO instance;
    private Connection connection;

    /**
     * Sentencia SQL para la creación de la tabla.
     */
    public static final String CREATE_TABLE_ADMINISTRADOR = """
            CREATE TABLE IF NOT EXISTS ADMINISTRADOR (
                id_usuario INT PRIMARY KEY,
                telefono_guardia VARCHAR(20) NOT NULL,
                CONSTRAINT fk_administrador_usuario FOREIGN KEY (id_usuario)
                REFERENCES USUARIO(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
            );
            """;

    // Consultas SQL predefinidas
    private static final String INSERT_USUARIO_QUERY =
            "INSERT INTO USUARIO (correo_electronico, contrasena, nombre, fecha_de_nacimiento, tipo_usuario) VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_ADMIN_QUERY =
            "INSERT INTO ADMINISTRADOR (id_usuario, telefono_guardia) VALUES (?, ?)";

    /**
     * Constructor privado que inicializa la conexión mediante el Singleton DBConnection.
     */
    private AdministradorDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Proporciona la instancia única del DAO.
     */
    public static synchronized AdministradorDAO getInstance() {
        if (instance == null) {
            instance = new AdministradorDAO();
        }
        return instance;
    }

    /**
     * Ejecuta la sentencia DDL para crear la tabla ADMINISTRADOR si no existe.
     * @throws SQLException Si ocurre un error al crear la tabla.
     */
    public void crearTabla() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_ADMINISTRADOR);
        }
    }

    /**
     * Inserta un nuevo administrador.
     * El proceso asegura que el registro se cree en la tabla USUARIO, se recupere
     * el ID generado y se vincule correctamente en la tabla ADMINISTRADOR.
     * Si cualquier paso falla, se ejecuta un rollback para mantener la base de datos limpia.
     * @param admin Objeto Administrador con los datos a persistir.
     * @throws SQLException Si ocurre un error de persistencia o violación de restricciones.
     */
    public void insertAdministrador(Administrador admin) throws SQLException {
        // Desactivamos el autocommit para gestionar la transacción manualmente.
        connection.setAutoCommit(false);

        try (PreparedStatement psUser = connection.prepareStatement(INSERT_USUARIO_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            // Datos para la tabla USUARIO
            psUser.setString(1, admin.getCorreo_electronico());
            psUser.setString(2, admin.getContrasena());
            psUser.setString(3, admin.getNombre());
            psUser.setString(4, admin.getFecha_de_nacimiento());
            psUser.setString(5, "Administrador");
            psUser.executeUpdate();

            // Recuperamos el ID autoincremental generado por la base de datos
            ResultSet rs = psUser.getGeneratedKeys();
            int generatedId = 0;
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

            // Inserción en tabla hija usando la constante de clase
            try (PreparedStatement psAdmin = connection.prepareStatement(INSERT_ADMIN_QUERY)) {
                psAdmin.setInt(1, generatedId);
                psAdmin.setString(2, admin.getTelefono_guardia());
                psAdmin.executeUpdate();
            }

            // Confirmación de la transacción
            connection.commit();

        } catch (SQLException e) {
            // Deshacemos los cambios si algo sale mal
            connection.rollback();
            throw e;
        } finally {
            // Devolvemos el autocommit a true para no romper futuras consultas
            connection.setAutoCommit(true);
        }
    }
}