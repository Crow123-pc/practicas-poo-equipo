# Buenas prácticas de Git y GitHub

Repositorio: https://github.com/Crow123-pc/practicas-poo-equipo
Autor de esta práctica: Mario Saucedo
Semana: 4

## Convención de nombres de ramas

Utilizamos nombres descriptivos para identificar cada tarea:

- docs/mario-semana3: aporte de Mario para revisión mediante pull request.
- practica/semana2: práctica de ramas y fusiones.
- practica/horario-a: propuesta del horario del lunes.
- practica/horario-b: propuesta del horario del martes.
- practica/semana4: práctica de stash y revert.
- practica/cherry-pick: aplicación de un commit de otra rama.

## Mensajes de commit claros

Los mensajes describen el cambio realizado. Utilizamos estos prefijos:

- feat: incorporación de código o funcionalidades.
- docs: cambios en documentación.
- fix: corrección de errores.

Ejemplos registrados en el repositorio:

- docs: agrega objetivo e integrantes al README
- docs: resuelve conflicto de horario
- feat: incorpora programa de saludo para revisión

Cada commit debe contener cambios relacionados con una tarea.

## Importancia de las ramas

Las ramas permiten desarrollar tareas por separado antes de incorporarlas a main.

En Semana 2 creamos ramas con propuestas diferentes de horario. Al fusionarlas apareció un conflicto, que resolvimos dejando el horario del martes.

## Estrategia de ramificación utilizada

Usamos main como rama principal y creamos ramas para las prácticas.

En Semana 2 realizamos fusiones locales como ejercicios.

En Semana 3 publicamos Hola.java en docs/mario-semana3 y abrimos el pull request número 1 para solicitar la revisión de un compañero antes de integrarlo a main.

La revisión y la integración de ese pull request están pendientes.

## Git Flow como estrategia de referencia

Git Flow organiza el desarrollo mediante las siguientes ramas:

- main: versiones estables.
- develop: integración de cambios durante el desarrollo.
- feature: nuevas funcionalidades.
- release: preparación de una entrega.
- hotfix: correcciones urgentes de una versión publicada.

En nuestro repositorio hemos utilizado main y ramas de tarea. No hemos implementado el flujo completo de Git Flow.

## Práctica de git stash

Modificamos acuerdos.txt añadiendo una línea temporal de Semana 4.

Guardamos el cambio sin hacer commit:

git stash push -m "avance temporal de semana 4"

Consultamos los cambios guardados:

git stash list

Recuperamos el cambio:

git stash apply

Comprobamos su recuperación con git diff y lo registramos en el commit 1b47311.

## Práctica de git revert

Deshicimos el cambio del commit 1b47311 mediante:

git revert --no-edit 1b47311

Git creó el commit 7e2e70b. El archivo volvió a contener únicamente el horario del martes y el commit original permaneció en el historial.

## Práctica de git cherry-pick

Creamos practica/cherry-pick desde main y aplicamos:

git cherry-pick 1b47311

El cambio se incorporó a esa rama mediante el nuevo commit 98125b7.

Comprobamos el contenido con Get-Content acuerdos.txt y consultamos el historial con git log --oneline -3.

## Publicación y evidencias

Publicamos las ramas practica/semana4 y practica/cherry-pick en GitHub.

Las capturas muestran el guardado y recuperación con stash, la reversión con revert y la aplicación del cambio mediante cherry-pick.

## Recomendaciones para el equipo

- Revisar git status antes de cambiar de rama.
- Consultar git diff antes de registrar cambios.
- Usar nombres de ramas y mensajes de commit descriptivos.
- Añadir únicamente los archivos correspondientes a la tarea.
- Solicitar la revisión de otro integrante en los pull requests.
- No subir contraseñas ni credenciales.
- Conservar capturas y enlaces de los aportes realizados.