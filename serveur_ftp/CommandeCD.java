import java.io.PrintStream;

public class CommandeCD extends Commande {
    public CommandeCD(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        try {
            if (incorrectParameters(1)) return;

            if (commandeArgs[0].equals("..")) {
                goBackOneDirectory();
                return;
            }

            goToDirectory(commandeArgs[0]);
        } catch (Exception e) {
            ps.println("1 Erreur lors de l'exécution de la commande CD : ");
            ps.println("1 " + e.getMessage());
            ps.println("2 terminé");
        }
    }

    private void goBackOneDirectory() {
        String path = CommandExecutor.getFolderSeparator() + commandExecutor.currentUser;
        if (commandExecutor.currentPath.equals(path)) {
            ps.println("2 Vous êtes déjà à la racine de votre espace");
            return;
        }

        commandExecutor.goBackOneDirectory();
        ps.println("0 Nouveau chemin : " + commandExecutor.currentPath);
    }

    private void goToDirectory(String directory) {
        String dir = commandExecutor.findSubDirectory(directory);
        if (!dir.equals("")) {
            commandExecutor.currentPath = CommandExecutor.addPath(commandExecutor.currentPath, dir);
            ps.println("0 Nouveau chemin : " + commandExecutor.currentPath);
            return;
        }

        ps.println("2 Erreur, le dossier n'existe pas");
    }
}
