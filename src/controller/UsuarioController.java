package controller;

import dao.AdministradorDAO;
import dao.UsuarioNormalDAO;
import dao.UsuarioDAO;
import model.Administrador;
import model.UsuarioNormal;
import model.Usuario;
import view.UsuarioView;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador principal para la gestión de Usuarios.
 * Este controlador coordina la interacción entre la vista de Usuario
 * (UsuarioView), y, a diferencia de los otros, debe solicitar
 * los datos a tres DAOs: UsuarioDAO, UsuarioNormalDAO y AdministradorDAO.
 */
public class UsuarioController {

    private UsuarioDAO usuarioDAO;
    private AdministradorDAO administradorDAO;
    private UsuarioNormalDAO usuarioNormalDAO;
    private UsuarioView usuarioView;

    public UsuarioController() {
        usuarioDAO = UsuarioDAO.getInstance();
        administradorDAO = AdministradorDAO.getInstance();
        usuarioNormalDAO = UsuarioNormalDAO.getInstance();
        usuarioView = new UsuarioView();
    }

    /**
     * READ. Muestra todos los usuarios.
     */
    public void mostrarTodosLosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.getAllUsuarios();
            usuarioView.mostrarUsuarios(usuarios);
        } catch (SQLException e) {
            usuarioView.mensaje("Error al recuperar el listado: " + e.getMessage());
        }
    }

    /**
     * READ. Busca un usuario por su ID.
     */
    public void mostrarIdUsuario() {
        try {
            int idNumero = usuarioView.obtenerIdUsuario();
            Usuario usuario = usuarioDAO.getUsuarioById(idNumero);
            usuarioView.mostrarUsuario(usuario);
        } catch (SQLException e) {
            usuarioView.mensaje("Error de base de datos: " + e.getMessage());
        }
    }

    /**
     * CREATE. Inserta un nuevo usuario (Administrador o Normal).
     */
    public void crearUsuario() {
        try {
            Usuario usuario = usuarioView.crearUsuario();
            if (usuario == null) return;

            if (usuario instanceof Administrador) {
                administradorDAO.insertAdministrador((Administrador) usuario);
                usuarioView.mensaje("Administrador guardado correctamente");
            } else if (usuario instanceof UsuarioNormal) {
                usuarioNormalDAO.insertUsuarioNormal((UsuarioNormal) usuario);
                usuarioView.mensaje("Usuario Normal guardado correctamente");
            }
        } catch (SQLException e) {
            usuarioView.mensaje("Error al crear usuario: " + e.getMessage());
        }
    }

    /**
     * UPDATE. Gestiona la actualización de los datos de un Usuario en base a su ID.
     */
    public void actualizarUsuario() {
        try {
            // 1) Interacción con la Vista. Solicita el ID del usuario
            int id = usuarioView.obtenerIdUsuario();
            usuarioView.mensaje("Introduce los nuevos datos generales:");

            // 2) Se usa el método de creación de la vista para capturar los
            // nuevos atributos en un objeto temporal
            Usuario temp = usuarioView.crearUsuario();

            // 3) Se valida el flujo. Si el usuario cancela o hay un error en
            // la captura, temp será nulo
            if (temp != null) {
                // 4) Adherimos el ID original al nuevo objeto para asegurar que
                // el UPDATE de SQL afecta al registro correcto
                temp.setIdUsuario(id);
                // 5) Se envía el objeto al DAO para que ejecute la sentencia SQL
                usuarioDAO.updateUsuario(temp);
                usuarioView.mensaje("Usuario actualizado correctamente.");
            }
        } catch (SQLException e) {
            usuarioView.mensaje("Error al actualizar: " + e.getMessage());
        }
    }

    /**
     * DELETE. Borra un Usuario en base a su ID.
     */
    public void eliminarUsuario() {
        try {
            int idUsuario = usuarioView.obtenerIdUsuario();

            boolean eliminado = usuarioDAO.deleteUsuarioById(idUsuario);

            if (eliminado) {
                usuarioView.mensaje("Usuario con ID " + idUsuario + " eliminado correctamente");
            } else {
                usuarioView.mensaje("Error: No se encontró ningún usuario con el ID " + idUsuario);
            }
        } catch (SQLException e) {
            usuarioView.mensaje("Error de base de datos: " + e.getMessage());
        }
    }

    /**
     * READ. Búsqueda a un Usuario por nombre.
     */
    public void buscarPorNombre() {
        try {
            String nombre = usuarioView.obtenerNombreBusqueda();
            List<Usuario> usuarios = usuarioDAO.getUsuariosByNombre(nombre);

            if (usuarios.isEmpty()) {
                usuarioView.mensaje("No se encontraron usuarios con ese nombre.");
            } else {
                usuarioView.mostrarUsuarios(usuarios);
            }
        } catch (SQLException e) {
            usuarioView.mensaje("Error al buscar: " + e.getMessage());
        }
    }

    /**
     * READ. Búsqueda por correo.
     *
     */
    public void buscarPorCorreo() {
        try {
            System.out.print("Introduce el correo exacto: ");
            String correo = new java.util.Scanner(System.in).nextLine();
            Usuario usuario = usuarioDAO.getUsuarioByCorreo(correo);

            if (usuario != null) {
                usuarioView.mostrarUsuario(usuario);
            } else {
                usuarioView.mensaje("No existe ningún usuario con el correo: " + correo);
            }
        } catch (SQLException e) {
            usuarioView.mensaje("Error al buscar por correo: " + e.getMessage());
        }
    }

    /**
     * READ. Lista usuarios filtrando por su rol (Administrador o Normal).
     * @param tipo Cadena que define el tipo de usuario a buscar ("Administrador" o "Normal").
     */
    public void listarPorTipo(String tipo) {
        try {
            List<Usuario> lista = usuarioDAO.getUsuariosPorTipo(tipo);

            if (lista.isEmpty()) {
                usuarioView.mensaje("No hay usuarios registrados de tipo: " + tipo);
            } else {
                usuarioView.mostrarUsuarios(lista);
            }
        } catch (SQLException e) {
            usuarioView.mensaje("Error al filtrar usuarios: " + e.getMessage());
        }
    }
}