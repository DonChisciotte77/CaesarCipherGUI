package cipher;

/*
* Sottoclasse che deriva dalla superclasse CaesarCipher. Si distacca dalla classe padre per
* rispettare il principio di singola responsabilità. DecryptThread è responsabile di decifrare solo e soltanto una parte
* del testo e in modo parallelo ad altre istanze. Infatti implementa l'interfaccia Runnable e il relativo metodo run(),
* richiamato dal ExecutorService.
* */
public class DecryptThread extends CaesarCipher implements Runnable {
    private final DecryptedString decryptedString;

    public DecryptThread(DecryptedString decryptedString) {
        super(decryptedString.getStringToDecrypt());
        this.decryptedString = decryptedString;
    }

    // L'implementazione di run consiste nel modificare lo stato dell'istanza DecryptedString passata nel costruttore
    @Override
    public void run() {
        this.decryptedString.setStringToDecrypt(decrypt(decryptedString.getKey()));
    }
}
