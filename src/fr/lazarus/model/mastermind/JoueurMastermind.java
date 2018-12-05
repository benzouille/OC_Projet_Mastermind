package fr.lazarus.model.mastermind;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.ModelJoueur;
import fr.lazarus.model.Partie;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;
import fr.lazarus.view.game.PopUpFinPartie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Classe du model du mastermind qui renvoie une proposition en fonction de l'indice reçu du Bean Partie
 */
public class JoueurMastermind implements ModelJoueur, Observable {

    //-- Les logs
    private static final Logger logger = LogManager.getLogger();

    private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
    private Observateur obs;

    private Partie partie;
    private Configuration config;

    private String indices, derniereAction;
    private int tailleIndice, nbreNoir, nbreBlanc;
    private int tour = 1, positionCouleur1 = 0, valeurDefaut = -1;

    boolean victoire = false;

    private ArrayList<Integer> couleur = new ArrayList<>();
    private ArrayList<Integer> poubelle = new ArrayList<>();
    private ArrayList<Integer> verrou = new ArrayList<>();
    private ArrayList<Integer> proposition = new ArrayList<>();
    private ArrayList<Integer> solution = new ArrayList<>();
    private ArrayList<Integer> indice = new ArrayList<>();

    /**
     * Constructeur, recupere les parametres et initialise la classe
     * @param config
     * @param partie
     * @param obs
     */
    public JoueurMastermind(Configuration config, Partie partie, Observateur obs){
        this.config = config;
        this.partie = partie;
        this.obs = obs;
        this.addObservateur(obs);
        initOrdinateur(partie);

        //ajout des couleurs dans l'arraylist couleur.
        for(int i = 0; i<config.getCouleurMast(); i++) {
            couleur.add(i);
        }
        //ajout des -1 dans l'arrayList proposition et verrou.
        for (int i = 0; i<config.getCombiMast(); i++) {
            proposition.add(-1);
            verrou.add(-1);
        }
    }

    /**
     * Au début d'une partie lance la première proposition de l'ordinateur.
     */
    public void initOrdinateur(Partie partie) {
        if (partie.getIndice().equals("vide")) {
            String proposition = "";
            for (int i = 0; i<config.getCombiMast(); i++) {
                proposition += '0';
            }
            derniereAction = "monoChrome";
            this.partie.setProposition(proposition);
        }
    }

    /**
     * Renvoie une proposition dans le Bean Partie en fonction de l'indice extrait du Bean.
     * @param partie Partie
     * @return
     */
    public String propositionOrdinateur(Partie partie) {

	    /*
	    --stringToList(indices) pour convertir un string en arrayList.
	    --noirIgnore(nbreVerrou()) pour elever le nombre d'indice noir dont on connais deja les emplacements/couleurs.
	    --lecteurIndice() pour compter le nombre de noir et de blanc dans l'indice.
	    --Formattage avec un monoChrome avec des -1.
	    --verrouillé() qui va ajouter à la proposition les emplacements/valeurs que l'on a verouillé auparavant.
	    --Interpretation de l'indices et retourne un string de proposition.
	     */

        /* Formattage de l'indice */
        indice = stringToList(partie.getIndice());
        tailleIndice = indice.size();
        noirIgnore(nbreVerrou());
        lecteurIndice();

        /* verifie que les conditions de victoire soient bien réunis */
        if(partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            endGame();
        }

        /* Formattage de la proposition */
        for (int i = 0; i<config.getCombiMast(); i++) {
            proposition.set(i, -1);
        }
        verrouille();

        /*
         ********************************
         *  Interpretation de l'indice  *
         ********************************
         */

        /* verifie si l'indice modifié par noirIgnore() est vide */
        if(nbreBlanc == 0 && nbreNoir == 0) {
            aLaPoubelle(0);
            monoChrome(couleur.get(0));

            /* On ajoute un tour et on retourne la proposition en un string */
            this.partie.setProposition(listToString(proposition));
            if(partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
                this.partie.addTour();
            }
            updateObservateur();
            return listToString(proposition);
        }

        /* Si la derniere action est un monoChrome() */
        if(derniereAction.equals("monoChrome")){
            if(nbreNoir == 1) {
                /* remise à zéro de la positionCouleur1 */
                positionCouleur1 = 0;
                biChrome(couleur.get(0),positionCouleur1,couleur.get(1));
            }
            else{

                /* le nombre de noir -1 donne le nombre de fois la couleur à ajouter à l'arrayList couleur */
                positionCouleur1 = 0;
                biChrome(couleur.get(0),positionCouleur1,couleur.get(1));
                ajoutCouleur(nbreNoir-1, couleur.get(0));
            }
            this.partie.setProposition(listToString(proposition));
            if(partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
                this.partie.addTour();
            }
            updateObservateur();
            return listToString(proposition);
        }
        /* Si la derniere action est un biChrome() */

        /* Si pas de blanc */
        if(nbreBlanc == 0) {
            /* Si 1 noir */
            if (nbreNoir == 1) {
                //couleur bonne -> verrouillage et la couleur va à la poubelle, couleur2 absente => poubelle, monoChrome(couleur 0).

                ajoutAuVerrou(positionCouleur1, couleur.get(0));
                verrouille();
                aLaPoubelle(0);
                aLaPoubelle(0);
                monoChrome(couleur.get(0));
            }
            /* Si plusieurs noirs */
            else if (nbreNoir > 1) {
                //couleur1 bonne position bonne -> couleur1 verrouillage puis poubelle, biChrome couleur2.
                ajoutAuVerrou(positionCouleur1, couleur.get(0));
                verrouille();
                aLaPoubelle(0);
                ajoutCouleur(nbreNoir, couleur.get(0));
                positionCouleur1 = 0;
                biChrome(couleur.get(0),positionCouleur1,couleur.get(1));
            }
            this.partie.setProposition(listToString(proposition));
            if(partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
                this.partie.addTour();
            }
            updateObservateur();
            return listToString(proposition);
        }
        /* Si pas de noir */
        if (nbreNoir == 0) {
            /* Si 1 blanc */
            if (nbreBlanc == 1) {
                //couleur bonne, position mauvaise -> bichrome position +1, et couleur2 poubelle, biChrome()
                //peux être amélioré
                aLaPoubelle(1);
                positionCouleur1++;
                biChrome(couleur.get(0),positionCouleur1,couleur.get(1));
            }
            /* Si plusieurs blancs */
            else if (nbreBlanc == 2) {
                //couleur1 bonne, position mauvaise La couleur 2 ne peux etre que à la place de la couleur 1

                ajoutAuVerrou(positionCouleur1, couleur.get(1));
                verrouille();
                aLaPoubelle(1);
                if(tailleIndice != config.getCombiMast()) {
                    biChrome(couleur.get(0), positionCouleur1, couleur.get(1));
                }
                else{
                    monoChrome(couleur.get(0));
                }
            }
            this.partie.setProposition(listToString(proposition));
            if(partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
                this.partie.addTour();
            }

            updateObservateur();
            return listToString(proposition);
        }

        if (nbreNoir == 1 || nbreBlanc == 1){
            //rien a la poubelle couleur 1 présente à la mauvaise place couleur 2 présente, positionCouleur++ faire un biChrome avec les deux meme couleurs
            positionCouleur1++;
            biChrome(couleur.get(0), positionCouleur1, couleur.get(1));

        }
        else if (nbreNoir > 1 || nbreBlanc > 1){
            //couleur1 présente mauvaise place et couleur2 présente : positionCouleur1++; biChrome(couleur1, positionCouleur1, couleur2);
            positionCouleur1++;
            biChrome(couleur.get(0), positionCouleur1, couleur.get(1));
        }

        this.partie.setProposition(listToString(proposition));

        if(partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            this.partie.addTour();
        }
        updateObservateur();
        return listToString(proposition);
    }

    /**
     * Methode dénombrant le nombre d'indice noir(int = 1) et le nombre d'indice blanc(int = 2) contenu dans l'ArrayList.
     */
    private void lecteurIndice(){
        nbreBlanc = 0;
        nbreNoir = 0;

        for(int i = 0; i<indice.size(); i++) {
            if (indice.get(i).equals(0)) {
                nbreNoir++;
            } else {
                nbreBlanc++;
            }
        }
        indice = new ArrayList<>();
    }

    /**
     * Retire une couleur de l'ArrayList couleur la met dans l'ArrayList poubelle, puis passe à la couleur suivante.
     * @param indexColor int
     */
    private void aLaPoubelle(int indexColor){
        poubelle.add(couleur.get(indexColor));
        couleur.remove(indexColor);
    }

    /**
     * Insert une ou plusieurs fois une couleur dans l'arrayList couleur juste après la couleur en cours d'utilisation.
     * @param nbreAjout int nombre de fois qu'il faut inserer la couleur
     * @param color int couleur à inserer
     */
    private void ajoutCouleur(int nbreAjout, int color){
        for(int i = 0; i<nbreAjout; i++) {
            couleur.add(couleur.size(), color);
        }
    }

    /**
     * Retourne un string rempli de la couleur donnée au emplacement dont la valeur est -1
     * Modifie le String derniereAction en monoChrome si le paramètre d'entrée est différent de -1.
     * @param couleur int
     */
    private void monoChrome(int couleur) {
        for (int i = 0; i<config.getCombiMast(); i++) {
            if(proposition.get(i) == -1) {
                proposition.set(i, couleur);
            }
        }
        if(!(couleur == -1)) {
            derniereAction = "monoChrome";
        }
    }

    /**
     * Place couleur1 à l'index donnée si l'index n'est pas déjà occupé par une couleur verrouillé et remplie le reste avec la couleur2.
     * @param couleur1 int
     * @param positionCouleur1 int
     * @param couleur2 int
     */
    private void biChrome(int couleur1, int positionCouleur1, int couleur2) {
        int a = positionCouleur1;
        int ajout =0;
        while(verrou.get(a++) != valeurDefaut){ ajout++; }
        positionCouleur1 += ajout;
        proposition.set(positionCouleur1, couleur1);
        monoChrome(couleur2);
        derniereAction = "biChrome";
    }

    /**
     * Verouille la couleur dans l'index de la proposition si il n'est pas déjà occupé
     * @param index int.
     * @param chiffre int.
     */
    private void ajoutAuVerrou(int index, int chiffre) {
        int a = index;
        int ajout =0;
        while(verrou.get(a++) != -1){ ajout++; }
        verrou.set(index+ajout, chiffre);
    }

    /**
     * pour mettre dans la proposition les couleurs/index qui sont verrouillées
     * à mettre avant les mono/bi chrome
     */
    private void verrouille() {
        for(int i = 0; i < config.getCombiMast() ; i++) {
            if (verrou.get(i) != valeurDefaut) {
                proposition.set(i, verrou.get(i));
            }
        }
    }

    /**
     * Comptabilise le nombre d'éléments verouillés présent dans l'arrayList verrou.
     * @return nbre le nombre d'éléments verouillés.
     */
    private int nbreVerrou() {
        int nbre = 0;
        for(int i = 0; i < config.getCombiMast() ; i++) {
            if (!verrou.get(i).equals(-1)) {nbre++;}
        }
        return nbre;
    }

    /**
     * Ignore un nombre de noire dans l'indice lorsque la couleur est dans le verrou
     * @param nombre nombre de noir a ignorer contenu dans l'indice.
     */
    private void noirIgnore(int nombre) {
        for(int i = 0 ; i < nombre; i++) {
            indice.remove(indice.lastIndexOf(0));
        }
    }


    /**
     * Verifie si l'objet partie correspond aux conditions de victoire ou défaite
     * @return partie
     */
    public Partie endGame() {
        boolean b = false;
        if (partie.getIndice().length() == config.getCombiMast()) {
            for (int i = 0; i < config.getCombiMast(); i++) {
                if (Character.getNumericValue(partie.getIndice().charAt(i)) == 0) {
                    b = true;
                } else {
                    b = false;
                    break;
                }
            }
        }
        if(b){
            partie.setEnCours(false);
            @SuppressWarnings("unused")
            PopUpFinPartie pufp = new PopUpFinPartie(null, "Perdu", true, partie, obs);
            logger.info("Perdu");
        }
        else {
            if(partie.getTour() == config.getTourMast()) {
                partie.setEnCours(false);
                @SuppressWarnings("unused")
                PopUpFinPartie pufp = new PopUpFinPartie(null, "Gagné", true, partie, obs);
                logger.info("Gagné");
            }
        }
        updateObservateur();
        return partie;
    }

    /**
     * Methode transformant un string en arrayList
     * @param string string
     * @return ArrayList
     */
    private ArrayList<Integer> stringToList(String string) {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0 ; i < string.length(); i++) {
            list.add(Character.getNumericValue(string.charAt(i)));
        }
        return list;
    }

    /**
     * Methode transformant un arrayList en string
     * @param list ArrayList<Integer>
     * @return string
     */
    private String listToString(ArrayList<Integer> list) {
        String string = "";
        for(int i = 0 ; i < list.size(); i++) {
            string += list.get(i);
        }
        return string;
    }

    //-- GETTER SETTER
    public String getSolution() {return listToString(solution);}
    public void setSolution(String solution) {this.solution = stringToList(solution);}

    public String getProposition() {return listToString(proposition);}
    public void setProposition(String proposition) {this.proposition = stringToList(proposition);}

    public int getTour() { return tour; }
    public void setTour(int tour) { this.tour = tour; }

    public void addObservateur(Observateur obs) { listObservateur.add(obs); }

    public void updateObservateur() {
        for(Observateur obs : listObservateur) {
            obs.update(partie);
        }
        logger.info(partie.toString());
    }

    public void delObservateur() { }
}
