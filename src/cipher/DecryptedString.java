package cipher;

public class DecryptedString implements Comparable<DecryptedString> {
    private String stringToDecrypt; // Stringa da decriptare.
    private final Integer key;  // Chiave utilizzata.

    public DecryptedString(String string, Integer key) {
        this.stringToDecrypt = string;
        this.key = key;
    }

    // Override del metodo compareTo per permette di ordinare collezioni (per valore della chiave).
    @Override
    public int compareTo(DecryptedString o) {
        return this.key > o.getKey() ? 1 : -1;
    }

    public Integer getKey() {
        return key;
    }

    public String getStringToDecrypt() {
        return stringToDecrypt;
    }

    public void setStringToDecrypt(String stringToDecrypt) {
        this.stringToDecrypt = stringToDecrypt;
    }

    // Richiamato dalla JList per mostrare gli elementi contenuti.
    @Override
    public String toString() {
        return "Key: " + this.key + " String: " + this.stringToDecrypt;
    }
}
