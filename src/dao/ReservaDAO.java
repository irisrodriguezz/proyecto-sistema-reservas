package dao;

import model.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO especializado en la gestión de Reservas.
 * Centraliza la persistencia y gestiona la integridad referencial entre Usuarios y Recursos.
 * Sigue el patrón de diseño con constantes SQL y acceso Singleton.
 */
public class ReservaDAO {
    private static ReservaDAO instance;
    private Connection connection;

    public static final String CREATE_TABLE_RESERVA = """
            CREATE TABLE IF NOT EXISTS RESERVA (
                id_recurso INT NOT NULL,
                id_reserva_local INT NOT NULL,
                id_usuario INT NOT NULL,
                fecha DATE NOT NULL,
                hora_inicio TIME NOT NULL,
                hora_fin TIME NOT NULL,
                coste DOUBLE,
                numero_plazas INT,
                motivo VARCHAR(200),
                observaciones TEXT,
                PRIMARY KEY (id_recurso, id_reserva_local),
                CONSTRAINT fk_reserva_recurso FOREIGN KEY (id_recurso) 
                    REFERENCES RECURSO(id_recurso) ON DELETE CASCADE,
                CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario) 
                    REFERENCES USUARIO(id_usuario) ON DELETE CASCADE
            );
            """;

    private static final String INSERT_QUERY = """
            INSERT INTO RESERVA (id_recurso, id_reserva_local, id_usuario, fecha, 
            hora_inicio, hora_fin, coste, numero_plazas, motivo, observaciones) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_ALL_QUERY = "SELECT * FROM RESERVA";

    private static final String DELETE_QUERY = "DELETE FROM RESERVA WHERE id_recurso = ? AND id_reserva_local = ?";

    private static final String UPDATE_QUERY = """
            UPDATE RESERVA SET id_usuario = ?, fecha = ?, hora_inicio = ?, 
            hora_fin = ?, coste = ?, numero_plazas = ?, motivo = ?, 
            observaciones = ? WHERE id_recurso = ? AND id_reserva_local = ?
            """;

    private ReservaDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Obtiene la instancia única de ReservaDAO.
     * @return Instancia única de ReservaDAO.
     */
    public static synchronized ReservaDAO getInstance() {
        if (instance == null) instance = new ReservaDAO();
        return instance;
    }

    /**
     * Crea la tabla física utilizando la constante DDL.
     * @throws SQLException Si ocurre un error en la definición de la tabla.
     */
    public void crearTabla() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_RESERVA);
        }
    }

    /**
     * Inserta una nueva reserva utilizando INSERT_QUERY.
     * @param r Objeto Reserva con la información a persistir.
     * @throws SQLException Si hay conflictos de clave primaria o errores de integridad.
     */
    public void insertReserva(Reserva r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
            ps.setInt(1, r.getIdRecurso());
            ps.setInt(2, r.getIdReservaLocal());
            ps.setInt(3, r.getIdUsuario());
            ps.setString(4, r.getFecha());
            ps.setTime(5, Time.valueOf(r.getHoraInicio()));
            ps.setTime(6, Time.valueOf(r.getHoraFin()));
            ps.setDouble(7, r.getCoste());
            ps.setInt(8, r.getNumeroPlazas());
            ps.setString(9, r.getMotivo());
            ps.setString(10, r.getObservaciones());
            ps.executeUpdate();
        }
    }

    /**
     * Recupera todas las reservas registradas.
     * @return Lista de objetos Reserva.
     * @throws SQLException Si falla la consulta.
     */
    public List<Reserva> getAllReservas() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_QUERY)) {
            while (rs.next()) {
                lista.add(extraerReserva(rs));
            }
        }
        return lista;
    }

    public List<Reserva> getReservasByUsuario(int idUsuario) throws SQLException {
        return getReservasByFiltro("id_usuario", idUsuario);
    }

    public List<Reserva> getReservasByRecurso(int idRecurso) throws SQLException {
        return getReservasByFiltro("id_recurso", idRecurso);
    }

    /**
     * Método auxiliar para centralizar la lógica de filtrado.
     */
    private List<Reserva> getReservasByFiltro(String columna, int id) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM RESERVA WHERE " + columna + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerReserva(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Actualiza una reserva existente utilizando UPDATE_QUERY.
     */
    public boolean updateReserva(Reserva r) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY)) {
            ps.setInt(1, r.getIdUsuario());
            ps.setString(2, r.getFecha());
            ps.setTime(3, java.sql.Time.valueOf(r.getHoraInicio()));
            ps.setTime(4, java.sql.Time.valueOf(r.getHoraFin()));
            ps.setDouble(5, r.getCoste());
            ps.setInt(6, r.getNumeroPlazas());
            ps.setString(7, r.getMotivo());
            ps.setString(8, r.getObservaciones());
            ps.setInt(9, r.getIdRecurso());
            ps.setInt(10, r.getIdReservaLocal());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina una reserva mediante su clave primaria compuesta utilizando DELETE_QUERY.
     */
    public boolean deleteReserva(int idRecurso, int idLocal) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_QUERY)) {
            ps.setInt(1, idRecurso);
            ps.setInt(2, idLocal);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Método privado para evitar duplicidad de código al leer del ResultSet.
     */
    private Reserva extraerReserva(ResultSet rs) throws SQLException {
        return new Reserva(
                rs.getInt("id_recurso"),
                rs.getInt("id_reserva_local"),
                rs.getInt("id_usuario"),
                rs.getString("fecha"),
                rs.getTime("hora_inicio").toLocalTime(),
                rs.getTime("hora_fin").toLocalTime(),
                rs.getDouble("coste"),
                rs.getInt("numero_plazas"),
                rs.getString("motivo"),
                rs.getString("observaciones")
        );
    }
}