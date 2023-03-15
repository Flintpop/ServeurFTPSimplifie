import java.io.*;

public class CommandeMKDIR extends Commande{

    public CommandeMKDIR(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        if (this.incorrectParameters(1)) return;

        if (commandeArgs[0].contains(".") || commandeArgs[0].contains(":")) {
            ps.println("2 Le programme ne supporte pas l'inscription du caractère '.' dans le nom");
            return;
        }

        String directoryPath = CommandExecutor.addPath(commandExecutor.getAbsolutePath(),commandeArgs[0]);
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
    }
}