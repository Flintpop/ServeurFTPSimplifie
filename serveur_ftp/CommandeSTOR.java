import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CommandeSTOR extends Commande {

    public CommandeSTOR(PrintStream ps, String commandeStr) {
        super(ps, commandeStr);
    }

    public void execute() {
        ps.println("0 Nouveau socket sur le port 4000 est créé pour la transmission des données");
        String filepath = CommandExecutor.addPath(CommandExecutor.currentPath, commandeArgs[0]);

        try (ServerSocket dataSocket = new ServerSocket(4000)) {
            Socket socket = dataSocket.accept();

            InputStream in = socket.getInputStream();
            OutputStream file = new FileOutputStream(filepath);

            byte[] buffer = new byte[4096];
            int count;

            // On copie le fichier
            while ((count = in.read(buffer)) > 0) {
                String s = new String(buffer, 0, count);
                System.out.println(s);
                file.write(buffer, 0, count);
            }
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

}
