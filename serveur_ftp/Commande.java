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

}
