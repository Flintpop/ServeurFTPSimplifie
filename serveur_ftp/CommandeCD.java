import java.io.File;
import java.io.PrintStream;

public class CommandeCD extends Commande {
    public CommandeCD(PrintStream ps, String commandeStr) {
        super(ps, commandeStr);
    }

    public void execute() {
        try {
            if (commandeArgs[0].equals("..")) {
                if (CommandExecutor.currentPath.equals(CommandExecutor.rootPath + "\\" + CommandExecutor.currentUser)) {
                    ps.println("2 Vous êtes déjà à la racine de votre espace");
                    return;
                }
                CommandExecutor.currentPath = CommandExecutor.currentPath.substring(0, CommandExecutor.currentPath.lastIndexOf("\\"));
                return;
            }

            String dir = findSubDirectory();
            if (!dir.equals("")) {
                CommandExecutor.currentPath = CommandExecutor.currentPath + dir + "\\";
                ps.println("0 Nouveau chemin : " + CommandExecutor.currentPath);
                return;
            }

            ps.println("2 Erreur, le dossier n'existe pas");
        } catch (Exception e) {
            System.err.println("1 Erreur lors de l'exécution de la commande CD : ");
            System.err.println("1 " + e.getMessage());
            System.err.println("2 terminé");
        }
    }

    public String findSubDirectory() {
        // On veut aller dans un subdirectory
        File file = new File(CommandExecutor.currentPath);
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        if (directories != null) {
            for (String directory : directories) {
                if (commandeArgs[0].equals(directory)) {
                    return directory;
                }
            }
        }
        return "";
    }
}
