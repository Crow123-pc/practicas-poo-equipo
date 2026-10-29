# Sistema de gestión de talleres de bienestar digital

Proyecto de Técnicas de Programación Orientada a Objetos, semanas 7 a 11.
La aplicación administra participantes, talleres, inscripciones, asistencia y evaluaciones mediante un menú de consola. Incluye 40 requerimientos documentados; RF01 a RF15 están implementados.

## Abrir en Apache NetBeans

1. Extraer el ZIP completo. Mantener juntos `build.xml`, `nbproject` y `src`.
2. En NetBeans elegir Archivo > Abrir proyecto y seleccionar `BienestarDigital_NetBeans`.
3. Ejecutar el proyecto con el boton verde. La clase principal ya esta configurada como `bienestar.Main`.
4. Usar JDK 17 o superior. Las opciones aparecen en el panel Output o Salida y se responden escribiendo alli y pulsando Enter.
5. Para las 36 pruebas, usar Probar proyecto. Tambien se puede ejecutar `probar.cmd`.

No hace falta crear otro proyecto ni abrir los siete archivos por separado.

## Ejecutar en Windows

1. Instalar un JDK 17 o superior y comprobar `java -version` y `javac -version`.
2. Extraer el ZIP completo en una carpeta de trabajo.
3. Abrir `ejecutar.cmd`. No hace falta instalar bibliotecas ni una base de datos.
4. Las opciones 1 a 15 corresponden a RF01 a RF15. La opción 0 termina la sesión.

El ZIP incluye también `bienestar-digital.jar`, que puede ejecutarse con `java -jar bienestar-digital.jar` si ya se dispone de Java 17 o superior. Los fuentes y scripts permiten recompilarlo.

Alternativa desde PowerShell, dentro de esta carpeta:

```powershell
javac -encoding UTF-8 --release 17 -d build src/bienestar/*.java
java -cp build bienestar.Main
```

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

La batería comprueba 36 casos: 30 escenarios de RF01 a RF15 y 6 casos adicionales de persistencia, integridad, cupo, notas y errores de escritura. El registro reproducible está en `docs/resultados_pruebas.csv`. Cada caso usa datos ficticios y un directorio temporal independiente del archivo de trabajo del usuario.

## Organización

- `src/bienestar`: interfaz, entidades, reglas y acceso CSV.
- `pruebas`: pruebas de aceptación ejecutables.
- `docs`: 40 requerimientos, historias, diagrama, manejo de archivos y resultados.
- `datos` y `reportes`: se crean al usar la aplicación y están excluidos de Git.

Las entidades se expresan mediante records Java inmutables con validación en sus constructores. El servicio aplica las reglas de negocio. La persistencia confirma los cambios en memoria después de escribir correctamente el archivo. El registro de participantes tiene métodos sobrecargados y mantiene el enfoque de colecciones y excepciones practicado en la semana 5.

## Incorporar al repositorio del equipo

En una copia local de `https://github.com/Crow123-pc/practicas-poo-equipo`, crear una rama de trabajo y copiar esta carpeta completa bajo `bienestar-digital/`. Así se conservan los ejercicios anteriores:

```powershell
git switch -c feature/bienestar-digital
git add bienestar-digital
git commit -m "feat: incorpora gestion de talleres de bienestar digital"
git push -u origin feature/bienestar-digital
```

Crear un pull request hacia main y pedir a un compañero que ejecute el programa y revise los cambios. Cada integrante debe realizar sus propios aportes y commits; no se atribuyen revisiones ni contribuciones que no se hayan realizado.

## Alcance y actividad social

El software se prepara como producto académico. La charla del 19 de octubre de 2026 en el colegio Toribio Casanova es una actividad de responsabilidad social ya realizada. No se presenta esta aplicación como utilizada durante aquella charla. Las evaluaciones que permite registrar el programa no equivalen a evaluaciones aplicadas a los alumnos de ese colegio.
