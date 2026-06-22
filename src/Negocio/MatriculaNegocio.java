package Negocio;

import Excepciones.DatoInvalidoException;
import Excepciones.RegistroDuplicadoException;
import Modelo.Curso;
import Modelo.Estudiante;
import Modelo.Matricula;
import Repositorio.MatriculaRepositorio;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatriculaNegocio {

    public static final int LIMITE_CREDITOS = 24; // ajustable si tu profesor pide otro valor

    private MatriculaRepositorio repositorio;

    public MatriculaNegocio(MatriculaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void matricular(Estudiante estudiante, Curso curso, LocalDate fecha)
            throws DatoInvalidoException, RegistroDuplicadoException {
        Matricula m = new Matricula(0, estudiante, curso, fecha, "Activa");
        validar(m, false);
        repositorio.agregar(m);
    }

    public void editar(Matricula m) throws DatoInvalidoException, RegistroDuplicadoException {
        validar(m, true);
        repositorio.editar(m);
    }

    /** Anula una matrícula (no la borra): conserva el historial. */
    public void anular(int id) {
        Matricula m = repositorio.buscarPorId(id);
        if (m != null) {
            m.setEstado("Anulada");
            repositorio.editar(m);
        }
    }

    /** Reactiva una matrícula anulada, validando duplicados y créditos de nuevo. */
    public void reactivar(int id) throws DatoInvalidoException, RegistroDuplicadoException {
        Matricula m = repositorio.buscarPorId(id);
        if (m == null) return;

        if (!m.getEstudiante().isActivo()) {
            throw new DatoInvalidoException("No se puede reactivar: el estudiante está inactivo.");
        }
        if (!m.getCurso().isActivo()) {
            throw new DatoInvalidoException("No se puede reactivar: el curso está inactivo.");
        }
        if (repositorio.existeMatriculaActivaParaEdicion(m.getEstudiante().getId(), m.getCurso().getId(), m.getId())) {
            throw new RegistroDuplicadoException(
                    "No se puede reactivar: el estudiante ya tiene una matrícula activa en ese curso.");
        }

        int creditosActuales = 0;
        for (Matricula activa : repositorio.obtenerActivasPorEstudiante(m.getEstudiante().getId())) {
            creditosActuales += activa.getCurso().getCreditos();
        }
        if (creditosActuales + m.getCurso().getCreditos() > LIMITE_CREDITOS) {
            throw new DatoInvalidoException(
                    "No se puede reactivar: el estudiante superaría el límite de " + LIMITE_CREDITOS + " créditos.");
        }

        m.setEstado("Activa");
        repositorio.editar(m);
    }

    public void eliminar(int id) {
        repositorio.eliminar(id);
    }

    public List<Matricula> obtenerTodos() {
        return repositorio.obtenerTodos();
    }

    public Matricula buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }

    public List<Matricula> buscar(String texto, String campo) {
        return repositorio.buscar(texto, campo);
    }

    public List<Matricula> ordenarPorFecha() {
        List<Matricula> lista = repositorio.obtenerTodos();
        Collections.sort(lista, Comparator.comparing(Matricula::getFechaMatricula));
        return lista;
    }

    public List<Matricula> ordenarPorEstudiante() {
        List<Matricula> lista = repositorio.obtenerTodos();
        Collections.sort(lista, Comparator.comparing(m -> m.getEstudiante().getNombre()));
        return lista;
    }

    public int obtenerCreditosMatriculados(int idEstudiante) {
        int total = 0;
        for (Matricula m : repositorio.obtenerActivasPorEstudiante(idEstudiante)) {
            total += m.getCurso().getCreditos();
        }
        return total;
    }

    private void validar(Matricula m, boolean esEdicion) throws DatoInvalidoException, RegistroDuplicadoException {
        Estudiante estudiante = m.getEstudiante();
        Curso curso = m.getCurso();

        if (estudiante == null) {
            throw new DatoInvalidoException("Debe seleccionar un estudiante.");
        }
        if (curso == null) {
            throw new DatoInvalidoException("Debe seleccionar un curso.");
        }
        if (m.getFechaMatricula() == null) {
            throw new DatoInvalidoException("Debe indicar la fecha de matrícula.");
        }
        if (!estudiante.isActivo()) {
            throw new DatoInvalidoException(
                    "No se puede matricular: el estudiante \"" + estudiante.getNombre() + "\" está inactivo.");
        }
        if (!curso.isActivo()) {
            throw new DatoInvalidoException(
                    "No se puede matricular: el curso \"" + curso.getNombre() + "\" está inactivo.");
        }

        if (m.isActiva()) {
            boolean duplicada = esEdicion
                    ? repositorio.existeMatriculaActivaParaEdicion(estudiante.getId(), curso.getId(), m.getId())
                    : repositorio.existeMatriculaActiva(estudiante.getId(), curso.getId());
            if (duplicada) {
                throw new RegistroDuplicadoException(
                        "El estudiante \"" + estudiante.getNombre()
                        + "\" ya tiene una matrícula activa en el curso \"" + curso.getNombre() + "\".");
            }

            int creditosActuales = 0;
            for (Matricula activa : repositorio.obtenerActivasPorEstudiante(estudiante.getId())) {
                if (!esEdicion || activa.getId() != m.getId()) {
                    creditosActuales += activa.getCurso().getCreditos();
                }
            }
            if (creditosActuales + curso.getCreditos() > LIMITE_CREDITOS) {
                throw new DatoInvalidoException(
                        "No se puede matricular: el estudiante superaría el límite de " + LIMITE_CREDITOS
                        + " créditos (actualmente tiene " + creditosActuales
                        + " y el curso aporta " + curso.getCreditos() + ").");
            }
        }
    }
}