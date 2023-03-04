import java.io.File;
import java.io.FileInputStream;
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
		File file = new File(CommandExecutor.rootPath + "/" + CommandExecutor.currentUser + "/" + "pw.txt");
		String pass = file.getAbsolutePath();
		try {
			FileInputStream fis = new FileInputStream(pass);
			byte[] buffer = new byte[4096];

			if(fis.read(buffer)!=-1) {
				String str = new String(buffer);
				if (!commandeArgs[0].equals(str)) {
					ps.println("2 Le mot de passe est faux");
					CommandExecutor.currentUser = "";
					return;
				}
			}else{
				ps.println("2 aucun mot de passe n'a été trouvé");
				return;
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
