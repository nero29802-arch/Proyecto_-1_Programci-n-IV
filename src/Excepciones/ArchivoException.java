
package Excepciones;

/**
 *
 * @author Charly Cimino
 * Aprendé más Java en mi canal: https://www.youtube.com/c/CharlyCimino
 * Encontrá más código en mi repo de GitHub: https://github.com/CharlyCimino
 */

public class ArchivoException extends Exception {
    public ArchivoException(String mensaje) {
        super(mensaje);
    }
    public ArchivoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}