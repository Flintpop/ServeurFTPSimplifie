import java.io.*;
import java.sql.ClientInfoStatus;

public class CommandeGET extends Commande {
	
	public CommandeGET(PrintStream ps, String commandeStr) {
		super(ps, commandeStr);
	}

	public void execute() {
		try{
			File file = new File(commandeArgs[0]);
			String src = file.getAbsolutePath();
			String dest = src + "/../" + "Client/";
			FileInputStream fis = new FileInputStream(src);
			FileOutputStream fos = new FileOutputStream(dest);
			byte[] buffer = new byte[4096];
			int nbLecture;
			while((nbLecture = fis.read(buffer)) != -1){
				fos.write(buffer, 0, nbLecture);
			}

			fis.close();
			fos.close();
			ps.println("Transfert terminé");
		}catch(Exception e){
			System.out.println("Transfert échoué");
		}
	}

}
