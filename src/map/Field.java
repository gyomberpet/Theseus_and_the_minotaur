package map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import game.saveload.LoadImages;
import things.Thing;

/**
 * A j�t�kban egy Mez�t reprezent�l.
 * A labirintus Mez�kb�l �p�l fel, melyeken az egyes Dolgok elhelyezkedhetnek.
 * A Mez� lehet fal (ha fal, lehet n�v�nyes vagy sima), t�rolja a n�gy k�l�nb�z� ir�nyban l�v� szomsz�dj�t (ha van),
 * a sajat (X,Y) koordin�taj�t �s a rajta l�v� Dolgok list�j�t.
 */

public class Field {
	private boolean isWall;
	private boolean wallHasGrass;
	private final int grassProbability = 4;
	private Map <Direction, Field> neighbor;
	private int posX;
	private int posY;
	private List <Thing> things;
	public Field(int posY, int posX) {
		isWall = false;
		wallHasGrass = false;
		neighbor = new HashMap<>();
		this.posY = posY*LoadImages.size;
		this.posX = posX*LoadImages.size;
		this.things = new ArrayList<>();
	}
	
	/**
	 * A Mez� �rt�k�t "fal"-ra �ll�tja.
	 */
	public void setWall(boolean b) {
		isWall=b;
		if(b) {
			Random r = new Random();
			if(Math.abs(r.nextInt()%grassProbability)==0) {
				wallHasGrass = true;
			}
		}
	}
	
	/**
	 * Visszaadja a Mez�n l�v� �sszes Dolgot egy list�ban.
	 */
	public List<Thing> getThings() {
		return things;
	}
	
	/**
	 * Ha a Mez� egy fal, igaz �rtekkel t�r vissza.
	 */
	public boolean getWall() {
		return isWall;
	}
	
	/**
	 * A param�terk�nt kapott ir�nyban l�v� szomsz�dj�t a kapott Mez�re �ll�tja.
	 */
	public void setNeighbor(Direction d, Field field) {
		neighbor.put(d, field);
	}
	
	/**
	 * Visszaadja a kapott ir�nyban l�v� szomsz�dj�t.
	 */
	public Field getNeighbor(Direction d) {
		return neighbor.get(d);
	}
	
	/**
	 * A Mez� az ablakban elfoglalt t�nyleges poz�ci�j�nak X koordin�t�j�t adja vissza (bal fels� sarok).
	 */
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Be�ll�tja a Mez� X poz�ci�j�t a param�terk�nt kapott �rt�kre.
	 */
	public void setPosX(int x) {
		posX = x;
	}
	
	/**
	 * A Mez� az ablakban elfoglalt t�nyleges poz�ci�j�nak Y koordin�t�j�t adja vissza (bal fels� sarok).
	 */
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Be�ll�tja a Mez� Y poz�ci�j�t a param�terk�nt kapott �rt�kre.
	 */
	public void setPosY(int y) {
		posY = y;
	}
	
	/**
	 * A param�terk�nt kapott Dolgot hozz�adja a Mez�n t�rolt dolgokhoz.
	 * El�tte minden, m�r rajta l�v� dologgal �tk�zteti.
	 */
	public void addThing(Thing t) {
		for(int i = 0; i < things.size(); ++i) {
			t.collideWith(things.get(i));
		}
		things.add(t);
	}
	
	/**
	 * A param�terk�nt kapott Dolgot t�rli a rajta l�v� Dolgok k�z�l.
	 */
	public void removeThing(Thing t) {
		things.remove(t);
	}
	
	/**
	 * Igaz �rt�kkel t�r vissza, ha a Mez�n egyetlen Dolog sincs.
	 */
	public boolean getIsEmpty() {
		return things.size()==0;
	}
	
	/**
	 * Igaz �rtekkel t�r vissza, ha a Mez� egy keresztez�d�s.
	 * A jatekban minden Mez� keresztez�d�snek tekinthet�, amelynek legal�bb 3 olyan szomsz�dja van, ami nem fal.
	 */
	public boolean isCrossRoad() {
		List<Direction> directions = new ArrayList<Direction>();
		directions.add(Direction.RIGHT);
		directions.add(Direction.LEFT);
		directions.add(Direction.UP);
		directions.add(Direction.DOWN);
		Collections.shuffle(directions);
		int db = 0;
		for(Direction d: directions) {
			if(!neighbor.get(d).isWall)
				db++;
		}
		return db >=3;
	}
	
	/**
	 * Kirajzolja a Mez�t.
	 * Ha a Mez� nem fal, f�vet rajzol r�.
	 * Ha a Mez� fal, att�l f�gg�en hogy milyen fal (n�v�nyes vagy sima), �gy rajzolja ki.
	 * A (0,1)-es Mez�re egy hercegn�t rajzol, aki tartja Theseus fonal�nak a v�g�t.
	 */
	public void draw(Graphics g) {
		int size = LoadImages.size;
		BufferedImage wallImage=LoadImages.images.get("Wall");
		BufferedImage grassImage=LoadImages.images.get("Grass");
		BufferedImage princessImage=LoadImages.images.get("Princess");
		if(isWall) {
			int x = 0;
			if(wallHasGrass) {
				x = 1;
			}
			g.drawImage(wallImage,posX,posY,posX+size,posY+size,2*x*size,0,2*(x+1)*size,2*size,null);
		}
		if(!isWall) {
			g.drawImage(grassImage,posX,posY,posX+size,posY+size,2*size,0,2*2*size,2*size,null);
		}
		if(posY==0 && posX==LoadImages.size) {
			g.drawImage(grassImage,posX,posY,posX+size,posY+size,2*size,0,2*2*size,2*size,null);		
			g.drawImage(princessImage,posX,posY,posX+size,posY+size,0,0,size,size,null);
		}
	}
	
	/**
	 * A rajta l�v� �ssze Dolognak megh�vja a kirajzol� f�ggv�ny�t, �gy megjelen�tve azokat a Mez�n.
	 */
	public void drawThings(Graphics g) {
		for(Thing t: things) {
			t.draw(g);
		}
	}
}
