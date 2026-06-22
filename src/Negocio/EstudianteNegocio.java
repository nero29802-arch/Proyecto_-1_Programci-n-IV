package Negocio;

import Excepciones.DatoInvalidoException;
import Excepciones.RegistroDuplicadoException;
import Modelo.Estudiante;
import Repositorio.EstudianteRepositorio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EstudianteNegocio {

    private EstudianteRepositorio repositorio = new EstudianteRepositorio();


    
    // Metodos CRUD
    public void agregar(Estudiante e)
            throws DatoInvalidoException, RegistroDuplicadoException {

        validar(e);

        if (repositorio.existeCorreo(e.getCorreo())) {
            throw new RegistroDuplicadoException(
                    "Ya existe un estudiante con el correo: " + e.getCorreo()
            );
        }

        repositorio.agregar(e);
    }


    public void editar(Estudiante e)
            throws DatoInvalidoException, RegistroDuplicadoException {

        validar(e);

        if (repositorio.existeCorreoParaEdicion(e.getCorreo(), e.getId())) {
            throw new RegistroDuplicadoException(
                    "Ya existe otro estudiante con el correo: " + e.getCorreo()
            );
        }

        repositorio.editar(e);
    }


    public void eliminar(int id) {
        repositorio.eliminar(id);
    }


    public List<Estudiante> obtenerTodos() {
        return repositorio.obtenerTodos();
    }


    public Estudiante buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }


   
    // Busquedas 
    public List<Estudiante> buscar(String texto, String criterio) {

        List<Estudiante> resultado = new ArrayList<>();

        for (Estudiante e : repositorio.obtenerTodos()) {

            switch (criterio) {

                case "Nombre":
                    if (e.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                        resultado.add(e);
                    }
                    break;

                case "Carrera":
                    if (e.getCarrera().toLowerCase().contains(texto.toLowerCase())) {
                        resultado.add(e);
                    }
                    break;

                case "Estado":

                    String estado = e.isActivo() ? "activo" : "inactivo";

                    if (estado.contains(texto.toLowerCase())) {
                        resultado.add(e);
                    }

                    break;

                default:
                    resultado.add(e);
            }
        }

        return resultado;
    }



    // ArrayLists y orden 
    public List<Estudiante> ordenarPorNombre() {

        List<Estudiante> lista = new ArrayList<>(repositorio.obtenerTodos());

        Collections.sort(
                lista,
                Comparator.comparing(Estudiante::getNombre)
        );

        return lista;
    }


    public List<Estudiante> ordenarPorEdad() {

        List<Estudiante> lista = new ArrayList<>(repositorio.obtenerTodos());

        Collections.sort(
                lista,
                Comparator.comparingInt(Estudiante::getEdad)
        );

        return lista;
    }



    // Validaciones 
    private void validar(Estudiante e) throws DatoInvalidoException {

        if (e.getNombre() == null || e.getNombre().trim().length() < 3) {

            throw new DatoInvalidoException(
                    "El nombre es obligatorio y debe tener al menos 3 caracteres."
            );
        }


        if (e.getEdad() <= 0) {

            throw new DatoInvalidoException(
                    "La edad debe ser mayor que 0."
            );
        }


        if (e.getCorreo() == null ||
                !e.getCorreo().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {

            throw new DatoInvalidoException(
                    "El correo no tiene un formato válido."
            );
        }


        if (e.getTelefono() != null
                && !e.getTelefono().isEmpty()
                && !e.getTelefono().matches("\\d+")) {

            throw new DatoInvalidoException(
                    "El teléfono solo puede contener números."
            );
        }


        if (e.getCarrera() == null || e.getCarrera().trim().isEmpty()) {

            throw new DatoInvalidoException(
                    "La carrera es obligatoria."
            );
        }
    }
}
