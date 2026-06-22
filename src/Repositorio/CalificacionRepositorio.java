package Repositorio;

import Modelo.Calificacion;
import java.util.ArrayList;
import java.util.List;

public class CalificacionRepositorio {
                                                             
    private List<Calificacion> lista = new ArrayList<>();

    public void agregar(Calificacion c) {
        lista.add(c);
    }

    public List<Calificacion> obtenerTodos() {
        return lista;
    }

    public void eliminar(Calificacion c) {
        lista.remove(c);
    }
}