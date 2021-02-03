package cipher;

import java.util.ArrayList;

/*
* Sottoclasse che deriva dalla superclasse CaesarCipher. Si distacca dalla classe padre per
* rispettare il principio di singola responsabilità. DecryptThread è responsabile di decifrare solo e soltanto una parte
* del testo e in modo parallelo ad altre istanze. Infatti implementa l'interfaccia Runnable e il relativo metodo run(),
* richiamato dal ExecutorService.
* */
public class DecryptThread extends CaesarCipher implements Runnable {
    private final DecryptedString decryptedString;
    private final ArrayList<DecryptedString> list;

    public DecryptThread(DecryptedString decryptedString, ArrayList<DecryptedString> list) {
        super(decryptedString.getStringToDecrypt());
        this.decryptedString = decryptedString;
        this.list = list;
    }

    // L'implementazione di run consiste nel modificare lo stato dell'istanza DecryptedString passata nel costruttore
    @Override
    public void run() {
        synchronized(list) {
            this.decryptedString.setStringToDecrypt(decrypt(decryptedString.getKey()));
            list.add(this.decryptedString);
        }
    }
}
