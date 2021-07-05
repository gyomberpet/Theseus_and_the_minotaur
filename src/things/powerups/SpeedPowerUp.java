package things.powerups;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import map.Field;
import game.saveload.LoadImages;
import map.Maze;
import things.characters.Theseus;


/**
 * Theseus sebess�g�t k�pes n�velni egy bizonyos ideig.
 * Lesz�rmazottja a DurationPowerUp-nak.
 */
public class SpeedPowerUp extends DurationPowerUp{
	public SpeedPowerUp(Maze maze, Field currentField) {
		super(maze, currentField, "S");
	}
	
	/**
	 * Ha Theseus nekimegy, megn�veli a sebess�g�t a k�tszeres�re.
	 * Hozz�adja mag�t Theseus aktu�lis DurationPowerUp-jainak list�j�hoz.
	 */
	public void hitBy(Theseus t) {
		super.hitBy(t);
		t.setSpeed(t.getSpeed()*2);
		t.addPowerUp(this);
	}
	
	/**
	 * Akkor h�v�dik, ha a hat�sa lej�rt.
	 * Ekkor vissza�ll�tja a param�ter�l kapott Theseus sebess�g�t, azaz a fel�re cs�kkenti azt.
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
