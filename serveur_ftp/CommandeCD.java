import java.io.PrintStream;

public class CommandeCD extends Commande {
    public CommandeCD(PrintStream ps, String commandeStr) {
        super(ps, commandeStr);
    }

    public void execute() {
        try {
            if (commandeArgs[0].equals("..")) {
                String path = CommandExecutor.addPath(CommandExecutor.rootPath, CommandExecutor.currentUser);
                if (CommandExecutor.currentPath.equals(path)) {
                    ps.println("2 Vous êtes déjà à la racine de votre espace");
                    return;
                }

                goBackOneDirectory();

                ps.println("0 Nouveau chemin : " + CommandExecutor.currentPath);
                return;
            }

            String dir = findSubDirectory(commandeArgs[0]);
            if (!dir.equals("")) {
                CommandExecutor.currentPath = CommandExecutor.addPath(CommandExecutor.currentPath, dir);
                ps.println("0 Nouveau chemin : " + CommandExecutor.currentPath);
                return;
            }

            ps.println("2 Erreur, le dossier n'existe pas");
        } catch (Exception e) {
            ps.println("1 Erreur lors de l'exécution de la commande CD : ");
            ps.println("1 " + e.getMessage());
            ps.println("2 terminé");
        }
    }

    public void goBackOneDirectory() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            CommandExecutor.currentPath = CommandExecutor.currentPath.substring(0, CommandExecutor.currentPath.lastIndexOf("\\"));
            CommandExecutor.currentPath = CommandExecutor.currentPath.substring(0, CommandExecutor.currentPath.lastIndexOf("\\"));
            CommandExecutor.currentPath = CommandExecutor.currentPath + "\\";
        }
        else {
            CommandExecutor.currentPath = CommandExecutor.currentPath.substring(0, CommandExecutor.currentPath.lastIndexOf("/"));
            CommandExecutor.currentPath = CommandExecutor.currentPath.substring(0, CommandExecutor.currentPath.lastIndexOf("/"));
            CommandExecutor.currentPath = CommandExecutor.currentPath + "/";
        }
    }
}
