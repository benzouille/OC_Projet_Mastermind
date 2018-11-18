package fr.lazarus.view.game.mastermind;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.Partie;
import fr.lazarus.model.mastermind.Balle;
import fr.lazarus.model.mastermind.TypeCouleur;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CenterGamePanelMastermind extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -7562434621456598977L;
    private Configuration config;
    private Partie partie;

    private JPanel jpLeft = new JPanel(),
            jpCenter = new JPanel();

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

    private Container contentPane = this;

    public CenterGamePanelMastermind(Configuration config, Partie partie) {

        this.config = config;
        this.partie = partie;

        initPanel();
    }

    public void initPanel(){

        jpLeft.setLayout(new GridLayout(config.getTourPlusMoins()+1, 1, 10, 5));
        jpLeft.setPreferredSize(new Dimension(125, 700));
        jpLeft.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        jpCenter.setLayout(new GridLayout(config.getTourPlusMoins()+1, 2, 5, 5));
        jpCenter.setPreferredSize(new Dimension(500, 700));
        jpCenter.setBorder(BorderFactory.createLineBorder(Color.PINK));

        //-- Je rempli le GridLayout
        for (int i=0; i<config.getTourPlusMoins()+1; i++) {
            //- La numérotation du nombre de tour
            JLabel jlTour = new JLabel(tour[i]);
            jlTour.setVisible(false);
            jlTour.setFont(font);
            jlTour.setHorizontalAlignment(JLabel.RIGHT);
            jpLeft.add(jlTour);

            //-- Les champs de saisie
            JPanel jpProp = new JPanel();
            if (i > 0) {
                jpProp.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            }
            jpProp.setVisible(false);
            jpProp.setBackground(Color.WHITE);
            jpProp.setPreferredSize(new Dimension(350, 35));
            jpProp.setFont(font);
            jpCenter.add(jpProp);

            //- Les indications du Maitre du jeux
            JPanel jpIndic = new JPanel();
            jpIndic.setVisible(false);
            jpProp.setPreferredSize(new Dimension(150, 35));
            jpIndic.setFont(font);
            jpIndic.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            jpCenter.add(jpIndic);

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

        contentPane.add(jpLeft, BorderLayout.WEST);
        contentPane.add(jpCenter, BorderLayout.CENTER);
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