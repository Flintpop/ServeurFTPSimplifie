import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandeLS extends Commande {
	
	public CommandeLS(PrintStream ps, String commandeStr) {
		super(ps, commandeStr);
	}


	public void execute() {
		try {
			Path path = Paths.get(CommandExecutor.currentPath);
			java.nio.file.Files.list(path).forEach((p) -> ps.println("1 " + p.getFileName()));
			ps.println("0 Fin de la liste");
		} catch (Exception e) {
			ps.println("1 Erreur lors de l'exécution de la commande LS : ");
			ps.println("1 " + e.getMessage());
			ps.println("2 terminé");
		}
	}

}
