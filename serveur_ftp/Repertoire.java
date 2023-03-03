import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Repertoire implements Serializable {
    String nomRepertoire;
    static long serialVersionUID = 1L;
    boolean estRacine;
    ArrayList<File> fichiers;
    ArrayList<Repertoire> sousRepertoires;

    Repertoire () {
        this.nomRepertoire = "";
        this.fichiers = new ArrayList<File>();
        this.sousRepertoires = new ArrayList<Repertoire>();
        this.estRacine = true;
    }

    Repertoire (String nomRepertoire) {
        this.nomRepertoire = nomRepertoire;
        this.fichiers = new ArrayList<>();
        this.sousRepertoires = new ArrayList<>();
        this.estRacine = false;
    }
}