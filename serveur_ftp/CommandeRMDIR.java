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

        findSubDirectory(commandeArgs[0]);
    }

}
