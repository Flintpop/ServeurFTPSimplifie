/*
 * TP JAVA RIP
 * Min Serveur FTP
 * */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("Le Serveur FTP");

		ServerSocket serveurFTP = new ServerSocket(2001);
		Socket socket = serveurFTP.accept();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream sendClient = new PrintStream(socket.getOutputStream());
		
		sendClient.println("1 Bienvenue ! ");
		sendClient.println("1 Serveur FTP Personnel.");
		sendClient.println("0 Authentification : ");
		
		String commande;

		// Attente de reception de commandes et leur execution
		while(!(commande=br.readLine()).equals("bye")) {
			System.out.println(">> "+commande);
			sendClient.println("1 Vous avez exécute la commande : " + commande);
			CommandExecutor.executeCommande(sendClient, commande);
		}
		
		serveurFTP.close();
		socket.close();
	}

	// Function to get all login and password from a file
	public static String [][] getAllLogin() {
		// [login][password]
		// Get them via file saving and loading.
		// Encrypt them if asked
//		String filename = "login.txt";

//		try {
//			FileInputStream fis = new FileInputStream(filename);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}

		return new String[1][1];
	}

	public static void printClientCommandLineDecoration(PrintStream sendClient) {
		if (CommandExecutor.userOk && CommandExecutor.pwOk) {
			sendClient.println("-1");
			sendClient.println(CommandExecutor.currentUser + " >> ");
		}
	}
}
