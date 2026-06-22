package Util;

import java.io.FileWriter;
import java.io.IOException;

public class ArchivoUtil {

    public static void exportarTXT(String texto, String ruta)
            throws IOException {

        FileWriter writer = new FileWriter(ruta);

        writer.write(texto);

        writer.close();
    }
}