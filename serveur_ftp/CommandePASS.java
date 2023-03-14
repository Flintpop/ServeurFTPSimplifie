import java.io.*;

public class CommandePASS extends Commande {
	
	public CommandePASS(PrintStream ps, String commandeStr,CommandExecutor commandExecutor) {
		super(ps, commandeStr,commandExecutor);
	}

	public void execute() {
		if (this.incorrectParameters(1)) return;

		if (!commandExecutor.userOk) {
			ps.println("2 Il faut d'abord renseigner l'user");
			commandExecutor.currentUser = "";
			return;
		}
		commandExecutor.currentPath = CommandExecutor.addPath(commandExecutor.rootPath, commandExecutor.currentUser);
		String path = CommandExecutor.addPath(commandExecutor.currentPath, "pw.txt");
		File file = new File(path);
		String pass = file.getAbsolutePath();
		try {
			try (BufferedReader fis = new BufferedReader(new FileReader(pass))) {
				String str = fis.readLine();
				if (str != null) {
					if (!commandeArgs[0].equals(str)) {
						ps.println("2 Le mot de passe est faux");
						commandExecutor.currentUser = "";
						return;
					}
				} else {
					ps.println("2 aucun mot de passe n'a été trouvé");
					return;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		commandExecutor.pwOk = true;
		ps.println("1 Commande pass OK");
		ps.println("0 " + commandExecutor.currentUser + " est connecté sur notre serveur");
		CommandExecutor.addPath(commandExecutor.currentPath, commandExecutor.currentUser);
		commandExecutor.currentServerPath = commandExecutor.currentUser;
	}

}
