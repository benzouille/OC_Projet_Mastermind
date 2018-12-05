package fr.lazarus.view.game.mastermind;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.Partie;
import fr.lazarus.model.mastermind.Balle;
import fr.lazarus.model.mastermind.TypeCouleur;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * JDialog Permetant de choisir la solution dans le mode defenseur
 */
public class PopUpCombiMastermind extends JDialog implements Observable {

    /**
     *
     */
    private static final long serialVersionUID = -1975827366674767282L;
    //-- Les logs
    private static final Logger logger = LogManager.getLogger();

    protected ArrayList<Observateur> listObservateur = new ArrayList<>();

    private JLabel jlBleu, jlBrun, jlCyan, jlJaune, jlOrange, jlRose, jlRouge, jlVert, jlVertClair, jlViolet;
    private JLabel label[] = {jlBleu, jlBrun, jlCyan, jlJaune, jlOrange, jlRose, jlRouge, jlVert, jlVertClair, jlViolet};

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

    private JPanel content, jpSouth, jpCombi, panCombi;
    private JLabel jlCombi;
    private Configuration config;
    private Partie partie;
    private boolean isOkData;
    private int nbreChiffre;
    private String solution, strProposition;

    /**
     * Constructeur utilisant les bean Partie et Configuration
     * @param parent JFrame
     * @param title String
     * @param modal boolean
     * @param config Configuration
     * @param partie Partie
     * @param obs Observateur
     */
    public PopUpCombiMastermind(JFrame parent, String title, boolean modal, Configuration config, Partie partie, Observateur obs) {
        super(parent, title, modal);
        this.config =config;
        this.partie = partie;
        nbreChiffre = config.getCombiMast();
        addObservateur(obs);
        setSize(800, 260);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponent();
        setVisible(true);
    }

    /**
     * Intitiation du contenu du Panel
     */
    private void initComponent() {

        //Choix de la combinaison
        jlCombi = new JLabel("Choisissez votre code à " + nbreChiffre + " balles :");
        jpCombi = new JPanel();
        jpCombi.setBackground(Color.white);
        jpCombi.setPreferredSize(new Dimension(380, 40));
        jpCombi.setFont(new Font("Lucida Console", Font.BOLD, 30));

        strProposition ="";

        jpSouth =new JPanel();
        jpSouth.setBackground(Color.white);
        Balle[] typeBalle = balles;
        int nbreBalle =config.getCouleurMast();
        for (int i = 0; i < nbreBalle; i++) {
            label[i] = new JLabel(typeBalle[i].getImageIcon());
            label[i].setBackground(Color.WHITE);
            label[i].setPreferredSize(new Dimension(60, 60));
            label[i].addMouseListener(new CouleurListener<GamePanelMastermind>(balles[i], label[i]));
            jpSouth.add(label[i]);
        }

        JButton randomButton = new JButton("Aléatoire");
        randomButton.addActionListener(new RandomListener());

        panCombi = new JPanel();
        panCombi.setBackground(Color.white);
        panCombi.setPreferredSize(new Dimension(790, 80));
        panCombi.setBorder(BorderFactory.createTitledBorder("Choix de la combinaison"));
        panCombi.add(jlCombi);
        panCombi.add(jpCombi);
        panCombi.add(randomButton);

        //-- Ajout de tout les composants au content
        content = new JPanel();
        content.setBackground(Color.white);
        content.add(panCombi, BorderLayout.CENTER);
        content.add(jpSouth, BorderLayout.SOUTH);

        JPanel control = new JPanel();
        JButton okBouton = new JButton("OK");

        okBouton.addActionListener(new RunListener());

        JButton cancelBouton = new JButton("Annuler");
        cancelBouton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                strProposition ="";
                jpCombi.removeAll();
                jpCombi.repaint();
            }
        });

        control.setBackground(Color.white);
        control.add(okBouton);
        control.add(cancelBouton);

        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

    /**
     * Verifie l'integrité des données
     */
    private void acurateData() {

        isOkData = true;

        if (strProposition.length() != nbreChiffre) {
            JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez entrer une combinaison à "+ nbreChiffre +" balles.", "ERREUR", JOptionPane.ERROR_MESSAGE);
            isOkData = false;
            strProposition ="";
            jpCombi.removeAll();
            jpCombi.repaint();
        }
        else {
            solution = strProposition;
        }
    }

    /**
     * NestedClass du bouton "ok"
     */
    class RunListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            acurateData();
            //- ferme la fenetre lorsque les données sont bonnes
            if (isOkData) {
                setVisible(false);
                partie.setSolution(solution);
                updateObservateur();
            }

        }

    }

    /**
     * NestedClass du bouton "random", permet de choisir une combinaison aléatoire
     */
    class RandomListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            jpCombi.removeAll();
            jpCombi.repaint();
            strProposition = partie.random(nbreChiffre, config.getCouleurMast());
            stringToImage(strProposition, jpCombi);
        }
    }

    /**
     * transforme les charactères d'un string en suite d'images inserés dans un JPanel.
     * @param str String à convertir
     * @param jPanel panel dans lequel y ajouter les images
     */
    private void stringToImage(String str, JPanel jPanel){
        for(int i = 0; i<str.length(); i++){
            JLabel jLabel = new JLabel(balles[Character.getNumericValue(str.charAt(i))].getImageIconMoy());
            jPanel.add(jLabel);
        }
        jPanel.revalidate();
    }

    /**
     * Bouton Cliquable utilisant les objets Balle
     * @param <PopUpCombiMastermind>
     */
    class CouleurListener<PopUpCombiMastermind> implements MouseListener {
        private Balle balle;
        private JLabel jLabel;
        public CouleurListener(Balle balle, JLabel jLabel) {
            this.balle = balle;
            this.jLabel = jLabel;
        }

        public void smallSize() {
            jLabel.setIcon(balle.getImageIconSmall());
            jLabel.revalidate();
        }

        public void bigSize() {
            jLabel.setIcon(balle.getImageIcon());
            jLabel.revalidate();
        }

        public void mouseClicked(MouseEvent arg0) { }

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
            smallSize();
        }

        public void mouseReleased(MouseEvent e) {
            bigSize();
            if (jpCombi.getComponentCount()+1 > config.getCombiMast()){
                JOptionPane.showMessageDialog(null, "Erreur ! \n Vous avez atteint le nombre maximal de balles.", "ERREUR", JOptionPane.ERROR_MESSAGE);
            }
            else {
                strProposition += balle.getTypeCouleur().getValeur();
                JLabel jLabel = new JLabel(balle.getImageIconMoy());
                jpCombi.add(jLabel);
            }
            jpCombi.revalidate();
        }
    }

    public void addObservateur(Observateur obs) {
        listObservateur.add(obs);
    }

    public void updateObservateur() {
        for(Observateur obs : listObservateur) {
            obs.update(partie);
        }
        logger.info(partie.toString());
    }

    public void delObservateur() {}
}