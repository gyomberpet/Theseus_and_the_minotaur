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
 * A játékban egy Mezõt reprezentál.
 * A labirintus Mezõkbõl épül fel, melyeken az egyes Dolgok elhelyezkedhetnek.
 * A Mezõ lehet fal (ha fal, lehet növényes vagy sima), tárolja a négy különbözõ irányban lévõ szomszédját (ha van),
 * a sajat (X,Y) koordinátaját és a rajta lévõ Dolgok listáját.
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
	 * A Mezõ értékét "fal"-ra állítja.
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
	 * Visszaadja a Mezõn lévõ összes Dolgot egy listában.
	 */
	public List<Thing> getThings() {
		return things;
	}
	
	/**
	 * Ha a Mezõ egy fal, igaz értekkel tér vissza.
	 */
	public boolean getWall() {
		return isWall;
	}
	
	/**
	 * A paraméterként kapott irányban lévõ szomszédját a kapott Mezõre állítja.
	 */
	public void setNeighbor(Direction d, Field field) {
		neighbor.put(d, field);
	}
	
	/**
	 * Visszaadja a kapott irányban lévõ szomszédját.
	 */
	public Field getNeighbor(Direction d) {
		return neighbor.get(d);
	}
	
	/**
	 * A Mezõ az ablakban elfoglalt tényleges pozíciójának X koordinátáját adja vissza (bal felsõ sarok).
	 */
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Beállítja a Mezõ X pozícióját a paraméterként kapott értékre.
	 */
	public void setPosX(int x) {
		posX = x;
	}
	
	/**
	 * A Mezõ az ablakban elfoglalt tényleges pozíciójának Y koordinátáját adja vissza (bal felsõ sarok).
	 */
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Beállítja a Mezõ Y pozícióját a paraméterként kapott értékre.
	 */
	public void setPosY(int y) {
		posY = y;
	}
	
	/**
	 * A paraméterként kapott Dolgot hozzéadja a Mezõn tárolt dolgokhoz.
	 * Elõtte minden, már rajta lévõ dologgal ütközteti.
	 */
	public void addThing(Thing t) {
		for(int i = 0; i < things.size(); ++i) {
			t.collideWith(things.get(i));
		}
		things.add(t);
	}
	
	/**
	 * A paraméterként kapott Dolgot törli a rajta lévõ Dolgok közül.
	 */
	public void removeThing(Thing t) {
		things.remove(t);
	}
	
	/**
	 * Igaz értékkel tér vissza, ha a Mezõn egyetlen Dolog sincs.
	 */
	public boolean getIsEmpty() {
		return things.size()==0;
	}
	
	/**
	 * Igaz értekkel tér vissza, ha a Mezõ egy keresztezõdés.
	 * A jatekban minden Mezõ keresztezõdésnek tekinthetõ, amelynek legalább 3 olyan szomszédja van, ami nem fal.
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
	 * Kirajzolja a Mezõt.
	 * Ha a Mezõ nem fal, füvet rajzol rá.
	 * Ha a Mezõ fal, attól függõen hogy milyen fal (növényes vagy sima), úgy rajzolja ki.
	 * A (0,1)-es Mezõre egy hercegnõt rajzol, aki tartja Theseus fonalának a végét.
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
	 * A rajta lévõ össze Dolognak meghívja a kirajzoló függvényét, így megjelenítve azokat a Mezõn.
	 */
	public void drawThings(Graphics g) {
		for(Thing t: things) {
			t.draw(g);
		}
	}
}
