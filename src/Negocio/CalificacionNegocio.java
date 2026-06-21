package Negocio;

import Excepciones.NotaInvalidaException;
import Modelo.Calificacion;
import Repositorio.CalificacionRepositorio;

public class CalificacionNegocio {
                                                                                                                                                                                                  
    private CalificacionRepositorio repo = new CalificacionRepositorio();

    public void agregar(Calificacion c) throws NotaInvalidaException {  

        if (c.getNota() < 0 || c.getNota() > 100) {                                          
            throw new NotaInvalidaException("La nota debe estar entre 0 y 100");
        }

        repo.agregar(c);
    }
    
    public double calcularPromedio() {

    double suma = 0;

    for (Calificacion c : repo.obtenerTodos()) {
        suma += c.getNota();
    }

    if (repo.obtenerTodos().isEmpty()) {
        return 0;
    }

    return suma / repo.obtenerTodos().size();
}
}