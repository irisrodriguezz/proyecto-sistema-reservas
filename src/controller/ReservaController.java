package controller;

import dao.ReservaDAO;
import model.Reserva;
import view.ReservaView;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador para la gestión de Reservas.
 * Coordina la interacción entre la vista de reservas (ReservaView) y el DAO (ReservaDAO).
 * Gestiona la lógica de filtrado por usuarios y recursos.
 */
public class ReservaController {
    // Usamos el Singleton del DAO y la instancia de la Vista.
    private ReservaDAO dao = ReservaDAO.getInstance();
    private ReservaView view = new ReservaView();

    /**
     * READ. Lista todas las reservas obtenidas de la base de datos.
     * Se mostrará un mensaje si el sistema no contiene registros.
     */
    public void listarReservas() {
        try {
            List<Reserva> reservas = dao.getAllReservas();
            if (reservas.isEmpty()) {
                view.mensaje("No hay reservas registradas");
            } else {
                view.mostrarReservas(reservas);
            }
        } catch (SQLException e) {
            view.mensaje("Error a acceder a la base de datos: " + e.getMessage());
        }
    }

    /**
     * CREATE. Gestiona el proceso de creación de una reserva.
     */
    public void crearReserva() {
        // 1) Se obtiene el objeto Reserva.
        Reserva nueva = view.pedirDatos();

        // 2) Se verifica que los datos introducidos (horas, números) sean válidos.
        if (nueva != null) {
            try {
                dao.insertReserva(nueva);
                view.mensaje("Reserva confirmada con éxito");
            } catch (SQLException e) {
                // Captura errores de clave foránea (si el usuario o recurso no existen)
                // o errores de clave primaria duplicada (id_recurso + id_reserva_local)
                view.mensaje("Error de persistencia: " + e.getMessage());
                view.mensaje("Asegúrese de que los IDs existan y la combinación (Recurso/ID Local) no esté repetida");
            }
        } else {
            view.mensaje("Operación cancelada");
        }
    }

    /**
     * READ. Consulta y filtra las reservas realizadas por el usuario solicitado.
     * Solicita el ID a la vista y gestiona la visualización de los resultados.
     */
    public void listarPorUsuario() {
        // Se pide el ID del usuario como filtro
        int id = view.pedirIdParaConsulta("Usuario");

        // Y si el formato del ID es correcto...
        if (id != -1) {
            try {
                List<Reserva> resultados = dao.getReservasByUsuario(id);
                if (resultados.isEmpty()) {
                    view.mensaje("No se encontraron reservas para el Usuario con ID: " + id);
                } else {
                    view.mostrarReservas(resultados);
                }
            } catch (SQLException e) {
                view.mensaje("Error en la consulta: " + e.getMessage());
            }
        }
    }

    /**
     * READ. Consulta y filtra todas las reservas asociadas a un recurso concreto.
     */
    public void listarPorRecurso() {
        // Se solicita el ID del recurso como filtro
        int id = view.pedirIdParaConsulta("Recurso");

        // Y si el formato del ID es correcto...
        if (id != -1) {
            try {
                List<Reserva> resultados = dao.getReservasByRecurso(id);
                if (resultados.isEmpty()) {
                    view.mensaje("No se encontraron reservas para el Recurso con ID: " + id);
                } else {
                    view.mostrarReservas(resultados);
                }
            } catch (SQLException e) {
                view.mensaje("Error en la consulta: " + e.getMessage());
            }
        }
    }

    /**
     * DELETE. Gestiona el borrado de una reserva pidiendo los dos IDs de su clave compuesta.
     */
    public void eliminarReserva() {
        int[] ids = view.pedirIdsClavePrimaria();
        if (ids != null) {
            try {
                if (dao.deleteReserva(ids[0], ids[1])) {
                    view.mensaje("Reserva eliminada correctamente");
                } else {
                    view.mensaje("No se encontró ninguna reserva con esos IDs");
                }
            } catch (SQLException e) {
                view.mensaje("Error al eliminar: " + e.getMessage());
            }
        }
    }

    /**
     * UPDATE. Gestiona la actualización de una reserva existente.
     */
    public void modificarReserva() {
        Reserva r = view.pedirDatos();
        if (r != null) {
            try {
                if (dao.updateReserva(r)) {
                    view.mensaje("Reserva actualizada con éxito");
                } else {
                    view.mensaje("No se pudo actualizar: la reserva no existe");
                }
            } catch (SQLException e) {
                view.mensaje("Error al modificar: " + e.getMessage());
            }
        }
    }
}