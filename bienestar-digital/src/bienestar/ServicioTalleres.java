package bienestar;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import static bienestar.Inscripcion.Asistencia;

/** Reglas del negocio. Cada cambio se guarda antes de confirmarse en memoria. */
public final class ServicioTalleres {
    private static final String CABECERA = "tipo;codigo;dato1;dato2;dato3;dato4;dato5";
    private final Path archivo;
    private Map<String, Participante> participantes = new LinkedHashMap<>();
    private Map<String, Taller> talleres = new LinkedHashMap<>();
    private Map<String, Inscripcion> inscripciones = new LinkedHashMap<>();

    public ServicioTalleres(Path archivo) throws IOException {
        this.archivo = archivo.toAbsolutePath();
        if (Files.exists(this.archivo)) cargar();
    }
    private static String clave(String taller, String participante) { return taller + ":" + participante; }
    private void confirmar(Map<String, Participante> p, Map<String, Taller> t, Map<String, Inscripcion> i) throws IOException {
        List<String> filas = new ArrayList<>(); filas.add(CABECERA);
        p.values().forEach(v -> filas.add(ArchivoCsv.linea("P", v.codigo(), v.nombre(), ""+v.edad(), v.grado(), "", "")));
        t.values().forEach(v -> filas.add(ArchivoCsv.linea("T", v.codigo(), v.titulo(), v.fecha().toString(), ""+v.minutos(), ""+v.cupo(), "")));
        i.values().forEach(v -> filas.add(ArchivoCsv.linea("I", v.taller(), v.participante(), v.asistencia().name(), v.nota()==null?"":""+v.nota(), "", "")));
        ArchivoCsv.guardar(archivo, filas);
        participantes = p; talleres = t; inscripciones = i;
    }
    private void cargar() throws IOException {
        List<String> filas = ArchivoCsv.leer(archivo);
        if (filas.isEmpty() || !filas.get(0).equals(CABECERA)) throw new IOException("Cabecera CSV incorrecta; revise el archivo de datos");
        Map<String, Participante> p = new LinkedHashMap<>(); Map<String, Taller> t = new LinkedHashMap<>();
        Map<String, Inscripcion> ins = new LinkedHashMap<>();
        for (int n=1; n<filas.size(); n++) {
            try {
                List<String> c = ArchivoCsv.separar(filas.get(n));
                if (c.size()!=7) throw new IllegalArgumentException("Se esperaban 7 columnas");
                switch(c.get(0)) {
                    case "P" -> {
                        Participante v = new Participante(c.get(1), c.get(2), Integer.parseInt(c.get(3)), c.get(4));
                        if (p.putIfAbsent(v.codigo(), v)!=null) throw new IllegalArgumentException("Participante duplicado");
                    }
                    case "T" -> {
                        Taller v = new Taller(c.get(1), c.get(2), LocalDate.parse(c.get(3)), Integer.parseInt(c.get(4)), Integer.parseInt(c.get(5)));
                        if (t.putIfAbsent(v.codigo(), v)!=null) throw new IllegalArgumentException("Taller duplicado");
                    }
                    case "I" -> {
                        Inscripcion v = new Inscripcion(c.get(1), c.get(2), Asistencia.valueOf(c.get(3)), c.get(4).isEmpty()?null:Integer.valueOf(c.get(4)));
                        if (ins.putIfAbsent(clave(v.taller(),v.participante()), v)!=null) throw new IllegalArgumentException("Inscripción duplicada");
                    }
                    default -> throw new IllegalArgumentException("Tipo de registro desconocido");
                }
            } catch (RuntimeException | IOException ex) { throw new IOException("No se pudo cargar la línea " + (n+1) + ": " + ex.getMessage(), ex); }
        }
        for (Inscripcion v : ins.values()) {
            if (!p.containsKey(v.participante()) || !t.containsKey(v.taller())) throw new IOException("Inscripción con referencia inexistente");
        }
        for (Taller v : t.values()) {
            long total = ins.values().stream().filter(x -> x.taller().equals(v.codigo())).count();
            if (total > v.cupo()) throw new IOException("Inscripciones superiores al cupo del taller " + v.codigo());
        }
        participantes=p; talleres=t; inscripciones=ins;
    }
    // RF01. Sobrecarga: datos individuales y objeto validado.
    public void registrarParticipante(String codigo, String nombre, int edad, String grado) throws IOException {
        registrarParticipante(new Participante(codigo,nombre,edad,grado));
    }
    public void registrarParticipante(Participante v) throws IOException {
        if (v==null) throw new IllegalArgumentException("Participante nulo");
        if (participantes.containsKey(v.codigo())) throw new IllegalArgumentException("Código de participante duplicado");
        var nuevos = new LinkedHashMap<>(participantes); nuevos.put(v.codigo(),v); confirmar(nuevos,talleres,inscripciones);
    }
    public List<Participante> listarParticipantes() { return List.copyOf(participantes.values()); } // RF02
    public Participante buscarParticipante(String codigo) { // RF03
        Participante v = participantes.get(Validador.codigo(codigo));
        if(v==null) throw new IllegalArgumentException("Participante no encontrado"); return v;
    }
    public void actualizarParticipante(String codigo, String nombre, int edad, String grado) throws IOException { // RF04
        String id=buscarParticipante(codigo).codigo(); Participante v=new Participante(id,nombre,edad,grado);
        var nuevos=new LinkedHashMap<>(participantes); nuevos.put(id,v); confirmar(nuevos,talleres,inscripciones);
    }
    public void eliminarParticipante(String codigo) throws IOException { // RF05
        String id=buscarParticipante(codigo).codigo();
        if(inscripciones.values().stream().anyMatch(v->v.participante().equals(id))) throw new IllegalArgumentException("Cancele las inscripciones antes de eliminar");
        var nuevos=new LinkedHashMap<>(participantes); nuevos.remove(id); confirmar(nuevos,talleres,inscripciones);
    }
    public void crearTaller(String codigo,String titulo,LocalDate fecha,int minutos,int cupo) throws IOException { // RF06
        Taller v=new Taller(codigo,titulo,fecha,minutos,cupo);
        if(talleres.containsKey(v.codigo())) throw new IllegalArgumentException("Código de taller duplicado");
        var nuevos=new LinkedHashMap<>(talleres); nuevos.put(v.codigo(),v); confirmar(participantes,nuevos,inscripciones);
    }
    public List<Taller> listarTalleres() { return List.copyOf(talleres.values()); } // RF07
    public Taller buscarTaller(String codigo) { // RF08
        Taller v=talleres.get(Validador.codigo(codigo));
        if(v==null) throw new IllegalArgumentException("Taller no encontrado"); return v;
    }
    public void actualizarTaller(String codigo,String titulo,LocalDate fecha,int minutos,int cupo) throws IOException { // RF09
        String id=buscarTaller(codigo).codigo(); Taller v=new Taller(id,titulo,fecha,minutos,cupo);
        if(listarInscritos(id).size()>cupo) throw new IllegalArgumentException("El cupo no puede ser menor al número de inscritos");
        var nuevos=new LinkedHashMap<>(talleres); nuevos.put(id,v); confirmar(participantes,nuevos,inscripciones);
    }
    public void inscribir(String taller,String participante) throws IOException { // RF10
        String t=buscarTaller(taller).codigo(), p=buscarParticipante(participante).codigo(), id=clave(t,p);
        if(inscripciones.containsKey(id)) throw new IllegalArgumentException("El participante ya está inscrito");
        if(listarInscritos(t).size()>=buscarTaller(t).cupo()) throw new IllegalArgumentException("Taller sin cupos disponibles");
        var nuevos=new LinkedHashMap<>(inscripciones); nuevos.put(id,new Inscripcion(t,p,Asistencia.PENDIENTE,null));
        confirmar(participantes,talleres,nuevos);
    }
    public List<Inscripcion> listarInscritos(String taller) { // RF11
        String t=buscarTaller(taller).codigo(); return inscripciones.values().stream().filter(v->v.taller().equals(t)).toList();
    }
    private Inscripcion buscarInscripcion(String taller,String participante) {
        String t=Validador.codigo(taller), p=Validador.codigo(participante);
        Inscripcion v=inscripciones.get(clave(t,p));
        if(v==null) throw new IllegalArgumentException("Inscripción no encontrada"); return v;
    }
    public void cancelarInscripcion(String taller,String participante) throws IOException { // RF12
        Inscripcion v=buscarInscripcion(taller,participante); var nuevos=new LinkedHashMap<>(inscripciones);
        nuevos.remove(clave(v.taller(),v.participante())); confirmar(participantes,talleres,nuevos);
    }
    public void registrarAsistencia(String taller,String participante,Asistencia estado) throws IOException { // RF13
        Inscripcion v=buscarInscripcion(taller,participante);
        var nuevos=new LinkedHashMap<>(inscripciones);
        nuevos.put(clave(v.taller(),v.participante()),new Inscripcion(v.taller(),v.participante(),estado,v.nota()));
        confirmar(participantes,talleres,nuevos);
    }
    public void registrarEvaluacion(String taller,String participante,int nota) throws IOException { // RF14
        Inscripcion v=buscarInscripcion(taller,participante);
        var nuevos=new LinkedHashMap<>(inscripciones);
        nuevos.put(clave(v.taller(),v.participante()),new Inscripcion(v.taller(),v.participante(),v.asistencia(),nota));
        confirmar(participantes,talleres,nuevos);
    }
    public Path exportarReporte(String taller,Path destino) throws IOException { // RF15
        Taller t=buscarTaller(taller);
        if(destino.toAbsolutePath().normalize().equals(archivo.normalize())) throw new IllegalArgumentException("El reporte debe guardarse en otro archivo");
        List<String> filas=new ArrayList<>();
        filas.add(ArchivoCsv.linea("taller","titulo","fecha","participante","nombre","grado","asistencia","nota"));
        for(Inscripcion i:listarInscritos(t.codigo())) {
            Participante p=buscarParticipante(i.participante());
            // Prefijo de texto para impedir fórmulas al abrir el reporte en una hoja de cálculo.
            filas.add(ArchivoCsv.linea(t.codigo(),celdaSegura(t.titulo()),t.fecha().toString(),p.codigo(),celdaSegura(p.nombre()),celdaSegura(p.grado()),i.asistencia().name(),i.nota()==null?"":""+i.nota()));
        }
        ArchivoCsv.guardar(destino,filas); return destino.toAbsolutePath();
    }
    private static String celdaSegura(String valor) { return "=+-@".indexOf(valor.charAt(0))>=0 ? "'"+valor : valor; }
}
