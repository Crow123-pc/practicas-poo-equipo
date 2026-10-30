# Práctica de manejo de archivos en Java

La clase ArchivoCsv utiliza Path, Files y StandardCharsets.UTF_8. El servicio guarda los participantes (P), talleres (T) e inscripciones (I) en una única instantánea `datos/sistema.csv`. Las filas comparten siete columnas; su interpretación depende del tipo. Las notas vacías representan evaluaciones pendientes, y cero es una nota válida.

La lectura verifica cabecera, número de columnas, comillas, tipos de dato, códigos únicos, referencias y cupos antes de aceptar el estado completo. Una línea incorrecta genera IOException y detiene el inicio; el archivo no se reemplaza.

La escritura crea un archivo temporal en el mismo directorio, escribe todas las filas en UTF-8 y lo mueve al destino. Solicita ATOMIC_MOVE; cuando el sistema de archivos no lo admite, utiliza REPLACE_EXISTING. La memoria se actualiza después de guardar. Si falla la escritura, la operación no se confirma en memoria.

La exportación produce otro CSV con cabecera, taller, participante, asistencia y nota. Los nombres y títulos que comienzan con signos de fórmula se exportan como texto. No se permite usar como destino el archivo principal de datos.

Práctica reproducible: registrar un participante cuyo nombre contenga tildes, punto y coma y comillas, cerrar y reabrir el servicio y comparar los valores. ARCH01 comprueba esta recuperación y una nota cero; ARCH02 comprueba un archivo dañado, ARCH03 una escritura fallida y ARCH04 una referencia inexistente.

El programa está diseñado para una única instancia local. Copiar `datos/sistema.csv` con el programa cerrado permite conservar un respaldo manual. La importación masiva desde un archivo externo figura como RF24 y no está implementada en esta versión.
