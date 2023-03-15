import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class IHMClient extends JFrame implements ActionListener {
    private final JTextField textFieldUsername;
    private final JTextField textFieldPassword;
    private final JTextField textFieldNewDir;
    private final JTextField textFieldNewUser;
    private final JTextField textFieldNewPassword;
    private final JTextField textFieldStor;
    private final JButton buttonConnect;
    private final JButton buttonMKDIR;
    private final JButton buttonRMDIR;
    private final JButton buttonADDUSER;
    private final JButton buttonDownload;
    private final JButton buttonChangeDirectory;
    private final JButton buttonUpload;
    private final JLabel labelUsername;
    private final JLabel labelPassword;
    JList<String> fileList;
    DefaultListModel<String> stringFolderFilesList;

    // Socket de connexion au serveur FTP
    private Socket socket = null;

    BufferedReader server = null;
    PrintWriter sendServer = null;

    JPanel panel1;
    JPanel panel2;
    JPanel panel3;
    JPanel panel4;
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
        textFieldStor = new JTextField(20);
        buttonConnect = new JButton("Connect");
        buttonDownload = new JButton("Download");
        buttonUpload = new JButton("Upload");
        buttonMKDIR = new JButton("Make new directory");
        buttonRMDIR = new JButton("Remove directory");
        buttonADDUSER = new JButton("Add user");
        buttonChangeDirectory = new JButton("Change directory");

        stringFolderFilesList = new DefaultListModel<>();
        fileList = new JList<>(stringFolderFilesList);
        fileList.setPreferredSize(new Dimension(500, 500));

        // Ajout des composants à la fenêtre

        initConnectFrame();
        frame2 = new JFrame("FTP");

        panel2 = new JPanel();

        fileList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    changeDirectory(sendServer, server);
                }
            }
        });
        JScrollPane comp = new JScrollPane(fileList);
        comp.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    changeDirectorySelect(sendServer, server);
                }
            }
        });
        panel2.add(comp);
        panel2.add(buttonDownload);
        panel2.add(buttonChangeDirectory);
        panel2.add(buttonRMDIR);

        panel3 = new JPanel();
        panel3.add(new JLabel("New directory's name:"));
        panel3.add(textFieldNewDir);
        panel3.add(buttonMKDIR);
        panel3.add(textFieldStor);
        panel3.add(buttonUpload);

        panel4 = new JPanel();
        panel4.add(textFieldNewUser);
        panel4.add(textFieldNewPassword);
        panel4.add(buttonADDUSER);

        frame2.add(panel2, "Center");
        frame2.add(panel3, "North");
        frame2.add(panel4, "South");
        // Ajout des écouteurs d'événements aux boutons
        buttonConnect.addActionListener(this);
        buttonDownload.addActionListener(this);
        buttonUpload.addActionListener(this);
        buttonMKDIR.addActionListener(this);
        buttonRMDIR.addActionListener(this);
        buttonADDUSER.addActionListener(this);
        buttonChangeDirectory.addActionListener(this);

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
        frame2.addWindowListener(new WindowEventHandler());
        frame1.addWindowListener(new WindowEventHandler());
        frame1.setVisible(true);
        frame2.setVisible(false);
        connect();
    }

    public void initConnectFrame() {
        panel1 = new JPanel();
        panel1.add(labelUsername);
        panel1.add(textFieldUsername);
        panel1.add(labelPassword);
        panel1.add(textFieldPassword);
        panel1.add(buttonConnect);

        frame1 = new JFrame("Connexion");
        frame1.add(panel1, "North");
    }

    // Méthode pour se connecter au serveur FTP
    private void connect() {
        try {
            int port = 2000;
            socket = client.connectToServer(port);

            server = new BufferedReader(new InputStreamReader(Objects.requireNonNull(socket).getInputStream()));
            sendServer = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            client.printWelcomeMessage(server);

            boolean connected = sendUserData(sendServer, server);
            if (connected) displayCurrentFolder();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to FTP server.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("Erreur le serveur s'est brusquement arrêté");
            System.err.println(e.getMessage());
            e.printStackTrace();

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
        sendServer.println("get " + fileList.getSelectedValue().substring(5));
        client.printsMessagesFromServer(server);
        client.getFile(fileList.getSelectedValue().substring(5), server);
    }

    // Méthode pour téléverser un fichier vers le serveur FTP
    private void upload() {
        sendServer.println("stor " + textFieldStor.getText());
        client.printsMessagesFromServer(server);
        client.sendFile(textFieldStor.getText());
        getAndProcessLSCall();
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
        } else if (e.getSource() == buttonChangeDirectory) {
            changeDirectory(sendServer, server);
        }
    }

    private void changeDirectory(PrintWriter sendServer, BufferedReader server) {
        int index = fileList.getSelectedIndex();

        if (index == 0) {
            sendServer.println("cd ..");
            client.printsMessagesFromServer(server);
            displayCurrentFolder();
            return;
        }

        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a directory.");
            return;
        }

        if (!stringFolderFilesList.get(index).contains(".")) {
            sendServer.println("cd " + stringFolderFilesList.get(index).substring(4));
            client.printsMessagesFromServer(server);
            displayCurrentFolder();
            return;
        }

        JOptionPane.showMessageDialog(this, "You did not select a directory.");
    }

    private void changeDirectorySelect(PrintWriter sendServer, BufferedReader server) {
        String selected = fileList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a directory.");
            return;
        }

        if (selected.equals("..")) {
            sendServer.println("cd ..");
            client.printsMessagesFromServer(server);
            displayCurrentFolder();
            return;
        }

        if (!selected.contains(".")) {
            sendServer.println("cd " + selected.substring(4));
            client.printsMessagesFromServer(server);
            displayCurrentFolder();
            return;
        }

        JOptionPane.showMessageDialog(this, "You did not select a directory.");
    }
    public boolean sendUserData(PrintWriter sendServer, BufferedReader server) {
//        String user = "user " + this.textFieldUsername.getText();
//        String password = "pass " + this.textFieldPassword.getText();
        boolean userConnected;
        String user = "user abdelaziz";
        String password = "pass abdoul";
        // Send data to server
        sendServer.println(user);
        userConnected = client.printsMessagesFromServer(server);
        sendServer.println(password);
        userConnected = userConnected && client.printsMessagesFromServer(server);

        if (!userConnected) {
            JOptionPane.showMessageDialog(this, "Could not connect to FTP server.");
            return false;
        }

        frame1.setVisible(false);
        frame2.setVisible(true);
        return true;
    }

    public void sendNewUser(PrintWriter sendServer, BufferedReader server) {
        String newUser = "adduser " + this.textFieldNewUser.getText() + " " + this.textFieldNewPassword.getText();

        // Send data to server
        sendServer.println(newUser);
        boolean res = client.printsMessagesFromServer(server);
        if (!res) JOptionPane.showMessageDialog(this, "Could not add user. You are not an admin.");
    }

    public void sendNewDir(PrintWriter sendServer, BufferedReader server) {
        String newDir = "mkdir " + this.textFieldNewDir.getText();

        // Send data to server
        sendServer.println(newDir);
        client.printsMessagesFromServer(server);
        displayCurrentFolder();
    }

    public void sendRmDir(PrintWriter sendServer, BufferedReader server) {
        int index = fileList.getSelectedIndex();
        if (index == 0 || index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a directory.");
            return;
        }

        if (!stringFolderFilesList.get(index).contains(".")) {
            sendServer.println("rmdir " + stringFolderFilesList.get(index).substring(4));
            client.printsMessagesFromServer(server);
            displayCurrentFolder();
            return;
        }

        JOptionPane.showMessageDialog(this, "You did not select a directory.");
//        String rmDir = "rmdir " + this.textFieldRMDir.getText();
//
//        // Send data to server
//        sendServer.println(rmDir);
//        client.printsMessagesFromServer(server);
//        displayCurrentFolder();
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

    public void displayCurrentFolder() {
        ArrayList<String> listDirectories = getAndProcessLSCall();
        if (listDirectories == null) {
            JOptionPane.showMessageDialog(this, "Could not load this folder.");
            client.printsMessagesFromServer(server);
            return;
        }

        // Testons
        stringFolderFilesList.clear();
        for (String listDirectory : listDirectories) {
            stringFolderFilesList.addElement(listDirectory);
        }
        // TODO: Faire la détection de clics sur la liste JLabel voir didi lien je crois

    }

    public ArrayList<String> getAndProcessLSCall() {
        sendServer.println("ls");

        ArrayList<String> directoriesAndFiles = getQueryFromServer();

        if (directoriesAndFiles == null) return null;
        directoriesAndFiles.set(0, "..");

        String curItem;
        for (int i = 0; i < directoriesAndFiles.size(); i++) {
            curItem = directoriesAndFiles.get(i);
            if (curItem.contains("0 Fin de la liste")) directoriesAndFiles.remove(directoriesAndFiles.get(i));
            else if (curItem.contains(":")) directoriesAndFiles.remove(directoriesAndFiles.get(i));
        }

        for (int i = 0; i < directoriesAndFiles.size(); i++) {
            curItem = directoriesAndFiles.get(i);
            if (!curItem.contains(".")) directoriesAndFiles.set(i, curItem.replaceAll("1", "📁 "));
            else directoriesAndFiles.set(i, curItem.replaceAll("1", "🗎  "));

        }

        return directoriesAndFiles;
    }

    public ArrayList<String> getQueryFromServer() {
        String message = client.getMessageFromServer(server);
        ArrayList<String> messages = new ArrayList<>(Collections.singleton(message));
        char messageContinue = '1';
        char messageErrorEnd = '2';

        while (message.charAt(0) == messageContinue) {
            message = client.getMessageFromServer(server);
            messages.add(message);
        }
        if (message.charAt(0) == messageErrorEnd) {
            return null;
        } else {
            return messages;
        }
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        new IHMClient();
    }
}
