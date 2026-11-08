package dao;

import model.Horario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO especializado en la gestión de la entidad Horario.
 * Sigue el patrón de diseño con constantes SQL y acceso Singleton.
 */
public class HorarioDAO {
    private static HorarioDAO instance;
    private Connection connection;

    /**
     * Sentencia SQL para la creación de la tabla.
     */

    public static final String CREATE_TABLE_HORARIO = """
            CREATE TABLE IF NOT EXISTS HORARIO (
                id_horario INT PRIMARY KEY,
                dia_semana ENUM ('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo') NOT NULL,
                hora_inicio TIME NOT NULL,
                hora_fin TIME NOT NULL,
                CONSTRAINT chk_hora CHECK (hora_fin > hora_inicio)
            );
            """;

    private static final String INSERT_QUERY = "INSERT INTO HORARIO (id_horario, dia_semana, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM HORARIO";
    private static final String UPDATE_QUERY = "UPDATE HORARIO SET dia_semana = ?, hora_inicio = ?, hora_fin = ? WHERE id_horario = ?";
    private static final String DELETE_QUERY = "DELETE FROM HORARIO WHERE id_horario = ?";
    private static final String TOTAL_HORARIOS_QUERY = "SELECT COUNT(*) FROM HORARIO";

    private HorarioDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Proporciona la instancia única de HorarioDAO.
     * @return Instancia única de HorarioDAO.
     */
    public static synchronized HorarioDAO getInstance() {
        if (instance == null) {
            instance = new HorarioDAO();
        }
        return instance;
    }

    /**
     * Crea la tabla física usando la constante DDL.
     * @throws SQLException Si ocurre un error en la definición de la tabla.
     */
    public void crearTabla() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_HORARIO);
        }
    }

    /**
     * Inserta un nuevo registro de horario utilizando INSERT_QUERY.
     * @param h Objeto Horario con la información a persistir.
     * @throws SQLException Si hay conflictos con el ID o el formato del ENUM.
     */
    public void insertHorario(Horario h) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
            ps.setInt(1, h.getIdHorario());
            ps.setString(2, h.getDiaSemana());
            ps.setTime(3, Time.valueOf(h.getHoraInicio()));
            ps.setTime(4, Time.valueOf(h.getHoraFin()));
            ps.executeUpdate();
        }
    }

    /**
     * Recupera todos los horarios registrados mediante SELECT_ALL_QUERY.
     * @return Lista de objetos Horario.
     * @throws SQLException Si fallase la consulta.
     */
    public List<Horario> getAllHorarios() throws SQLException {
        List<Horario> horarios = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_QUERY)) {
            while (rs.next()) {
                horarios.add(new Horario(
                        rs.getInt("id_horario"),
                        rs.getString("dia_semana"),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fin").toLocalTime()
                ));
            }
        }
        return horarios;
    }

    /**
     * Actualiza un horario existente utilizando UPDATE_QUERY.
     * @param h Objeto Horario con los nuevos datos.
     * @throws SQLException Si la actualización infringe el 'chk_hora'.
     */
    public void updateHorario(Horario h) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY)) {
            ps.setString(1, h.getDiaSemana());
            ps.setTime(2, java.sql.Time.valueOf(h.getHoraInicio()));
            ps.setTime(3, java.sql.Time.valueOf(h.getHoraFin()));
            ps.setInt(4, h.getIdHorario());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un horario del sistema utilizando DELETE_QUERY.
     * @param id Identificador único del horario.
     * @throws SQLException Si el registro está vinculado en tablas externas.
     */
    public void deleteHorario(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_QUERY)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * [!] Este método no se expone directamente en el menú del usuario, pero
     * es fundamental para la capa de servicios. CÓDIGO DE INFRAESTRUCTURA.
     * Obtiene el número total de registros mediante TOTAL_HORARIOS_QUERY.
     * @return Cantidad de registros en la tabla.
     * @throws SQLException Si falla la ejecución.
     */
    public int totalHorarios() throws SQLException {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(TOTAL_HORARIOS_QUERY)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}