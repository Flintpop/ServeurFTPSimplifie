import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

public class CommandePWD extends Commande {
	
	public CommandePWD(PrintStream ps, String commandeStr) {
		super(ps, commandeStr);
	}

	public void execute() {

		/*String s;
		try{
			BufferedReader br = new BufferedReader(new FileReader("pwd.txt"));
			s = br.readLine();
			ps.println("0 " + s);
		}catch (Exception e){
			e.printStackTrace();
		}*/

		ps.println("0 " + CommandExecutor.currentPath);
	}

}
