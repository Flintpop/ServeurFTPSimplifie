import java.io.File;
import java.io.PrintStream;

public class CommandeADDUSER extends Commande {
    public CommandeADDUSER(PrintStream ps, String commande, CommandExecutor commandExecutor) {
        super(ps, commande, commandExecutor);
    }

    @Override
    public void execute() {
        // adduser nom motdepasse
        if (incorrectParameters(2)) return;


        // Il faut vérifier que l'user n'existe pas déjà.
        // Il faut créer un dossier.
        // Ajouter un mot de passe.

        if (!commandExecutor.findSubDirectory(commandeArgs[0]).equals("")) {
            ps.println("2 Erreur, l'user existe déjà");
            return;
        }

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
