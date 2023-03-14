import java.io.*;

public class CommandeADDUSER extends Commande {
    public CommandeADDUSER(PrintStream ps, String commande, CommandExecutor commandExecutor) {
        super(ps, commande, commandExecutor);
    }

    @Override
    public void execute() {
        // adduser nom motdepasse
        if (incorrectParameters(2)) return;
        String userName = commandeArgs[0];

        if (!commandExecutor.findSubDirectory(userName, commandExecutor.rootPath).equals("")) {
            ps.println("2 Erreur, l'user existe déjà");
            return;
        }

        File directory = new File(CommandExecutor.addPath(commandExecutor.rootPath, userName));

        if (directory.exists()) {
            ps.println("2 Le dossier existe déjà");
            return;
        }

        if (!directory.mkdir()) {
            ps.println("2 Erreur lors de la création du dossier");
            return;
        }

        ps.println("1 Le dossier a été créé");

        String filePath = CommandExecutor.addPath(commandExecutor.rootPath, userName);
        filePath = CommandExecutor.addPath(filePath, "pw.txt");

        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(filePath));

            fos.write(commandeArgs[1]);

            fos.close();
            ps.println("0 Le mot de passe a été enregistré");
        } catch (IOException e) {
            ps.println("2 Erreur lors de la création du fichier de mot de passe");
            System.err.println("Erreur lors de la création du fichier de mot de passe");
            throw new RuntimeException(e);
        }
    }
}
