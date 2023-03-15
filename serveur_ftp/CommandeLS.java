import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.file.Files.list;

public class CommandeLS extends Commande {
	
	public CommandeLS(PrintStream ps, String commandeStr, CommandExecutor commandExecutor) {
		super(ps, commandeStr, commandExecutor);
	}


	public void execute() {
		try {
			Path path = Paths.get(commandExecutor.getAbsolutePath());
			list(path).forEach((p) -> ps.println("1 " + p.getFileName()));
			ps.println("0 Fin de la liste");
		} catch (Exception e) {
			ps.println("1 Erreur lors de l'exécution de la commande LS : ");
			ps.println("1 " + e.getMessage());
			ps.println("2 terminé");
		}
	}

}
