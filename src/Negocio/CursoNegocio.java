/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

/**
 *
 * @author HP
 */
import Excepciones.DatoInvalidoException;
import Excepciones.RegistroDuplicadoException;
import Modelo.Curso;
import Repositorio.CursoRepositorio;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Capa de Negocio del módulo Cursos.
 * Aquí viven TODAS las validaciones y reglas de negocio del curso.
 * La capa de Presentación (CursoPanel) nunca valida nada directamente:
 * solo llama a estos métodos y muestra los mensajes de error que lleguen.
 */
public class CursoNegocio {

    private CursoRepositorio repositorio;

    public CursoNegocio(CursoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Registra un nuevo curso, validando los datos y verificando que el
     * código no esté duplicado antes de guardarlo.
     */
    public void agregar(Curso curso) throws DatoInvalidoException, RegistroDuplicadoException {
        validar(curso, false);
        repositorio.agregar(curso);
    }

    /**
     * Edita un curso existente, validando los datos y verificando duplicados
     * (ignorando el propio curso que se está editando).
     */
    public void editar(Curso curso) throws DatoInvalidoException, RegistroDuplicadoException {
        validar(curso, true);
        repositorio.editar(curso);
    }

    /**
     * Elimina un curso por su ID. La confirmación ("¿Está seguro?") se pide
     * en la capa de Presentación con JOptionPane antes de llamar a este método.
     */
    public void eliminar(int id) {
        repositorio.eliminar(id);
    }

    public List<Curso> obtenerTodos() {
        return repositorio.obtenerTodos();
    }

    public List<Curso> buscar(String texto, String campo) {
        return repositorio.buscar(texto, campo);
    }

    /**
     * Devuelve solo los cursos activos. Lo usa el módulo de Matrícula,
     * ya que solo se puede matricular en cursos activos.
     */
    public List<Curso> obtenerActivos() {
        return repositorio.obtenerActivos();
    }

    /**
     * Ordena la lista de cursos por código usando Collections.sort + Comparator
     * (requisito obligatorio del proyecto).
     */
    public List<Curso> ordenarPorCodigo() {
        List<Curso> lista = repositorio.obtenerTodos();
        Collections.sort(lista, Comparator.comparing(Curso::getCodigo));
        return lista;
    }

    /**
     * Ordena la lista de cursos por nombre usando Collections.sort + Comparator.
     */
    public List<Curso> ordenarPorNombre() {
        List<Curso> lista = repositorio.obtenerTodos();
        Collections.sort(lista, Comparator.comparing(Curso::getNombre));
        return lista;
    }

    /**
     * Valida las reglas de negocio del curso:
     * - Código y nombre obligatorios
     * - Créditos mayores que 0
     * - Profesor obligatorio
     * - Código no duplicado (distinto comportamiento si es edición o registro nuevo)
     *
     * Lanza excepciones personalizadas en vez de simplemente retornar true/false,
     * para que la interfaz pueda mostrar un mensaje claro con JOptionPane.
     */
    private void validar(Curso curso, boolean esEdicion) throws DatoInvalidoException, RegistroDuplicadoException {
        if (curso.getCodigo() == null || curso.getCodigo().trim().isEmpty()) {
            throw new DatoInvalidoException("El código del curso es obligatorio.");
        }
        if (curso.getNombre() == null || curso.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del curso es obligatorio.");
        }
        if (curso.getCreditos() <= 0) {
            throw new DatoInvalidoException("Los créditos deben ser mayores que 0.");
        }
        if (curso.getProfesor() == null || curso.getProfesor().trim().isEmpty()) {
            throw new DatoInvalidoException("El profesor es obligatorio.");
        }

        if (esEdicion) {
            if (repositorio.existeCodigoParaEdicion(curso.getCodigo(), curso.getId())) {
                throw new RegistroDuplicadoException("Ya existe otro curso con el código: " + curso.getCodigo());
            }
        } else {
            if (repositorio.existeCodigo(curso.getCodigo())) {
                throw new RegistroDuplicadoException("Ya existe un curso con el código: " + curso.getCodigo());
            }
        }
    }
}