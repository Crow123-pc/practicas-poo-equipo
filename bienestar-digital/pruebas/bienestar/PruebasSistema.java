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
        probar("RF16","Eliminar taller sin inscritos","T001 sin inscripciones","Taller eliminado",()->{
            var s=preparado();s.eliminarTaller("T001");comprobar(s.listarTalleres().isEmpty());
        });
        probar("RF16","Conservar taller con inscritos","T001 con P001","Rechazo sin eliminar",()->{
            var s=preparado();s.inscribir("T001","P001");rechazar(()->s.eliminarTaller("T001"));comprobar(s.listarTalleres().size()==1);
        });
        probar("RF17","Historial de dos talleres","P001 inscrito en T001 y T002","Dos talleres con asistencia y nota",()->{
            var s=preparado();s.crearTaller("T002","Segundo taller",LocalDate.of(2026,10,20),60,2);
            s.inscribir("T001","P001");s.inscribir("T002","P001");
            s.registrarAsistencia("T001","P001",Inscripcion.Asistencia.PRESENTE);s.registrarEvaluacion("T002","P001",18);
            var historial=s.historialParticipante("P001");
            comprobar(historial.size()==2 && historial.get(0).asistencia()==Inscripcion.Asistencia.PRESENTE
                    && historial.get(1).taller().codigo().equals("T002") && historial.get(1).nota()==18);
        });
        probar("RF17","Historial vacío","P001 sin inscripciones","Lista vacía",()->comprobar(preparado().historialParticipante("P001").isEmpty()));
        probar("RF18","Filtrar por grado","5to secundaria con coincidencia","Solo grado solicitado",()->{
            var s=preparado();s.registrarParticipante("P002","Otra prueba",15,"4to");
            comprobar(s.filtrarParticipantesPorGrado("5TO SECUNDARIA").size()==1);
        });
        probar("RF18","Grado sin coincidencias","3ro","Lista vacía",()->comprobar(preparado().filtrarParticipantesPorGrado("3ro").isEmpty()));
        probar("RF19","Intervalo inclusivo","2026-10-19 a 2026-10-20","Incluye ambos extremos",()->{
            var s=preparado();s.crearTaller("T002","Segundo",LocalDate.of(2026,10,20),60,2);
            s.crearTaller("T003","Tercero",LocalDate.of(2026,10,21),60,2);
            var filtrados=s.filtrarTalleresPorFechas(LocalDate.of(2026,10,19),LocalDate.of(2026,10,20));
            comprobar(filtrados.size()==2 && filtrados.get(0).codigo().equals("T001") && filtrados.get(1).codigo().equals("T002"));
        });
        probar("RF19","Intervalo invertido","Inicio posterior al fin","Rechazo",()->rechazar(()->preparado().filtrarTalleresPorFechas(LocalDate.of(2026,10,21),LocalDate.of(2026,10,19))));
        probar("RF20","Un cupo disponible","Cupo 2 con un inscrito","Devuelve 1",()->{
            var s=preparado();s.inscribir("T001","P001");comprobar(s.cuposDisponibles("T001")==1);
        });
        probar("RF20","Cupo agotado","Cupo 2 con dos inscritos","Devuelve 0",()->{
            var s=preparado();s.registrarParticipante("P002","Prueba dos",16,"5to");
            s.inscribir("T001","P001");s.inscribir("T001","P002");comprobar(s.cuposDisponibles("T001")==0);
        });
        probar("RF21","Tres estados de asistencia","Presente, ausente y pendiente","Un registro por estado",()->{
            var s=preparado();s.actualizarTaller("T001","Bienestar",LocalDate.of(2026,10,19),60,3);
            s.registrarParticipante("P002","Prueba dos",16,"5to");s.registrarParticipante("P003","Prueba tres",16,"5to");
            for(String p:List.of("P001","P002","P003")) s.inscribir("T001",p);
            s.registrarAsistencia("T001","P001",Inscripcion.Asistencia.PRESENTE);
            s.registrarAsistencia("T001","P002",Inscripcion.Asistencia.AUSENTE);
            var r=s.resumenAsistencia("T001");comprobar(r.presentes()==1 && r.ausentes()==1 && r.pendientes()==1);
        });
        probar("RF21","Resumen sin inscritos","T001 vacío","Tres contadores en cero",()->{
            var r=preparado().resumenAsistencia("T001");comprobar(r.presentes()==0 && r.ausentes()==0 && r.pendientes()==0);
        });
        probar("RF22","Promedio sin pendientes","Notas 10 y 20; una pendiente","Promedio 15",()->{
            var s=preparado();s.actualizarTaller("T001","Bienestar",LocalDate.of(2026,10,19),60,3);
            s.registrarParticipante("P002","Prueba dos",16,"5to");s.registrarParticipante("P003","Prueba tres",16,"5to");
            for(String p:List.of("P001","P002","P003")) s.inscribir("T001",p);
            s.registrarEvaluacion("T001","P001",10);s.registrarEvaluacion("T001","P002",20);
            comprobar(s.promedioEvaluacion("T001").orElseThrow()==15.0);
        });
        probar("RF22","Sin evaluaciones","Notas pendientes","Sin promedio",()->{
            var s=preparado();s.inscribir("T001","P001");comprobar(s.promedioEvaluacion("T001").isEmpty());
        });
        probar("RF23","Orden alfabético","Tres nombres fuera de orden","Álvaro, Bruno, Zoe",()->{
            var s=nuevo();s.registrarParticipante("P003","Zoe",16,"5to");
            s.registrarParticipante("P002","Bruno",16,"5to");s.registrarParticipante("P001","Álvaro",16,"5to");
            comprobar(s.participantesOrdenadosPorNombre().stream().map(Participante::codigo).toList().equals(List.of("P001","P002","P003")));
        });
        probar("RF23","Desempate por código","Dos participantes Ana","P001 antes de P002",()->{
            var s=nuevo();s.registrarParticipante("P002","Ana",16,"5to");s.registrarParticipante("P001","Ana",16,"5to");
            comprobar(s.participantesOrdenadosPorNombre().get(0).codigo().equals("P001"));
        });
        probar("RF24","Importación completa","Dos participantes válidos","Ambos registrados",()->{
            var s=preparado();Path lote=base.resolve("lote-valido.csv");
            Files.write(lote,List.of("codigo;nombre;edad;grado",ArchivoCsv.linea("P002","María; Prueba","17","5to"),ArchivoCsv.linea("P003","José","16","4to")));
            comprobar(s.importarParticipantes(lote)==2 && s.listarParticipantes().size()==3
                    && s.buscarParticipante("P002").nombre().equals("María; Prueba"));
        });
        probar("RF24","Duplicado rechaza todo","P002 válido y P001 repetido en línea 3","Sin importación parcial",()->{
            var s=preparado();Path lote=base.resolve("lote-duplicado.csv");
            Files.write(lote,List.of("codigo;nombre;edad;grado",ArchivoCsv.linea("P002","Prueba dos","16","5to"),ArchivoCsv.linea("P001","Repetido","16","5to")));
            try{s.importarParticipantes(lote);throw new AssertionError();}
            catch(IllegalArgumentException ex){comprobar(ex.getMessage().contains("Línea 3") && s.listarParticipantes().size()==1);}
        });
        probar("RF25","Asignar expositor","T001 y nombre válido","Nombre visible y persistente",()->{
            Path p=base.resolve("expositor.csv");var s=new ServicioTalleres(p);
            s.crearTaller("T001","Bienestar",LocalDate.of(2026,10,19),60,2);s.asignarExpositor("T001","María Prueba");
            comprobar(new ServicioTalleres(p).buscarTaller("T001").expositor().equals("María Prueba")
                    && s.buscarTaller("T001").toString().contains("María Prueba"));
        });
        probar("RF25","Expositor vacío","Nombre vacío","Conserva expositor anterior",()->{
            var s=preparado();s.asignarExpositor("T001","María Prueba");
            rechazar(()->s.asignarExpositor("T001"," "));comprobar(s.buscarTaller("T001").expositor().equals("María Prueba"));
        });
        probar("ARCH05","Compatibilidad con CSV anterior","Taller con sexta columna vacía","Carga y actualización conservadas",()->{
            Path p=base.resolve("anterior.csv");Files.write(p,List.of("tipo;codigo;dato1;dato2;dato3;dato4;dato5",
                    ArchivoCsv.linea("T","T001","Bienestar","2026-10-19","60","2","")));
            var s=new ServicioTalleres(p);comprobar(s.buscarTaller("T001").expositor().isEmpty());
            s.asignarExpositor("T001","Expositor prueba");
            comprobar(new ServicioTalleres(p).buscarTaller("T001").expositor().equals("Expositor prueba"));
        });
        probar("ARCH06","Proteger archivo de datos","Reporte dirigido al archivo principal","Rechazo y datos intactos",()->{
            Path p=base.resolve("principal.csv");var s=new ServicioTalleres(p);
            s.crearTaller("T001","Bienestar",LocalDate.of(2026,10,19),60,2);
            rechazar(()->s.exportarReporte("T001",p));
            comprobar(new ServicioTalleres(p).buscarTaller("T001").codigo().equals("T001"));
        });
        Path salida=Path.of(args.length==0?"docs/resultados_pruebas.csv":args[0]);ArchivoCsv.guardar(salida,resultados);
        System.out.println("RESULTADO: "+correctos+" casos conformes. Datos temporales: "+base);
    }
}
