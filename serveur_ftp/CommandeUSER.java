import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandeUSER extends Commande {

    public CommandeUSER(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        super(ps, commandeStr, commandExecutor);
    }

    public void execute() {
        if (this.incorrectParameters(1)) return;

        //on récupère le nom du dossier root
        File directory = new File(commandExecutor.rootPath);
        if(directory.exists() && directory.isDirectory()){
            File[] files = directory.listFiles();
            List<String> list = new ArrayList<>();
            //on récupère les noms des répertoires dans une liste
            for (File f : Objects.requireNonNull(files)) {
                if (f.isDirectory()) {
                    list.add(f.getName());
                }
            }
            //on compare la liste des répertoire avec le nom du user
            for (String s : list) {
                if(s.equals(commandeArgs[0])){
                    commandExecutor.userOk = true;
                    commandExecutor.currentUser = commandeArgs[0];
                    ps.println("0 Commande user OK");
                    return;
                }
            }
            ps.println("2 Le user " + commandeArgs[0] + " n'existe pas");
        } else {
            ps.println("2 Le dossier root n'existe pas " + directory.getAbsolutePath());
        }
    }

}
