package fr.lazarus.view.game.mastermind;

import javax.swing.*;

/**
 * Objet contenant les jPanel de chaque ligne du tableau du Panel mastermind
 */
public class LigneTableauMast {

    private JLabel jlTour;
    private JPanel jpProps;
    private JPanel jpIndic;

    /**
     * Constructeur
     * @param jlTour JLabel
     * @param jpProps JPanel
     * @param jpIndic JPanel
     */
    public LigneTableauMast(JLabel jlTour, JPanel jpProps, JPanel jpIndic) {
        this.jlTour = jlTour;
        this.jpProps = jpProps;
        this.jpIndic = jpIndic;
    }

    //-- GETTER ET SETTER
    public JLabel getJlTour() { return jlTour; }
    public void setJlTour(JLabel jlTour) { this.jlTour = jlTour; }

    public JPanel getJpProps() { return jpProps; }
    public void setJpProps(JPanel jlProps) { this.jpProps = jlProps; }

    public JPanel getJpIndic() { return jpIndic; }
    public void setJpIndic(JPanel jlIndic) { this.jpIndic = jlIndic; }
}