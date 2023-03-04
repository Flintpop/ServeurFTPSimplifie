import java.io.PrintStream;

public class CommandePASS extends Commande {
	
	public CommandePASS(PrintStream ps, String commandeStr) {
		super(ps, commandeStr);
	}

	public void execute() {
		if (!CommandExecutor.userOk) {
			ps.println("2 Il faut d'abord renseigner l'user");
			CommandExecutor.currentUser = "";
			return;
		}

		if (!commandeArgs[0].equalsIgnoreCase("bretons")) {
			ps.println("2 Le mot de passe est faux");
			CommandExecutor.currentUser = "";
			return;
		}

		CommandExecutor.pwOk = true;
		ps.println("1 Commande pass OK");
		ps.println("0 " + CommandExecutor.currentUser + " est connecté sur notre serveur");
		CommandExecutor.currentPath = CommandExecutor.currentPath + CommandExecutor.currentUser + "\\";
	}

}
