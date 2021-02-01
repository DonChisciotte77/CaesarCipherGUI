package cipher;

public class CaesarCipher {
    private String text; // Contenuto del file da cifrare/decifrare.

    public CaesarCipher(String text) {
        this.text = text;
    }

    /*
    * Metodo di criptazione.
    * In ingresso riceve la chiave che verrà sommata a tutti i caratteri della stringa text.
    * Il metodo fa in modo di non convertire caratteri fuori dal range delle lettere maiuscole o minuscole.
    * Ritorna il testo cifrato.
    * */
    public String encrypt(Integer key) {
        StringBuilder cipheredText = new StringBuilder(this.text.length());

        for (Character ch : this.text.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                ch = (char) (ch + key);

                if (ch > 'z')
                    ch = (char) (ch - 'z' + 'a' - 1);
            } else if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch + key);

                if (ch > 'Z')
                    ch = (char) (ch - 'Z' + 'A' - 1);
            }
            cipheredText.append(ch);
        }

        return cipheredText.toString();
    }

    /*
    * Metodo di decriptazione.
    * In ingresso riceve la chiave che verrà sottratta a tutti i caratteri della stringa text.
    * Come encrypt, ci sono controlli in modo tale da lavorare sempre su caratteri alfabetici.
    * Ritorna il testo decifrato.
    * */
    public String decrypt(Integer key) {
        StringBuilder plainText = new StringBuilder(this.text.length());

        for (Character ch : this.text.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                ch = (char) (ch - key);

                if (ch < 'a')
                    ch = (char) (ch + 'z' - 'a' + 1);
            } else if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch - key);

                if (ch < 'A')
                    ch = (char) (ch + 'Z' - 'A' + 1);
            }
            plainText.append(ch);
        }

        return plainText.toString();
    }
}
