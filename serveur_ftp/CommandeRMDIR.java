import java.io.File;
import java.io.PrintStream;
import java.util.Objects;

public class CommandeRMDIR extends Commande {

    public CommandeRMDIR(PrintStream ps, String commandeStr,CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        if (this.incorrectParameters(1)) return;

        // Check si l'argument est valide
        if (commandeArgs[0].equals("")) {
            ps.println("2 Erreur, pas de paramètre dans la commande rmdir");
            return;
        }

        String directoryFoundString = commandExecutor.findSubDirectory(commandeArgs[0]);
        if (directoryFoundString.equals("")) {
            ps.println("2 '" + commandeArgs[0] + "' n'existe pas dans le répertoire courant");
            return;
        }

        // Vérifier si le dossier n'est pas vide
        File directory = new File(CommandExecutor.addPath(commandExecutor.getAbsolutePath(), directoryFoundString));

        if (!directory.isDirectory()) {
            ps.println("2 C'est un fichier et non un dossier. Impossible de le supprimer.");
            return;
        }

        if (Objects.requireNonNull(directory.listFiles()).length > 0) {
            ps.println("2 Le dossier '" + directory.length() + "' n'est pas vide");
            return;
        }

        // C'est bon
        boolean op = directory.delete();
        String directoryNamePath = directory.getName();
        directoryNamePath = CommandExecutor.addPath(commandExecutor.currentPath, directoryNamePath);

        if (op) {
            ps.println("0 Dossier '" + directoryNamePath + "' supprimé");
            return;
        }

        ps.println("2 Erreur : Dossier '" + directoryNamePath + "' non supprimé");
    }

}
