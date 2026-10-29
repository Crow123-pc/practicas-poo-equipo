package bienestar;

import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;

/** Pruebas de aceptación sin dependencias externas. Cada caso usa datos ficticios. */
public final class PruebasSistema {
    @FunctionalInterface interface Caso { void ejecutar() throws Exception; }
    static Path base; static int correctos=0; static final List<String> resultados=new ArrayList<>();
    static ServicioTalleres nuevo() throws Exception { return new ServicioTalleres(base.resolve(UUID.randomUUID()+".csv")); }
    static void comprobar(boolean condicion) { if(!condicion) throw new AssertionError("Resultado inesperado"); }
    static void rechazar(Caso caso) throws Exception {
        try { caso.ejecutar(); } catch(IllegalArgumentException e) { return; }
        throw new AssertionError("Se esperaba rechazo de la operación");
    }
    static void probar(String id,String caso,String entrada,String esperado,Caso prueba) throws Exception {
        prueba.ejecutar(); correctos++; resultados.add(ArchivoCsv.linea(id,caso,entrada,esperado,"Conforme"));
        System.out.println("OK "+id+" "+caso);
    }
    static ServicioTalleres preparado() throws Exception {
        var s=nuevo(); s.registrarParticipante("P001","Participante de prueba",16,"5to secundaria");
        s.crearTaller("T001","Bienestar digital",LocalDate.of(2026,10,19),60,2); return s;
    }
    public static void main(String[] args) throws Exception {
        base=Files.createTempDirectory("pruebas-bienestar-");
        resultados.add(ArchivoCsv.linea("requisito","caso","entrada","resultado esperado","resultado observado"));
        probar("RF01","Registro válido","P001, Ana ficticia, 16, 5to","Participante registrado",()->{var s=nuevo();s.registrarParticipante("P001","Ana ficticia",16,"5to");comprobar(s.listarParticipantes().size()==1);});
        probar("RF01","Código duplicado","P001 repetido","Rechazo sin duplicar",()->{var s=preparado();rechazar(()->s.registrarParticipante("p001","Otro",17,"5to"));comprobar(s.listarParticipantes().size()==1);});
        probar("RF02","Listado con datos","1 participante","Lista de un registro",()->comprobar(preparado().listarParticipantes().size()==1));
        probar("RF02","Listado vacío","Sin participantes","Lista vacía",()->comprobar(nuevo().listarParticipantes().isEmpty()));
        probar("RF03","Búsqueda existente","p001","Devuelve P001",()->comprobar(preparado().buscarParticipante("p001").codigo().equals("P001")));
        probar("RF03","Búsqueda inexistente","P999","Participante no encontrado",()->rechazar(()->preparado().buscarParticipante("P999")));
        probar("RF04","Actualización válida","P001, nombre nuevo, 17, 5to","Datos actualizados",()->{var s=preparado();s.actualizarParticipante("P001","Nuevo",17,"5to");comprobar(s.buscarParticipante("P001").edad()==17);});
        probar("RF04","Edad inválida","Edad 0","Rechazo y datos originales",()->{var s=preparado();rechazar(()->s.actualizarParticipante("P001","Nuevo",0,"5to"));comprobar(s.buscarParticipante("P001").edad()==16);});
        probar("RF05","Eliminar sin inscripciones","P001","Participante eliminado",()->{var s=preparado();s.eliminarParticipante("P001");comprobar(s.listarParticipantes().isEmpty());});
        probar("RF05","Eliminar inscrito","P001 inscrito en T001","Rechazo y conserva participante",()->{var s=preparado();s.inscribir("T001","P001");rechazar(()->s.eliminarParticipante("P001"));comprobar(s.listarParticipantes().size()==1);});
        probar("RF06","Crear taller","T001, 2026-10-19, 60, 2","Taller registrado",()->comprobar(preparado().buscarTaller("T001").minutos()==60));
        probar("RF06","Taller duplicado","T001 repetido","Rechazo sin duplicar",()->{var s=preparado();rechazar(()->s.crearTaller("T001","Otro",LocalDate.now(),60,2));comprobar(s.listarTalleres().size()==1);});
        probar("RF07","Listado de talleres","1 taller","Lista de un taller",()->comprobar(preparado().listarTalleres().size()==1));
        probar("RF07","Talleres vacíos","Sin talleres","Lista vacía",()->comprobar(nuevo().listarTalleres().isEmpty()));
        probar("RF08","Buscar taller","t001","Devuelve T001",()->comprobar(preparado().buscarTaller("t001").codigo().equals("T001")));
        probar("RF08","Taller inexistente","T999","Taller no encontrado",()->rechazar(()->preparado().buscarTaller("T999")));
        probar("RF09","Actualizar taller","Título actualizado, duración 90","Cambios guardados",()->{var s=preparado();s.actualizarTaller("T001","Nuevo taller",LocalDate.of(2026,10,20),90,3);comprobar(s.buscarTaller("T001").minutos()==90);});
        probar("RF09","Reducir cupo bajo inscritos","2 inscritos, cupo 1","Rechazo y cupo original",()->{var s=preparado();s.registrarParticipante("P002","Luis ficticio",16,"5to");s.inscribir("T001","P001");s.inscribir("T001","P002");rechazar(()->s.actualizarTaller("T001","Taller",LocalDate.now(),60,1));comprobar(s.buscarTaller("T001").cupo()==2);});
        probar("RF10","Inscripción válida","T001, P001","Una inscripción pendiente",()->{var s=preparado();s.inscribir("T001","P001");comprobar(s.listarInscritos("T001").size()==1);});
        probar("RF10","Inscripción duplicada","T001, P001 repetidos","Rechazo sin duplicar",()->{var s=preparado();s.inscribir("T001","P001");rechazar(()->s.inscribir("T001","P001"));comprobar(s.listarInscritos("T001").size()==1);});
        probar("RF11","Listar inscritos","T001 con P001","Devuelve un inscrito",()->{var s=preparado();s.inscribir("T001","P001");comprobar(s.listarInscritos("T001").get(0).participante().equals("P001"));});
        probar("RF11","Taller sin inscritos","T001 sin inscripciones","Lista vacía",()->comprobar(preparado().listarInscritos("T001").isEmpty()));
        probar("RF12","Cancelar inscripción","T001, P001 inscrito","Inscripción eliminada",()->{var s=preparado();s.inscribir("T001","P001");s.cancelarInscripcion("T001","P001");comprobar(s.listarInscritos("T001").isEmpty());});
        probar("RF12","Cancelar inexistente","T001, P001 sin inscripción","Inscripción no encontrada",()->rechazar(()->preparado().cancelarInscripcion("T001","P001")));
        probar("RF13","Marcar presente","T001, P001, PRESENTE","Estado PRESENTE",()->{var s=preparado();s.inscribir("T001","P001");s.registrarAsistencia("T001","P001",Inscripcion.Asistencia.PRESENTE);comprobar(s.listarInscritos("T001").get(0).asistencia()==Inscripcion.Asistencia.PRESENTE);});
        probar("RF13","Asistencia sin inscripción","T001, P001 no inscrito","Inscripción no encontrada",()->rechazar(()->preparado().registrarAsistencia("T001","P001",Inscripcion.Asistencia.PRESENTE)));
        probar("RF14","Evaluación válida","T001, P001, nota 18","Nota 18 guardada",()->{var s=preparado();s.inscribir("T001","P001");s.registrarEvaluacion("T001","P001",18);comprobar(s.listarInscritos("T001").get(0).nota()==18);});
        probar("RF14","Nota fuera de rango","Nota 21","Rechazo y nota sin registrar",()->{var s=preparado();s.inscribir("T001","P001");rechazar(()->s.registrarEvaluacion("T001","P001",21));comprobar(s.listarInscritos("T001").get(0).nota()==null);});
        probar("RF15","Exportar reporte","T001 con P001","CSV con cabecera y un registro",()->{var s=preparado();s.inscribir("T001","P001");Path r=base.resolve("reporte.csv");s.exportarReporte("T001",r);comprobar(ArchivoCsv.leer(r).size()==2);});
        probar("RF15","Reporte inexistente","T999","Rechazo sin crear archivo",()->{var s=preparado();Path r=base.resolve("no-crear.csv");rechazar(()->s.exportarReporte("T999",r));comprobar(!Files.exists(r));});
        probar("ARCH01","Persistencia y caracteres especiales","José; \"Prueba\" y nota 0","Datos recuperados tras reiniciar",()->{
            Path p=base.resolve("persistencia.csv");var s=new ServicioTalleres(p);
            s.registrarParticipante("P001","José; \"Prueba\"",16,"5to");s.crearTaller("T001","Móvil",LocalDate.of(2026,10,19),60,1);
            s.inscribir("T001","P001");s.registrarEvaluacion("T001","P001",0);
            var reabierto=new ServicioTalleres(p);comprobar(reabierto.buscarParticipante("P001").nombre().equals("José; \"Prueba\""));comprobar(reabierto.listarInscritos("T001").get(0).nota()==0);
        });
        probar("ARCH02","Archivo dañado","Cabecera inválida","Rechazo y archivo intacto",()->{
            Path p=base.resolve("danado.csv");Files.writeString(p,"archivo dañado");
            try{new ServicioTalleres(p);throw new AssertionError();}catch(IOException e){comprobar(Files.readString(p).equals("archivo dañado"));}
        });
        probar("ARCH03","Fallo al guardar","Ruta de destino ocupada por carpeta","Error sin confirmar cambios en memoria",()->{
            Path p=base.resolve("fallo.csv");var s=new ServicioTalleres(p);Files.createDirectory(p);Files.writeString(p.resolve("bloqueo"),"x");
            try{s.registrarParticipante("P001","Prueba",16,"5to");throw new AssertionError();}catch(IOException e){comprobar(s.listarParticipantes().isEmpty());}
        });
        probar("RF10","Cupo agotado","Cupo 1 y segundo participante","Rechazo del segundo inscrito",()->{var s=preparado();s.actualizarTaller("T001","Taller",LocalDate.now(),60,1);s.registrarParticipante("P002","Prueba dos",16,"5to");s.inscribir("T001","P001");rechazar(()->s.inscribir("T001","P002"));comprobar(s.listarInscritos("T001").size()==1);});
        probar("RF14","Límites de evaluación","Notas 0 y 20","Ambos límites aceptados",()->{var s=preparado();s.inscribir("T001","P001");s.registrarEvaluacion("T001","P001",0);comprobar(s.listarInscritos("T001").get(0).nota()==0);s.registrarEvaluacion("T001","P001",20);comprobar(s.listarInscritos("T001").get(0).nota()==20);});
        probar("ARCH04","Integridad referencial","Inscripción sin participante","Carga rechazada",()->{
            Path p=base.resolve("huerfano.csv");Files.write(p,List.of("tipo;codigo;dato1;dato2;dato3;dato4;dato5",ArchivoCsv.linea("I","T001","P001","PENDIENTE","","","")));
            try{new ServicioTalleres(p);throw new AssertionError();}catch(IOException e){comprobar(e.getMessage().contains("referencia"));}
        });
        Path salida=Path.of(args.length==0?"docs/resultados_pruebas.csv":args[0]);ArchivoCsv.guardar(salida,resultados);
        System.out.println("RESULTADO: "+correctos+" casos conformes. Datos temporales: "+base);
    }
}
