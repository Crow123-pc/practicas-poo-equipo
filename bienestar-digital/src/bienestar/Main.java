package bienestar;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** Interfaz por consola: las opciones 1 a 15 corresponden a RF01 a RF15. */
public final class Main {
    private final Scanner entrada = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
    private final ServicioTalleres servicio;
    private Main(ServicioTalleres servicio) { this.servicio=servicio; }
    public static void main(String[] args) {
        try {
            Path datos=Path.of(args.length==0?"datos/sistema.csv":args[0]);
            new Main(new ServicioTalleres(datos)).ejecutar();
        } catch(IOException ex) {
            System.err.println("No se inició el sistema: "+ex.getMessage());
            System.err.println("Revise el archivo. No se sobrescribieron los datos.");
            System.exit(1);
        }
    }
    private String pedir(String mensaje) {
        System.out.print(mensaje+": ");
        if(!entrada.hasNextLine()) throw new java.util.NoSuchElementException();
        return entrada.nextLine();
    }
    private int numero(String mensaje) { return Integer.parseInt(pedir(mensaje).strip()); }
    private void ejecutar() {
        boolean activo=true;
        while(activo) {
            System.out.println("\nBIENESTAR DIGITAL - GESTIÓN DE TALLERES");
            System.out.println("1 Registrar participante   2 Listar participantes   3 Buscar participante");
            System.out.println("4 Actualizar participante  5 Eliminar participante  6 Crear taller");
            System.out.println("7 Listar talleres          8 Buscar taller          9 Actualizar taller");
            System.out.println("10 Inscribir              11 Listar inscritos      12 Cancelar inscripción");
            System.out.println("13 Registrar asistencia   14 Registrar evaluación  15 Exportar reporte CSV");
            System.out.println("0 Salir");
            try {
                int opcion=numero("Opción");
                switch(opcion) {
                    case 0 -> activo=false;
                    case 1 -> servicio.registrarParticipante(pedir("Código"),pedir("Nombre"),numero("Edad"),pedir("Grado"));
                    case 2 -> mostrar(servicio.listarParticipantes());
                    case 3 -> System.out.println(servicio.buscarParticipante(pedir("Código")));
                    case 4 -> servicio.actualizarParticipante(pedir("Código"),pedir("Nombre nuevo"),numero("Edad"),pedir("Grado"));
                    case 5 -> servicio.eliminarParticipante(pedir("Código"));
                    case 6 -> servicio.crearTaller(pedir("Código del taller"),pedir("Título"),LocalDate.parse(pedir("Fecha AAAA-MM-DD")),numero("Duración en minutos"),numero("Cupo"));
                    case 7 -> mostrar(servicio.listarTalleres());
                    case 8 -> System.out.println(servicio.buscarTaller(pedir("Código del taller")));
                    case 9 -> servicio.actualizarTaller(pedir("Código del taller"),pedir("Título nuevo"),LocalDate.parse(pedir("Fecha AAAA-MM-DD")),numero("Duración en minutos"),numero("Cupo"));
                    case 10 -> servicio.inscribir(pedir("Código del taller"),pedir("Código del participante"));
                    case 11 -> mostrar(servicio.listarInscritos(pedir("Código del taller")));
                    case 12 -> servicio.cancelarInscripcion(pedir("Código del taller"),pedir("Código del participante"));
                    case 13 -> servicio.registrarAsistencia(pedir("Código del taller"),pedir("Código del participante"),Inscripcion.Asistencia.valueOf(pedir("Estado PRESENTE, AUSENTE o PENDIENTE").strip().toUpperCase(java.util.Locale.ROOT)));
                    case 14 -> servicio.registrarEvaluacion(pedir("Código del taller"),pedir("Código del participante"),numero("Nota de 0 a 20"));
                    case 15 -> System.out.println("Reporte: "+servicio.exportarReporte(pedir("Código del taller"),Path.of(pedir("Ruta del reporte, por ejemplo reportes/taller.csv"))));
                    default -> { System.out.println("Opción inválida"); continue; }
                }
                if(opcion>=1 && opcion<=15) System.out.println("Operación completada");
            } catch(NumberFormatException ex) { System.out.println("Entrada inválida: ingrese un número entero"); }
              catch(DateTimeParseException ex) { System.out.println("Fecha inválida: use AAAA-MM-DD y una fecha existente"); }
              catch(IllegalArgumentException | IOException ex) { System.out.println("Operación rechazada: "+ex.getMessage()); }
              catch(java.util.NoSuchElementException ex) { activo=false; }
        }
        System.out.println("Sesión finalizada");
    }
    private static void mostrar(java.util.List<?> filas) {
        if(filas.isEmpty()) System.out.println("Sin registros"); else filas.forEach(System.out::println);
    }
}
