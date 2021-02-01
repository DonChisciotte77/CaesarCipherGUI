package starter;

import GUI.CaesarCipherGUI;
import java.awt.*;

public class Starter {
    public static void main(String[] args) {
        /*
        * L'EDT (Event Dispatching Thread) è il thread responsabile dell'aggiornamento della GUI in Java Swing.
        * Con l'utilizzo del metodo invokeLater() di EventQueue, è possibile separare il thread di update della interfaccia
        * grafica da quelli che effettuano elaborazioni complesse o necessitano tanto tempo di esecuzione.
        * In questo modo, la GUI rimane reattiva anche durante elaborazioni lunghe.
        * */
        EventQueue.invokeLater(() -> new CaesarCipherGUI("Caesar Cipher").run());
    }
}
