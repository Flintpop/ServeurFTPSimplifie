import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class IHMClient extends JFrame implements ActionListener {
    // Composants de l'interface
    private JLabel labelUsername, labelPassword;
    private JTextField textFieldUsername, textFieldPassword;
    private JButton buttonConnect, buttonDownload, buttonUpload;
    private JList<String> fileList;

    // Socket de connexion au serveur FTP
    private Socket socket;

    // Constructeur
    public IHMClient() {
        super("FTP Client");

        // Initialisation des composants
        labelUsername = new JLabel("Username:");
        labelPassword = new JLabel("Password:");
        textFieldUsername = new JTextField(20);
        textFieldPassword = new JTextField(20);
        buttonConnect = new JButton("Connect");
        buttonDownload = new JButton("Download");
        buttonUpload = new JButton("Upload");
        fileList = new JList<String>();

        // Ajout des composants à la fenêtre
        JPanel panel1 = new JPanel();
        panel1.add(labelUsername);
        panel1.add(textFieldUsername);
        panel1.add(labelPassword);
        panel1.add(textFieldPassword);
        panel1.add(buttonConnect);

        JPanel panel2 = new JPanel();
        panel2.add(new JScrollPane(fileList));
        panel2.add(buttonDownload);
        panel2.add(buttonUpload);

        getContentPane().add(panel1, "North");
        getContentPane().add(panel2, "Center");

        // Ajout des écouteurs d'événements aux boutons
        buttonConnect.addActionListener(this);
        buttonDownload.addActionListener(this);
        buttonUpload.addActionListener(this);

        // Configuration de la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 300);
        setVisible(true);
    }

    // Méthode pour se connecter au serveur FTP
    private void connect() {
        BufferedReader server;
        PrintWriter sendServer;
        Socket socket = null;

        try {
            socket = client.connectToServer(2000);

            server = new BufferedReader(new InputStreamReader(Objects.requireNonNull(socket).getInputStream()));
            sendServer = new PrintWriter(socket.getOutputStream(), true);
            String commande;

            client.printWelcomeMessage(server);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Boucle principale
            sendUserData(sendServer, server);

            while (!(commande = reader.readLine()).equals("bye")) {
                sendServer.println(commande);
                client.printsMessagesFromServer(server);

                if (commande.startsWith("stor")) {
                    client.sendFile(commande.substring(5));
                }

                if (commande.startsWith("get")) {
                    client.getFile(commande.substring(4), server);
                }

                if (commande.startsWith("bye")) {
                    System.out.println("Connection closed");
                }
            }

            socket.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to FTP server.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("Erreur le serveur s'est brusquement arrêté");

            // Fermer le socket proprement
            try {
                Objects.requireNonNull(socket).close();
            } catch (IOException ex) {
                System.err.println("Erreur lors de la fermeture du socket");
                throw new RuntimeException(ex);
            }
        }
    }

    // Méthode pour télécharger un fichier depuis le serveur FTP
    private void download() {
        // Code pour télécharger un fichier depuis le serveur FTP
    }

    // Méthode pour téléverser un fichier vers le serveur FTP
    private void upload() {
        // Code pour téléverser un fichier vers le serveur FTP
    }

    // Méthode pour gérer les événements des boutons
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonConnect) {
            connect();
        } else if (e.getSource() == buttonDownload) {
            download();
        } else if (e.getSource() == buttonUpload) {
            upload();
        }
    }

    public void sendUserData(PrintWriter sendServer, BufferedReader server) {
        String user = "user " + this.textFieldUsername.getText();
        String password = "pass " + this.textFieldPassword.getText();

        // Send data to server
        sendServer.println(user);
        client.printsMessagesFromServer(server);
        sendServer.println(password);
        client.printsMessagesFromServer(server);
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        new IHMClient();
    }
}
