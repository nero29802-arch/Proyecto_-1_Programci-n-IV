
//Paquete
package Modelo;

//Clase 
public class Estudiante {

    private int id;
    private String nombre;
    private int edad;
    private String carrera;
    private String genero;
    private boolean activo;
    private String correo;
    private String telefono;
    private String observaciones;

// Constructor 
    public Estudiante(int id, String nombre, int edad, String carrera,String genero, boolean activo, String correo,String telefono, String observaciones)
    
    {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.carrera = carrera;
        this.genero = genero;
        this.activo = activo;
        this.correo = correo;
        this.telefono = telefono;
        this.observaciones = observaciones;
    }

 //Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getCarrera() {
        return carrera;
    }

    public String getGenero() {
        return genero;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getObservaciones() {
        return observaciones;
    }

 //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    

    @Override
    public String toString() {
        return nombre + " (" + correo + ")";
    }
    
    
    public Estudiante() {
}
}




