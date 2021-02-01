package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class FileManager {
    /*
    * Lettura del file, indicato dal percorso assoluto (parametro).
    * In questo caso si leggono tutte le stringhe e memorizzate in un ArrayList di tipo String.
    * */
    public static ArrayList<String> readAllLines(String pathFile) throws IOException {
        return (ArrayList<String>) Files.readAllLines(Paths.get(pathFile));
    }

    /*
    * Lettura di ogni byte (corrispettivo carattere) all'interno del file. La stream viene "copiata" (si crea un oggetto di
    * tipo String che riceve in ingresso nel costruttore lo stream di byte) in una stringa.
    * */
    public static String readAllBytes(String pathFile) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pathFile)));
    }

    /*
    * Scrittura su file.
    * Come parametro riceve il percorso assoluto del file e il testo da scrivere.
    * Paths.get trasforma un percorso String in Path.
    * Collections.singleton ritorna un set immutabile contenente l'oggetto specificato.
    * UTF-8 codifica scelta.
    * */
    public static void writeOnFile(String text, String pathFile) throws IOException {
        Files.write(Paths.get(pathFile), Collections.singleton(text), StandardCharsets.UTF_8);
    }
}
