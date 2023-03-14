import java.io.*;

public class CommandeMKDIR extends Commande{

    public CommandeMKDIR(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        if (this.incorrectParameters(1)) return;

        String directoryPath = CommandExecutor.addPath(commandExecutor.currentPath,commandeArgs[0]);
        File directory = new File(directoryPath);

        if (directory.exists()) {
            ps.println("2 L'utilisateur existe déjà");
            return;
        }

        if (!directory.mkdir()) {
            ps.println("2 Erreur lors de la création du dossier");
            return;
        }

        ps.println("0 Le dossier a été créé");

        // Création de pw.txt
        try {
            FileOutputStream passwordFile = new FileOutputStream(CommandExecutor.addPath(directoryPath, "pw.txt"));
            passwordFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
