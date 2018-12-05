package fr.lazarus.view.game.mastermind;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.Partie;
import fr.lazarus.model.mastermind.Balle;
import fr.lazarus.model.mastermind.TypeCouleur;

import javax.swing.*;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe etant le centre du panel de jeu, affiche les données de la partie
 */
public class CenterGamePanelMastermind extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -7562434621456598977L;
    private Configuration config;
    private Partie partie;

    private JPanel jpLeft = new JPanel(),
            jpCenter = new JPanel(),
            jpRight = new JPanel(),
            jpScrollable = new JPanel();

    private JScrollPane jScrollPane = new JScrollPane();


    private Font font = new Font("Sego UI",Font.PLAIN,24);

    private String[] tour = { "#", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19","20"};

    private Balle bleu = new Balle(TypeCouleur.BLEU),
            brun = new Balle(TypeCouleur.BRUN),
            cyan = new Balle(TypeCouleur.CYAN),
            jaune = new Balle(TypeCouleur.JAUNE),
            orange = new Balle(TypeCouleur.ORANGE),
            rose = new Balle(TypeCouleur.ROSE),
            rouge = new Balle(TypeCouleur.ROUGE),
            vert = new Balle(TypeCouleur.VERT),
            vertClair = new Balle(TypeCouleur.VERT_CLAIR),
            violet = new Balle(TypeCouleur.VIOLET);

    private Balle balles[] = {bleu, brun, cyan, jaune, orange, rose, rouge, vert, vertClair, violet};

    private Balle noir = new Balle(TypeCouleur.NOIR),
            blanc = new Balle(TypeCouleur.BLANC);

    private Balle ballesIndice[] = {noir, blanc};

    private LigneTableauMast ligneTableau = null;
    private List<LigneTableauMast> listLigneTableau = new ArrayList<>();

    public CenterGamePanelMastermind(Configuration config, Partie partie) {

        this.config = config;
        this.partie = partie;

        initPanel();
    }

    /**
     * Initialisation du panel
     */
    public void initPanel(){

        jpLeft.setLayout(new GridLayout(config.getTourMast()+1, 1, 10, 5));
        jpCenter.setLayout(new GridLayout(config.getTourMast()+1, 2, 5, 5));
        jpRight.setLayout(new GridLayout(config.getTourMast()+1, 1, 5, 5));


        //-- Je rempli le GridLayout
        for (int i=0; i<config.getTourMast()+1; i++) {
            //- La numérotation du nombre de tour
            JLabel jlTour = new JLabel(tour[i]);
            jlTour.setVisible(false);
            jlTour.setFont(font);
            jlTour.setPreferredSize(new Dimension(75, 60));
            jlTour.setHorizontalAlignment(JLabel.RIGHT);
            jlTour.setVerticalAlignment(JLabel.CENTER);
            jpLeft.add(jlTour);

            //-- Les champs de saisie
            JPanel jpProp = new JPanel();
            if (i > 0) {
                jpProp.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            }
            jpProp.setVisible(false);
            jpProp.setBackground(Color.WHITE);
            jpProp.setPreferredSize(new Dimension(350, 60));
            jpProp.setFont(font);
            jpCenter.add(jpProp);

            //- Les indications du Maitre du jeux
            JPanel jpIndic = new JPanel();
            jpIndic.setVisible(false);
            jpIndic.setPreferredSize(new Dimension(150, 60));
            jpIndic.setFont(font);
            jpIndic.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            jpRight.add(jpIndic);

            ligneTableau = new LigneTableauMast(jlTour, jpProp, jpIndic);
            listLigneTableau.add(ligneTableau);
        }

        JLabel jlProps = new JLabel("Proposition");
        jlProps.setFont(font);
        JLabel jlIndic = new JLabel("Indice");
        jlIndic.setFont(font);

        listLigneTableau.get(0).getJpProps().add(jlProps);
        listLigneTableau.get(0).getJpIndic().add(jlIndic);
        if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
            stringToImage(partie.getProposition(), listLigneTableau.get(1).getJpProps());
        }

        setVisibleLine(0);

        jpScrollable.setLayout(new BorderLayout());
        jpScrollable.add(jpLeft, BorderLayout.WEST);
        jpScrollable.add(jpCenter, BorderLayout.CENTER);
        jpScrollable.add(jpRight, BorderLayout.EAST);
        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        jScrollPane.setViewportView(jpScrollable);
        jScrollPane.setPreferredSize(new Dimension(650, 700));
        this.add(jScrollPane, BorderLayout.CENTER);

    }

    /**
     * transforme les charactères d'un string en suite d'images inserés dans un JPanel.
     * @param str String à convertir
     * @param jPanel panel dans lequel y ajouter les images
     */
    public void stringToImage(String str, JPanel jPanel){
        for(int i = 0; i<str.length(); i++){
            JLabel jLabel = new JLabel(balles[Character.getNumericValue(str.charAt(i))].getImageIconMoy());
            jPanel.add(jLabel);
        }
        jPanel.revalidate();
    }

    /**
     * transforme les charactères d'un string en suite d'images inserés dans un JPanel.
     * @param str String à convertir
     * @param jPanel panel dans lequel y ajouter les images
     * @param balles le tableau d'objet balle pour aller y chercher les images
     */
    public void stringToImage(String str, JPanel jPanel,Balle[] balles){
        for(int i = 0; i<str.length(); i++){
            if (balles == ballesIndice){
                JLabel jLabel = new JLabel(balles[Character.getNumericValue(str.charAt(i))].getImageIconNano());
                jPanel.add(jLabel);
            }
            else {
                JLabel jLabel = new JLabel(balles[Character.getNumericValue(str.charAt(i))].getImageIconMoy());
                jPanel.add(jLabel);
            }

        }
        jPanel.revalidate();
    }

    /**
     * Rend la ligne à l'index visible dans le JScrollPane
     * @param index
     */
    public void setVisibleLine(int index) {
        if (partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
            listLigneTableau.get(index).getJlTour().setVisible(true);
            listLigneTableau.get(index).getJpIndic().setVisible(true);
            listLigneTableau.get(index).getJpProps().setVisible(true);
        }
        else if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            listLigneTableau.get(index).getJlTour().setVisible(true);
            listLigneTableau.get(index).getJpIndic().setVisible(true);
            listLigneTableau.get(index).getJpProps().setVisible(true);
            listLigneTableau.get(index+1).getJlTour().setVisible(true);
            listLigneTableau.get(index+1).getJpProps().setVisible(true);
        }
    }

    /**
     * Ajoute les données dans la ligne en fonction du tour
     * @param partie
     */
    public void addDataLine(Partie partie) {
        if (partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
            stringToImage(partie.getProposition(), listLigneTableau.get(partie.getTour()).getJpProps());

        }
        else if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            stringToImage(partie.getProposition(), listLigneTableau.get(partie.getTour()+1).getJpProps());
        }
        if(!partie.getIndice().equals("vide")){
            stringToImage(partie.getIndice(), listLigneTableau.get(partie.getTour()).getJpIndic(), ballesIndice);
        }
        setVisibleLine(partie.getTour());
        this.repaint();
    }
}