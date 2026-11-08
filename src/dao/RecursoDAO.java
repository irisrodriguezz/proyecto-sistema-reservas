package dao;

import model.Recurso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO especializado en la gestión de la entidad Recurso.
 * Se encarga de las operaciones CRUD sobre la tabla RECURSO.
 * Sigue el patrón de diseño con constantes SQL y acceso Singleton.
 */
public class RecursoDAO {

    private static RecursoDAO instance;
    private Connection connection;

    public static final String CREATE_TABLE_RECURSO = """
            CREATE TABLE IF NOT EXISTS RECURSO (
                id_recurso INT PRIMARY KEY AUTO_INCREMENT,
                nombre VARCHAR(100) NOT NULL,
                descripcion TEXT,
                ubicacion VARCHAR(200),
                capacidad INT
            );
            """;

    private static final String INSERT_QUERY = "INSERT INTO RECURSO (nombre, descripcion, ubicacion, capacidad) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM RECURSO";
    private static final String UPDATE_QUERY = "UPDATE RECURSO SET nombre = ?, ubicacion = ?, capacidad = ?, descripcion = ? WHERE id_recurso = ?";
    private static final String DELETE_QUERY = "DELETE FROM RECURSO WHERE id_recurso = ?";
    private static final String TOTAL_RECURSOS_QUERY = "SELECT COUNT(*) FROM RECURSO";

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private RecursoDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Obtiene la instancia única de RecursoDAO.
     * @return Instancia única de RecursoDAO.
     */
    public static synchronized RecursoDAO getInstance() {
        if (instance == null) {
            instance = new RecursoDAO();
        }
        return instance;
    }

    /**
     * Crea la tabla física usando la constante DDL.
     * @throws SQLException Si ocurre un error en la definición de la tabla.
     */
    public void crearTabla() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_RECURSO);
        }
    }

    /**
     * Inserta un nuevo recurso utilizando INSERT_QUERY.
     * @param recurso Objeto con los datos a insertar.
     * @throws SQLException Si falla la operación de inserción.
     */
    public void insertRecurso(Recurso recurso) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
            ps.setString(1, recurso.getNombre());
            ps.setString(2, recurso.getDescripcion());
            ps.setString(3, recurso.getUbicacion());
            ps.setInt(4, recurso.getCapacidad());
            ps.executeUpdate();
        }
    }

    /**
     * Recupera todos los recursos registrados mediante SELECT_ALL_QUERY.
     * @return Lista de objetos Recurso.
     * @throws SQLException Si ocurre un error en la consulta SELECT.
     */
    public List<Recurso> getAllRecursos() throws SQLException {
        List<Recurso> recursos = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_QUERY)) {
            while (rs.next()) {
                recursos.add(new Recurso(
                        rs.getInt("id_recurso"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("ubicacion"),
                        rs.getInt("capacidad")
                ));
            }
        }
        return recursos;
    }

    /**
     * Actualiza un recurso existente utilizando UPDATE_QUERY.
     * @param r El objeto Recurso con los nuevos datos.
     * @throws SQLException Si falla la conexión o la sentencia SQL.
     */
    public void updateRecurso(Recurso r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY)) {
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getUbicacion());
            ps.setInt(3, r.getCapacidad());
            ps.setString(4, r.getDescripcion());
            ps.setInt(5, r.getIdRecurso());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un recurso permanentemente utilizando DELETE_QUERY.
     * @param id Identificador del recurso a borrar.
     * @throws SQLException Si el recurso tiene dependencias que impiden su borrado.
     */
    public void deleteRecurso(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_QUERY)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * [!] Este método no se expone directamente en el menú del usuario, pero
     * es fundamental para la capa de servicios. CÓDIGO DE INFRAESTRUCTURA.
     * Obtiene la cantidad total de recursos mediante TOTAL_RECURSOS_QUERY.
     * @return Número total de recursos registrados.
     * @throws SQLException Si falla la consulta de agregación.
     */
    public int totalRecursos() throws SQLException {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(TOTAL_RECURSOS_QUERY)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}