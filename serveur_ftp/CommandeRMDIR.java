import java.io.File;
import java.io.PrintStream;

public class CommandeRMDIR extends Commande {

    public CommandeRMDIR(PrintStream ps, String commandeStr) {
        super(ps, commandeStr);
    }

    public void execute() {
        // Check si l'argument est valide
        if (commandeArgs[0].equals("")) {
            ps.println("2 Erreur, pas de paramètre dans la commande rmdir");
            return;
        }

        String res = findSubDirectory(commandeArgs[0]);
        if (res.equals("")) {
            ps.println("2 '" + commandeArgs[0] + "' n'existe pas dans le répertoire courant");
            return;
        }

        // Vérifier si le dossier n'est pas vide
        File directory = new File(CommandExecutor.addPath(CommandExecutor.currentPath, res));
        if (directory.isDirectory()) {
            if (directory.length() > 0) {
                ps.println("2 Le dossier '" + res + "' n'est pas vide");
                return;
            }
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
