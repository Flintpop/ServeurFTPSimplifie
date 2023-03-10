import java.io.PrintStream;

public class CommandeCD extends Commande {
    public CommandeCD(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        try {
            if (this.incorrectParameters(1)) return;

            if (commandeArgs[0].equals("..")) {
                String path = CommandExecutor.addPath(commandExecutor.rootPath, commandExecutor.currentUser);
                if (commandExecutor.currentPath.equals(path)) {
                    ps.println("2 Vous êtes déjà à la racine de votre espace");
                    return;
                }

                commandExecutor.goBackOneDirectory();

                ps.println("0 Nouveau chemin : " + commandExecutor.currentPath);
                return;
            }

            String dir = commandExecutor.findSubDirectory(commandeArgs[0]);
            if (!dir.equals("")) {
                commandExecutor.currentPath = CommandExecutor.addPath(commandExecutor.currentPath, dir);
                ps.println("0 Nouveau chemin : " + commandExecutor.currentPath);
                return;
            }

            ps.println("2 Erreur, le dossier n'existe pas");
        } catch (Exception e) {
            ps.println("1 Erreur lors de l'exécution de la commande CD : ");
            ps.println("1 " + e.getMessage());
            ps.println("2 terminé");
        }
    }

}
