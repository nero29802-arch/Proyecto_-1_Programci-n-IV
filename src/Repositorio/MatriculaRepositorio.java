
package Repositorio;

import Modelo.Matricula;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author nero2
 */
public class MatriculaRepositorio {
    
    private List<Matricula> matriculas = new ArrayList<>();
    
    private Set<String> clavesActivas = new HashSet<>();
    
    private int contadorId = 1;
    
    private String generarClave(int idEstudiante, int idCurso) {
        return idEstudiante + "-" + idCurso;
    }
    
    
    public void agregar(Matricula m) {
        m.setId(contadorId++);
        matriculas.add(m);
        if (m.isActiva()) {
            clavesActivas.add(generarClave(m.getEstudiante().getId(), m.getCurso().getId()));
        }
    } 
    
    public void editar(Matricula matriculaEditada) {
        for (int i = 0; i < matriculas.size(); i++) {
            Matricula actual = matriculas.get(i);
            if (actual.getId() == matriculaEditada.getId()) {
                if (actual.isActiva()) {
                    clavesActivas.remove(generarClave(actual.getEstudiante().getId(), actual.getCurso().getId()));
                }
                
                if (matriculaEditada.isActiva()) {
                    clavesActivas.add(generarClave(
                            matriculaEditada.getEstudiante().getId(), matriculaEditada.getCurso().getId()));
                }
                
                matriculas.set(i, matriculaEditada);
                return;
            }
        }
    }
    
     public void eliminar(int id) {
        matriculas.removeIf(m -> {
            if (m.getId() == id) {
                if (m.isActiva()) {
                    clavesActivas.remove(generarClave(m.getEstudiante().getId(), m.getCurso().getId()));
                }
                return true;
            }
            return false;
        });
    }

    public List<Matricula> obtenerTodos() {
        return new ArrayList<>(matriculas);
    }

    public Matricula buscarPorId(int id) {
        for (Matricula m : matriculas) {
            if (m.getId() == id) return m;
        }
        return null;
    }
    
     public boolean existeMatriculaActiva(int idEstudiante, int idCurso) {
        return clavesActivas.contains(generarClave(idEstudiante, idCurso));
    }
    
    public boolean existeMatriculaActivaParaEdicion (int idEstudiante, int idCurso, int idActual) {
        for (Matricula m : matriculas) {
            if (m.isActiva()
                    && m.getEstudiante().getId() == idEstudiante
                    && m.getCurso().getId() == idCurso
                    && m.getId() != idActual) {
                return true;
            }
        }
        
        return false;
    }
    
    public   List<Matricula> obtenerActivasPorEstudiante(int idEstudiante) {
        List<Matricula> resultado = new ArrayList<>();
        for (Matricula m : matriculas) {
            if (m.isActiva() && m.getEstudiante().getId() == idEstudiante) {
                resultado.add(m);
            }
        }
        
        return resultado;
    }
    
    public List<Matricula> buscar(String texto, String campo) {
           List<Matricula> resultado = new ArrayList<>();
        String textoBusqueda = texto.toLowerCase();
        for (Matricula m : matriculas) {
            String nombreEstudiante = m.getEstudiante().getNombre().toLowerCase();
            String codigoCurso = m.getCurso().getCodigo().toLowerCase();
            String nombreCurso = m.getCurso().getNombre().toLowerCase();
            String estado = m.getEstado().toLowerCase();
            
            switch (campo) {
                 case "Estudiante":
                    if (nombreEstudiante.contains(textoBusqueda)) resultado.add(m);
                    break;
                case "Curso":
                    if (codigoCurso.contains(textoBusqueda) || nombreCurso.contains(textoBusqueda)) resultado.add(m);
                    break;
                case "Estado":
                    if (estado.contains(textoBusqueda)) resultado.add(m);
                    break;
                 default:
                    if (nombreEstudiante.contains(textoBusqueda)
                            || codigoCurso.contains(textoBusqueda)
                            || nombreCurso.contains(textoBusqueda)
                            || estado.contains(textoBusqueda)) {
                        resultado.add(m);
                    }
                    
                  
                 }
            
        }
        
        return resultado;
    }
    
}