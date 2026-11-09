package bienestar;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/** Genera evidencia de las tres vistas con registros ficticios. */
public final class CapturasInterfazSwing {
    private static JTabbedPane pestañas(Container raiz) {
        for (Component componente : raiz.getComponents()) {
            if (componente instanceof JTabbedPane tabs) return tabs;
            if (componente instanceof Container contenedor) {
                JTabbedPane encontrado = pestañas(contenedor);
                if (encontrado != null) return encontrado;
            }
        }
        return null;
    }

    private static <T extends Component> T buscar(Container raiz, Class<T> tipo, String nombre) {
        for (Component componente : raiz.getComponents()) {
            if (tipo.isInstance(componente) && nombre.equals(componente.getName())) return tipo.cast(componente);
            if (componente instanceof Container contenedor) {
                T encontrado = buscar(contenedor, tipo, nombre);
                if (encontrado != null) return encontrado;
            }
        }
        return null;
    }

    private static JButton boton(Container raiz, String texto) {
        for (Component componente : raiz.getComponents()) {
            if (componente instanceof JButton b && texto.equals(b.getText())) return b;
            if (componente instanceof Container contenedor) {
                JButton encontrado = boton(contenedor, texto);
                if (encontrado != null) return encontrado;
            }
        }
        return null;
    }

    private static void guardar(AppSwing app, JTabbedPane tabs, int vista, Path destino) throws Exception {
        tabs.setSelectedIndex(vista);
        app.validate();
        Container contenido = app.getContentPane();
        BufferedImage imagen = new BufferedImage(contenido.getWidth(), contenido.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D dibujo = imagen.createGraphics();
        contenido.printAll(dibujo);
        dibujo.dispose();
        ImageIO.write(imagen, "png", destino.toFile());
        System.out.println("Evidencia: " + destino.toAbsolutePath());
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Indique el directorio de capturas");
        Path salida = Path.of(args[0]);
        Files.createDirectories(salida);
        Path datos = Files.createTempDirectory("capturas-bienestar-").resolve("sistema.csv");
        ServicioTalleres servicio = new ServicioTalleres(datos);
        servicio.registrarParticipante("P001", "Ana de prueba", 16, "5to secundaria");
        servicio.registrarParticipante("P002", "Luis de prueba", 17, "5to secundaria");
        servicio.crearTaller("T001", "Uso responsable de dispositivos", LocalDate.of(2026, 10, 19), 60, 30);
        servicio.asignarExpositor("T001", "Expositor de prueba");
        servicio.inscribir("T001", "P001");
        servicio.registrarAsistencia("T001", "P001", Inscripcion.Asistencia.PRESENTE);
        servicio.registrarEvaluacion("T001", "P001", 18);
        SwingUtilities.invokeAndWait(() -> {
            AppSwing app = new AppSwing(servicio);
            try {
                app.setVisible(true);
                JTabbedPane tabs = pestañas(app.getContentPane());
                if (tabs == null || tabs.getTabCount() != 3) throw new AssertionError("Faltan vistas");
                guardar(app, tabs, 0, salida.resolve("01_participantes.png"));
                guardar(app, tabs, 1, salida.resolve("02_talleres.png"));
                guardar(app, tabs, 2, salida.resolve("03_inscripciones.png"));
                tabs.setSelectedIndex(0);
                Container panel = (Container) tabs.getComponentAt(0);
                buscar(panel, JTextField.class, "participante.codigo").setText("P003");
                buscar(panel, JTextField.class, "participante.nombre").setText("Caso inválido");
                buscar(panel, JTextField.class, "participante.edad").setText("0");
                buscar(panel, JTextField.class, "participante.grado").setText("5to secundaria");
                Timer cierre = new Timer(300, evento -> {
                    for (Window ventana : Window.getWindows()) {
                        if (ventana instanceof JDialog dialogo && dialogo.isVisible()) dialogo.dispose();
                    }
                });
                cierre.setRepeats(false);
                cierre.start();
                JButton registrar = boton(panel, "Registrar · RF01");
                if (registrar == null) throw new AssertionError("Falta el botón Registrar");
                registrar.doClick();
                guardar(app, tabs, 0, salida.resolve("04_validacion_edad.png"));
            } catch (Exception error) {
                throw new RuntimeException(error);
            } finally {
                app.dispose();
            }
        });
    }
}
