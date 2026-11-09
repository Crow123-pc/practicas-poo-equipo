package bienestar;

import java.awt.Component;
import java.awt.Container;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** Comprueba eventos reales de las tres vistas sin depender de ratón ni datos del usuario. */
public final class PruebasInterfazSwing {
    private static <T extends Component> T buscar(Container raiz, Class<T> tipo, String nombre) {
        for (Component componente : raiz.getComponents()) {
            if (tipo.isInstance(componente) && (nombre == null || nombre.equals(componente.getName())))
                return tipo.cast(componente);
            if (componente instanceof Container contenedor) {
                T encontrado = buscar(contenedor, tipo, nombre);
                if (encontrado != null) return encontrado;
            }
        }
        return null;
    }

    private static JButton boton(Container raiz, String texto) {
        for (Component componente : raiz.getComponents()) {
            if (componente instanceof JButton b && b.getText().equals(texto)) return b;
            if (componente instanceof Container contenedor) {
                JButton encontrado = boton(contenedor, texto);
                if (encontrado != null) return encontrado;
            }
        }
        return null;
    }

    private static void escribir(Container raiz, String nombre, String valor) {
        JTextField campo = buscar(raiz, JTextField.class, nombre);
        if (campo == null) throw new AssertionError("No aparece el campo " + nombre);
        campo.setText(valor);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    public static void main(String[] args) throws Exception {
        Path archivo = Files.createTempDirectory("interfaz-bienestar-").resolve("sistema.csv");
        ServicioTalleres servicio = new ServicioTalleres(archivo);
        SwingUtilities.invokeAndWait(() -> {
            AppSwing app = new AppSwing(servicio);
            try {
                JTabbedPane vistas = buscar(app.getContentPane(), JTabbedPane.class, "vistas");
                comprobar(vistas != null && vistas.getTabCount() == 3, "Deben existir tres vistas");

                Container participantes = (Container) vistas.getComponentAt(0);
                escribir(participantes, "participante.codigo", "P001");
                escribir(participantes, "participante.nombre", "Ana de prueba");
                escribir(participantes, "participante.edad", "16");
                escribir(participantes, "participante.grado", "5to");
                JButton registrar = boton(participantes, "Registrar · RF01");
                if (registrar == null) throw new AssertionError("No aparece Registrar: " + botones(participantes));
                registrar.doClick();
                comprobar(servicio.listarParticipantes().size() == 1
                        && buscar(participantes, JTable.class, null).getRowCount() == 1,
                        "El evento de registro debe actualizar la vista y el servicio");

                Container talleres = (Container) vistas.getComponentAt(1);
                escribir(talleres, "taller.codigo", "T001");
                escribir(talleres, "taller.titulo", "Bienestar digital");
                boton(talleres, "Crear · RF06").doClick();
                comprobar(servicio.listarTalleres().size() == 1
                        && buscar(talleres, JTable.class, null).getRowCount() == 1,
                        "El evento de creación debe actualizar el taller");

                Container inscripciones = (Container) vistas.getComponentAt(2);
                escribir(inscripciones, "inscripcion.taller", "T001");
                escribir(inscripciones, "inscripcion.participante", "P001");
                boton(inscripciones, "Inscribir · RF10").doClick();
                comprobar(servicio.listarInscritos("T001").size() == 1
                        && buscar(inscripciones, JTable.class, null).getRowCount() == 1,
                        "El evento de inscripción debe relacionar las entidades");
                escribir(inscripciones, "inscripcion.nota", "18");
                boton(inscripciones, "Evaluar · RF14").doClick();
                comprobar(servicio.listarInscritos("T001").get(0).nota() == 18,
                        "La nota debe guardarse desde la tercera vista");
            } finally {
                app.dispose();
            }
        });
        ServicioTalleres reabierto = new ServicioTalleres(archivo);
        comprobar(reabierto.listarInscritos("T001").get(0).nota() == 18,
                "Los cambios de la interfaz deben persistir al reiniciar");
        System.out.println("OK GUI: tres vistas, eventos, tablas y persistencia");
    }

    private static String botones(Container raiz) {
        StringBuilder texto = new StringBuilder();
        for (Component componente : raiz.getComponents()) {
            if (componente instanceof JButton b) texto.append('[').append(b.getText()).append(']');
            if (componente instanceof Container contenedor) texto.append(botones(contenedor));
        }
        return texto.toString();
    }
}
