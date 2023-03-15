import java.io.File;
import java.io.PrintStream;
import java.util.Objects;
import java.util.concurrent.Executor;

public class CommandeRM extends Commande {
    public CommandeRM(PrintStream ps, String commande, CommandExecutor commandExecutor) {
        super(ps, commande, commandExecutor);
    }

    @Override
    public void execute() {
        if (this.incorrectParameters(1)) return;


        // Check si l'argument est valide
        if (commandeArgs[0].equals("")) {
            ps.println("2 Erreur, pas de paramètre dans la commande rm");
            return;
        }

        if (commandeArgs[0].equals("pw.txt")) {
            ps.println("2 Error, cannot delete pw.txt");
            return;
        }

        String fileString = commandExecutor.findFile(commandeArgs[0]);
        if (fileString.equals("")) {
            ps.println("2 '" + commandeArgs[0] + "' n'existe pas dans le répertoire courant");
            return;
        }

        // Vérifier si le dossier n'est pas vide
        File file = new File(CommandExecutor.addPath(commandExecutor.getAbsolutePath(), fileString));

        if (!file.isFile()) {
            ps.println("2 C'est un dossier et non un fichier. Impossible de le supprimer.");
            return;
        }

        // C'est bon
        boolean op = file.delete();
        String fileNamePath = file.getName();
        fileNamePath = CommandExecutor.addPath(commandExecutor.currentPath, fileNamePath);

        if (op) {
            ps.println("0 File '" + fileNamePath + "' deleted");
            return;
        }

        ps.println("2 Erreur : File '" + fileNamePath + "' deleted");
    }
}
