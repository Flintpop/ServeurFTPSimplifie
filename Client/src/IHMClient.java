import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class IHMClient extends JFrame implements ActionListener {
    private final JTextField textFieldUsername;
    private final JTextField textFieldPassword;
    private final JTextField textFieldNewDir;
    private final JTextField textFieldNewUser;
    private final JTextField textFieldNewPassword;
    private final JTextField textFieldRMDir;
    private final JButton buttonConnect;
    private final JButton buttonMKDIR;
    private final JButton buttonRMDIR;
    private final JButton buttonADDUSER;
    private final JButton buttonDownload;
    private final JButton buttonUpload;
    private final JLabel labelUsername;
    private final JLabel labelPassword;
    JList<String> fileList;

    // Socket de connexion au serveur FTP
    private Socket socket = null;

    BufferedReader server = null;
    PrintWriter sendServer = null;

    JPanel panel1;
    JPanel panel2;
    JPanel panel3;
    JFrame frame1;
    JFrame frame2;

    // Constructeur
    public IHMClient() {
        super("FTP Client");

        // Initialisation des composants de l'interface
        labelUsername = new JLabel("Username:");
        labelPassword = new JLabel("Password:");
        textFieldUsername = new JTextField(20);
        textFieldPassword = new JTextField(20);
        textFieldNewDir = new JTextField(20);
        textFieldNewUser = new JTextField(20);
        textFieldNewPassword = new JTextField(20);
        textFieldRMDir = new JTextField(20);
        buttonConnect = new JButton("Connect");
        buttonDownload = new JButton("Download");
        buttonUpload = new JButton("Upload");
        buttonMKDIR = new JButton("Make new directory");
        buttonRMDIR = new JButton("Remove directory");
        buttonADDUSER = new JButton("Add user");
        fileList = new JList<>();

        // Ajout des composants à la fenêtre
        panel1 = new JPanel();
        panel1.add(labelUsername);
        panel1.add(textFieldUsername);
        panel1.add(labelPassword);
        panel1.add(textFieldPassword);
        panel1.add(buttonConnect);
        panel1.add(buttonADDUSER);


        frame1 = new JFrame("Connexion");
        frame2 = new JFrame("FTP");

        frame1.add(panel1, "North");

        panel2 = new JPanel();
        panel2.add(new JScrollPane(fileList));
        panel2.add(buttonDownload);
        panel2.add(buttonUpload);
        panel3 = new JPanel();
        panel3.add(new JLabel("New directory's name:"));
        panel3.add(textFieldNewDir);
        panel3.add(buttonMKDIR);
        panel3.add(new JLabel("Directory's name to remove:"));
        panel3.add(textFieldRMDir);
        panel3.add(buttonRMDIR);

        frame2.add(panel2, "Center");
        frame2.add(panel3, "North");
        // Ajout des écouteurs d'événements aux boutons
        buttonConnect.addActionListener(this);
        buttonDownload.addActionListener(this);
        buttonUpload.addActionListener(this);
        buttonMKDIR.addActionListener(this);
        buttonRMDIR.addActionListener(this);
        buttonADDUSER.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Code pour fermer l'application
                System.exit(0);
            }
        });

        // Configuration de la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame1.setSize(1500, 300);
        frame2.setSize(1500, 300);
        addWindowListener(new WindowEventHandler());
        frame2.setVisible(true);
        frame1.setVisible(true);
        this.pack();
    }

    // Méthode pour se connecter au serveur FTP
    private void connect() {
        try {
            int port = 2000;
            socket = client.connectToServer(port);

            server = new BufferedReader(new InputStreamReader(Objects.requireNonNull(socket).getInputStream()));
            sendServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            client.printWelcomeMessage(server);

            // Boucle principale
            sendUserData(sendServer, server);
            sendServer.println(reader.readLine());


        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to FTP server.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("Erreur le serveur s'est brusquement arrêté");
            System.err.println(e.getMessage());
            e.printStackTrace();
            try {
                Thread.sleep(5000000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

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
        } else if (e.getSource() == buttonMKDIR) {
            sendNewDir(sendServer, server);
        } else if (e.getSource() == buttonRMDIR) {
            sendRmDir(sendServer, server);
        } else if (e.getSource() == buttonADDUSER) {
            sendNewUser(sendServer, server);
        }
    }

    public void sendUserData(PrintWriter sendServer, BufferedReader server) {
        //String user = "user " + this.textFieldUsername.getText();
        //String password = "pass " + this.textFieldPassword.getText();
        boolean userConnected;
        String user = "user abdelaziz";
        String password = "pass abdoul";
        // Send data to server
        sendServer.println(user);
        userConnected = client.printsMessagesFromServer(server);
        sendServer.println(password);
        userConnected = userConnected && client.printsMessagesFromServer(server);

        // S'il s'est connecté. Comment on sait ?
        if (!userConnected) {
            JOptionPane.showMessageDialog(this, "Could not connect to FTP server.");
            return;
        }

        frame1.setVisible(false);
        frame2.setVisible(true);
    }

    public void sendNewUser(PrintWriter sendServer, BufferedReader server) {
        String newUser = "adduser " + this.textFieldNewUser.getText() + " " + this.textFieldNewPassword.getText();

        // Send data to server
        sendServer.println(newUser);
        client.printsMessagesFromServer(server);
    }

    public void sendNewDir(PrintWriter sendServer, BufferedReader server) {
        String newDir = "mkdir " + this.textFieldNewDir.getText();

        // Send data to server
        sendServer.println(newDir);
        client.printsMessagesFromServer(server);
    }

    public void sendRmDir(PrintWriter sendServer, BufferedReader server) {
        String rmDir = "rmdir " + this.textFieldRMDir.getText();

        // Send data to server
        sendServer.println(rmDir);
        client.printsMessagesFromServer(server);
    }

    private class WindowEventHandler extends WindowAdapter {
        public void windowClosing(WindowEvent evt) {
            try {
                // Fermer la connexion au serveur FTP ici (si elle est ouverte)
                if (socket != null) {
                    socket.close();
                    if (sendServer != null)
                        sendServer.println("bye");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Fermer l'application
            System.exit(0);
        }
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        new IHMClient();
    }
}
