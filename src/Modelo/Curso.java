/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author HP
 */
 /**
 * Clase Modelo (entidad) que representa un Curso dentro del sistema académico.
 * Solo contiene atributos, constructor y getters/setters: no tiene lógica
 * de validación ni de negocio (eso vive en CursoNegocio).
 */
public class Curso {

    private int id;            // Identificador único, se asigna automáticamente en el Repositorio
    private String codigo;     // Código del curso, ej: "PROG4". No se permiten duplicados.
    private String nombre;     // Nombre del curso, ej: "Programación IV"
    private int creditos;      // Cantidad de créditos del curso (debe ser mayor que 0)
    private String profesor;   // Nombre del profesor a cargo del curso
    private boolean activo;    // Indica si el curso está activo (true) o inactivo (false)

    /**
     * Constructor con todos los atributos del curso.
     */
    public Curso(int id, String codigo, String nombre, int creditos, String profesor, boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.profesor = profesor;
        this.activo = activo;
    }

    // ----- Getters y Setters -----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public String getProfesor() { return profesor; }
    public void setProfesor(String profesor) { this.profesor = profesor; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /**
     * Representación en texto del curso. Se usa, por ejemplo, cuando un objeto
     * Curso se muestra dentro de un JComboBox (en el módulo de Matrícula).
     */
    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}