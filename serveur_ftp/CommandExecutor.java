import java.io.File;
import java.io.PrintStream;

public class CommandExecutor {
    // Q: What does this class does ?
    // A: This class is the main class of the server. It is used to execute the commands sent by the client.

    public boolean userOk;
    public boolean pwOk;

    File file;
    String pathToAdd;
    String rootPath;
    String currentPath;
    String currentServerPath;
    String currentUser;

    public CommandExecutor(){
        this.userOk = false;
        this.pwOk = false;
        file = new File(".");
        pathToAdd = addPath("serveur_ftp", "root");
        rootPath = addPath(file.getAbsoluteFile().toString().
                replace(".", ""), pathToAdd).replace("\\\\", "\\");
        currentPath = "";
        currentUser = "";
    }

    public void executeCommande(PrintStream ps, String commande) {
        if (!userOk || !pwOk) {
            // User pas connecté
            switch (commande.split(" ")[0]) {
                case "pass" -> (new CommandePASS(ps, commande, this)).execute();

                case "user" -> (new CommandeUSER(ps, commande, this)).execute();

                default -> ps.println("2 Vous n'êtes pas connecté !");
            }
            return;
        }

        // User connecté
        switch (commande.split(" ")[0]) {
            // Changer de repertoire. Un (..) permet de revenir au repertoire supérieur
            case "cd" -> (new CommandeCD(ps, commande, this)).execute();

            // Télécharger un fichier
            case "get" -> (new CommandeGET(ps, commande, this)).execute();

            // Afficher la liste des fichiers et des dossiers du repertoire courant
            case "ls" -> (new CommandeLS(ps, commande, this)).execute();

            // Afficher le repertoire courant
            case "pwd" -> (new CommandePWD(ps, commande, this)).execute();

            // Envoyer (uploader) un fichier
            case "stor" -> (new CommandeSTOR(ps, commande, this)).execute();

            // Créer un dossier
            case "mkdir" -> (new CommandeMKDIR(ps, commande, this)).execute();

            // Supprimer un dossier vide
            case "rmdir" -> (new CommandeRMDIR(ps, commande, this)).execute();

            case "adduser" -> {
                if (currentUser.equals("su"))
                    new CommandeADDUSER(ps, commande, this).execute();
                else ps.println("2 Vous n'avez pas les droits pour effectuer cette commande");
            }

            case "rm" -> (new CommandeRM(ps, commande, this)).execute();

            default -> ps.println("2 Erreur, la commande n'existe pas");
        }
    }

    public static String getFolderSeparator() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.contains("win")) ? "\\" : "/";
    }

    public static String addPath(String path, String add) {

        String folderSeparator = getFolderSeparator();

        // Avoir path et add bien formaté afin qu'ils puissent être ajoutés entre eux
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
        // On veut revenir au repertoire supérieur (..)
        String folderSeparator = getFolderSeparator();

        currentPath = currentPath.substring(0, currentPath.lastIndexOf(folderSeparator));
    }

    public void reset() {
        userOk = false;
        pwOk = false;
        currentPath = rootPath;
        currentUser = "";
    }

    public String findSubDirectory(String dir, String basePath) {
        File file = new File(basePath);

        // This line is used to get all the directories in the current path.
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

        if (directories == null) {
            return "";
        }

        for (String directory : directories) {
            if (dir.equals(directory)) {
                return directory;
            }
        }
        return "";
    }

    public String findFile(String filename, String basePath) {
        File file = new File(basePath);

        String[] files = file.list((current, name) -> new File(current, name).isFile());

        if (files == null) return "";

        for (String curFile : files) {
            if (filename.equals(curFile)) {
                return curFile;
            }
        }
        return "";
    }

    public String findSubDirectory(String dir) {
        return findSubDirectory(dir, getAbsolutePath());
    }

    public String findFile(String filename) {
        return findFile(filename, getAbsolutePath());
    }

    public String getAbsolutePath() {
        return addPath(this.rootPath, this.currentPath);
    }
}
