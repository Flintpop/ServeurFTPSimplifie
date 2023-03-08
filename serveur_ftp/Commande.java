import java.io.File;
import java.io.PrintStream;

public abstract class Commande {

    protected PrintStream ps;
    protected String commandeNom;
    protected String[] commandeArgs;
    protected CommandExecutor commandExecutor;

    public Commande(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
        this.ps = ps;
        this.commandExecutor = commandExecutor;
        String[] args = commandeStr.split(" ");
        commandeNom = args[0];
        commandeArgs = new String[args.length - 1];

        System.arraycopy(args, 1, commandeArgs, 0, commandeArgs.length);
    }

    public abstract void execute();

}
