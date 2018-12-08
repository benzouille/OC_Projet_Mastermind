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

/**
 * Panel de jeu du Mastermind
 */
public class GamePanelMastermind  extends JPanel {

    private Configuration config;
    private ModeDeJeu modeDeJeu;
    private Partie partie;
    private Controller controller;

    private Container contentPane = this;

    private JLabel jlBleu, jlBrun, jlCyan, jlJaune, jlOrange, jlRose, jlRouge, jlVert, jlVertClair, jlViolet, jlNoir, jlBlanc;
    private JLabel label[] = {jlBleu, jlBrun, jlCyan, jlJaune, jlOrange, jlRose, jlRouge, jlVert, jlVertClair, jlViolet, jlNoir, jlBlanc};

    private Balle bleu = new Balle(TypeCouleur.BLEU),
            brun = new Balle(TypeCouleur.BRUN),
            cyan = new Balle(TypeCouleur.CYAN),
            jaune = new Balle(TypeCouleur.JAUNE),
            orange = new Balle(TypeCouleur.ORANGE),
            rose = new Balle(TypeCouleur.ROSE),
            rouge = new Balle(TypeCouleur.ROUGE),
            vert = new Balle(TypeCouleur.VERT),
            vertClair = new Balle(TypeCouleur.VERT_CLAIR),
            violet = new Balle(TypeCouleur.VIOLET),
            noir = new Balle(TypeCouleur.NOIR),
            blanc = new Balle(TypeCouleur.BLANC);

    private Balle balles[] = {bleu, brun, cyan, jaune, orange, rose, rouge, vert, vertClair, violet, noir, blanc};

    private JPanel jpSouth, jpGauche, jpGaucheHaut, jpGaucheBas, jpCentre, jpCentreHaut, jpCentreBas, jpDroit, jpDroitHaut, jpDroitBas, jpSolution, jpProposition;
    private JLabel jlTitreSolution, jlTitreProposition, jlTitreIndice, jlInstruction;
    private JButton okButton, cancelButton;

    private JPanel jpTop = new JPanel(), jpRight = new JPanel();
    private JLabel jlModeDeJeu = new JLabel();

    private CenterGamePanelMastermind centerGamePanel;

    private Font font = new Font("Sego UI",Font.PLAIN,24), fontTitre = new Font("Sego UI",Font.PLAIN,40),
            font2 = new Font("Sego UI",Font.PLAIN,18);

    private String strProposition = "";

    private int nbreBalle;

    private boolean devMode;
    private boolean dataIsOk;
    private boolean boutonActif = true;

    /**
     * Constructeur
     * @param config Configuration
     * @param modeDeJeu ModeDeJeu
     * @param partie Partie
     * @param controller Controller
     */
    public GamePanelMastermind(Configuration config, ModeDeJeu modeDeJeu, Partie partie, Controller controller) {
        this.partie = partie;
        this.modeDeJeu = modeDeJeu;
        this.config = config;
        this.controller = controller;
        devMode = config.isDevModEnJeu();

        this.setSize(800, 1000);
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 15, true));

        jpTop.setPreferredSize(new Dimension(750, 90));

        if (partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
            jlModeDeJeu.setText("Mode challenger Mastermind");
            jlModeDeJeu.setForeground(Color.RED);
            jlInstruction = new JLabel("La combinaison est de " + config.getCombiMast() + " balles");
            jlInstruction.setPreferredSize(new Dimension(380, 30));
            jlInstruction.setForeground(Color.gray);
            jlInstruction.setFont(font2);
            jlInstruction.setHorizontalAlignment(SwingConstants.CENTER);
            jlInstruction.setVerticalAlignment(SwingConstants.CENTER);
        }
        else if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            jlModeDeJeu.setText("Mode défenseur Mastermind");
            jlModeDeJeu.setForeground(Color.BLUE);
        }
        jlModeDeJeu.setHorizontalAlignment(SwingConstants.CENTER);
        jlModeDeJeu.setVerticalAlignment(SwingConstants.CENTER);
        jlModeDeJeu.setPreferredSize(new Dimension(700, 55));
        jlModeDeJeu.setFont(fontTitre);
        jpTop.add(jlModeDeJeu, BorderLayout.CENTER);
        if(partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
            jpTop.add(jlInstruction, BorderLayout.SOUTH);
        }

        initPanelEast();

        centerGamePanel = new CenterGamePanelMastermind(config, partie);
        centerGamePanel.add(jpRight, BorderLayout.EAST);

        if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            centerGamePanel.addDataLine(partie);
        }

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

        nbreBalle = config.getCouleurMast();
        int i = 0;
        if(partie.getModeDePartie().equals(ModeDePartie.MAST_DEF)) {
            nbreBalle = balles.length;
            i = 10;
        }
        for (i =i ;i < nbreBalle; i++) {
            label[i] = new JLabel(balles[i].getImageIcon());
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

    /**
     * Panel avec les indications proposition, indice, solution.
     */
    private void initPanelGauche() {
        jpGauche = new JPanel();
        jpGauche.setPreferredSize(new Dimension(125, 150));

        jpGaucheHaut = new JPanel();
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
        jpGaucheBas.setPreferredSize(new Dimension(125, 75));

        jlTitreSolution = new JLabel("Solution");
        jlTitreSolution.setFont(font);

        if (partie.getModeDePartie() == ModeDePartie.MAST_DEF || devMode) {
            jpGaucheBas.add(jlTitreSolution);
            jpGaucheBas.setVisible(true);
        }

        jpGauche.setLayout(new BorderLayout());
        jpGauche.add(jpGaucheHaut, BorderLayout.NORTH);
        jpGauche.add(jpGaucheBas, BorderLayout.CENTER);

        jpSouth.add(jpGauche, BorderLayout.WEST);
    }

    /**
     * Panel contenant l'affichage de la proposition/indices et de la solution
     */
    private void initPanelCentre() {

        jpCentre = new JPanel();
        jpCentre.setLayout(new BorderLayout());
        jpCentre.setPreferredSize(new Dimension(300, 150));

        jpCentreHaut = new JPanel();
        jpCentreHaut.setPreferredSize(new Dimension(350, 75));

        jpProposition = new JPanel();
        jpProposition.setBackground(Color.white);
        jpProposition.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        jpProposition.setPreferredSize(new Dimension(380, 70));
        jpProposition.addKeyListener(new EnterListener());



        jpCentreHaut.add(jpProposition);

        jpCentreBas = new JPanel();
        jpCentreBas.setPreferredSize(new Dimension(350, 75));
        jpSolution = new JPanel();
        centerGamePanel.stringToImage(partie.getSolution(), jpSolution, balles);
        jpSolution.setFont(font);
        jpSolution.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        jpSolution.setPreferredSize(new Dimension(380, 70));
        jpCentreBas.add(jpSolution);
        jpCentreBas.setVisible(devMode);
        if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
            jpCentreBas.setVisible(true);
        }

        jpCentre.add(jpCentreHaut, BorderLayout.NORTH);
        jpCentre.add(jpCentreBas, BorderLayout.CENTER);

        jpSouth.add(jpCentre, BorderLayout.CENTER);
    }

    /**
     * Panel contenant le bouton ok et annuler
     */
    private void initPanelDroit() {

        jpDroit = new JPanel();
        jpDroit.setPreferredSize(new Dimension(125, 150));

        jpDroitHaut = new JPanel();
        jpDroitHaut.setPreferredSize(new Dimension(125, 75));
        okButton = new JButton("Ok");
        okButton.setPreferredSize(new Dimension(125, 70));
        okButton.setHorizontalAlignment(JButton.CENTER);
        okButton.setVerticalAlignment(JButton.CENTER);
        okButton.addActionListener(new OkButtonListener());
        jpDroitHaut.add(okButton);

        jpDroitBas = new JPanel();
        jpDroitBas.setPreferredSize(new Dimension(125, 75));
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

    /**
     * Methode permettant avec le mode dev activé de mettre directement l'indice dans le JPanel
     */
    public void devIndice() {
        strProposition = partie.getIndice();
        for(int  i =0; i < strProposition.length(); i++) {
            if (strProposition.charAt(i) == '0') {
                JLabel jLabel = new JLabel(noir.getImageIconMoy());
                jpProposition.add(jLabel);
            } else {
                JLabel jLabel = new JLabel(blanc.getImageIconMoy());
                jpProposition.add(jLabel);
            }
        }
    }

    /**
     * Methode pour activer le Panel dans le mode duel
     */
    public void newTurn() {
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 15, true));
        okButton.setEnabled(true);
        boutonActif = true;
        this.revalidate();
    }

    /**
     * Methode pour désactiver le Panel dans le mode duel
     */
    public void stopTurn() {
        this.setBorder(BorderFactory.createLineBorder(Color.RED, 15, true));
        okButton.setEnabled(false);
        boutonActif = false;
        this.revalidate();
    }

    /**
     * Methode pour le Mode Challenger, envoie l'objet partie au controlleur puis au CenterGamePanel si les données sont conforme
     */
    public void okMastChal() {

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

    /**
     * Methode pour le Mode Defenseur, envoie l'objet partie au controlleur puis au CenterGamePanel si les données sont conforme
     */
    public void okMastDef() {
        if(isOkIndice(strProposition)){
            jpProposition.removeAll();
            partie.setIndice(strProposition);
            partie.setActif(false);
            controller.sendIndice(partie);
            centerGamePanel.addDataLine(partie);
            if(config.isDevModEnJeu() && partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
                controller.sendProposition(partie);
                strProposition = partie.getIndice();
                for(int  i =0; i < strProposition.length(); i++){
                    if(strProposition.charAt(i) == '0'){
                        JLabel jLabel = new JLabel(noir.getImageIconMoy());
                        jpProposition.add(jLabel);
                    }
                    else{
                        JLabel jLabel = new JLabel(blanc.getImageIconMoy());
                        jpProposition.add(jLabel);
                    }
                    jpProposition.repaint();
                    jpProposition.revalidate();
                }
            }
            else {
                strProposition = "";
                jpProposition.removeAll();
                jpProposition.repaint();
                jpProposition.revalidate();
            }
            if(modeDeJeu == ModeDeJeu.MAST_DUEL) {
                stopTurn();
            }
            jpProposition.repaint();
            jpProposition.revalidate();
            this.revalidate();
        }
    }

    /**
     * Verifie l'integritée de la proposition pour le mode defenseur
     * @param indice String
     * @return dataIsOk boolean
     */
    public boolean isOkIndice(String indice) {
        dataIsOk = true;
        if (indice.length() > config.getCombiMast()) {
            JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez entrer une longueur d'indice plus petite que "+ config.getCombiMast() +".", "ERREUR", JOptionPane.ERROR_MESSAGE);
            dataIsOk = false;
            strProposition = "";
            jpProposition.removeAll();
            jpProposition.repaint();
            jpProposition.revalidate();
        }
        else {
            dataIsOk = true;
        }
        return dataIsOk;
    }

    /**
     * Verifie l'integritée de la proposition pour le mode challenger
     * @param proposition String
     * @return dataIsOk boolean
     */
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

    /**
     * Listener du bouton ok utilise les methodes  okMastChal() et  okMastDef()
     */
    class OkButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
                okMastChal();
            }
            else if (partie.getModeDePartie() == ModeDePartie.MAST_DEF) {
                okMastDef();
            }
        }
    }

    /**
     *  Listener du bouton annuler, vide le panel jpProposition et le string strProposition
     */
    class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            strProposition = "";
            jpProposition.removeAll();
            jpProposition.repaint();
            jpProposition.revalidate();
        }
    }

    /**
     * Bouton Cliquable utilisant les objets Balle
     * @param <GamePanelMastermind>
     */
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

        public void mouseClicked(MouseEvent arg0) { }

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
            if (boutonActif) {
                smallSize();
            }
        }

        public void mouseReleased(MouseEvent e) {
                bigSize();
            if (boutonActif) {
                if (jpProposition.getComponentCount() + 1 > config.getCombiMast()) {
                    JOptionPane.showMessageDialog(null, "Erreur ! \n Vous avez atteint le nombre maximal de balles.", "ERREUR", JOptionPane.ERROR_MESSAGE);
                } else {
                    int chiffre = balle.getTypeCouleur().getValeur();
                    if (chiffre == 10) {
                        chiffre = 0;
                    } else if (chiffre == 11) {
                        chiffre = 1;
                    }
                    strProposition += chiffre;
                    JLabel jLabel = new JLabel(balle.getImageIconMoy());
                    jpProposition.add(jLabel);
                }
                jpProposition.revalidate();
            }
        }
    }

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