import java.io.File;
import java.io.PrintStream;

public class CommandExecutor {

    public static boolean userOk = false;
    public static boolean pwOk = false;

    static File file = new File(".");
    static String pathToAdd = addPath("serveur_ftp\\", "root");
    static String rootPath = addPath(file.getAbsoluteFile().toString().replace(".", ""), pathToAdd).replace("\\\\", "\\");
    static String currentPath = rootPath;

    static String currentUser;

    public static void executeCommande(PrintStream ps, String commande) {
        if (userOk && pwOk) {
            // Changer de repertoire. Un (..) permet de revenir au repertoire superieur
            if (commande.split(" ")[0].equals("cd")) {
                (new CommandeCD(ps, commande)).execute();
            }

            // Télécharger un fichier
            if (commande.split(" ")[0].equals("get")) {
                (new CommandeGET(ps, commande)).execute();
            }

            // Afficher la liste des fichiers et des dossiers du repertoire courant
            if (commande.split(" ")[0].equals("ls")) {
                (new CommandeLS(ps, commande)).execute();
            }

            // Afficher le repertoire courant
            if (commande.split(" ")[0].equals("pwd")) {
                (new CommandePWD(ps, commande)).execute();
            }

            // Envoyer (uploader) un fichier
            if (commande.split(" ")[0].equals("stor")) {
                (new CommandeSTOR(ps, commande)).execute();
            }

            // Créer un dossier
            if (commande.split(" ")[0].equals("mkdir")) {
                (new CommandeMKDIR(ps, commande)).execute();
            }

            // Supprimer un dossier vide
            if (commande.split(" ")[0].equals("rmdir")) {
                (new CommandeRMDIR(ps, commande)).execute();
            }
        } else {
            if (commande.split(" ")[0].equals("pass") || commande.split(" ")[0].equals("user")) {
                // Le mot de passe pour l'authentification
                if (commande.split(" ")[0].equals("pass")) {
                    (new CommandePASS(ps, commande)).execute();
                }

                // Le login pour l'authentification
                if (commande.split(" ")[0].equals("user")) {
                    (new CommandeUSER(ps, commande)).execute();
                }
            } else
                ps.println("2 Vous n'êtes pas connecté !");
        }
    }

    public static String addPath(String path, String add) {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("win")) {
            return path + add + "\\";
        } else {
            return path + add + "/";
        }
    }
}
