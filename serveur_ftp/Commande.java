import java.io.File;
import java.io.PrintStream;

public abstract class Commande {

    protected PrintStream ps;
    protected String commandeNom;
    protected String[] commandeArgs;

    public Commande(PrintStream ps, String commandeStr) {
        this.ps = ps;
        String[] args = commandeStr.split(" ");
        commandeNom = args[0];
        commandeArgs = new String[args.length - 1];

        System.arraycopy(args, 1, commandeArgs, 0, commandeArgs.length);
    }

    public abstract void execute();

    public String findSubDirectory(String dir) {
        // On veut aller dans un subdirectory
        File file = new File(CommandExecutor.currentPath);
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        if (directories != null) {
            for (String directory : directories) {
                if (dir.equals(directory)) {
                    return directory;
                }
            }
        }
        return "";
    }

}
