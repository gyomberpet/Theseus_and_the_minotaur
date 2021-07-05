package things.powerups;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import map.Field;
import game.saveload.LoadImages;
import map.Maze;
import things.characters.Theseus;

/**
 * Theseus életerejét képes növelni.
 * A PowerUp-bol oroklodik.
 */
public class LifePowerUp extends PowerUp{
	public LifePowerUp(Maze maze, Field currentField) {
		super(maze, currentField, "L");
	}
	
	/**
	 * Ha Theseus nekiment, növeli az életerejét eggyel, feltéve, hogy a hármat nem haladhatja meg.
	 */
	public void hitBy(Theseus t) {
		super.hitBy(t);
		if(t.getHp()<3)
			t.addHp();
	}
	
	/**
	 * Kirajzolja a LifePowerUp-ot.
	 */
	public void draw(Graphics g) {
		int size = LoadImages.size;
		BufferedImage heartImage = LoadImages.images.get("Heart");
		g.drawImage(heartImage,getPosX(),getPosY(),getPosX()+size,getPosY()+size,0,0,size,size,null);
	}
}
