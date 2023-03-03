import java.io.PrintStream;

public class CommandeUSER extends Commande {

    public CommandeUSER(PrintStream ps, String commandeStr) {
        super(ps, commandeStr);
    }

    public void execute() {
        // Ce serveur accepte uniquement l'user personne
        if (commandeArgs[0].equalsIgnoreCase("personne")) {
            CommandExecutor.userOk = true;
            CommandExecutor.currentUser = commandeArgs[0];
            ps.println("0 Commande user OK");
        } else {
            ps.println("2 Le user " + commandeArgs[0] + " n'existe pas");
        }

    }

}
