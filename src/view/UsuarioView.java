package view;

import model.Administrador;
import model.Usuario;
import model.UsuarioNormal;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Vista para la gestión de usuarios.
 * Maneja la lógica de captura para diferentes tipos de roles (usando Herencia)
 */
public class UsuarioView {
    private Scanner scanner;

    public UsuarioView() {
        scanner = new Scanner(System.in);
    }

    /**
     * Muestra la lista de usuarios en formato tabla.
     */
    public void mostrarUsuarios(List<Usuario> usuarios) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("                                     LISTADO GENERAL DE USUARIOS");
        System.out.println("=".repeat(120));
        System.out.printf("%-5s | %-30s | %-15s | %-20s | %-12s | %-10s\n",
                "ID", "CORREO ELECTRÓNICO", "CONTRASEÑA", "NOMBRE", "FECHA NAC.", "TIPO");
        System.out.println("-".repeat(120));

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en el sistema.");
        } else {
            usuarios.forEach(System.out::println);
        }
        System.out.println("=".repeat(120) + "\n");
    }

    /**
     * Crea un objeto de tipo Administrador o UsuarioNormal pidiendo datos por consola.
     */
    public Usuario crearUsuario() {
        System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();

        String fecha = pedirFecha();

        String tipo = "";
        while (true) {
            System.out.print("Tipo de usuario (Normal/Administrador): ");
            tipo = scanner.nextLine();
            if (tipo.equalsIgnoreCase("Normal") || tipo.equalsIgnoreCase("Administrador")) break;
            System.out.println("Error: Tipo no válido.");
        }

        if (tipo.equalsIgnoreCase("Administrador")) {
            System.out.print("Teléfono de guardia: ");
            String telf = scanner.nextLine();
            return new Administrador(correo, pass, nombre, fecha, telf);
        } else {
            System.out.print("Dirección: ");
            String dir = scanner.nextLine();
            System.out.print("Teléfono móvil: ");
            String movil = scanner.nextLine();
            System.out.print("Nombre de archivo de fotografía: ");
            String foto = scanner.nextLine();
            return new UsuarioNormal(correo, pass, nombre, fecha, dir, movil, foto);
        }
    }

    /**
     * Método auxiliar para validar el formato de fecha
     */
    private String pedirFecha() {
        while (true) {
            try {
                System.out.print("Fecha de nacimiento (AAAA-MM-DD): ");
                String fecha = scanner.nextLine();
                LocalDate.parse(fecha);
                return fecha;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha incorrecto");
            }
        }
    }

    public int obtenerIdUsuario() {
        while (true) {
            System.out.print("Introduce el ID del usuario: ");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: El ID debe ser un número");
            }
        }
    }

    /**
     * Muestra la ficha detallada de un usuario específico.
     */
    public void mostrarUsuario(Usuario usuario) {
        if (usuario != null) {
            System.out.println("\n===============================================================");
            System.out.println(" DETALLES DEL USUARIO ");
            System.out.println("===============================================================");
            System.out.printf(" ID: %d\n", usuario.getIdUsuario());
            System.out.printf(" CORREO ELECTRÓNICO: %s\n", usuario.getCorreo_electronico());
            System.out.printf(" NOMBRE: %s\n", usuario.getNombre());
            System.out.printf(" FECHA DE NACIMIENTO: %s\n", usuario.getFecha_de_nacimiento());
            System.out.printf(" TIPO DE USUARIO: %s\n", usuario.getTipo_usuario());

            if (usuario instanceof Administrador) {
                System.out.println("TELÉFONO GUARDIA: %s\n" + ((Administrador) usuario).getTelefono_guardia());
            } else if (usuario instanceof UsuarioNormal) {
                UsuarioNormal un = (UsuarioNormal) usuario;
                System.out.println("DIRECCIÓN: %s\n" + un.getDireccion());
                System.out.println("MÓVIL: %s \n" + un.getTelefono_movil());
                System.out.println("FOTO: %s \n" + un.getFotografia());
            }
            System.out.println("===============================================================\n");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    public String obtenerNombreBusqueda() {
        System.out.print("Introduce el nombre a buscar: ");
        return scanner.nextLine();
    }

    public void mensaje(String m) {
        System.out.println(m);
    }
}