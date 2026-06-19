package Repositorio;

import Modelo.Estudiante;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//clase 
public class EstudianteRepositorio {

    private List<Estudiante> estudiantes = new ArrayList<>();
    private Set<String> correosRegistrados = new HashSet<>();
    private int contadorId = 1;

    
// Metodos CRUD
    public void agregar(Estudiante e) {
        e.setId(contadorId++);
        estudiantes.add(e);
        correosRegistrados.add(e.getCorreo().toLowerCase());
    }

    public void editar(Estudiante e) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getId() == e.getId()) {
                correosRegistrados.remove(estudiantes.get(i).getCorreo().toLowerCase());
                correosRegistrados.add(e.getCorreo().toLowerCase());
                estudiantes.set(i, e);
                return;
            }
        }
    }

    public void eliminar(int id) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getId() == id) {
                correosRegistrados.remove(estudiantes.get(i).getCorreo().toLowerCase());
                estudiantes.remove(i);
                return;
            }
        }
    }

     // Consultas 
    public List<Estudiante> obtenerTodos() {
        return estudiantes;
    }

    public boolean existeCorreo(String correo) {
        return correosRegistrados.contains(correo.toLowerCase());
    }

    public boolean existeCorreoParaEdicion(String correo, int idActual) {
        for (Estudiante e : estudiantes) {
            if (e.getCorreo().equalsIgnoreCase(correo) && e.getId() != idActual) {
                return true;
            }
        }
        return false;
    }

    public Estudiante buscarPorId(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) return e;
        }
        return null;
    }
}
