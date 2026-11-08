package view;

import model.Horario;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Vista especializada en la interacción con el usuario para la gestión de horarios
 */
public class HorarioView {
    private Scanner scanner = new Scanner(System.in);
    // Este formateador sirve para que el programa acepte tanto HH:mm como HH:mm:ss
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H[H]:mm[:ss]");

    /**
     * Imprime una lista de horarios en formato tabla
     */
    public void mostrarHorarios(List<Horario> horarios) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                LISTADO DE HORARIOS");
        System.out.println("=".repeat(60));
        System.out.printf("%-8s | %-12s | %-15s\n", "ID", "DÍA", "RANGO HORARIO");
        System.out.println("-".repeat(60));

        if (horarios.isEmpty()) {
            System.out.println("No hay horarios registrados actualmente");
        } else {
            for (Horario h : horarios) {
                System.out.println(h);
            }
        }
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Solicita datos para crear un NUEVO horario (sin pedir ID)
     */
    public Horario pedirDatosNuevoHorario() {
        return leerDatos(false);
    }

    /**
     * Solicita datos para actualizar un horario existente (pidiendo ID)
     */
    public Horario pedirDatosEditarHorario() {
        return leerDatos(true);
    }

    /**
     * Lógica interna para capturar datos de teclado y convertirlos a LocalTime
     */
    private Horario leerDatos(boolean incluirId) {
        try {
            int id = 0;
            if (incluirId) {
                System.out.print("Introduce el ID del horario a modificar: ");
                id = Integer.parseInt(scanner.nextLine());
            }

            System.out.print("Día de la semana (Lunes, Martes...): ");
            String dia = scanner.nextLine();

            System.out.print("Hora inicio (HH:mm:ss): ");
            LocalTime inicio = LocalTime.parse(scanner.nextLine(), timeFormatter);

            System.out.print("Hora fin (HH:mm:ss): ");
            LocalTime fin = LocalTime.parse(scanner.nextLine(), timeFormatter);

            if (incluirId) {
                return new Horario(id, dia, inicio, fin);
            } else {
                return new Horario(dia, inicio, fin);
            }

        } catch (DateTimeParseException e) {
            System.out.println("Error: Formato de hora no válido. Use HH:mm:ss (ej: 09:00:00)");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero");
        }
        return null;
    }

    public int pedirIdHorario() {
        System.out.print("Introduce el ID del horario: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID no válido.");
            return -1;
        }
    }

    public void mensaje(String m) {
        System.out.println(m);
    }
}