import java.io.*;

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
		CommandExecutor.currentPath = CommandExecutor.addPath(CommandExecutor.rootPath, CommandExecutor.currentUser);
		String path = CommandExecutor.addPath(CommandExecutor.currentPath, "pw.txt");
		File file = new File(path);
		String pass = file.getAbsolutePath();
		try {
			try (BufferedReader fis = new BufferedReader(new FileReader(pass))) {
				String str = fis.readLine();
				if (str != null) {
					if (!commandeArgs[0].equals(str)) {
						ps.println("2 Le mot de passe est faux");
						CommandExecutor.currentUser = "";
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

		CommandExecutor.pwOk = true;
		ps.println("1 Commande pass OK");
		ps.println("0 " + CommandExecutor.currentUser + " est connecté sur notre serveur");
		CommandExecutor.addPath(CommandExecutor.currentPath, CommandExecutor.currentUser);
	}

}
