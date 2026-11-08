package app;

import controller.HorarioController;
import controller.RecursoController;
import controller.UsuarioController;
import controller.ReservaController;
import dao.DBConnection;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    // Inicialización de controladores
    private static UsuarioController usuarioControlador = new UsuarioController();
    private static RecursoController recursoControlador = new RecursoController();
    private static HorarioController horarioControlador = new HorarioController();
    private static ReservaController reservaControlador = new ReservaController();

    public static void main(String[] args) {
        // Establece la conexión
        DBConnection.getConnection();
        mostrarMenuPrincipal();
    }

    // --- MENÚ PRINCIPAL ---

    private static void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("     SISTEMA DE RESERVAS");
            System.out.println("=".repeat(30));
            System.out.println("1. Gestión de usuarios");
            System.out.println("2. Gestión de recursos");
            System.out.println("3. Gestión de horarios");
            System.out.println("4. Gestión de reservas");
            System.out.println("5. Consultas y listados");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    menuUsuarios();
                    break;
                case 2:
                    menuRecursos();
                    break;
                case 3:
                    menuHorarios();
                    break;
                case 4:
                    menuReservas();
                    break;
                case 5:
                    menuConsultas();
                    break;
                case 0:
                    System.out.println("Cerrando conexión y saliendo...");
                    DBConnection.closeConnection();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    // --- SUBMENÚ USUARIOS ---

    private static void menuUsuarios() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE USUARIOS ---");
            System.out.println("1. Alta usuario");
            System.out.println("2. Baja usuario");
            System.out.println("3. Modificar usuario");
            System.out.println("4. Listar todos");
            System.out.println("5. Buscar por nombre");
            System.out.println("6. Buscar por ID");
            System.out.println("7. Buscar por correo");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    usuarioControlador.crearUsuario();
                    break;
                case 2:
                    usuarioControlador.eliminarUsuario();
                    break;
                case 3:
                    usuarioControlador.actualizarUsuario();
                    break;
                case 4:
                    usuarioControlador.mostrarTodosLosUsuarios();
                    break;
                case 5:
                    usuarioControlador.buscarPorNombre();
                    break;
                case 6:
                    usuarioControlador.mostrarIdUsuario();
                    break;
                case 7:
                    usuarioControlador.buscarPorCorreo();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // --- SUBMENÚ RECURSOS ---

    private static void menuRecursos() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE RECURSOS ---");
            System.out.println("1. Alta recurso");
            System.out.println("2. Baja recurso");
            System.out.println("3. Modificar recurso");
            System.out.println("4. Listar recursos");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    recursoControlador.crearRecurso();
                    break;
                case 2:
                    recursoControlador.eliminarRecurso();
                    break;
                case 3:
                    recursoControlador.actualizarRecurso();
                    break;
                case 4:
                    recursoControlador.listarRecursos();
                    break;
            }
        } while (opcion != 0);
    }

    // --- SUBMENÚ HORARIOS ---

    private static void menuHorarios() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE HORARIOS ---");
            System.out.println("1. Listar horarios");
            System.out.println("2. Crear horario");
            System.out.println("3. Modificar horario");
            System.out.println("4. Eliminar horario");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    horarioControlador.listarHorarios();
                    break;
                case 2:
                    horarioControlador.crearHorario();
                    break;
                case 3:
                    horarioControlador.modificarHorario();
                    break;
                case 4:
                    horarioControlador.eliminarHorario();
                    break;
            }
        } while (opcion != 0);
    }

    // --- SUBMENÚ RESERVAS ---

    private static void menuReservas() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE RESERVAS ---");
            System.out.println("1. Nueva reserva");
            System.out.println("2. Baja reserva");
            System.out.println("3. Modificar reserva");
            System.out.println("4. Listar todas");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    reservaControlador.crearReserva();
                    break;
                case 2:
                    reservaControlador.eliminarReserva();
                    break;
                case 3:
                    reservaControlador.modificarReserva();
                    break;
                case 4:
                    reservaControlador.listarReservas();
                    break;
            }
        } while (opcion != 0);
    }

    // --- SUBMENÚ CONSULTAS ---

    private static void menuConsultas() {
        int opcion;
        do {
            System.out.println("\n--- CONSULTAS Y LISTADOS ---");
            System.out.println("1. Listar administradores");
            System.out.println("2. Listar usuarios normales");
            System.out.println("3. Reservas por usuario");
            System.out.println("4. Reservas por recurso");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    usuarioControlador.listarPorTipo("Administrador");
                    break;
                case 2:
                    usuarioControlador.listarPorTipo("Normal");
                    break;
                case 3:
                    reservaControlador.listarPorUsuario();
                    break;
                case 4:
                    reservaControlador.listarPorRecurso();
                    break;
            }
        } while (opcion != 0);
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}