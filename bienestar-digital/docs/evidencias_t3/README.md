# Evidencia reproducible de la interfaz T3

La aplicación abre por defecto `bienestar.AppSwing` en NetBeans. Su ventana contiene tres vistas: Participantes, Talleres e Inscripciones y resultados. Las capturas de esta carpeta se generaron ejecutando los componentes Swing reales con datos ficticios; no son maquetas.

| Archivo | Qué demuestra |
| --- | --- |
| `01_participantes.png` | Registro y listado de participantes; campos y botones de la primera vista. |
| `02_talleres.png` | Creación y listado de talleres; navegación a la segunda vista. |
| `03_inscripciones.png` | Inscripción de un participante y vista de resultados. |
| `04_validacion_edad.png` | Rechazo de una edad fuera de rango y retroalimentación visible en rojo. |
| `ejecucion_pruebas.txt` | 58 casos del servicio conformes y prueba de integración de tres vistas, eventos, tablas y persistencia. |

Para reproducir la prueba en un entorno con JDK 17 o superior y Ant:

```sh
ant test-gui
```

Para regenerar las capturas en un entorno gráfico:

```sh
ant capturas-gui
```

Los botones de la interfaz delegan al mismo `ServicioTalleres` que usa la consola. El programa conserva los datos en `datos/sistema.csv` después de cada operación válida. La prueba de interfaz crea un archivo temporal propio para no alterar datos de uso normal.
