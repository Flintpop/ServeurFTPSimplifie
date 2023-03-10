import java.io.File;
import java.io.PrintStream;

public class CommandeMKDIR extends Commande{

    public CommandeMKDIR(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        if (this.incorrectParameters(1)) return;

        File directory = new File(CommandExecutor.addPath(commandExecutor.currentPath,commandeArgs[0]));

        if (directory.exists()) {
            ps.println("2 Le dossier existe déjà");
            return;
        }

        if (!directory.mkdir()) {
            ps.println("2 Erreur lors de la création du dossier");
            return;
        }

        ps.println("0 Le dossier a été créé");
    }
}
