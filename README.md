Sistema Académico Proyecto.1
Descripción

Sistema Académico desarrollado en Java utilizando NetBeans, orientado a la gestión de estudiantes, cursos, matrículas y calificaciones. El sistema permite administrar la información académica mediante una interfaz gráfica amigable y organizada por módulos.

El proyecto implementa una arquitectura por capas que separa la lógica de negocio, acceso a datos, presentación y modelos, facilitando el mantenimiento y escalabilidad del sistema.


| Integrante                             |                                                                                                                |
| -------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| Breyner Mora Jiménez               | Módulo Estudiantes: modelo, repositorio, negocio, panel, validaciones, búsqueda y ordenamiento.                                   |
| Joselyn Tatinana Rodríguez Bonilla | Módulo Cursos: modelo, repositorio, negocio, panel, validaciones, búsqueda y ordenamiento.                                        |
| Jason Ortiz Tenorio                | Módulo Matrícula: modelo, repositorio, negocio, panel, reglas de matrícula y control de duplicados.                               |
| Pamela María Serrano Montiel       | Módulo Calificaciones, Reportes y Archivos: calificaciones, promedios, reportes, ArchivoUtil y exportación mediante JFileChooser. |


Características Principales
Gestión de Estudiantes
Registro de estudiantes.
Modificación y eliminación de registros.
Validación de datos.
Búsqueda y ordenamiento de información.
Gestión de Cursos
Registro de cursos.
Actualización y eliminación de cursos.
Validación de datos.
Búsqueda y ordenamiento.
Gestión de Matrículas
Registro de matrículas.
Control de duplicados.
Aplicación de reglas de matrícula.
Asociación entre estudiantes y cursos.
Gestión de Calificaciones
Registro de notas.
Cálculo de promedios.
Validación de calificaciones.
Reportes y Archivos
Generación de reportes.
Exportación e importación de información.
Manejo de archivos mediante JFileChooser.
Arquitectura del Proyecto

El sistema está organizado en los siguientes paquetes:

Modelo

Contiene las entidades principales:

Estudiante
Curso
Matricula
Calificacion
Repositorio

Gestiona el almacenamiento y recuperación de datos.

EstudianteRepositorio
CursoRepositorio
MatriculaRepositorio
CalificacionRepositorio
Negocio

Implementa las reglas de negocio.

EstudianteNegocio
CursoNegocio
MatriculaNegocio
CalificacionNegocio
Presentación

Contiene la interfaz gráfica del sistema.

MainFrame
EstudiantePanel
CursoPanel
MatriculaPanel
CalificacionPanel
ReportePanel
Utilidades

Herramientas auxiliares para el manejo de archivos e historial.

ArchivoUtil
Historial
Excepciones

Manejo personalizado de errores.

ArchivoException
DatoInvalidoException
NotaInvalidaException
RegistroDuplicadoException
Tecnologías Utilizadas
Java
Java Swing
NetBeans IDE
JFileChooser
Programación Orientada a Objetos (POO)
