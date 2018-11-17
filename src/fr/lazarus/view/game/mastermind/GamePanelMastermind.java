package fr.lazarus.view.game.mastermind;

import fr.lazarus.controller.Controller;
import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDeJeu;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.Partie;
import fr.lazarus.model.mastermind.Balle;
import fr.lazarus.model.mastermind.TypeCouleur;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

public class GamePanelMastermind  extends JPanel {

    private Configuration config;
    private ModeDeJeu modeDeJeu;
    private Partie partie;
    private Controller controller;

    private Container contentPane = this;

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

    private Balle noir = new Balle(TypeCouleur.NOIR),
            blanc = new Balle(TypeCouleur.BLANC);

    private Balle ballesIndice[] = {noir, blanc};

    private JPanel jpSouth, jpGauche, jpGaucheHaut, jpGaucheBas, jpCentre, jpCentreHaut, jpCentreBas, jpDroit, jpDroitHaut, jpDroitBas, jpSolution, jpProposition;
    private JLabel jlTitreSolution, jlTitreProposition, jlTitreIndice;
    private JTextField jtfIndice;
    private JButton okButton, cancelButton;

    private JPanel jpTop = new JPanel(), jpRight = new JPanel();
    private JLabel jlModeDeJeu = new JLabel();

    private CenterGamePanelMastermind centerGamePanel;

    private Font font = new Font("Sego UI",Font.PLAIN,24), fontTitre = new Font("Sego UI",Font.PLAIN,40);

    private String strProposition = "";

    private boolean devMode;
    private boolean dataIsOk;

    /**
     * Constructeur
     */
    public GamePanelMastermind(Configuration config, ModeDeJeu modeDeJeu, Partie partie, Controller controller) {
        this.partie = partie;
        partie.setTour(1);
        this.modeDeJeu = modeDeJeu;
        this.config = config;
        this.controller = controller;
        devMode = config.isDevModEnJeu();

        this.setSize(800, 1000);
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 15, true));

        jpTop.setPreferredSize(new Dimension(750, 90));
        jpTop.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        if (partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
            jlModeDeJeu.setText("Mode challenger Mastermind");
            jlModeDeJeu.setForeground(Color.RED);
        }
        else if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            jlModeDeJeu.setText("Mode défenseur Mastermind");
            jlModeDeJeu.setForeground(Color.BLUE);
        }

        jlModeDeJeu.setFont(fontTitre);
        jpTop.add(jlModeDeJeu);

        initPanelEast();

        centerGamePanel = new CenterGamePanelMastermind(config, partie);
        centerGamePanel.add(jpRight, BorderLayout.EAST);

        initPanelSouth();

        contentPane.add(jpTop, BorderLayout.NORTH);
        contentPane.add(centerGamePanel, BorderLayout.CENTER);
        contentPane.add(jpSouth, BorderLayout.SOUTH);

        if(!partie.isActif()) {
            stopTurn();
        }
    }

    /**
     * Panel avec les boutons cliquable
     */
    public void initPanelEast() {
        jpRight.setPreferredSize(new Dimension(125, 700));
        jpRight.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));

        Balle[] typeBalle = balles;
        int nbreBalle =config.getCouleurMast();
        if(partie.getModeDePartie().equals(ModeDePartie.MAST_DEF)) {
            nbreBalle = ballesIndice.length;
            typeBalle = ballesIndice;
        }
            for (int i = 0; i < nbreBalle; i++) {
                label[i] = new JLabel(typeBalle[i].getImageIcon());
                label[i].setBackground(Color.WHITE);
                label[i].setPreferredSize(new Dimension(60, 60));
                label[i].addMouseListener(new GamePanelMastermind.CouleurListener<GamePanelMastermind>(balles[i], label[i]));
                jpRight.add(label[i]);
            }
        }

    /**
     * Panel sud avec les entrées pour les propositions en mode challanger et les indices en mode defenseur
     */
    public void initPanelSouth() {

        jpSouth = new JPanel();
        jpSouth.setPreferredSize(new Dimension(750,150));
        jpSouth.setLayout(new BorderLayout());

        initPanelGauche();
        initPanelCentre();
        initPanelDroit();
    }

    private void initPanelGauche() {
        jpGauche = new JPanel();
        jpGauche.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        jpGauche.setPreferredSize(new Dimension(125, 150));

        jpGaucheHaut = new JPanel();
        jpGaucheHaut.setBorder(BorderFactory.createLineBorder(Color.RED));
        jpGaucheHaut.setPreferredSize(new Dimension(125, 75));

        jlTitreProposition = new JLabel("Proposition");
        jlTitreProposition.setFont(font);
        jlTitreProposition.setVerticalTextPosition(JLabel.CENTER);

        jlTitreIndice = new JLabel("Indice");
        jlTitreIndice.setFont(font);
        jlTitreIndice.setVerticalTextPosition(JLabel.CENTER);

        if(partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
            jpGaucheHaut.add(jlTitreProposition);
        }
        else if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            jpGaucheHaut.add(jlTitreIndice);
        }

        jpGaucheBas = new JPanel();
        jpGaucheBas.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        jpGaucheBas.setPreferredSize(new Dimension(125, 75));

        jlTitreSolution = new JLabel("Solution");
        jlTitreSolution.setFont(font);

        if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF || devMode == true) {
            jpGaucheBas.add(jlTitreSolution);
            jpGaucheBas.setVisible(true);
        }

        jpGauche.setLayout(new BorderLayout());
        jpGauche.add(jpGaucheHaut, BorderLayout.NORTH);
        jpGauche.add(jpGaucheBas, BorderLayout.CENTER);

        jpSouth.add(jpGauche, BorderLayout.WEST);
    }

    private void initPanelCentre() {

        jpCentre = new JPanel();
        jpCentre.setLayout(new BorderLayout());
        jpCentre.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        jpCentre.setPreferredSize(new Dimension(300, 150));

        jpCentreHaut = new JPanel();
        jpCentreHaut.setPreferredSize(new Dimension(350, 75));
        jpCentreHaut.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        jpProposition = new JPanel();
        jpProposition.setBackground(Color.white);
        jpProposition.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        jpProposition.setPreferredSize(new Dimension(350, 70));
        jpProposition.addKeyListener(new EnterListener());

        jpCentreHaut.add(jpProposition);

        jpCentreBas = new JPanel();
        jpCentreBas.setPreferredSize(new Dimension(350, 75));
        jpCentreBas.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        jpSolution = new JPanel();
        stringToImage(partie.getSolution(), jpSolution, balles);
        jpSolution.setFont(font);
        jpSolution.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        jpSolution.setAlignmentX(35);
        jpSolution.setPreferredSize(new Dimension(350, 70));
        jpCentreBas.add(jpSolution);
        jpCentreBas.setVisible(devMode);
        if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
            jpCentreBas.setVisible(true);
        }

        jpCentre.add(jpCentreHaut, BorderLayout.NORTH);
        jpCentre.add(jpCentreBas, BorderLayout.CENTER);

        jpSouth.add(jpCentre, BorderLayout.CENTER);
    }

    private void initPanelDroit() {

        jpDroit = new JPanel();
        jpDroit.setBorder(BorderFactory.createLineBorder(Color.RED));
        jpDroit.setPreferredSize(new Dimension(125, 150));

        jpDroitHaut = new JPanel();
        jpDroitHaut.setPreferredSize(new Dimension(125, 75));
        jpDroitHaut.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        okButton = new JButton("Ok");
        okButton.setPreferredSize(new Dimension(125, 70));
        okButton.setHorizontalAlignment(JButton.CENTER);
        okButton.setVerticalAlignment(JButton.CENTER);
        okButton.addActionListener(new OkButtonListener());
        jpDroitHaut.add(okButton);

        jpDroitBas = new JPanel();
        jpDroitBas.setPreferredSize(new Dimension(125, 75));
        jpDroitBas.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        cancelButton = new JButton("Annuler");
        cancelButton.setPreferredSize(new Dimension(125, 70));
        cancelButton.setHorizontalAlignment(JButton.CENTER);
        cancelButton.setVerticalAlignment(JButton.CENTER);
        cancelButton.addActionListener(new CancelButtonListener());
        jpDroitBas.add(cancelButton);
        jpDroitBas.setVisible(true);

        jpDroit.setLayout(new BorderLayout());
        jpDroit.add(jpDroitHaut, BorderLayout.NORTH);
        jpDroit.add(jpDroitBas, BorderLayout.CENTER);

        jpSouth.add(jpDroit, BorderLayout.EAST);

    }

    public void stringToImage(String str, JPanel jPanel,Balle[] balles){
        for(int i = 0; i<str.length(); i++){
            JLabel jLabel = new JLabel(balles[Character.getNumericValue(str.charAt(i))].getImageIconMoy());
            jPanel.add(jLabel);
        }
        jPanel.revalidate();
    }

    public void devIndice() {
        //TODO modifier pour y inserer les image des balle directement, peut etre avec un stringToBall()
        //jtfProposition.setText(partie.getIndice());

    }

    public void newTurn() {
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 15, true));
        //TODO desactiver le panel droit
        okButton.setEnabled(true);
        this.revalidate();
    }

    public void stopTurn() {
        this.setBorder(BorderFactory.createLineBorder(Color.RED, 15, true));
        //TODO activer le panel droit
        okButton.setEnabled(false);
        this.revalidate();
    }

    public void okMastChal() {
        System.out.println("okPlusChal() de GamePanelPlusMoins");

        System.out.println("resultat de isOkProposition : "+ isOkProposition(strProposition) + "longueur de la prop : "+strProposition.length()+ "longueur de la config :"+config.getCombiPlusMoins());
        if (isOkProposition(strProposition)) {
            partie.setProposition(strProposition);
            partie.setActif(false);
            controller.sendProposition(partie);
            centerGamePanel.addDataLine(partie);
            cancelButton.doClick();
            if(modeDeJeu == ModeDeJeu.MAST_DUEL) {
                stopTurn();
            }

            this.revalidate();
        }
    }

    public void okMastDef() {
        System.out.println("okPlusDef() de GamePanelPlusMoins");
        String indice = "";
        if(isOkIndice(indice)){
            partie.setIndice(indice);
            partie.setActif(false);
            controller.sendIndice(partie);
            centerGamePanel.addDataLine(partie);
            if(config.isDevModEnJeu() == true && partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
                controller.sendProposition(partie);
                //TODO mettre l'indice transformé en image dans le jfProposition
                //jtfProposition.setText(partie.getIndice());
            }
            else {
                jpProposition.removeAll();
                jpProposition.repaint();
                jpProposition.revalidate();
            }
            if(modeDeJeu == ModeDeJeu.MAST_DUEL) {
                stopTurn();
            }
            this.revalidate();
        }
    }

    public boolean isOkIndice(String indice) {
        //TODO verifier que l'indice ne dépasse pas la taille du config.getCombiMast();
        dataIsOk = true;
        if (indice.length() != config.getCombiMast()) {
            JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez entrer une solution à "+ config.getCombiMast() +".", "ERREUR", JOptionPane.ERROR_MESSAGE);
            dataIsOk = false;
            cancelButton.doClick();
        }
        else {
            dataIsOk = true;
        }
        return dataIsOk;
    }

    public Boolean isOkProposition(String proposition) {
        dataIsOk = true;

        if (proposition.length() != config.getCombiMast()) {
            JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez entrer une proposition à "+ config.getCombiMast() +" balles.", "ERREUR", JOptionPane.ERROR_MESSAGE);
            dataIsOk = false;
            cancelButton.doClick();
        }
        else {
            dataIsOk = true;
        }
        return dataIsOk;
    }

    class OkButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
                okMastChal();
            }
            else if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
                okMastDef();
            }
            else {
            }
        }
    }

    class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            strProposition = "";
            jpProposition.removeAll();
            jpProposition.repaint();
            jpProposition.revalidate();
            System.out.println("bouton cancel");
        }
    }


    class CouleurListener<GamePanelMastermind> implements MouseListener {
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

        public void mouseClicked(MouseEvent arg0) {
            System.out.println("ajout de la balle "+ balle.getTypeCouleur().getCouleur() + ".");
            strProposition += balle.getTypeCouleur().getValeur();
            System.out.println(strProposition);
            JLabel jLabel = new JLabel(balle.getImageIconMoy());
            jpProposition.add(jLabel);
            jpProposition.revalidate();
        }

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
            smallSize();
        }

        public void mouseReleased(MouseEvent e) {
            bigSize();
        }
    }

    public JLabel[] getLabel() {return label;}
    public void setLabel(JLabel[] label) {this.label = label;}

    class EnterListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()==KeyEvent.VK_ENTER){
                okButton.doClick();
            }
        }
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }
}