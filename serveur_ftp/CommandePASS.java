import java.io.PrintStream;

public class CommandePASS extends Commande {
	
	public CommandePASS(PrintStream ps, String commandeStr) {
		super(ps, commandeStr);
	}

	public void execute() {
		// Le mot de passe est : abcd
		if (!CommandExecutor.userOk) {
			ps.println("2 Il faut d'abord renseigner l'user");
			CommandExecutor.currentUser = "";
			return;
		}

		if (!commandeArgs[0].equalsIgnoreCase("abcd")) {
			ps.println("2 Le mot de passe est faux");
			CommandExecutor.currentUser = "";
			return;
		}

		CommandExecutor.pwOk = true;
		ps.println("1 Commande pass OK");
		ps.println("0 " + CommandExecutor.currentUser + " est connecté sur notre serveur");
		//TODO: bug peut pas faire ls car il manque le \\ à la fin ? Voir
		/*
		Message d'erreur :
			System.err.print("1 Erreur lors de l'exécution de la commande LS : ");
			System.err.println("1 " + e.getMessage());
			System.err.println("2 terminé");
		 */
		CommandExecutor.currentPath = CommandExecutor.currentPath + CommandExecutor.currentUser + "\\";
	}

}
