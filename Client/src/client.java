import java.io.*;
import java.net.Socket;

public class client {
    public static void main(String[] args) {
        // create a socket with server IP and port
        BufferedReader input;
        PrintWriter output;

        try (Socket socket = new Socket("localhost", 2001)) {
            // get input and output streams
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            String commande;

            // Boucle principale
            getWelcomeMessage(input);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!(commande = reader.readLine()).equals("bye")) {
                output.println(commande);
                printsMessagesFromServer(input);
            }

        } catch (IOException e) {
            System.err.println("Error while connecting to server");
            System.err.println(e.getMessage());
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
        String message;
        message = getMessageFromServer(server);

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

    public static void sendUserData(PrintWriter sendServer) {
        String user;
        String password;

        try {
            System.out.println("User : ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            user = reader.readLine();
            System.out.println("Password : ");
            password = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Send data to server
        sendServer.println(user);
        sendServer.println(password);

    }
}