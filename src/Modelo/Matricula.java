
package Modelo;

import java.time.LocalDate;
/**
 *
 * @author nero2
 */
public class Matricula {
    private int id;
    private Estudiante estudiante;
    private Curso curso;
    private LocalDate fechaMatricula;
    private String estado;
    
    
    public Matricula(int id, Estudiante estudiante, Curso curso, LocalDate fechaMatricula, String estado) {
        this.id = id;
        this.estudiante = estudiante;
        this.curso = curso;
        this.fechaMatricula = fechaMatricula;
        this.estado = estado;
        
        
    }
    
    // ---- Getters y Setters
    
    public int getId() {
        return id;
    }
    
    public void setId( int id) {
        this.id = id; 
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Curso getCurso() {
        return curso;
        
    }
    
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public LocalDate getFechaMatricula() {
        return fechaMatricula;
    }
    
    public void setFechtaMatricula(LocalDate fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }
    
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public boolean isActiva() {
        return "Activa".equalsIgnoreCase(estado);
    }
    
     @Override
    public String toString() {
        String nombreEstudiante = (estudiante != null) ? estudiante.getNombre() : "(sin estudiante)";
        String codigoCurso = (curso != null) ? curso.getCodigo() : "(sin curso)";
        return nombreEstudiante + " - " + codigoCurso + " (" + estado + ")";
    }
    
}