# Matriz de trazabilidad T3: requisitos y controles Swing

La entrega de las semanas 12 y 13 conservó sus criterios de aceptación para el menú de consola. Para T3, `AppSwing` usa el mismo `ServicioTalleres` y añade un control visible para cada requisito implementado. Las tres vistas son pestañas de `JTabbedPane`. Los 50 escenarios de RF01 a RF25, más ocho casos de integridad y archivos, constan en `resultados_pruebas.csv`; la ejecución actual aparece en `evidencias_t3/ejecucion_pruebas.txt`.

| RF / HU | Vista Swing | Botón | Método del servicio |
| --- | --- | --- | --- |
| RF01 / HU01 | Participantes | Registrar | `registrarParticipante` |
| RF02 / HU02 | Participantes | Listar | `listarParticipantes` |
| RF03 / HU03 | Participantes | Buscar | `buscarParticipante` |
| RF04 / HU04 | Participantes | Actualizar | `actualizarParticipante` |
| RF05 / HU05 | Participantes | Eliminar | `eliminarParticipante` |
| RF18 / HU18 | Participantes | Filtrar grado | `filtrarParticipantesPorGrado` |
| RF23 / HU23 | Participantes | Ordenar | `participantesOrdenadosPorNombre` |
| RF24 / HU24 | Participantes | Importar CSV | `importarParticipantes` |
| RF06 / HU06 | Talleres | Crear | `crearTaller` |
| RF07 / HU07 | Talleres | Listar | `listarTalleres` |
| RF08 / HU08 | Talleres | Buscar | `buscarTaller` |
| RF09 / HU09 | Talleres | Actualizar | `actualizarTaller` |
| RF16 / HU16 | Talleres | Eliminar | `eliminarTaller` |
| RF19 / HU19 | Talleres | Buscar fechas | `filtrarTalleresPorFechas` |
| RF20 / HU20 | Talleres | Ver cupos | `cuposDisponibles` |
| RF25 / HU25 | Talleres | Asignar expositor | `asignarExpositor` |
| RF10 / HU10 | Inscripciones y resultados | Inscribir | `inscribir` |
| RF11 / HU11 | Inscripciones y resultados | Listar inscritos | `listarInscritos` |
| RF12 / HU12 | Inscripciones y resultados | Cancelar | `cancelarInscripcion` |
| RF13 / HU13 | Inscripciones y resultados | Asistencia | `registrarAsistencia` |
| RF14 / HU14 | Inscripciones y resultados | Evaluar | `registrarEvaluacion` |
| RF15 / HU15 | Inscripciones y resultados | Exportar CSV | `exportarReporte` |
| RF17 / HU17 | Inscripciones y resultados | Historial | `historialParticipante` |
| RF21 / HU21 | Inscripciones y resultados | Resumen | `resumenAsistencia` |
| RF22 / HU22 | Inscripciones y resultados | Promedio | `promedioEvaluacion` |

RF26 a RF40 permanecen planificados y no tienen botón. Los mensajes de error de la interfaz proceden de las validaciones del dominio; `AppSwing` muestra el rechazo en la barra inferior y en un cuadro de aviso. Las capturas de las tres vistas y de un rechazo por edad inválida están en `evidencias_t3/`.
