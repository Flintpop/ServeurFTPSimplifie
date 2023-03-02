import java.io.PrintStream;

public class CommandeGET extends Commande {
	
	public CommandeGET(PrintStream ps, String commandeStr) {
		super(ps, commandeStr);
	}

	public void execute() {
		ps.println("La commande get n'est pas encoré implémentée");
	}

}
