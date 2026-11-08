package controller;

import dao.HorarioDAO;
import model.Horario;
import view.HorarioView;

import java.sql.SQLException;

/**
 * Controlador para la gestión de Horarios.
 * Con ello establecemos la comunicación entre la vista (HorarioView) y el DAO (HorarioDAO),
 * permite realizar las operaciones de lectura, creación, actualización y borrado (CRUD).
 */
public class HorarioController {
    // Atributos privados: en el DAO se usa el patrón Singleton y en la Vista se instancia directamente
    private HorarioDAO dao = HorarioDAO.getInstance();
    private HorarioView view = new HorarioView();

    /**
     * READ. Recupera todos los horarios de la base de datos y solicitar a la Vista (View),
     * que los muestre + captura posibles errores de conexión o sintaxis SQL.
     */
    public void listarHorarios() {
        try {
            view.mostrarHorarios(dao.getAllHorarios());
        } catch (SQLException e) {
            view.mensaje("Error al listar horarios: " + e.getMessage());
        }
    }

    /**
     * CREATE. Establece el proceso de alta de un nuevo horario.
     * Solicita los datos al usuario y valida que el objeto no sea nulo.
     */
    public void crearHorario() {
        // 1) Se obtiene el objeto Horario
        Horario nuevo = view.pedirDatosNuevoHorario();

        // 2) Se verifica si la construcción del objeto fue exitosa (validando el formato)
        if (nuevo != null) {
            try {
                dao.insertHorario(nuevo);
                view.mensaje("Horario creado con éxito");
            } catch (SQLException e) {
                // Controla excepciones específicas como "ID duplicado"
                view.mensaje("Error de base de datos al insertar: " + e.getMessage());
            }
        } else {
            // Se ejecuta si el usuario falla al introducir datos erróneos en el formato (HH:mm, etc.)
            view.mensaje("No se pudo crear el horario: los datos introducidos no son válidos");
        }
    }

    /**
     * UPDATE. Permite modificar un horario existente.
     * Solicita un ID y pide los nuevos datos. Si el ID ya existe, el registro se actualiza
     * por este último.
     */
    public void modificarHorario() {
        Horario h = view.pedirDatosEditarHorario();
        if (h != null) {
            try {
                dao.updateHorario(h);
                view.mensaje("Horario actualizado correctamente");
            } catch (SQLException e) {
                view.mensaje("Error al modificar: " + e.getMessage());
            }
        }
    }

    /**
     * DELETE. Elimina un horario del sistema en base a su ID.
     * Valida que el ID introducido sea un número válido antes de realizar la acción.
     */
    public void eliminarHorario() {
        int id = view.pedirIdHorario();
        if (id != -1) {
            try {
                dao.deleteHorario(id);
                view.mensaje("Horario eliminado correctamente");
            } catch (SQLException e) {
                view.mensaje("Error al eliminar: " + e.getMessage());
            }
        }
    }
}