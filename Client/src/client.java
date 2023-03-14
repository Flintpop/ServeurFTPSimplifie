import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;

public class client {
    public static void main(String[] args) {
        BufferedReader server;
        PrintWriter sendServer;
        Socket socket = null;

        try {
            socket = connectToServer(2000);

            server = new BufferedReader(new InputStreamReader(Objects.requireNonNull(socket).getInputStream()));
            sendServer = new PrintWriter(socket.getOutputStream(), true);
            String commande;

            printWelcomeMessage(server);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Boucle principale
            //sendUserData(sendServer, server);

            while (!(commande = reader.readLine()).equals("bye")) {
                sendServer.println(commande);
                printsMessagesFromServer(server);

                manageSpecialsCommands(commande, server);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            // e.printStackTrace();
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

    private static void manageSpecialsCommands(String commande, BufferedReader server) {
        if (commande.startsWith("stor")) {
            sendFile(commande.substring(5));
        } else if (commande.startsWith("get")) {
            getFile(commande.substring(4), server);
        } else if (commande.startsWith("bye")) {
            System.out.println("Connection closed");
        }
    }

    public static void getFile(String fileName, BufferedReader server) {

        try (Socket socketFile = connectToServer(4000)) {

            File directory = new File("Download");

            if (directory.mkdirs()) {
                System.out.println("Un dossier de téléchargement a été créé");
            }

            InputStream is = socketFile.getInputStream();
            OutputStream fos = new FileOutputStream(directory.getName() + "/" + fileName);

            byte[] buffer = new byte[1024];
            int nbOctetsLus;

            while ((nbOctetsLus = is.read(buffer)) > 0) {
                fos.write(buffer, 0, nbOctetsLus);
            }

            System.out.println("Transfert terminé");
            printsMessagesFromServer(server);
            fos.close();
            is.close();

        } catch (Exception e) {
            System.err.println("Erreur lors de la création du socket pour la transmission des données");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static void sendFile(String fileName) {
        try {
            int bufferSize = 4096;

            InputStream file;
            OutputStream sendFile;

            Socket socketFile = connectToServer(4000);

            try {
                file = new FileInputStream(fileName);
                sendFile = socketFile.getOutputStream();

                byte[] buffer = new byte[bufferSize];
                int count = file.read(buffer);

                while ((count) > 0) {
                    sendFile.write(buffer, 0, count);
                    count = file.read(buffer);
                }

                socketFile.close();
            } catch (FileNotFoundException e) {
                System.err.println("Fichier introuvable");
            } finally {
                Objects.requireNonNull(socketFile).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Socket connectToServer(int port) {
        Socket socket;
        int attempts = 0;
        while (attempts < 10) {
            socket = connectToServerTry(port);
            if (socket != null) return socket;
            attempts++;
        }
        throw new RuntimeException("Connection failed");
    }

    /**
     * Essaie de se connecter au serveur via un socket TCP. Si cela n'est pas possible, la fonction affiche
     * "Connection failed. Trying again in 1 second"
     * @param port
     * @return
     */
    public static Socket connectToServerTry(int port) {
        Socket socket;
        try {
            socket = new Socket("localhost", port);
            System.out.println("Connection successful");
            return socket;
        } catch (ConnectException e) {
            System.err.println("Connection failed. Trying again in 1 second");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            System.err.println("Error while connecting to server");
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void printWelcomeMessage(BufferedReader server) throws IOException {
        // Get welcome message
        printsMessagesFromServer(server);
    }

    /**
     * Reçoit un message du serveur et vérifie qu'il est valide.
     *
     * @param server BufferedReader utilisé pour lire les messages du serveur
     * @return Le message du serveur
     */
    public static String getMessageFromServer(BufferedReader server) {
        String message;

        try {
            message = server.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (message.charAt(0) != '0' && message.charAt(0) != '1' && message.charAt(0) != '2') {
            System.err.println("Message : ");
            System.err.println(message);
            throw new RuntimeException("Le message du serveur est invalide");
        }

        return message;
    }

    /**
     * Affiche les messages du serveur jusqu'à ce qu'il envoie un message de fin.
     *
     * @param server BufferedReader utilisé pour lire les messages du serveur
     */
    public static boolean printsMessagesFromServer(BufferedReader server) {
        String message = getMessageFromServer(server);
        char messageContinue = '1';
        char messageErrorEnd = '2';

        while (message.charAt(0) == messageContinue) {
            System.out.println(message);
            message = getMessageFromServer(server);
        }
        if (message.charAt(0) == messageErrorEnd) {
            System.err.println(message);
            return false;
        } else {
            System.out.println(message);
            return true;
        }
    }

    /**
     * Fonction de debug pour skip la partie connection.
     *
     * @param sendServer Utilisé pour envoyer des données au serveur sftp
     * @param server     Utilisé pour afficher les messages de réponse du serveur sftp
     */
    public static void sendUserData(PrintWriter sendServer, BufferedReader server) {
        String user = "user abdelaziz";
        String password = "pass abdoul";

        // Send data to server
        sendServer.println(user);
        printsMessagesFromServer(server);
        sendServer.println(password);
        printsMessagesFromServer(server);
    }
}