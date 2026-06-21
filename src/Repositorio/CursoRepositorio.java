/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repositorio;

/**
 *
 * @author HP
 */
import Modelo.Curso;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Capa de Repositorio del módulo Cursos.
 * Se encarga únicamente de almacenar y manipular los datos en memoria
 * (List y Set). No contiene validaciones de negocio: esas van en CursoNegocio.
 */
public class CursoRepositorio {

    // Lista principal donde se guardan todos los cursos registrados (requisito: List<Curso>)
    private List<Curso> cursos = new ArrayList<>();

    // Set usado para controlar códigos de curso duplicados de forma rápida (requisito: Set<String>)
    private Set<String> codigosRegistrados = new HashSet<>();

    // Contador interno que simula un ID autoincremental
    private int contadorId = 1;

    /**
     * Agrega un nuevo curso a la lista y registra su código para control de duplicados.
     */
    public void agregar(Curso curso) {
        curso.setId(contadorId++);
        cursos.add(curso);
        codigosRegistrados.add(curso.getCodigo().toUpperCase());
    }

    /**
     * Reemplaza un curso existente (buscado por ID) con los datos editados.
     * Actualiza también el Set de códigos por si el código cambió.
     */
    public void editar(Curso cursoEditado) {
        for (int i = 0; i < cursos.size(); i++) {
            if (cursos.get(i).getId() == cursoEditado.getId()) {
                codigosRegistrados.remove(cursos.get(i).getCodigo().toUpperCase());
                codigosRegistrados.add(cursoEditado.getCodigo().toUpperCase());
                cursos.set(i, cursoEditado);
                return;
            }
        }
    }

    /**
     * Elimina un curso de la lista según su ID y libera su código del Set.
     */
    public void eliminar(int id) {
        cursos.removeIf(c -> {
            if (c.getId() == id) {
                codigosRegistrados.remove(c.getCodigo().toUpperCase());
                return true;
            }
            return false;
        });
    }

    /**
     * Devuelve una copia de la lista completa de cursos (para no exponer la lista original).
     */
    public List<Curso> obtenerTodos() {
        return new ArrayList<>(cursos);
    }

    /**
     * Verifica si ya existe un curso con ese código (usado al REGISTRAR un curso nuevo).
     */
    public boolean existeCodigo(String codigo) {
        return codigosRegistrados.contains(codigo.toUpperCase());
    }

    /**
     * Verifica duplicados de código pero ignorando el propio curso que se está editando
     * (para que un curso no choque consigo mismo al editar sin cambiar el código).
     */
    public boolean existeCodigoParaEdicion(String codigo, int idActual) {
        for (Curso c : cursos) {
            if (c.getCodigo().equalsIgnoreCase(codigo) && c.getId() != idActual) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca un curso por su ID. Devuelve null si no existe.
     */
    public Curso buscarPorId(int id) {
        for (Curso c : cursos) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    /**
     * Búsqueda de cursos por texto, filtrando por código, nombre o ambos ("Todos").
     */
    public List<Curso> buscar(String texto, String campo) {
        List<Curso> resultado = new ArrayList<>();
        String textoBusqueda = texto.toLowerCase();
        for (Curso c : cursos) {
            switch (campo) {
                case "Código":
                    if (c.getCodigo().toLowerCase().contains(textoBusqueda)) resultado.add(c);
                    break;
                case "Nombre":
                    if (c.getNombre().toLowerCase().contains(textoBusqueda)) resultado.add(c);
                    break;
                default:
                    // "Todos": busca coincidencias tanto en código como en nombre
                    if (c.getCodigo().toLowerCase().contains(textoBusqueda) ||
                        c.getNombre().toLowerCase().contains(textoBusqueda)) resultado.add(c);
            }
        }
        return resultado;
    }

    /**
     * Devuelve únicamente los cursos marcados como activos.
     * Lo usará el módulo de Matrícula, ya que solo se puede matricular en cursos activos.
     */
    public List<Curso> obtenerActivos() {
        List<Curso> activos = new ArrayList<>();
        for (Curso c : cursos) {
            if (c.isActivo()) activos.add(c);
        }
        return activos;
    }
}