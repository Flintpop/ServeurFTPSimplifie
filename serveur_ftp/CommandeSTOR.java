import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CommandeSTOR extends Commande {

    public CommandeSTOR(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        if (this.incorrectParameters(1)) return;

        ps.println("0 Nouveau socket sur le port 4000 est créé pour la transmission des données");
        String filepath = CommandExecutor.addPath(commandExecutor.currentPath, commandeArgs[0]);

        try (ServerSocket dataSocket = new ServerSocket(4000)) {// créer une socket serveur sur le port 4000
            Socket socket = dataSocket.accept(); // attendre la connexion d'un client

            InputStream in = socket.getInputStream();// récupérer le flux d'entrée du socket
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String res = br.readLine();
            if (res.equals("fail")) {
                System.out.println("Erreur lors de la création du fichier");
                return;
            }

            OutputStream file = new FileOutputStream(filepath);// récupérer le flux de sortie du fichier

            byte[] buffer = new byte[4096];
            int count;
            // absolutepath = new File(filepath).getAbsolutePath();
            // On copie le fichier
            while ((count = in.read(buffer)) > 0) {
                String s = new String(buffer, 0, count);// convertir le tableau de byte en String
                System.out.println(s);
                file.write(buffer, 0, count);// écrire dans le fichier
            }
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}