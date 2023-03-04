import java.io.File;
import java.io.PrintStream;

public class CommandeMKDIR extends Commande{

    public CommandeMKDIR(PrintStream ps, String commandeStr) {
        super(ps, commandeStr);
    }

    public void execute() {
        File directory = new File(CommandExecutor.currentPath + "/" + commandeArgs[0]);
        if (!directory.exists()) {
            if(directory.mkdir()){
                ps.println("0 Le dossier a été créé");
            } else {
                ps.println("2 Erreur lors de la création du dossier");
            }
        } else {
            ps.println("2 Le dossier existe déjà");
        }
    }
}
