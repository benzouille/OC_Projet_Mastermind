package fr.lazarus.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration {

	//-- Les logs
	private static final Logger logger = LogManager.getLogger();

	//-- Config.properties
	private String path = "resources/config.properties";
	private Properties prop = new Properties();
	private InputStream input = null;
	private OutputStream output = null;

	private int tourPlusMoins, tourMast, combiPlusMoins, combiMast, couleurMast;
	private boolean devMod, devModEnJeu;

	public Configuration() {
		getCfg();

	}

	public Configuration(int tourPlusMoins, int tourMast, int combiPlusMoins, int combiMast, int couleurMast, boolean devMod, boolean devModEnJeu) {
		this.tourPlusMoins = tourPlusMoins;
		this.tourMast = tourMast;
		this.combiPlusMoins = combiPlusMoins;
		this.combiMast = combiMast;
		this.couleurMast = couleurMast;
		this.devMod = devMod;
		this.devModEnJeu = devModEnJeu;
	}

	/**
	 * Récuperation des configurations du jeu et du mode dev
	 */
	public void getCfg() {
		try {
			input = new FileInputStream(path);
			prop.load(input);

			// recuperation des clés/values
			devMod = Boolean.parseBoolean(prop.getProperty("devMode"));
			devModEnJeu = Boolean.parseBoolean(prop.getProperty("devModeEnJeu"));
			tourPlusMoins = Integer.valueOf(prop.getProperty("nbreTourPlusMoins"));
			combiPlusMoins = Integer.valueOf(prop.getProperty("nbreCombiPlusMoins"));
			tourMast = Integer.valueOf(prop.getProperty("nbreTourMast"));
			combiMast = Integer.valueOf(prop.getProperty("nbreCombiMast"));
			couleurMast = Integer.valueOf(prop.getProperty("nbreCouleur"));
			logger.debug("recuperation des clés/values dans config.properties : "+devMod+" "+tourPlusMoins+" "+tourMast+" "+combiPlusMoins+" "+combiMast+" "+couleurMast);

		}catch(Exception e) {
			e.printStackTrace();
			logger.catching(e);
		}
	}

	/**
	 * Ecriture des configurations dans le fichier config.properties
	 */
	public void setCfg() {
		//TODO faire l'enregistrement de la configuration dans le config.properties 
		try {
			output = new FileOutputStream(path);

			prop.setProperty("devMode", String.valueOf(devMod));
			prop.setProperty("devModeEnJeu", String.valueOf(devModEnJeu));
			prop.setProperty("nbreTourPlusMoins", String.valueOf(tourPlusMoins));
			prop.setProperty("nbreCombiPlusMoins", String.valueOf(combiPlusMoins));
			prop.setProperty("nbreTourMast", String.valueOf(tourMast));
			prop.setProperty("nbreCombiMast", String.valueOf(combiMast));
			prop.setProperty("nbreCouleur", String.valueOf(couleurMast));

			prop.store(output, null);
			logger.debug("modification du config.properties par setCfg()");

		}catch(Exception e) {
			e.printStackTrace();
			logger.catching(e);
		}
	}
	
	public String toString() {
		return "Configuration [tourPlusMoins=" + tourPlusMoins + ", tourMast=" + tourMast + ", combiPlusMoins="
				+ combiPlusMoins + ", combiMast=" + combiMast + ", couleurMast=" + couleurMast + ", devMod=" + devMod
				+ ", devModEnJeu=" + devModEnJeu + "]";
	}

	public int getTourPlusMoins() {return tourPlusMoins;}
	public void setTourPlusMoins(int tourPlusMoins) {this.tourPlusMoins = tourPlusMoins;}

	public int getTourMast() {return tourMast;}
	public void setTourMast(int tourMast) {this.tourMast = tourMast;}

	public int getCombiPlusMoins() {return combiPlusMoins;}
	public void setCombiPlusMoins(int combiPlusMoins) {this.combiPlusMoins = combiPlusMoins;}

	public int getCombiMast() {return combiMast;}
	public void setCombiMast(int combiMast) {this.combiMast = combiMast;}

	public int getCouleurMast() {return couleurMast;}
	public void setCouleurMast(int couleurMast) {this.couleurMast = couleurMast;}

	public boolean isDevMod() {return devMod;}
	public void setDevMod(boolean devMod) {this.devMod = devMod;}

	public boolean isDevModEnJeu() {return devModEnJeu;}
	public void setDevModEnJeu(boolean devModEnJeu) {this.devModEnJeu = devModEnJeu;}

}


