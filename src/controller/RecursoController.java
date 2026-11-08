package controller;

import dao.RecursoDAO;
import model.Recurso;
import view.RecursoView;
import java.sql.SQLException;

/**
 * Controlador para la gestión de Recursos.
 * Establece la comunicación entre la Vista (RecursoView) y el DAO (RecursoDAO),
 * permite realizar las operaciones de lectura, creación, actualización y borrado (CRUD).
 */
public class RecursoController {
    //Atributos privados siguiendo el patrón de diseño MVC
    private RecursoDAO dao = RecursoDAO.getInstance();
    private RecursoView view = new RecursoView();

    /**
     * READ. Solicita al DAO la lista completa de recursos y se la envía a la vista para que aparezca en pantalla.
     * Maneja excepciones de SQL en caso de errores en la consulta.
     */
    public void listarRecursos() {
        try {
            view.mostrarRecursos(dao.getAllRecursos());
        } catch (SQLException e) {
            view.mensaje("Error al listar: " + e.getMessage());
        }
    }

    /**
     * CREATE. Coordina el alta de un nuevo recurso.
     * Captura los datos desde la interfaz de usuario y los persiste en la base de datos.
     */
    public void crearRecurso() {
        Recurso nuevo = view.pedirDatosRecurso();
        try {
            dao.insertRecurso(nuevo);
            view.mensaje("Recurso guardado correctamente");
        } catch (SQLException e) {
            view.mensaje("Error al guardar: " + e.getMessage());
        }
    }

    /**
     * UPDATE. Gestiona la modificación de un recurso.
     * Pide el ID y los nuevos datos a la vista y solicita al DAO que se actualicen.
     */
    public void actualizarRecurso() {
        //Usa el método que pide datos para modificar
        Recurso r = view.pedirDatosParaModificar();

        if (r != null) {
            try {
                dao.updateRecurso(r);
                view.mensaje("El recurso con ID " + r.getIdRecurso() + " se ha actualizado con éxito");
            } catch (SQLException e) {
                view.mensaje("Error al actualizar: " + e.getMessage());
            }
        }
    }

    /**
     * DELETE. Gestiona la eliminación de un recurso mediante su ID.
     * Incluye una validación previa para asegurar que el ID se ajusta al formato solicitado.
     */
    public void eliminarRecurso() {
        int id = view.pedirIdRecurso("Introduce el ID del recurso a eliminar: ");
        if (id != -1) {
            try {
                dao.deleteRecurso(id);
                view.mensaje("Recurso eliminado");
            } catch (SQLException e) {
                view.mensaje("Error al eliminar: " + e.getMessage());
            }
        }
    }
}