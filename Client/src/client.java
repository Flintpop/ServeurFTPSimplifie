import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;

public class client {
    public static void main(String[] args) {
        BufferedReader server;
        PrintWriter sendServer;

        Socket socket = connectToServer();

        try {
            server = new BufferedReader(new InputStreamReader(Objects.requireNonNull(socket).getInputStream()));
            sendServer = new PrintWriter(socket.getOutputStream(), true);
            String commande;

            printWelcomeMessage(server);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Boucle principale
            sendUserData(sendServer, server);

            while (!(commande = reader.readLine()).equals("bye")) {
                sendServer.println(commande);
                printsMessagesFromServer(server);
                if (commande.startsWith("stor")) {
                    sendFile(commande.substring(5));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendFile(String fileName) {
        try {
            int bufferSize = 4096;

            InputStream file;
            OutputStream sendFile;

            Socket socketFile = connectToServer();

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


    public static Socket connectToServer() {
        Socket socket;
        int attempts = 0;
        while (attempts < 10) {
            socket = connectToServerTry();
            if (socket != null) return socket;
            attempts++;
        }
        throw new RuntimeException("Connection failed");
    }

    public static Socket connectToServerTry() {
        Socket socket;
        try {
            socket = new Socket("localhost", 4000);
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
    public static void printsMessagesFromServer(BufferedReader server) {
        String message = getMessageFromServer(server);

        while (message.charAt(0) == '1') {
            System.out.println(message);
            message = getMessageFromServer(server);
        }
        if (message.charAt(0) == '2') {
            System.err.println(message);
        } else {
            System.out.println(message);
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