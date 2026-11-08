package view;

import model.Recurso;
import java.util.List;
import java.util.Scanner;

/**
 * Vista para la gestión de recursos físicos (salas, equipos, etc.)
 */
public class RecursoView {
    private Scanner scanner;

    public RecursoView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Muestra la lista de recursos dándole formato
     */
    public void mostrarRecursos(List<Recurso> recursos) {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                         LISTADO DE RECURSOS DISPONIBLES");
        System.out.println("=".repeat(100));
        System.out.printf("%-7s | %-20s | %-15s | %-10s | %-30s\n",
                "ID", "NOMBRE", "UBICACIÓN", "CAPACIDAD", "DESCRIPCIÓN");
        System.out.println("-".repeat(100));

        if (recursos.isEmpty()) {
            System.out.println("No hay recursos registrados en el sistema.");
        } else {
            for (Recurso r : recursos) {
                System.out.println(r);
            }
        }
        System.out.println("=".repeat(100) + "\n");
    }

    /**
     * Captura datos para un nuevo recurso.
     * @return Objeto Recurso sin ID (para inserción).
     */
    public Recurso pedirDatosRecurso() {
        System.out.println("\n--- ALTA DE NUEVO RECURSO ---");
        System.out.print("Nombre del recurso: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ubicación (ej: Torre A, Planta 3): ");
        String ubicacion = scanner.nextLine();

        int capacidad = pedirEntero("Capacidad (número de personas): ");

        return new Recurso(nombre, descripcion, ubicacion, capacidad);
    }

    /**
     * Captura datos para modificar un recurso existente.
     * @return Objeto Recurso con ID (para actualización).
     */
    public Recurso pedirDatosParaModificar() {
        System.out.println("\n--- MODIFICAR RECURSO EXISTENTE ---");
        int id = pedirEntero("Introduce el ID del recurso a modificar: ");

        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Nueva ubicación: ");
        String ubicacion = scanner.nextLine();

        int capacidad = pedirEntero("Nueva capacidad: ");

        System.out.print("Nueva descripción: ");
        String descripcion = scanner.nextLine();

        return new Recurso(id, nombre, descripcion, ubicacion, capacidad);
    }

    /**
     * Método auxiliar para asegurar que la entrada es un número entero válido.
     */
    private int pedirEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe introducir un número entero válido");
            }
        }
    }

    public int pedirIdRecurso(String mensaje) {
        return pedirEntero(mensaje);
    }

    public void mensaje(String m) {
        System.out.println(m);
    }
}