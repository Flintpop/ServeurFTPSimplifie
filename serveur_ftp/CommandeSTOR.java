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
        String filepath = commandExecutor.getAbsolutePath();
        filepath = CommandExecutor.addPath(filepath, commandeArgs[0]);

        try (ServerSocket dataSocket = new ServerSocket(4000)) {// créer une socket serveur sur le port 4000
            Socket socket = dataSocket.accept(); // attendre la connexion d'un client

            InputStream in = socket.getInputStream();// récupérer le flux d'entrée du socket
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            System.out.println("Attend le readline");
//            String res = br.readLine();
//            System.out.println("A lu le readline " + res);

//            if (res.equals("fail")) {
//                System.out.println("Erreur lors de la création du fichier");
//                return;
//            }

            OutputStream file = new FileOutputStream(filepath);// récupérer le flux de sortie du fichier

            byte[] buffer = new byte[4096];
            int count;

            // On copie le fichier
            int somOctetsEnvoye = 0;
            while ((count = in.read(buffer)) > 0) {
                String s = new String(buffer, 0, count);// convertir le tableau de byte en String
                System.out.println(s);
                somOctetsEnvoye += count;
                file.write(buffer, 0, count);// écrire dans le fichier
            }

            ps.println("1 " + somOctetsEnvoye + " octets envoyés");
            ps.println("0 Transfert terminé");

            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}