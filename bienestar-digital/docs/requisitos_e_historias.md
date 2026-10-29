# Requerimientos e historias de usuario

Alcance documentado: 40 requerimientos. Versión implementada: RF01 a RF15. RF16 a RF40 corresponden a ampliaciones planificadas. Los roles describen actores del proceso; esta versión local no implementa autenticación.

## RF01 Registrar participante

El sistema debe permitir registrar participantes con código, nombre, edad y grado.

**HU01.** Como coordinador del taller, necesito registrar participantes con código, nombre, edad y grado para organizar a los asistentes.

**Escenario 1.** Dado P001, Ana ficticia, 16, 5to, cuando el coordinador solicita registrar participante, el sistema registra P001 y lo incluye en la lista de participantes.

**Escenario 2. Código duplicado.** Dada esa condición, cuando el coordinador solicita la operación, el sistema conserva un único participante y rechaza el código repetido.

**Estado:** Implementado y probado; ServicioTalleres.registrarParticipante; opción 1.

## RF02 Listar participantes

El sistema debe permitir consultar la lista de participantes registrados.

**HU02.** Como coordinador del taller, necesito consultar la lista de participantes registrados para revisar los datos disponibles.

**Escenario 1.** Dado Un participante registrado, cuando el coordinador solicita listar participantes, el sistema devuelve la lista completa con código, nombre, edad y grado.

**Escenario 2. Lista vacía.** Dada esa condición, cuando el coordinador solicita la operación, el sistema devuelve una lista vacía y muestra Sin registros.

**Estado:** Implementado y probado; ServicioTalleres.listarParticipantes; opción 2.

## RF03 Buscar participante

El sistema debe permitir buscar un participante mediante su código.

**HU03.** Como coordinador del taller, necesito buscar un participante mediante su código para ubicar sus datos sin recorrer todo el registro.

**Escenario 1.** Dado Código P001 existente, cuando el coordinador solicita buscar participante, el sistema devuelve los datos de P001 y normaliza el código de búsqueda a mayúsculas.

**Escenario 2. Código inexistente.** Dada esa condición, cuando el coordinador solicita la operación, el sistema informa Participante no encontrado.

**Estado:** Implementado y probado; ServicioTalleres.buscarParticipante; opción 3.

## RF04 Actualizar participante

El sistema debe permitir actualizar nombre, edad y grado de un participante.

**HU04.** Como coordinador del taller, necesito actualizar nombre, edad y grado de un participante para corregir sus datos conservando el código.

**Escenario 1.** Dado P001, nombre nuevo, 17, 5to, cuando el coordinador solicita actualizar participante, el sistema conserva el código P001 y guarda el nombre, edad y grado nuevos.

**Escenario 2. Edad 0.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la edad y conserva los datos anteriores.

**Estado:** Implementado y probado; ServicioTalleres.actualizarParticipante; opción 4.

## RF05 Eliminar participante

El sistema debe permitir eliminar un participante sin inscripciones.

**HU05.** Como coordinador del taller, necesito eliminar un participante sin inscripciones para retirar registros que ya no se utilizarán.

**Escenario 1.** Dado P001 sin inscripciones, cuando el coordinador solicita eliminar participante, el sistema elimina P001 y deja de incluirlo en el listado.

**Escenario 2. Participante inscrito.** Dada esa condición, cuando el coordinador solicita la operación, el sistema solicita cancelar sus inscripciones y conserva el participante.

**Estado:** Implementado y probado; ServicioTalleres.eliminarParticipante; opción 5.

## RF06 Crear taller

El sistema debe permitir registrar un taller con código, título, fecha, duración y cupo.

**HU06.** Como coordinador del taller, necesito registrar un taller con código, título, fecha, duración y cupo para planificar una actividad de bienestar digital.

**Escenario 1.** Dado T001, Bienestar digital, 2026-10-19, 60, 2, cuando el coordinador solicita crear taller, el sistema registra T001 con fecha 2026-10-19, duración 60 minutos y cupo 2.

**Escenario 2. Código de taller duplicado.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza el código repetido y conserva el taller original.

**Estado:** Implementado y probado; ServicioTalleres.crearTaller; opción 6.

## RF07 Listar talleres

El sistema debe permitir consultar los talleres registrados.

**HU07.** Como coordinador del taller, necesito consultar los talleres registrados para revisar las actividades disponibles.

**Escenario 1.** Dado Un taller registrado, cuando el coordinador solicita listar talleres, el sistema muestra código, título, fecha, duración y cupo de cada taller registrado.

**Escenario 2. Lista vacía.** Dada esa condición, cuando el coordinador solicita la operación, el sistema devuelve una lista vacía y muestra Sin registros.

**Estado:** Implementado y probado; ServicioTalleres.listarTalleres; opción 7.

## RF08 Buscar taller

El sistema debe permitir buscar un taller mediante su código.

**HU08.** Como coordinador del taller, necesito buscar un taller mediante su código para consultar una actividad específica.

**Escenario 1.** Dado Código T001 existente, cuando el coordinador solicita buscar taller, el sistema devuelve los datos de T001 y normaliza el código de búsqueda a mayúsculas.

**Escenario 2. Código inexistente.** Dada esa condición, cuando el coordinador solicita la operación, el sistema informa Taller no encontrado.

**Estado:** Implementado y probado; ServicioTalleres.buscarTaller; opción 8.

## RF09 Actualizar taller

El sistema debe permitir modificar título, fecha, duración y cupo de un taller.

**HU09.** Como coordinador del taller, necesito modificar título, fecha, duración y cupo de un taller para mantener actualizada su planificación.

**Escenario 1.** Dado T001, título nuevo, 2026-10-20, 90, 3, cuando el coordinador solicita actualizar taller, el sistema guarda el título, fecha 2026-10-20, duración 90 minutos y cupo 3.

**Escenario 2. Cupo menor que el total inscrito.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la reducción y conserva el cupo anterior.

**Estado:** Implementado y probado; ServicioTalleres.actualizarTaller; opción 9.

## RF10 Inscribir participante

El sistema debe permitir inscribir un participante registrado en un taller con cupos.

**HU10.** Como coordinador del taller, necesito inscribir un participante registrado en un taller con cupos para relacionar cada asistente con su actividad.

**Escenario 1.** Dado T001 y P001 existentes, con cupo, cuando el coordinador solicita inscribir participante, el sistema crea una única inscripción de P001 en T001 con asistencia PENDIENTE y nota sin registrar.

**Escenario 2. Inscripción duplicada.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la segunda inscripción y conserva una sola relación.

**Estado:** Implementado y probado; ServicioTalleres.inscribir; opción 10.

## RF11 Listar inscritos por taller

El sistema debe permitir consultar los inscritos de un taller.

**HU11.** Como coordinador del taller, necesito consultar los inscritos de un taller para preparar el control de participación.

**Escenario 1.** Dado T001 con P001 inscrito, cuando el coordinador solicita listar inscritos por taller, el sistema muestra P001, su estado de asistencia y su nota pendiente o registrada.

**Escenario 2. Taller sin inscritos.** Dada esa condición, cuando el coordinador solicita la operación, el sistema devuelve una lista vacía para el taller existente.

**Estado:** Implementado y probado; ServicioTalleres.listarInscritos; opción 11.

## RF12 Cancelar inscripción

El sistema debe permitir cancelar la inscripción de un participante en un taller.

**HU12.** Como coordinador del taller, necesito cancelar la inscripción de un participante en un taller para liberar un cupo cuando corresponda.

**Escenario 1.** Dado T001 con P001 inscrito, cuando el coordinador solicita cancelar inscripción, el sistema elimina la relación T001-P001 y libera el cupo correspondiente.

**Escenario 2. Inscripción inexistente.** Dada esa condición, cuando el coordinador solicita la operación, el sistema informa Inscripción no encontrada y conserva las demás relaciones.

**Estado:** Implementado y probado; ServicioTalleres.cancelarInscripcion; opción 12.

## RF13 Registrar asistencia

El sistema debe permitir marcar asistencia como PRESENTE, AUSENTE o PENDIENTE.

**HU13.** Como coordinador del taller, necesito marcar asistencia como PRESENTE, AUSENTE o PENDIENTE para documentar la participación en el taller.

**Escenario 1.** Dado T001, P001 inscrito, PRESENTE, cuando el coordinador solicita registrar asistencia, el sistema guarda PRESENTE para la inscripción T001-P001.

**Escenario 2. Participante sin inscripción.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la asistencia de un participante no inscrito.

**Estado:** Implementado y probado; ServicioTalleres.registrarAsistencia; opción 13.

## RF14 Registrar evaluación

El sistema debe permitir registrar una calificación entera de 0 a 20 para un inscrito.

**HU14.** Como coordinador del taller, necesito registrar una calificación entera de 0 a 20 para un inscrito para conservar el resultado de una evaluación.

**Escenario 1.** Dado T001, P001 inscrito, nota 18, cuando el coordinador solicita registrar evaluación, el sistema guarda la nota 18 para la inscripción T001-P001.

**Escenario 2. Nota 21.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la nota y conserva el valor anterior o sin registrar.

**Estado:** Implementado y probado; ServicioTalleres.registrarEvaluacion; opción 14.

## RF15 Exportar reporte del taller

El sistema debe permitir exportar un reporte CSV con inscritos, asistencia y notas.

**HU15.** Como coordinador del taller, necesito exportar un reporte CSV con inscritos, asistencia y notas para consultar y compartir el resultado de una actividad.

**Escenario 1.** Dado T001 con una inscripción y una ruta válida, cuando el coordinador solicita exportar reporte del taller, el sistema crea un CSV UTF-8 con cabecera y una fila que incluye taller, participante, asistencia y nota.

**Escenario 2. Taller inexistente.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la exportación y no crea el archivo solicitado.

**Estado:** Implementado y probado; ServicioTalleres.exportarReporte; opción 15.

## RF16 Eliminar taller

El sistema debe permitir eliminar un taller que no tenga inscripciones.

**HU16.** Como coordinador del taller, necesito eliminar un taller que no tenga inscripciones para retirar actividades registradas por error.

**Escenario 1.** Dado Taller existente sin inscritos, cuando el coordinador solicita eliminar taller, el sistema elimina el taller y confirma que ya no figura en el listado.

**Escenario 2. Taller con inscritos.** Dada esa condición, cuando el coordinador solicita la operación, el sistema conserva el taller y solicita resolver las inscripciones.

**Estado:** Planificado, no implementado ni probado.

## RF17 Consultar historial del participante

El sistema debe permitir consultar los talleres en los que está inscrito un participante.

**HU17.** Como coordinador del taller, necesito consultar los talleres en los que está inscrito un participante para revisar su participación acumulada.

**Escenario 1.** Dado Participante inscrito en dos talleres, cuando el coordinador solicita consultar historial del participante, el sistema muestra ambos talleres con su asistencia y evaluación correspondientes.

**Escenario 2. Participante sin inscripciones.** Dada esa condición, cuando el coordinador solicita la operación, el sistema muestra un historial vacío sin crear inscripciones.

**Estado:** Planificado, no implementado ni probado.

## RF18 Filtrar participantes por grado

El sistema debe permitir filtrar los participantes por grado de estudios.

**HU18.** Como coordinador del taller, necesito filtrar los participantes por grado de estudios para organizar los grupos de atención.

**Escenario 1.** Dado Grado 5to con coincidencias, cuando el coordinador solicita filtrar participantes por grado, el sistema muestra únicamente los participantes cuyo grado coincide con 5to.

**Escenario 2. Grado sin coincidencias.** Dada esa condición, cuando el coordinador solicita la operación, el sistema muestra una lista vacía para el filtro solicitado.

**Estado:** Planificado, no implementado ni probado.

## RF19 Filtrar talleres por fechas

El sistema debe permitir consultar talleres dentro de un intervalo de fechas inclusive.

**HU19.** Como coordinador del taller, necesito consultar talleres dentro de un intervalo de fechas inclusive para organizar las actividades de un periodo.

**Escenario 1.** Dado Intervalo con talleres registrados, cuando el coordinador solicita filtrar talleres por fechas, el sistema incluye los talleres comprendidos entre ambas fechas, incluidos los extremos.

**Escenario 2. Fecha inicial posterior a la final.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza el intervalo y solicita corregir las fechas.

**Estado:** Planificado, no implementado ni probado.

## RF20 Consultar cupos disponibles

El sistema debe permitir consultar el cupo disponible de un taller.

**HU20.** Como coordinador del taller, necesito consultar el cupo disponible de un taller para decidir si se pueden aceptar más inscripciones.

**Escenario 1.** Dado Taller de cupo 2 con un inscrito, cuando el coordinador solicita consultar cupos disponibles, el sistema muestra un cupo disponible, calculado como capacidad menos inscritos.

**Escenario 2. Taller con cupo agotado.** Dada esa condición, cuando el coordinador solicita la operación, el sistema muestra cero cupos disponibles.

**Estado:** Planificado, no implementado ni probado.

## RF21 Consultar resumen de asistencia

El sistema debe permitir consultar cantidades de presentes, ausentes y pendientes por taller.

**HU21.** Como coordinador del taller, necesito consultar cantidades de presentes, ausentes y pendientes por taller para revisar el estado de la asistencia.

**Escenario 1.** Dado Taller con los tres estados registrados, cuando el coordinador solicita consultar resumen de asistencia, el sistema muestra los totales de cada estado y su suma coincide con el número de inscritos.

**Escenario 2. Taller sin inscritos.** Dada esa condición, cuando el coordinador solicita la operación, el sistema muestra cero en los tres estados.

**Estado:** Planificado, no implementado ni probado.

## RF22 Consultar promedio de evaluación

El sistema debe permitir consultar el promedio de las notas registradas de un taller.

**HU22.** Como coordinador del taller, necesito consultar el promedio de las notas registradas de un taller para resumir las evaluaciones sin contar notas pendientes.

**Escenario 1.** Dado Notas 10 y 20 y una sin registrar, cuando el coordinador solicita consultar promedio de evaluación, el sistema muestra promedio 15 calculado con dos notas y excluye la evaluación pendiente.

**Escenario 2. Todas las notas sin registrar.** Dada esa condición, cuando el coordinador solicita la operación, el sistema informa Sin evaluaciones y no presenta un promedio cero.

**Estado:** Planificado, no implementado ni probado.

## RF23 Ordenar participantes por nombre

El sistema debe permitir consultar los participantes ordenados alfabéticamente por nombre.

**HU23.** Como coordinador del taller, necesito consultar los participantes ordenados alfabéticamente por nombre para facilitar la búsqueda visual.

**Escenario 1.** Dado Tres nombres registrados fuera de orden, cuando el coordinador solicita ordenar participantes por nombre, el sistema muestra los nombres en orden alfabético sin modificar sus códigos.

**Escenario 2. Nombres repetidos.** Dada esa condición, cuando el coordinador solicita la operación, el sistema ordena por nombre y desempata por código.

**Estado:** Planificado, no implementado ni probado.

## RF24 Importar participantes desde CSV

El sistema debe permitir importar un lote de participantes de un archivo CSV.

**HU24.** Como coordinador del taller, necesito importar un lote de participantes de un archivo CSV para reducir la digitación de registros.

**Escenario 1.** Dado Archivo con códigos únicos y datos válidos, cuando el coordinador solicita importar participantes desde csv, el sistema registra todas las filas válidas del archivo en una sola operación.

**Escenario 2. Una fila con código repetido.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza todo el lote e identifica la fila sin importaciones parciales.

**Estado:** Planificado, no implementado ni probado.

## RF25 Asignar expositor

El sistema debe permitir asignar el nombre del expositor responsable de un taller.

**HU25.** Como coordinador del taller, necesito asignar el nombre del expositor responsable de un taller para identificar a quien dirige la actividad.

**Escenario 1.** Dado Taller existente y nombre de expositor, cuando el coordinador solicita asignar expositor, el sistema guarda el nombre del expositor y lo muestra al consultar el taller.

**Escenario 2. Nombre vacío.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la asignación vacía y conserva el responsable anterior.

**Estado:** Planificado, no implementado ni probado.

## RF26 Registrar institución y sección

El sistema debe permitir asociar una institución educativa y sección a un taller.

**HU26.** Como coordinador del taller, necesito asociar una institución educativa y sección a un taller para identificar el grupo al que se dirige la actividad.

**Escenario 1.** Dado Taller existente, colegio y sección válidos, cuando el coordinador solicita registrar institución y sección, el sistema guarda colegio y sección como datos del taller y los muestra al consultarlo.

**Escenario 2. Institución vacía.** Dada esa condición, cuando el coordinador solicita la operación, el sistema solicita la institución antes de guardar la asociación.

**Estado:** Planificado, no implementado ni probado.

## RF27 Registrar agenda del taller

El sistema debe permitir registrar bloques de actividades y sus duraciones.

**HU27.** Como coordinador del taller, necesito registrar bloques de actividades y sus duraciones para distribuir el tiempo de la sesión.

**Escenario 1.** Dado Bloques que suman como máximo la duración del taller, cuando el coordinador solicita registrar agenda del taller, el sistema guarda los bloques de agenda y muestra el total de minutos asignado.

**Escenario 2. Bloques que exceden la duración.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la agenda y muestra el exceso de minutos.

**Estado:** Planificado, no implementado ni probado.

## RF28 Registrar material educativo

El sistema debe permitir registrar título y referencia de un material educativo.

**HU28.** Como coordinador del taller, necesito registrar título y referencia de un material educativo para organizar los recursos de apoyo.

**Escenario 1.** Dado Título y referencia de material válidos, cuando el coordinador solicita registrar material educativo, el sistema registra el título y referencia del material y permite asociarlo al taller.

**Escenario 2. Título vacío.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza el material sin título.

**Estado:** Planificado, no implementado ni probado.

## RF29 Consultar materiales del taller

El sistema debe permitir consultar los materiales asociados a un taller.

**HU29.** Como coordinador del taller, necesito consultar los materiales asociados a un taller para ubicar los recursos preparados para la actividad.

**Escenario 1.** Dado Taller con materiales asociados, cuando el coordinador solicita consultar materiales del taller, el sistema muestra el título y la referencia de cada material vinculado al taller.

**Escenario 2. Taller sin materiales.** Dada esa condición, cuando el coordinador solicita la operación, el sistema muestra Sin materiales y permite continuar.

**Estado:** Planificado, no implementado ni probado.

## RF30 Crear pregunta dinámica

El sistema debe permitir registrar una pregunta con alternativas y respuesta correcta.

**HU30.** Como coordinador del taller, necesito registrar una pregunta con alternativas y respuesta correcta para preparar una actividad participativa.

**Escenario 1.** Dado Pregunta con alternativas y una respuesta correcta, cuando el coordinador solicita crear pregunta dinámica, el sistema guarda el enunciado, alternativas y respuesta correcta con un código único.

**Escenario 2. Respuesta fuera de las alternativas.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la pregunta y solicita una respuesta válida.

**Estado:** Planificado, no implementado ni probado.

## RF31 Editar pregunta dinámica

El sistema debe permitir modificar el enunciado y las alternativas de una pregunta no aplicada.

**HU31.** Como coordinador del taller, necesito modificar el enunciado y las alternativas de una pregunta no aplicada para corregir el contenido antes de utilizarlo.

**Escenario 1.** Dado Pregunta todavía no utilizada, cuando el coordinador solicita editar pregunta dinámica, el sistema guarda el enunciado y alternativas corregidos de la pregunta no aplicada.

**Escenario 2. Pregunta con respuestas registradas.** Dada esa condición, cuando el coordinador solicita la operación, el sistema conserva la versión aplicada y rechaza su edición.

**Estado:** Planificado, no implementado ni probado.

## RF32 Asociar pregunta a taller

El sistema debe permitir vincular una pregunta del banco a un taller.

**HU32.** Como coordinador del taller, necesito vincular una pregunta del banco a un taller para preparar la dinámica de preguntas de la sesión.

**Escenario 1.** Dado Pregunta y taller existentes, cuando el coordinador solicita asociar pregunta a taller, el sistema agrega la pregunta al conjunto de preguntas del taller.

**Escenario 2. Vínculo duplicado.** Dada esa condición, cuando el coordinador solicita la operación, el sistema conserva una única asociación entre pregunta y taller.

**Estado:** Planificado, no implementado ni probado.

## RF33 Registrar respuesta de participante

El sistema debe permitir registrar la alternativa elegida por un participante inscrito.

**HU33.** Como coordinador del taller, necesito registrar la alternativa elegida por un participante inscrito para conservar sus respuestas durante la dinámica.

**Escenario 1.** Dado Participante inscrito y alternativa existente, cuando el coordinador solicita registrar respuesta de participante, el sistema conserva la alternativa elegida junto al participante, taller y pregunta.

**Escenario 2. Participante no inscrito.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la respuesta sin modificar las respuestas existentes.

**Estado:** Planificado, no implementado ni probado.

## RF34 Calcular resultado de autoevaluación

El sistema debe permitir calcular aciertos y total de preguntas contestadas en un taller.

**HU34.** Como coordinador del taller, necesito calcular aciertos y total de preguntas contestadas en un taller para facilitar una reflexión sobre lo aprendido.

**Escenario 1.** Dado Tres respuestas con dos aciertos, cuando el coordinador solicita calcular resultado de autoevaluación, el sistema muestra dos aciertos de tres respuestas contestadas, sin diagnóstico de salud.

**Escenario 2. Participante sin respuestas.** Dada esa condición, cuando el coordinador solicita la operación, el sistema informa Sin respuestas y no asigna una puntuación ficticia.

**Estado:** Planificado, no implementado ni probado.

## RF35 Registrar compromiso digital

El sistema debe permitir registrar un compromiso de hábito digital de un participante.

**HU35.** Como coordinador del taller, necesito registrar un compromiso de hábito digital de un participante para conservar una acción de mejora elegida voluntariamente.

**Escenario 1.** Dado Participante existente y compromiso no vacío, cuando el coordinador solicita registrar compromiso digital, el sistema guarda el compromiso y lo vincula al participante.

**Escenario 2. Texto vacío.** Dada esa condición, cuando el coordinador solicita la operación, el sistema solicita un compromiso antes de guardarlo.

**Estado:** Planificado, no implementado ni probado.

## RF36 Consultar compromisos

El sistema debe permitir consultar los compromisos registrados por un participante.

**HU36.** Como coordinador del taller, necesito consultar los compromisos registrados por un participante para revisar sus acciones propuestas.

**Escenario 1.** Dado Participante con compromisos, cuando el coordinador solicita consultar compromisos, el sistema muestra los compromisos asociados al participante y sus fechas de registro.

**Escenario 2. Participante sin compromisos.** Dada esa condición, cuando el coordinador solicita la operación, el sistema muestra Sin compromisos.

**Estado:** Planificado, no implementado ni probado.

## RF37 Registrar seguimiento de compromiso

El sistema debe permitir registrar una fecha y observación de seguimiento de un compromiso.

**HU37.** Como coordinador del taller, necesito registrar una fecha y observación de seguimiento de un compromiso para conservar la revisión de la acción propuesta.

**Escenario 1.** Dado Compromiso existente y observación válida, cuando el coordinador solicita registrar seguimiento de compromiso, el sistema agrega la observación fechada al historial del compromiso existente.

**Escenario 2. Compromiso inexistente.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza el seguimiento sin crear un compromiso automáticamente.

**Estado:** Planificado, no implementado ni probado.

## RF38 Registrar satisfacción del taller

El sistema debe permitir registrar una valoración de satisfacción de 1 a 5 por inscrito.

**HU38.** Como coordinador del taller, necesito registrar una valoración de satisfacción de 1 a 5 por inscrito para recoger la opinión de los participantes.

**Escenario 1.** Dado Participante inscrito y valoración 4, cuando el coordinador solicita registrar satisfacción del taller, el sistema guarda una valoración 4 vinculada al inscrito y taller.

**Escenario 2. Valoración fuera de 1 a 5.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la valoración y conserva la respuesta anterior.

**Estado:** Planificado, no implementado ni probado.

## RF39 Generar resumen de satisfacción

El sistema debe permitir consultar distribución y promedio de valoraciones de un taller.

**HU39.** Como coordinador del taller, necesito consultar distribución y promedio de valoraciones de un taller para identificar oportunidades de mejora.

**Escenario 1.** Dado Valoraciones 3 y 5, cuando el coordinador solicita generar resumen de satisfacción, el sistema muestra dos respuestas, promedio 4 y una respuesta en cada valoración 3 y 5.

**Escenario 2. Taller sin valoraciones.** Dada esa condición, cuando el coordinador solicita la operación, el sistema informa Sin respuestas y no presenta un promedio cero.

**Estado:** Planificado, no implementado ni probado.

## RF40 Generar constancia de participación

El sistema debe permitir generar una constancia para un participante con asistencia PRESENTE.

**HU40.** Como coordinador del taller, necesito generar una constancia para un participante con asistencia PRESENTE para acreditar su participación en una actividad registrada.

**Escenario 1.** Dado Inscrito con asistencia PRESENTE, cuando el coordinador solicita generar constancia de participación, el sistema genera una constancia con participante, taller y fecha de la actividad.

**Escenario 2. Inscrito con asistencia AUSENTE o PENDIENTE.** Dada esa condición, cuando el coordinador solicita la operación, el sistema rechaza la constancia hasta que exista asistencia PRESENTE.

**Estado:** Planificado, no implementado ni probado.
