package bienestar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/** Interfaz visual de los talleres; conserva las reglas en ServicioTalleres. */
public final class AppSwing extends JFrame {
    private static final Color AZUL = new Color(28, 61, 91);
    private static final Color FONDO = new Color(246, 249, 251);
    private final ServicioTalleres servicio;
    private final JLabel estado = new JLabel("Listo. Los cambios se guardan automáticamente.");

    @FunctionalInterface
    private interface Operacion { void ejecutar() throws IOException; }
    private static final class OperacionCancelada extends RuntimeException { }

    public AppSwing(ServicioTalleres servicio) {
        super("Bienestar digital | Gestión de talleres");
        this.servicio = servicio;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 660));
        setSize(1160, 760);
        setLocationRelativeTo(null);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setName("vistas");
        pestañas.setFont(pestañas.getFont().deriveFont(Font.BOLD, 14f));
        pestañas.addTab("Participantes", participantes());
        pestañas.addTab("Talleres", talleres());
        pestañas.addTab("Inscripciones y resultados", inscripciones());

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(AZUL);
        cabecera.setBorder(BorderFactory.createEmptyBorder(18, 22, 17, 22));
        JLabel titulo = new JLabel("Gestión de talleres educativos sobre bienestar digital");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        cabecera.add(titulo, BorderLayout.CENTER);

        estado.setBorder(BorderFactory.createEmptyBorder(9, 17, 9, 17));
        estado.setOpaque(true);
        estado.setBackground(new Color(232, 240, 245));
        estado.setForeground(AZUL);

        add(cabecera, BorderLayout.NORTH);
        add(pestañas, BorderLayout.CENTER);
        add(estado, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Path archivo = Path.of(args.length == 0 ? "datos/sistema.csv" : args[0]);
                new AppSwing(new ServicioTalleres(archivo)).setVisible(true);
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, "No se pudo iniciar: " + error.getMessage(),
                        "Bienestar digital", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
    }

    private static JPanel formulario(Object... pares) {
        JPanel panel = new JPanel(new GridLayout(0, 4, 9, 8));
        panel.setOpaque(false);
        for (int i = 0; i < pares.length; i += 2) {
            panel.add(new JLabel((String) pares[i]));
            panel.add((java.awt.Component) pares[i + 1]);
        }
        if ((pares.length / 2) % 2 != 0) {
            panel.add(new JLabel());
            panel.add(new JLabel());
        }
        return panel;
    }

    private static JPanel botones() {
        JPanel panel = new JPanel(new GridLayout(0, 4, 8, 8));
        panel.setOpaque(false);
        return panel;
    }

    private JPanel vista(String titulo, String descripcion, JPanel campos, JPanel acciones, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout(0, 13));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JPanel superior = new JPanel(new BorderLayout(0, 10));
        superior.setOpaque(false);
        JPanel encabezado = new JPanel(new GridLayout(0, 1, 0, 2));
        encabezado.setOpaque(false);
        JLabel nombre = new JLabel(titulo);
        nombre.setForeground(AZUL);
        nombre.setFont(nombre.getFont().deriveFont(Font.BOLD, 17f));
        encabezado.add(nombre);
        encabezado.add(new JLabel(descripcion));
        superior.add(encabezado, BorderLayout.NORTH);
        JPanel controles = new JPanel(new BorderLayout(0, 12));
        controles.setOpaque(false);
        controles.add(campos, BorderLayout.NORTH);
        controles.add(acciones, BorderLayout.CENTER);
        superior.add(controles, BorderLayout.CENTER);

        tabla.setRowHeight(25);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private void boton(JPanel panel, String texto, Operacion operacion) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.addActionListener(evento -> {
            try {
                operacion.ejecutar();
                estado.setForeground(AZUL);
                estado.setText("Operación completada: " + texto);
            } catch (OperacionCancelada cancelada) {
                estado.setForeground(AZUL);
                estado.setText("Operación cancelada: " + texto);
            } catch (IllegalArgumentException | DateTimeParseException | IOException error) {
                estado.setForeground(new Color(156, 40, 40));
                estado.setText("Operación rechazada: " + error.getMessage());
                JOptionPane.showMessageDialog(this, error.getMessage(), "Revise los datos",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        panel.add(boton);
    }

    private static String texto(JTextField campo) { return campo.getText().strip(); }
    private static JTextField nombrar(JTextField campo, String nombre) {
        campo.setName(nombre);
        return campo;
    }
    private static int entero(JTextField campo, String nombre) {
        try { return Integer.parseInt(texto(campo)); }
        catch (NumberFormatException error) { throw new IllegalArgumentException(nombre + ": ingrese un número entero"); }
    }
    private static LocalDate fecha(JTextField campo) {
        try { return LocalDate.parse(texto(campo)); }
        catch (DateTimeParseException error) { throw new IllegalArgumentException("Fecha inválida: use AAAA-MM-DD"); }
    }
    private static void limpiar(DefaultTableModel modelo) { modelo.setRowCount(0); }

    private JPanel participantes() {
        JTextField codigo = nombrar(new JTextField(), "participante.codigo");
        JTextField nombre = nombrar(new JTextField(), "participante.nombre");
        JTextField edad = nombrar(new JTextField(), "participante.edad");
        JTextField grado = nombrar(new JTextField(), "participante.grado");
        DefaultTableModel modelo = modelo("Código", "Nombre", "Edad", "Grado");
        JTable tabla = new JTable(modelo);
        JPanel acciones = botones();

        boton(acciones, "Registrar · RF01", () -> {
            servicio.registrarParticipante(texto(codigo), texto(nombre), entero(edad, "Edad"), texto(grado));
            mostrarParticipantes(modelo, servicio.listarParticipantes());
        });
        boton(acciones, "Listar · RF02", () -> mostrarParticipantes(modelo, servicio.listarParticipantes()));
        boton(acciones, "Buscar · RF03", () ->
                mostrarParticipantes(modelo, List.of(servicio.buscarParticipante(texto(codigo)))));
        boton(acciones, "Actualizar · RF04", () -> {
            servicio.actualizarParticipante(texto(codigo), texto(nombre), entero(edad, "Edad"), texto(grado));
            mostrarParticipantes(modelo, servicio.listarParticipantes());
        });
        boton(acciones, "Eliminar · RF05", () -> {
            servicio.eliminarParticipante(texto(codigo));
            mostrarParticipantes(modelo, servicio.listarParticipantes());
        });
        boton(acciones, "Filtrar grado · RF18", () ->
                mostrarParticipantes(modelo, servicio.filtrarParticipantesPorGrado(texto(grado))));
        boton(acciones, "Ordenar · RF23", () ->
                mostrarParticipantes(modelo, servicio.participantesOrdenadosPorNombre()));
        boton(acciones, "Importar CSV · RF24", () -> {
            JFileChooser selector = new JFileChooser();
            if (selector.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) throw new OperacionCancelada();
            int total = servicio.importarParticipantes(selector.getSelectedFile().toPath());
            mostrarParticipantes(modelo, servicio.listarParticipantes());
            JOptionPane.showMessageDialog(this, "Participantes importados: " + total,
                    "Importación CSV", JOptionPane.INFORMATION_MESSAGE);
        });

        mostrarParticipantes(modelo, servicio.listarParticipantes());
        return vista("Participantes", "Registro, búsqueda, actualización e importación CSV.",
                formulario("Código", codigo, "Nombre", nombre, "Edad", edad, "Grado", grado), acciones, tabla);
    }

    private static void mostrarParticipantes(DefaultTableModel modelo, List<Participante> datos) {
        limpiar(modelo);
        for (Participante p : datos) modelo.addRow(new Object[]{p.codigo(), p.nombre(), p.edad(), p.grado()});
    }

    private JPanel talleres() {
        JTextField codigo = nombrar(new JTextField(), "taller.codigo");
        JTextField titulo = nombrar(new JTextField(), "taller.titulo");
        JTextField fecha = nombrar(new JTextField("2026-10-19"), "taller.fecha");
        JTextField duracion = nombrar(new JTextField("60"), "taller.duracion");
        JTextField cupo = nombrar(new JTextField("30"), "taller.cupo");
        JTextField expositor = nombrar(new JTextField(), "taller.expositor");
        JTextField desde = nombrar(new JTextField("2026-10-01"), "taller.desde");
        JTextField hasta = nombrar(new JTextField("2026-10-31"), "taller.hasta");
        DefaultTableModel modelo = modelo("Código", "Título", "Fecha", "Minutos", "Cupo", "Expositor");
        JTable tabla = new JTable(modelo);
        JPanel acciones = botones();

        boton(acciones, "Crear · RF06", () -> {
            servicio.crearTaller(texto(codigo), texto(titulo), fecha(fecha), entero(duracion, "Duración"), entero(cupo, "Cupo"));
            mostrarTalleres(modelo, servicio.listarTalleres());
        });
        boton(acciones, "Listar · RF07", () -> mostrarTalleres(modelo, servicio.listarTalleres()));
        boton(acciones, "Buscar · RF08", () ->
                mostrarTalleres(modelo, List.of(servicio.buscarTaller(texto(codigo)))));
        boton(acciones, "Actualizar · RF09", () -> {
            servicio.actualizarTaller(texto(codigo), texto(titulo), fecha(fecha), entero(duracion, "Duración"), entero(cupo, "Cupo"));
            mostrarTalleres(modelo, servicio.listarTalleres());
        });
        boton(acciones, "Eliminar · RF16", () -> {
            servicio.eliminarTaller(texto(codigo));
            mostrarTalleres(modelo, servicio.listarTalleres());
        });
        boton(acciones, "Buscar fechas · RF19", () ->
                mostrarTalleres(modelo, servicio.filtrarTalleresPorFechas(fecha(desde), fecha(hasta))));
        boton(acciones, "Ver cupos · RF20", () -> {
            Taller t = servicio.buscarTaller(texto(codigo));
            mostrarTalleres(modelo, List.of(t));
            JOptionPane.showMessageDialog(this, "Cupos disponibles: " + servicio.cuposDisponibles(t.codigo()),
                    "Cupo del taller", JOptionPane.INFORMATION_MESSAGE);
        });
        boton(acciones, "Asignar expositor · RF25", () -> {
            servicio.asignarExpositor(texto(codigo), texto(expositor));
            mostrarTalleres(modelo, servicio.listarTalleres());
        });

        mostrarTalleres(modelo, servicio.listarTalleres());
        return vista("Talleres", "Gestión de actividades, fechas, cupos y expositor.",
                formulario("Código", codigo, "Título", titulo, "Fecha AAAA-MM-DD", fecha,
                        "Duración en minutos", duracion, "Cupo", cupo, "Expositor", expositor,
                        "Desde", desde, "Hasta", hasta), acciones, tabla);
    }

    private static void mostrarTalleres(DefaultTableModel modelo, List<Taller> datos) {
        limpiar(modelo);
        for (Taller t : datos) modelo.addRow(new Object[]{t.codigo(), t.titulo(), t.fecha(),
                t.minutos(), t.cupo(), t.expositor()});
    }

    private JPanel inscripciones() {
        JTextField taller = nombrar(new JTextField(), "inscripcion.taller");
        JTextField participante = nombrar(new JTextField(), "inscripcion.participante");
        JTextField nota = nombrar(new JTextField(), "inscripcion.nota");
        JComboBox<Inscripcion.Asistencia> asistencia = new JComboBox<>(Inscripcion.Asistencia.values());
        DefaultTableModel modelo = modelo("Taller", "Participante", "Nombre", "Asistencia", "Nota");
        JTable tabla = new JTable(modelo);
        JPanel acciones = botones();

        boton(acciones, "Inscribir · RF10", () -> {
            servicio.inscribir(texto(taller), texto(participante));
            mostrarInscritos(modelo, texto(taller));
        });
        boton(acciones, "Listar inscritos · RF11", () -> mostrarInscritos(modelo, texto(taller)));
        boton(acciones, "Cancelar · RF12", () -> {
            servicio.cancelarInscripcion(texto(taller), texto(participante));
            mostrarInscritos(modelo, texto(taller));
        });
        boton(acciones, "Asistencia · RF13", () -> {
            servicio.registrarAsistencia(texto(taller), texto(participante),
                    (Inscripcion.Asistencia) asistencia.getSelectedItem());
            mostrarInscritos(modelo, texto(taller));
        });
        boton(acciones, "Evaluar · RF14", () -> {
            servicio.registrarEvaluacion(texto(taller), texto(participante), entero(nota, "Nota"));
            mostrarInscritos(modelo, texto(taller));
        });
        boton(acciones, "Historial · RF17", () -> {
            String id = servicio.buscarParticipante(texto(participante)).codigo();
            limpiar(modelo);
            for (ServicioTalleres.Historial h : servicio.historialParticipante(id)) {
                modelo.addRow(new Object[]{h.taller().codigo(), id, servicio.buscarParticipante(id).nombre(),
                        h.asistencia(), h.nota() == null ? "Pendiente" : h.nota()});
            }
        });
        boton(acciones, "Resumen · RF21", () -> JOptionPane.showMessageDialog(this,
                servicio.resumenAsistencia(texto(taller)), "Asistencia", JOptionPane.INFORMATION_MESSAGE));
        boton(acciones, "Promedio · RF22", () -> {
            var promedio = servicio.promedioEvaluacion(texto(taller));
            String mensaje = promedio.isPresent()
                    ? String.format(java.util.Locale.ROOT, "Promedio: %.2f", promedio.getAsDouble())
                    : "Sin evaluaciones registradas";
            JOptionPane.showMessageDialog(this, mensaje, "Evaluación", JOptionPane.INFORMATION_MESSAGE);
        });
        boton(acciones, "Exportar CSV · RF15", () -> {
            JFileChooser selector = new JFileChooser();
            selector.setSelectedFile(new java.io.File("reporte-taller.csv"));
            if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) throw new OperacionCancelada();
            Path destino = selector.getSelectedFile().toPath();
            if (java.nio.file.Files.exists(destino) && JOptionPane.showConfirmDialog(this,
                    "El archivo ya existe. ¿Reemplazarlo?", "Confirmar exportación",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) throw new OperacionCancelada();
            servicio.exportarReporte(texto(taller), destino);
            JOptionPane.showMessageDialog(this, "Reporte guardado en: " + destino.toAbsolutePath(),
                    "Exportación", JOptionPane.INFORMATION_MESSAGE);
        });

        if (!servicio.listarTalleres().isEmpty()) {
            taller.setText(servicio.listarTalleres().get(0).codigo());
            mostrarInscritos(modelo, texto(taller));
        }
        return vista("Inscripciones y resultados", "Inscriba participantes, registre asistencia y consulte resultados.",
                formulario("Código de taller", taller, "Código de participante", participante,
                        "Asistencia", asistencia, "Nota de 0 a 20", nota), acciones, tabla);
    }

    private void mostrarInscritos(DefaultTableModel modelo, String taller) {
        limpiar(modelo);
        for (Inscripcion i : servicio.listarInscritos(taller)) {
            Participante p = servicio.buscarParticipante(i.participante());
            modelo.addRow(new Object[]{i.taller(), i.participante(), p.nombre(), i.asistencia(),
                    i.nota() == null ? "Pendiente" : i.nota()});
        }
    }
}
