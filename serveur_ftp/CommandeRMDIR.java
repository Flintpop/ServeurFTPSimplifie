import java.io.File;
import java.io.PrintStream;
import java.util.Objects;

public class CommandeRMDIR extends Commande {

    public CommandeRMDIR(PrintStream ps, String commandeStr,CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        //TODO: Bug avec rmdir le dossier est vide mais il ne l'est pas (fraichement créé avec mkdir)

        // Check si l'argument est valide
        if (commandeArgs[0].equals("")) {
            ps.println("2 Erreur, pas de paramètre dans la commande rmdir");
            return;
        }

        String res = commandExecutor.findSubDirectory(commandeArgs[0]);
        if (res.equals("")) {
            ps.println("2 '" + commandeArgs[0] + "' n'existe pas dans le répertoire courant");
            return;
        }

        // Vérifier si le dossier n'est pas vide
        File directory = new File(CommandExecutor.addPath(commandExecutor.currentPath, res));
        if (directory.isDirectory()) {
            if (Objects.requireNonNull(directory.listFiles()).length > 0) {
                ps.println("2 Le dossier '" + directory.length() + "' n'est pas vide");
                return;
            }
        }
        else {
            ps.println("2 C'est un fichier et non un dossier. Impossible de le supprimer.");
        }

        // C'est bon
        boolean op = directory.delete();
        if (op) {
            ps.println("0 Fichier '" + directory + "' supprimé");
            return;
        }

        ps.println("2 Erreur : Fichier '" + directory + "' non supprimé");
    }

}
