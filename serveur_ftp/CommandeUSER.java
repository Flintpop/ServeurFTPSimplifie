import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CommandeUSER extends Commande {

    public CommandeUSER(PrintStream ps, String commandeStr) {
        super(ps, commandeStr);
    }

    public void execute() {
        //on récupère le nom du dossier root
        File directory = new File(CommandExecutor.rootPath);
        if(directory.exists() && directory.isDirectory()){
            File[] files = directory.listFiles();
            List<String> list = new ArrayList<>();
            //on récupère les noms des répertoires dans une liste
            for (File f : files) {
                if (f.isDirectory()) {
                    list.add(f.getName());
                }
            }
            //on compare la liste des répertoire avec le nom du user
            for (String s : list) {
                if(s.equals(commandeArgs[0])){
                    CommandExecutor.userOk = true;
                    CommandExecutor.currentUser = commandeArgs[0];
                    ps.println("0 Commande user OK");
                    return;
                }
            }
            ps.println("2 Le user " + commandeArgs[0] + " n'existe pas");
        } else {
            ps.println("2 Le dossier root n'existe pas" + directory.getAbsolutePath());
        }
    }

}
