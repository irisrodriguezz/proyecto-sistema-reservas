package dao;

import java.sql.*;

/**
 * DAO para la tabla intermedia DISPONIBLEEN.
 * Sigue el patrón Singleton para centralizar el acceso a la base de datos.
 */
public class DisponibleEnDAO {
    private static DisponibleEnDAO instance;
    private Connection connection;

    /**
     * Sentencia SQL para la creación de la tabla.
     * Incluye claves foráneas con borrado y actualización en cascada para
     * asegurar la integridad referencial de la relación.
     */
    public static final String CREATE_TABLE_DISPONIBLEEN = """
            CREATE TABLE IF NOT EXISTS DISPONIBLEEN (
                id_recurso INT NOT NULL,
                id_horario INT NOT NULL,
                PRIMARY KEY (id_recurso, id_horario),
                CONSTRAINT fk_disponibleen_recurso FOREIGN KEY (id_recurso)
                    REFERENCES RECURSO(id_recurso) ON DELETE CASCADE ON UPDATE CASCADE,
                CONSTRAINT fk_disponibleen_horario FOREIGN KEY (id_horario)
                    REFERENCES HORARIO(id_horario) ON DELETE CASCADE ON UPDATE CASCADE
            );
            """;

    /**
     * Constructor privado para evitar la instanciación externa.
     * Inicializa la conexión a la base de datos.
     */
    private DisponibleEnDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Proporciona la instancia única de DisponibleEnDAO.
     */
    public static synchronized DisponibleEnDAO getInstance() {
        if (instance == null) {
            instance = new DisponibleEnDAO();
        }
        return instance;
    }

    /**
     * Ejecuta la sentencia DDL para crear la tabla DISPONIBLEEN si no existe.
     * @throws SQLException Si ocurre un error al ejecutar la sentencia en la base de datos.
     */
    public void crearTabla() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_DISPONIBLEEN);
        }
    }
}