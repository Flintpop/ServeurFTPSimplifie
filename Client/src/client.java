import java.io.*;
import java.net.Socket;

public class client {
    public static void main(String[] args) {
        // create a socket with server IP and port
        BufferedReader server;
        PrintWriter sendServer;

        try (Socket socket = new Socket("localhost", 2001)) {
            // get input and output streams
            server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sendServer = new PrintWriter(socket.getOutputStream(), true);
            String commande;

            getWelcomeMessage(server);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Boucle principale
            sendUserData(sendServer, server);
            while (!(commande = reader.readLine()).equals("bye")) {
                sendServer.println(commande);
                printsMessagesFromServer(server);
                if (commande.startsWith("stor")) {
                    sendFile(sendServer, server, commande.substring(5));
                }
            }

        } catch (IOException e) {
            System.err.println("Error while connecting to server");
            System.err.println(e.getMessage());
        }
    }

    private static void sendFile(PrintWriter sendServer, BufferedReader server, String fileName) {
        try {
            try (Socket socketFile = new Socket("localhost", 4000)) {
                BufferedReader file = new BufferedReader(new FileReader(fileName));
                PrintWriter sendFile = new PrintWriter(socketFile.getOutputStream(), true);
                String line;
                while ((line = file.readLine()) != null) {
                    sendFile.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du socket pour la transmission des données");
            throw new RuntimeException(e);
        }
    }

    public static void getWelcomeMessage(BufferedReader server) throws IOException {
        // Get welcome message
        printsMessagesFromServer(server);
    }

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
            throw new RuntimeException("Message from server is not valid");
        }

        return message;
    }

    /**
     * Print all messages from server until it send a message with code 0
     *
     * @param server BufferedReader object to read from serve
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
        String user = "user breton";
        String password = "pass bretons";

        // Send data to server
        sendServer.println(user);
        printsMessagesFromServer(server);
        sendServer.println(password);
        printsMessagesFromServer(server);

    }
}