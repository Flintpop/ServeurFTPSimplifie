/*
 * TP JAVA RIP
 * Min Serveur FTP
 * */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main implements Runnable {

    private final Socket socket;

    public Main(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Le Serveur FTP");

        boolean interrupted = false;
        ServerSocket serveurFTP = new ServerSocket(2000);
        while (!interrupted) {
            Socket socket = serveurFTP.accept();
            new Thread(new Main(socket)).start();
        }
        serveurFTP.close();
    }

    @Override
    public void run() {
        CommandExecutor commandExecutor = new CommandExecutor();
        try {

            System.out.println("Nouveau client connecté : " + socket.getInetAddress());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream sendClient = new PrintStream(socket.getOutputStream());

            sendClient.println("1 Bienvenue ! ");
            sendClient.println("1 Serveur FTP Personnel.");
            sendClient.println("0 Authentification : ");

            String commande;

            // Attente de reception de commandes et leur execution
            while (!(commande = br.readLine()).equals("bye")) {
                System.out.println(">> " + commande);
                sendClient.println("1 Vous avez exécute la commande : " + commande);
                commandExecutor.executeCommande(sendClient, commande);
            }

            System.out.println("Le client s'est déconnecté.");

            socket.close();
            commandExecutor.reset();

        } catch (NullPointerException e) {
            // Client déconnecté normalement
            System.err.println("Le client s'est déconnecté.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            closeSocket(commandExecutor);

        } catch (Exception e) {
            System.err.println("Le client s'est déconnecté.");
            System.err.println("Erreur : " + e.getMessage());
            System.err.println("Traceback : ");
            e.printStackTrace();
            closeSocket(commandExecutor);
        }
    }

    public void closeSocket(CommandExecutor commandExecutor) {
        try {
            socket.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        commandExecutor.reset();
    }
}