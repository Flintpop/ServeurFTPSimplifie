import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

public class CommandePWD extends Commande {
	
	public CommandePWD(PrintStream ps, String commandeStr,CommandExecutor commandExecutor) {
		super(ps, commandeStr, commandExecutor);
	}

	public void execute() {
		ps.println("0 " + getPWD());
	}

	public String getPWD() {
		return commandExecutor.currentPath;
	}

}
