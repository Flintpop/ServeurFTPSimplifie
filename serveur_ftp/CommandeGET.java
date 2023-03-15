import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CommandeGET extends Commande {

    public CommandeGET(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        if (this.incorrectParameters(1)) return;

        ps.println("0 Nouveau socket sur le port 4000 est créé pour la transmission des données");

        File f = new File(CommandExecutor.addPath(commandExecutor.getAbsolutePath(), commandeArgs[0]));
        if (!f.exists()) {
//                out.write("Le fichier n'existe pas".getBytes());
            ps.println("2 Le fichier n'existe pas");
            return;
        }

        ps.println("0 Le fichier existe");
        try (ServerSocket socketTransfer = new ServerSocket(4000)) {
            Socket socketCli = socketTransfer.accept();


            OutputStream out = socketCli.getOutputStream();
            InputStream fis = new FileInputStream(CommandExecutor.addPath(commandExecutor.getAbsolutePath(), commandeArgs[0]));
            byte[] buffer = new byte[1024];
            int nbOctetsLus;
            int somOctetsEnvoye = 0;
            while ((nbOctetsLus = fis.read(buffer)) > 0) {
                out.write(buffer, 0, nbOctetsLus);
                somOctetsEnvoye+=nbOctetsLus;
            }
            ps.println("1 " + somOctetsEnvoye + " octets envoyés");
            ps.println("0 Transfert terminé");
            out.close();
            fis.close();
            socketCli.close();
        } catch (Exception e) {
            System.out.println("Transfert échoué");
        }
    }

}
