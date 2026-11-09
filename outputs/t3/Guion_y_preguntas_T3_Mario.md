# Guion de sustentación T3 - Mario Antonio Saucedo Guerrero

La presentación de 15 diapositivas es un apoyo visual. Ensaya con el proyecto abierto en NetBeans y responde con tus palabras. Las notas del expositor de cada diapositiva también incluyen un guion breve y la fuente de los datos.

## Recorrido sugerido

1. **Problema y objetivo:** explica que el proyecto académico organiza los registros de talleres educativos sobre bienestar digital. La charla escolar aportó el contexto social; el software no se usó en esa charla.
2. **Evolución semanal:** semanas 1-5, Git y Java inicial; 6-9, elección, análisis e historias; 10-11, CSV, 15 funciones y actividad social; 12-13, verificación y diez funciones nuevas. La interfaz Swing se añadió para preparar T3.
3. **Alcance:** se documentaron 40 requisitos e historias; RF01-RF25 están implementados; RF26-RF40 siguen planificados. No digas que las 40 funciones están terminadas.
4. **Diseño:** `AppSwing` construye la ventana; `ServicioTalleres` valida y coordina; `Participante`, `Taller` e `Inscripcion` representan datos; `ArchivoCsv` lee y guarda.
5. **Demostración:** sigue los cinco pasos de la diapositiva 14. Usa códigos nuevos si P001 o T001 ya existen. Muestra el rechazo de una edad inválida y vuelve a abrir la aplicación con el mismo archivo para verificar persistencia.
6. **Responsabilidad social:** explica el tema, la charla de una hora, las preguntas y dinámicas, y las evidencias. El resultado comprobable es la sesión realizada; no afirmes un cambio duradero de hábitos sin medición.

## Preguntas técnicas probables

**¿Qué es una clase y qué es un objeto en tu programa?**  
`ServicioTalleres` es una clase que define operaciones y estado. `new ServicioTalleres(ruta)` crea un objeto concreto que administra los datos de un archivo. `Participante` es un `record`; cada instancia representa a una persona inscrita en el sistema.

**¿Dónde aplicas encapsulamiento?**  
Los `Map` de `ServicioTalleres` son privados. La interfaz llama a métodos públicos, como `registrarParticipante` o `inscribir`, y no modifica las colecciones directamente. Así el servicio verifica las reglas antes de guardar.

**¿Dónde hay herencia?**  
`AppSwing extends JFrame`: hereda el comportamiento de una ventana Swing. Las entidades del dominio no tienen una jerarquía artificial, porque participante y taller cumplen funciones distintas.

**¿Qué diferencia hay entre asociación, agregación y composición aquí?**  
Una inscripción se asocia con un participante y un taller mediante sus códigos. El servicio agrega entidades que administra en sus colecciones. La ventana crea y contiene sus componentes Swing, incluido el panel de tres pestañas. Explica el alcance de cada relación; no afirmes que el servicio es dueño exclusivo de cada `Participante`, porque también pueden crearse fuera de él.

**¿Qué colecciones usas y por qué?**  
`LinkedHashMap` permite localizar entidades por código y mantiene un orden estable de inserción. Los listados y resultados se presentan como `List`. La importación CSV prepara una copia del mapa antes de confirmar cambios.

**¿Cómo manejas errores y excepciones?**  
Las entidades y el servicio rechazan datos inválidos con `IllegalArgumentException`; los fallos de archivos se propagan como `IOException`. La interfaz captura errores de entrada y muestra un mensaje visible. Las pruebas incluyen duplicados, cupos agotados, archivo dañado y fallo al guardar.

**¿Cómo protege los datos la escritura del CSV?**  
`ArchivoCsv.guardar` escribe primero a un archivo temporal en UTF-8 y después lo reemplaza. Intenta `ATOMIC_MOVE` y usa reemplazo normal si ese tipo de movimiento no está disponible. `ServicioTalleres` confirma el nuevo estado en memoria solo después de que la escritura termina.

**¿Por qué Swing y cómo funciona la navegación?**  
`JTabbedPane` agrupa Participantes, Talleres e Inscripciones y resultados. Cada botón registra un evento que invoca al servicio; la tabla se actualiza con el resultado. `main` inicia la ventana mediante `SwingUtilities.invokeLater`, en el hilo de eventos de Swing.

**¿Cómo compruebas los criterios de aceptación?**  
Cada RF01-RF25 tiene dos escenarios documentados. `ant test-gui` ejecuta primero 58 casos del servicio y luego un flujo de interfaz con tres vistas, eventos, tablas y persistencia. Las cuatro capturas permiten enseñar la interfaz, incluida una validación fallida.

**¿Qué hiciste tú en Git y qué hizo el equipo?**  
Explica con precisión tus commits, ramas y pull requests visibles en GitHub. Puedes describir la coordinación y las revisiones grupales. El historial y los PR son la evidencia de autoría y revisión; no atribuyas un commit concreto a un compañero si no aparece a su nombre.

**¿Qué limitaciones tiene el avance?**  
RF26-RF40 siguen planificados. El almacenamiento local CSV sirve para esta versión académica; una solución para uso simultáneo por varias personas requeriría otra capa de persistencia y control de acceso. La charla social no tiene medición de impacto a largo plazo.

## Antes de entrar a sustentar

- Abre `bienestar-digital` en NetBeans y confirma que la clase principal sea `bienestar.AppSwing`.
- Haz una prueba rápida de registro, creación de taller, inscripción y cierre/reapertura.
- Ten cerca el informe final, el Excel de historias, el diagrama y el registro de pruebas.
- Sube el archivo PPTX individual de apoyo a T3. El informe y el software respaldan la sustentación; las tres entregas grupales del ciclo deben permanecer en la plataforma según la consigna del docente.
