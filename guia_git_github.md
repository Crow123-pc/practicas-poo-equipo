# Guía de Git y GitHub

Curso: Técnicas de Programación Orientada a Objetos
Responsable de esta guía: Mario Saucedo
Repositorio: https://github.com/Crow123-pc/practicas-poo-equipo

## 1 Diferencia entre Git y GitHub

Git registra versiones de los archivos en la computadora.
GitHub permite publicar el repositorio, compartirlo y revisar cambios en equipo.

Un commit guarda una versión local. Para publicarla se utiliza git push.

## 2 Preparación

Instalar Git y comprobar su funcionamiento:

    git --version

Dentro del repositorio, configurar la identidad del autor:

    git config user.name "Mario Saucedo"
    git config user.email "CORREO_ASOCIADO_A_GITHUB"

Reemplazar CORREO_ASOCIADO_A_GITHUB por el correo de la cuenta.
Cada integrante debe configurar sus propios datos.

## 3 Crear un repositorio local

Crear una carpeta independiente y abrirla en PowerShell:

    mkdir practica-semana1-local
    cd practica-semana1-local
    git init -b main

git init crea el repositorio local y -b main define la rama inicial.

## 4 Clonar el repositorio compartido

Desde la carpeta donde se guardarán los proyectos:

    git clone https://github.com/Crow123-pc/practicas-poo-equipo.git
    cd practicas-poo-equipo

La clonación descarga los archivos y el historial del repositorio.

## 5 Navegación y estado

Consultar la ubicación y los archivos:

    Get-Location
    Get-ChildItem

Entrar a una carpeta:

    cd nombre-de-carpeta

Volver a la carpeta anterior:

    cd ..

Consultar la rama y los cambios pendientes:

    git status

Los comandos Get-Location y Get-ChildItem pertenecen a PowerShell.

## 6 Registrar cambios

Crear o modificar un archivo y guardarlo.

Revisar las modificaciones de un archivo que Git ya sigue:

    git diff

Preparar un archivo para el commit:

    git add README.md

Comprobar los cambios preparados:

    git diff --cached

Registrar la versión:

    git commit -m "docs: actualiza descripción del proyecto"

git add prepara el contenido y git commit lo guarda en el historial.
Los archivos nuevos aparecen en git status antes de añadirlos.

## 7 Consultar el historial

Mostrar los commits resumidos:

    git log --oneline

Mostrar ramas y fusiones:

    git log --oneline --graph --all

Consultar los cambios de un commit:

    git show HASH_DEL_COMMIT

Reemplazar HASH_DEL_COMMIT por el identificador correspondiente.

## 8 Publicar cambios

Publicar los commits de main:

    git push origin main

Publicar una rama de trabajo por primera vez:

    git push -u origin nombre-de-rama

Reemplazar nombre-de-rama por la rama utilizada.
Completar el inicio de sesión de GitHub si se solicita.

## 9 Crear y cambiar de rama

Crear una rama:

    git branch practica/semana2

Cambiar a ella:

    git checkout practica/semana2

También se puede crear y cambiar en un solo paso:

    git checkout -b practica/semana2

Las dos formas son alternativas; no ejecutarlas para crear
dos veces la misma rama.

Consultar las ramas:

    git branch

El asterisco identifica la rama activa.

## 10 Fusionar ramas

Con los cambios guardados y la copia de trabajo limpia:

    git checkout main
    git merge practica/semana2

La fusión incorpora a la rama activa los cambios de la otra rama.
Una fusión fast-forward avanza el puntero sin crear un commit de fusión.

## 11 Resolver conflictos

Un conflicto puede aparecer si dos ramas modifican de manera
incompatible la misma parte de un archivo.

En nuestra práctica, una rama propuso lunes y otra martes.

Procedimiento:

1. Consultar git status para identificar el archivo.
2. Abrir el archivo y revisar ambas propuestas.
3. Elegir el contenido final.
4. Eliminar las marcas <<<<<<<, ======= y >>>>>>>.
5. Guardar el archivo.
6. Registrar la resolución:

    git add acuerdos.txt
    git commit -m "docs: resuelve conflicto de horario"

Comprobar el resultado:

    git status
    git log --oneline --graph --all

En el ejercicio se conservó el horario del martes.

## 12 Colaborar en GitHub

El propietario invita a los integrantes desde:
Settings → Collaborators → Add people.

Cada integrante acepta la invitación y clona el repositorio.
Después configura su identidad y crea una rama para su tarea.

Publica sus commits y abre un pull request hacia la rama
de integración acordada.

## 13 Pull requests y revisión

Un pull request solicita incorporar los cambios de una rama.

Procedimiento:

1. Seleccionar main como base y la rama de trabajo como compare.
2. Escribir un título y describir el cambio.
3. Indicar las comprobaciones realmente realizadas.
4. Crear el pull request.
5. Otro integrante revisa Files changed.
6. Deja comentarios y aprueba o solicita correcciones.
7. Resolver las observaciones antes de integrar.

Una revisión del código no equivale a ejecutar el programa.
No afirmar que se realizó una prueba si no se ejecutó.

## 14 Actualizar la copia local

Con los cambios locales guardados:

    git checkout main
    git pull --ff-only origin main

Este comando actualiza main si puede avanzar sin crear una fusión.
Si se detiene por divergencia, revisar el historial antes de continuar.

## 15 Guardar cambios temporalmente con stash

Guardar cambios sin hacer commit:

    git stash push -m "avance temporal de semana 4"

Para incluir también archivos nuevos no ignorados:

    git stash push -u -m "avance con archivos nuevos"

Consultar las entradas:

    git stash list

Recuperar la entrada más reciente:

    git stash apply

Comprobar lo recuperado:

    git diff
    git status

apply conserva la entrada del stash.
Si aparece un conflicto, resolverlo antes de continuar.

## 16 Deshacer un cambio con revert

Con la copia de trabajo limpia:

    git revert --no-edit HASH_DEL_COMMIT

Reemplazar HASH_DEL_COMMIT por el commit que se desea revertir.
Este ejemplo se aplica a un commit normal, no a un commit de fusión.

revert crea un nuevo commit que deshace el cambio seleccionado.
El commit original permanece en el historial.

Práctica realizada:

    git revert --no-edit 1b47311

Resultado: commit 7e2e70b.

## 17 Aplicar un cambio con cherry-pick

Cambiar a la rama de destino y comprobar que está limpia:

    git checkout rama-de-destino
    git status

Aplicar un commit que todavía no esté incorporado:

    git cherry-pick HASH_DEL_COMMIT

cherry-pick copia el cambio y normalmente crea un commit
con un identificador diferente.

En practica/cherry-pick aplicamos el commit 1b47311
y se creó el commit 98125b7.

Si hay conflictos, resolver los archivos y ejecutar:

    git add archivo-resuelto
    git cherry-pick --continue

## 18 Buenas prácticas y estrategia de ramas

Usar nombres descriptivos y commits que agrupen cambios relacionados.

Prefijos de mensajes:
- feat: nueva funcionalidad.
- docs: documentación.
- fix: corrección.

En el equipo usamos main y ramas por tarea.
Las fusiones locales de Semana 2 fueron ejercicios.
La colaboración se documenta mediante pull requests y revisiones.

Git Flow distingue main, develop, feature, release y hotfix.
Se estudia como referencia; no implementamos su flujo completo.

Consultar buenas_practicas_git.md para los ejemplos del proyecto.

## 19 Archivos compilados

El archivo .gitignore contiene:

    out/
    *.class

Estas reglas excluyen los archivos compilados del programa Java.

## 20 Evidencias

Conservar capturas de:
- Creación y clonación del repositorio.
- Cambios en código y commits.
- Historial, ramas y fusiones.
- Conflicto y resolución.
- Colaboradores, pull requests y revisiones.
- Guardado y recuperación con stash.
- Revert y cherry-pick.
- Compilación y ejecución del programa.

Relacionar cada evidencia con su autor, semana y commit o enlace.

## 21 Documentación oficial

Git:
https://git-scm.com/docs

GitHub:
https://docs.github.com/en/pull-requests