# Sistema de gestión de talleres de bienestar digital

Proyecto de Técnicas de Programación Orientada a Objetos, semanas 7 a 13.
La aplicación administra participantes, talleres, inscripciones, asistencia y evaluaciones. La evaluación T3 incorpora una interfaz Swing de tres vistas y conserva el menú de consola. Incluye 40 requerimientos documentados; RF01 a RF25 están implementados.

## Abrir en Apache NetBeans

1. Extraer el ZIP completo. Mantener juntos `build.xml`, `nbproject` y `src`.
2. En NetBeans elegir Archivo > Abrir proyecto y seleccionar `bienestar-digital`, la carpeta que contiene `build.xml` y `nbproject`.
3. Ejecutar el proyecto con el botón verde. La clase principal está configurada como `bienestar.AppSwing`.
4. Usar JDK 17 o superior. Se abre una ventana con las vistas Participantes, Talleres e Inscripciones y resultados.
5. Para las 58 pruebas, usar Probar proyecto. Tambien se puede ejecutar `probar.cmd`.

No hace falta crear otro proyecto ni abrir los archivos Java por separado. El menú anterior sigue disponible con `bienestar.Main` o `ejecutar.cmd`.
El proyecto compila con compatibilidad Java 17 mediante la opción `--release 17` definida en `build.xml`. Si una copia extraída anteriormente muestra en rojo una advertencia sobre `-source 17`, volver a extraer la entrega actual y abrir esa carpeta en NetBeans.

## Ejecutar en Windows

1. Instalar un JDK 17 o superior y comprobar `java -version` y `javac -version`.
2. Extraer el ZIP completo en una carpeta de trabajo.
3. Abrir `ejecutar_gui.cmd` para la interfaz visual o `ejecutar.cmd` para el menú de consola. No hace falta instalar bibliotecas ni una base de datos.
4. La interfaz visual organiza las operaciones en tres vistas; el menú conserva las opciones 1 a 25.

El ZIP incluye también `bienestar-digital.jar`, que inicia la interfaz visual con `java -jar bienestar-digital.jar` si ya se dispone de Java 17 o superior. Los fuentes y scripts permiten recompilarlo.

Alternativa desde PowerShell, dentro de esta carpeta:

```powershell
javac -encoding UTF-8 --release 17 -d build src/bienestar/*.java
java -cp build bienestar.Main
```

Para iniciar la ventana desde PowerShell, sustituir la segunda línea por `java -cp build bienestar.AppSwing`.

## Interfaz de la evaluación T3

- **Participantes:** registro, búsqueda, actualización, eliminación, filtro por grado, orden e importación CSV.
- **Talleres:** creación, búsqueda, actualización, eliminación, filtro por fechas, consulta de cupos y asignación de expositor.
- **Inscripciones y resultados:** inscripción, listado, cancelación, asistencia, evaluación, historial, resumen, promedio y exportación CSV.

Los botones llaman a `ServicioTalleres` y muestran los datos en tablas. Las validaciones de las entidades y del servicio siguen siendo las mismas que usa la consola. Los errores se muestran en un cuadro de aviso y en la barra inferior. Los cambios se guardan automáticamente en `datos/sistema.csv`.

## Ejemplo de demostración

Usar datos ficticios: participante P001, Ana de prueba, 16 años, 5to secundaria; taller T001, Bienestar digital, fecha 2026-10-19, duración 60 minutos, cupo 30. La fecha y duración identifican el contexto de la charla; el cupo 30 y el participante son ejemplos de prueba, no cifras de asistencia real.

1. Registrar el participante con opción 1.
2. Crear el taller con opción 6.
3. Inscribir con opción 10; consultar con opción 11.
4. Marcar PRESENTE con opción 13 y registrar una nota de prueba con opción 14.
5. Exportar con opción 15 a `reportes/taller.csv`.
6. Salir y volver a abrir: los datos se recuperan de `datos/sistema.csv`.

Cada modificación se guarda automáticamente. El archivo usa UTF-8, punto y coma y campos entre comillas. Si el archivo contiene errores, el inicio se detiene para evitar sobrescribirlo. El almacenamiento es local y admite una sola instancia del programa a la vez.

## Pruebas

Abrir `probar.cmd` o ejecutar:

```powershell
javac -encoding UTF-8 --release 17 -d build src/bienestar/*.java pruebas/bienestar/PruebasSistema.java
java -cp build bienestar.PruebasSistema
```

La batería comprueba 58 casos: 50 escenarios de RF01 a RF25 y 8 casos adicionales de persistencia, integridad, cupo, notas, compatibilidad y protección del archivo principal. El registro reproducible está en `docs/resultados_pruebas.csv`. Cada caso usa datos ficticios y un directorio temporal independiente del archivo de trabajo del usuario.

## Ampliación de las semanas 12 y 13

Las opciones 16 a 25 implementan RF16 a RF25. Incluyen eliminación de talleres sin inscripciones, historial por participante, filtros, cupos, resúmenes, promedio de notas, orden alfabético, importación CSV y asignación de expositor. Las 15 opciones anteriores siguen disponibles.

Para RF24, el archivo de importación debe ser UTF-8 y comenzar con `codigo;nombre;edad;grado`. Cada fila contiene esos cuatro campos, separados por punto y coma; los valores pueden ir entre comillas y las comillas internas se duplican. Por ejemplo:

```csv
codigo;nombre;edad;grado
"P002";"María; Prueba";"17";"5to"
"P003";"José";"16";"4to"
```

Si una fila es inválida o repite un código, se informa su número y no se importa ninguna fila del lote. La opción 25 guarda el expositor del taller sin perder compatibilidad con los archivos CSV de la versión anterior.

## Organización

- `src/bienestar`: interfaz, entidades, reglas y acceso CSV.
- `pruebas`: pruebas de aceptación ejecutables.
- `docs`: 40 requerimientos, historias, diagrama, manejo de archivos y resultados.
- `../outputs/semanas_12_13`: informe integral de las semanas 1 a 13, plantilla Excel actualizada y JAR.
- `datos` y `reportes`: se crean al usar la aplicación y están excluidos de Git.

Las entidades se expresan mediante records Java inmutables con validación en sus constructores. El servicio aplica las reglas de negocio. La persistencia confirma los cambios en memoria después de escribir correctamente el archivo. El registro de participantes tiene métodos sobrecargados y mantiene el enfoque de colecciones y excepciones practicado en la semana 5.

## Repositorio y evidencias

Esta versión está preparada en la rama `codex/semanas-12-13` del repositorio `Crow123-pc/practicas-poo-equipo`. Los fuentes, el proyecto NetBeans, las historias en Markdown, el diagrama y las pruebas están en `bienestar-digital/`. El informe PDF, la plantilla Excel actualizada y el JAR están en `outputs/semanas_12_13/`.

La versión anterior de las semanas 7 a 11 permanece identificada por `feature/bienestar-digital` y el pull request 3. Las pruebas de esta ampliación se pueden repetir con `ant -f build.xml clean jar test`.

## Alcance y actividad social

El software se prepara como producto académico. La charla del 19 de octubre de 2026 en el colegio Toribio Casanova es una actividad de responsabilidad social ya realizada. No se presenta esta aplicación como utilizada durante aquella charla. Las evaluaciones que permite registrar el programa no equivalen a evaluaciones aplicadas a los alumnos de ese colegio.
