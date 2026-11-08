package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor central de la conexión a la base de datos (Singleton).
 * Coordina la inicialización de todos los DAOs para asegurar que la
 * estructura de tablas exista antes de cualquier operación.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/base_de_datos_mvc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    private DBConnection() {}

    /**
     * Obtiene la conexión y comienza la creación del esquema.
     * @return Connection objeto de conexión activa.
     */
    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DBConnection.class) {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                        // Inicialización de la estructura completa (DDL)
                        // Llamamos a cada DAO para que garantice su propia tabla
                        inicializarEsquema();

                    } catch (SQLException e) {
                        System.err.println("Error al conectar o verificar estructura: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }

    /**
     * Centraliza la creación de todas las tablas del sistema.
     * El orden es importante para que se puedan respetar las Claves Foráneas (FK)
     */
    private static void inicializarEsquema() throws SQLException {
        // 1. Tablas independientes (sin FK de otras)
        UsuarioDAO.getInstance().crearTabla();
        RecursoDAO.getInstance().crearTabla();
        HorarioDAO.getInstance().crearTabla();

        // 2. Tablas dependientes (con FK a las anteriores)
        AdministradorDAO.getInstance().crearTabla();
        UsuarioNormalDAO.getInstance().crearTabla();

        // 3. Tablas de relación y transaccionales
        DisponibleEnDAO.getInstance().crearTabla();
        ReservaDAO.getInstance().crearTabla();
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}