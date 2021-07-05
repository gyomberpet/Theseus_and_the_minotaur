package things.powerups;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import map.Field;
import game.saveload.LoadImages;
import map.Maze;
import things.characters.Theseus;


/**
 * Theseus sebességét képes növelni egy bizonyos ideig.
 * Leszármazottja a DurationPowerUp-nak.
 */
public class SpeedPowerUp extends DurationPowerUp{
	public SpeedPowerUp(Maze maze, Field currentField) {
		super(maze, currentField, "S");
	}
	
	/**
	 * Ha Theseus nekimegy, megnöveli a sebességét a kétszeresére.
	 * Hozzáadja magát Theseus aktuális DurationPowerUp-jainak listájához.
	 */
	public void hitBy(Theseus t) {
		super.hitBy(t);
		t.setSpeed(t.getSpeed()*2);
		t.addPowerUp(this);
	}
	
	/**
	 * Akkor hívódik, ha a hatása lejárt.
	 * Ekkor visszaállítja a paraméterül kapott Theseus sebességét, azaz a felére csökkenti azt.
	 */
	public void finished(Theseus t) {
		t.setSpeed(t.getSpeed()/2);
		t.removePowerUp(this);
	}
	
	/**
	 * Kirajzolja a SpeedPowerUp-ot.
	 */
	public void draw(Graphics g) {
		int size = LoadImages.size;
		BufferedImage speedtImage = LoadImages.images.get("Speed");
		g.drawImage(speedtImage,getPosX(),getPosY(),getPosX()+size,getPosY()+size,0,0,size,size,null);
	}
}
