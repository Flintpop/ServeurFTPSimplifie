import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.sql.ClientInfoStatus;

public class CommandeGET extends Commande {

    public CommandeGET(PrintStream ps, String commandeStr) {
        super(ps, commandeStr);
    }

    public void execute() {
        ps.println("1 Nouveau socket sur le port 4000 est créé pour la transmission des données");

        try (ServerSocket socketTransfer = new ServerSocket(4000)) {
            Socket socketCli = socketTransfer.accept();

            OutputStream out = socketCli.getOutputStream();
            InputStream fis = new FileInputStream(CommandExecutor.addPath(CommandExecutor.currentPath, commandeArgs[0]));
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
