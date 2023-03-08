import java.io.File;
import java.io.PrintStream;

public class CommandExecutor {

    public boolean userOk;
    public boolean pwOk;

    File file;
    String pathToAdd;

    String rootPath;
    String currentPath;
    String currentUser;

    public CommandExecutor(){
        this.userOk = false;
        this.pwOk = false;
        file = new File(".");
        pathToAdd = addPath("serveur_ftp", "root");
        rootPath = addPath(file.getAbsoluteFile().toString().
                replace(".", ""), pathToAdd).replace("\\\\", "\\");
        currentPath = rootPath;
        currentUser = "";
    }

    public void executeCommande(PrintStream ps, String commande) {
        if (userOk && pwOk) {
            // Changer de repertoire. Un (..) permet de revenir au repertoire superieur
            if (commande.split(" ")[0].equals("cd")) {
                (new CommandeCD(ps, commande, this)).execute();
            }

            // Télécharger un fichier
            if (commande.split(" ")[0].equals("get")) {
                (new CommandeGET(ps, commande, this)).execute();
            }

            // Afficher la liste des fichiers et des dossiers du repertoire courant
            if (commande.split(" ")[0].equals("ls")) {
                (new CommandeLS(ps, commande, this)).execute();
            }

            // Afficher le repertoire courant
            if (commande.split(" ")[0].equals("pwd")) {
                (new CommandePWD(ps, commande, this)).execute();
            }

            // Envoyer (uploader) un fichier
            if (commande.split(" ")[0].equals("stor")) {
                (new CommandeSTOR(ps, commande, this)).execute();
            }

            // Créer un dossier
            if (commande.split(" ")[0].equals("mkdir")) {
                (new CommandeMKDIR(ps, commande, this)).execute();
            }

            // Supprimer un dossier vide
            if (commande.split(" ")[0].equals("rmdir")) {
                (new CommandeRMDIR(ps, commande, this)).execute();
            }
        } else {
            if (commande.split(" ")[0].equals("pass") || commande.split(" ")[0].equals("user")) {
                // Le mot de passe pour l'authentification
                if (commande.split(" ")[0].equals("pass")) {
                    (new CommandePASS(ps, commande, this)).execute();
                }

                // Le login pour l'authentification
                if (commande.split(" ")[0].equals("user")) {
                    (new CommandeUSER(ps, commande, this)).execute();
                }
            } else
                ps.println("2 Vous n'êtes pas connecté !");
        }
    }

    public static String getFolderSeperator() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.contains("win")) ? "\\" : "/";
    }

    public static String addPath(String path, String add) {

        String folderSeparator = getFolderSeperator();

        // Avoir path et add bien formatté afin qu'ils puissent être ajouté entre eux
        boolean formatted = false;
        while (!formatted) {
            if (path.endsWith(folderSeparator)) path = path.substring(0, path.length() - 1);
            if (add.startsWith(folderSeparator)) add = add.substring(1);
            if (add.endsWith(folderSeparator)) add = add.substring(0, add.length() - 1);

            if (!path.endsWith(folderSeparator) || !add.startsWith(folderSeparator)) {
                formatted = true;
            }
        }

        return path + folderSeparator + add;
    }

    public void goBackOneDirectory() {
        String folderSeperator = getFolderSeperator();

        currentPath = currentPath.substring(0, currentPath.lastIndexOf(folderSeperator));
    }

    public void reset() {
        userOk = false;
        pwOk = false;
        currentPath = rootPath;
        currentUser = "";
    }

    public String findSubDirectory(String dir) {
        // On veut aller dans un subdirectory
        File file = new File(currentPath);
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        if (directories != null) {
            for (String directory : directories) {
                if (dir.equals(directory)) {
                    return directory;
                }
            }
        }
        return "";
    }
}
