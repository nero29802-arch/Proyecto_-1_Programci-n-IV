package Modelo;

public class Calificacion {

    private int id;
    private Estudiante estudiante;
    private Curso curso;
    private double nota;
    private String observacion;

    public Calificacion() {
    }

    public Calificacion(int id, Estudiante estudiante, Curso curso,
            double nota, String observacion) {
        this.id = id;
        this.estudiante = estudiante;
        this.curso = curso;
        this.nota = nota;
        this.observacion = observacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        if (nota >= 70) {
            return "Aprobado";
        } else {
            return "Reprobado";
        }
    }

    @Override
    public String toString() {
        return "Calificacion{" +
                "id=" + id +
                ", estudiante=" + estudiante +
                ", curso=" + curso +
                ", nota=" + nota +
                ", observacion=" + observacion +
                ", estado=" + getEstado() +
                '}';
    }
}