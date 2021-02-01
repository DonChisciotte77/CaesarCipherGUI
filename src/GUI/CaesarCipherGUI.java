package GUI;

import cipher.CaesarCipher;
import cipher.DecryptThread;
import cipher.DecryptedString;
import utils.Constants;
import utils.FileManager;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CaesarCipherGUI extends JFrame {
    /*
    * Componenti UI dell'app. L'interfaccia è stata creata usando drag&drop dell'IDE.
    * Tutte le caratteristiche dei componenti sono nel file uiDesigner.xml.
    * */
    private JPanel mainPanel;
    private JButton buttonChooseFile;
    private JButton buttonEncrypt;
    private JButton buttonDecrypt;
    private JList<DecryptedString> listOutput;
    private JTextArea textAreaPreview;
    private JSpinner spinnerKey;
    private JButton buttonSave;
    private JLabel labelPreview;
    private JLabel labelKey;
    private JLabel labelInformation;
    private JScrollPane scrollPanePreview;
    private JScrollPane scrollPaneOutput;
    /*
    * Attributi della classe necessari all'operazione di criptazione/decriptazione.
    * nameFile: nome del file su cui agire.
    * directory: percorso del file.
    * text: contenuto del file.
    * isEncrypting: premuto bottone di criptazione.
    * isDecrypting: premuto bottone di decriptazione.
    * */
    private String nameFile;
    private String directory;
    private String text;

    public CaesarCipherGUI(String nameApp) {
        super(nameApp); // Chiamata a costruttore della superclasse JFrame. In questo caso si passa il nome dell'app.
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);   // Alla chiusura del JFrame anche il processo termina.
                                                                        // WindowConstants è un'interfaccia di Java che riflette meglio l'intento.
        this.setContentPane(mainPanel); // JPanel creato nel .form con i relativi componenti viene settato come principale.
        this.setSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));  // Dimensione del JFrame - sostituisce .pack() perché diventava di dimensioni piccole.
        this.spinnerKey.setModel(new SpinnerNumberModel(Constants.KEY_START , 0, Constants.KEY_END, 1)); // Lo spinner potrà assumere valori solo tra 0 e 26.

        this.addListeners();    // Aggiunta dei Listeners, che si occupano di gestire gli eventi (generati dall'interazione tra utente e GUI)
    }

    // Il Frame diventa visibile e l'utente può interagire.
    public void run() {
        this.setVisible(true);
    }

    private void addListeners() {
        /*
         * Risposta all'evento "tasto premuto". La classe FileDialog (awt) serve per selezionare un file nel filesystem.
         * Selezionato il file, nome e directory vengono copiati nei relativi attributi. In una stringa text si copia l'intero contenuto
         * del file. Se non viene selezionato nessun file, compare una finestra di dialogo informativa.
         * */
        buttonChooseFile.addActionListener(e -> {
            FileDialog fileDialog = new FileDialog(this, "Choose a file", FileDialog.LOAD);
            fileDialog.setFile("*.txt");    // E' possibile selezionare solo file .txt.
            fileDialog.setVisible(true);

            if (this.isFileChosen(fileDialog.getFile())) {
                this.listOutput.removeAll();
                this.nameFile = fileDialog.getFile();   // Copia nome file e directory.
                this.directory = fileDialog.getDirectory();

                try {
                    this.text = FileManager.readAllBytes(this.directory + this.nameFile);  // Lettura di tutto il file. Vedi metodo statico in FileManager.
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(this, ioException.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                if (this.text.isEmpty() || this.text.isBlank()) // Se il file è vuoto o contiene solo spazi, si informa l'utente
                    JOptionPane.showMessageDialog(this, "File is empty.", "Information",
                            JOptionPane.INFORMATION_MESSAGE);

                this.labelInformation.setText("Path: " + this.directory + this.nameFile);   // "Feedback", mostra il percorso del file
                this.textAreaPreview.setText(text); // Preview del file aggiornata
            }
        });

        /*
        * ActionListener del bottone "buttonEncrypt" - criptazione del file.
        * Richiamato quando l'utente clicca il bottone.
        * Verifica che il file sia stato scelto. Se presente si aggiorna la preview del file, che è stato cifrato dal
        * metodo encrypt. In ingresso al metodo viene passato il valore contenuto nel JSpinner.
        * */
        buttonEncrypt.addActionListener(e -> {
            if (isFileChosen(this.nameFile))
                this.textAreaPreview.setText(new CaesarCipher(text).encrypt((Integer) spinnerKey.getValue()));
        });

        /*
        * ActionListener del bottone "buttonDecrypt" - decriptazione del file.
        * Richiamato quando l'utente clicca il bottone.
        * Verifica che il file sia stato scelto. Se presente, si effettua un controllo sulla key. Se posta a 0 vuol dire
        * che l'utente finale non conosce la chiave di cifratura e bisogna eseguire una tecnica di "brute force" per trovarla,
        * altrimenti si usa direttamente quella contenuta nel JSpinner. Il file viene decifrato dal metodo decrypt della classe
        * CaesarCipher; parametro è sempre la chiave.
        * */
        buttonDecrypt.addActionListener(e -> {
            if (isFileChosen(this.nameFile)) {
                Integer key = (Integer) this.spinnerKey.getValue();

                if (key == 0) {
                    findKey();
                } else
                    this.textAreaPreview.setText(new CaesarCipher(text).decrypt((Integer) spinnerKey.getValue()));
            }
        });

         /*
         * ActionListener del bottone "buttonSave" - salvataggio del file.
         * Richiamato quando l'utente clicca il bottone.
         * Controllo sul contenuto della textArea. Se è vuota si informa l'utente che non si può salvare. In caso contrario
         * si riceve in input il nome del file da creare su cui scrivere il contenuto della textArea.
         * */
        buttonSave.addActionListener(e -> {
            if (!textAreaPreview.getText().isEmpty()) {
                String newFile = JOptionPane.showInputDialog(this, "New file name");

                try {
                    if (newFile != null) {  // Controllo che il nome sia inserito.
                        if (!newFile.endsWith(".txt"))  // Se non termina con .txt, si aggiunge in automatico (sostituendo i . con _).
                            newFile = newFile.replace('.', '_').concat(".txt");

                        FileManager.writeOnFile(textAreaPreview.getText(), this.directory + newFile);   // Scrittura su file. Vedi metodo statico di FileManager.
                        this.labelInformation.setText("Saved: " + this.directory + newFile);    // "Feedback", percorso file salvato (stesso di quello caricato).
                    } else
                        this.labelInformation.setText("File not created");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(this, ioException.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else
                JOptionPane.showMessageDialog(this, "Be sure to encrypt/decrypt a file before save.",
                        "Preview is empty", JOptionPane.INFORMATION_MESSAGE);
        });

        /*
        * ActionListener della JList "listOutput" - lista contenente i risultati del "brute force".
        * Richiamato quando si seleziona un elemento della lista. L'elemento contiene la chiave da utilizzare per la traduzione
        * dell'intero file.
        * */
        listOutput.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                this.textAreaPreview.setText(new CaesarCipher(text).decrypt(listOutput.getSelectedValue().getKey()));
        });
    }

    /*
    * Il metodo si occupa di decifrare la prima riga del testo con ogni possibile chiave, in modo tale da poter decifrare
    * sempre un testo. La coppia stringa-chiave viene realizzata in una classe DecryptedString che costituisce anche il tipo
    * degli elementi salvati nella JList. Per migliorare le prestazioni, in particolare in tempo, si è optato per i thread.
    * I thread vengono gestiti in modo del tutto automatico dal ExecutorService, che si occupa di gestire task asincrone.
    * */
    private void findKey() {
        this.listOutput.removeAll();    // Si eliminano gli elementi correnti della JList, relativi a una vecchia esecuzione.
        ArrayList<DecryptedString> tempList = new ArrayList<>();    // ArrayList temporaneo usato da updateJList per aggiornare gli
                                                                    // elementi della JList.
        // Creazione executor. Il metodo static newFixedThreadPool ritorna un riferimento a un oggetto ExecutorService
        // e crea un pool di thread. Il numero dei thread utilizzati dipende da quanti sono disponibili a runtime.
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = Constants.KEY_START; i <= Constants.KEY_END; i++) {
            // text.lines().findFirst().orElseThrow() ritorna la prima riga del testo. orElseThrow() ritorna una
            // eccezione se non esiste un elemento.
            try {
                DecryptedString decryptedString = new DecryptedString(text.lines().findFirst().orElseThrow(), i);
                executor.submit(new DecryptThread(decryptedString));
                tempList.add(decryptedString);
            } catch (NoSuchElementException exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        executor.shutdown(); // Attesa della terminazione di tutte le task e "chiusura" dell'executor, nel senso che non può essere più utilizzato.

        try {
            // Blocca fino a quando tutte le attività non hanno completato l'esecuzione dopo una richiesta di arresto,
            // o si verifica il timeout o il thread corrente viene interrotto, a seconda di quale situazione si verifica per prima.
            if (executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS))
                updateJList(tempList);
        } catch (InterruptedException interruptedException) {
            JOptionPane.showMessageDialog(this, interruptedException.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
    * Aggiornamento lista.
    * Viene creato un nuovo modello, che contiene al suo interno oggetti DecryptedString ordinati per chiave.
    * Il modello viene passato alla JList, per aggiornarne il contenuto.
    * */
    private void updateJList(ArrayList<DecryptedString> list) {
        DefaultListModel<DecryptedString> model = new DefaultListModel<>();
        Collections.sort(list);

        for (DecryptedString decryptedString : list) {
            model.addElement(decryptedString);
        }

        this.listOutput.setModel(model);
    }

    /*
    * Verifica se il file è stato scelto dal FileDialog.
    * Metodo privato creato per evitare ripetizione di codice.
    * */
    private Boolean isFileChosen(String nameFile) {
        if (nameFile == null) {
            JOptionPane.showMessageDialog(this,
                    "File not chosen. \nTo encrypt or decrypt a file you have to choose one.", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }
}
