import java.io.File;
import java.io.PrintStream;

public class CommandePWD extends Commande {
	
	public CommandePWD(PrintStream ps, String commandeStr) {
		super(ps, commandeStr);
	}

	public void execute() {
		File file = new File(".");
		String s = file.getAbsoluteFile().toString();
		ps.println("0 " + s);
	}

}
