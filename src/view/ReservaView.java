package view;

import model.Reserva;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Vista para la gestión de reservas.
 * Maneja la complejidad de las claves compuestas y formatos de tiempo.
 */
public class ReservaView {
    private Scanner sc = new Scanner(System.in);
    // Igual que en HorarioView, este formateador sirve para que el programa acepte tanto HH:mm como HH:mm:ss
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H[H]:mm[:ss]");

    /**
     * Muestra el listado de reservas con formato.
     */
    public void mostrarReservas(List<Reserva> lista) {
        System.out.println("\n" + "=".repeat(110));
        System.out.println("                                     LISTADO DE RESERVAS");
        System.out.println("=".repeat(110));

        if (lista == null || lista.isEmpty()) {
            System.out.println("No se han encontrado reservas con los criterios seleccionados");
        } else {
            lista.forEach(System.out::println);
        }
        System.out.println("=".repeat(110) + "\n");
    }

    /**
     * Captura todos los datos para una nueva reserva
     */
    public Reserva pedirDatos() {
        try {
            System.out.println("\n--- DATOS DE LA RESERVA ---");
            int idRec = pedirEntero("ID Recurso: ");
            int idLoc = pedirEntero("ID Reserva Local (Manual): ");
            int idUsu = pedirEntero("ID Usuario: ");

            System.out.print("Fecha (AAAA-MM-DD): ");
            String fecha = sc.nextLine();

            System.out.print("Hora Inicio (HH:mm:ss): ");
            LocalTime inicio = LocalTime.parse(sc.nextLine(), timeFormatter);

            System.out.print("Hora Fin (HH:mm:ss): ");
            LocalTime fin = LocalTime.parse(sc.nextLine(), timeFormatter);

            System.out.print("Coste (ej: 15.50): ");
            double coste = Double.parseDouble(sc.nextLine());

            int plazas = pedirEntero("Número de plazas: ");

            System.out.print("Motivo: ");
            String motivo = sc.nextLine();

            System.out.print("Observaciones: ");
            String obs = sc.nextLine();

            return new Reserva(idRec, idLoc, idUsu, fecha, inicio, fin, coste, plazas, motivo, obs);

        } catch (DateTimeParseException e) {
            System.out.println("Error: Formato de hora no válido");
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico incorrecto (por favor, use '.' para decimales)");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
        return null;
    }

    /**
     * Específico para operaciones que requieren la Clave Primaria Compuesta (Borrar/Buscar).
     */
    public int[] pedirIdsClavePrimaria() {
        System.out.println("\n--- IDENTIFICACIÓN DE RESERVA ---");
        int idRec = pedirEntero("ID del Recurso: ");
        int idLoc = pedirEntero("ID Local de la reserva: ");
        return new int[]{idRec, idLoc};
    }

    public int pedirIdParaConsulta(String tipo) {
        return pedirEntero("Introduce el ID del " + tipo + " para filtrar: ");
    }

    /**
     * Método auxiliar para validar que la entrada sea un número entero
     */
    private int pedirEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ser un número entero");
            }
        }
    }

    public void mensaje(String m) {
        System.out.println(m);
    }
}