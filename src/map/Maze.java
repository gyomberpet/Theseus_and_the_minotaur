package map;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import game.saveload.LoadImages;
import things.powerups.LifePowerUp;
import things.characters.Minotaur;
import things.powerups.SpeedPowerUp;
import things.characters.Theseus;
import things.Thing;

/**
 * A Labirintus Mezõkbõl épül fel, benne mozognak az egyes Dolgok.
 */
public class Maze {
	public Field[][] fields;
	private int width;
	private int height;
	private Theseus theseus;
	private Minotaur minotaur;
	private int powerUpChance = 3000;
	public Maze(int w, int h) {
		this.width = w;
		this.height = h;
		if(width%2!=1) width -=1;
		if(height%2!=1) height -=1;
		if(width<13) width = 13;
		if(height<13) height =13;
		if(width>51) width = 51;
		if(height>51) height =51;
		fields= new Field[height][width];
	}
	/**
	 * A kezdetben üres Labirintust tényleges Labirintussá alakítja.
	 * Kialakítja a járatokat az (1,1)-es Mezõtõl indulva.
	 * Hozzáad magához egy Theseust (1,1)-re es egy Minotauruszt (valamely másik véletlen Mezõre).
	 */
	public void init() {
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				fields[y][x]=new Field(y,x);
				addWall(y,x);
			}
		}
		generateLevel(1,1);
		setAllNeighbors();
		Field theseusField = getStartField();
		theseus =new Theseus(this, theseusField);
		theseusField.addThing(theseus);
		Field minotaurField = getRandomEmptyField();
		minotaur = new Minotaur(this, minotaurField);
		minotaurField.addThing(minotaur);
	}
	
	/**
	 * Visszadja a Mezõk mátrixából a kapott sor és oszlop által meghatározott Mezõt.
	 */
	public Field getField(int y, int x) {
		return fields[y][x];
	}
	
	/**
	 * A Mezõ mátrxát beallítja a paraméterként kapott Mezõk mátrixára.
	 */
	public void setFields(Field[][] newFields) {
		fields = newFields;
	}
	
	/**
	 * Visszaadja a szélességet.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Visszaadja a magasságot.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Visszaadja Theseust.
	 */
	public Theseus getTheseus() {
		return theseus;
	}
	
	/**
	 * Visszaadja a Minotauruszt.
	 */
	public Minotaur getMinotaur() {
		return minotaur;
	}
	
	/**
	 * Véletlenszerûen hozzáad egy tetszõleges PowerUp-ot egy véletlen (üres) Mezõhoz.
	 */
	public void addPowerUp() {
		Random r = new Random();
		int add = r.nextInt(powerUpChance);
		if(add!=0) {
			return;
		}
		Field f1 = getRandomEmptyField();
		int powerUpNumber = r.nextInt(2);
		if(powerUpNumber==0) {
			f1.addThing(new LifePowerUp(this,f1));
		} else if(powerUpNumber==1) {
			f1.addThing(new SpeedPowerUp(this,f1));
		}
	}
	
	/**
	 * Beállítja Theseust.
	 */
	public void setTheseus(Theseus t) {
		theseus = t;
	}
	
	/**
	 * Beállítja a Minotauruszt.
	 */
	public void setMinotaur(Minotaur m) {
		minotaur = m;
	}
	
	/**
	 * Visszaadja a Mezõt, ahonnan Theseus indul.
	 */
	public Field getStartField() {
		return fields[1][1];
	}
	
	/**
	 * Egy véletlenszerûen választott üres (nem fal) Mezõt ad vissza.
	 */
	public Field getRandomEmptyField() {
		List<Field> reachableFields= new ArrayList<>();
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x< width; ++x) {
				if(!fields[y][x].getWall() && fields[y][x].getIsEmpty())
					reachableFields.add(fields[y][x]);
			}
		}
		Collections.shuffle(reachableFields);
		return reachableFields.get(0);
	}
	
	/**
	 * A paraméterként kapott koordinátak alapján visszaadja az azon lévõ Mezõjet.
	 */
	public Field getFieldAtPosition(int posY, int posX) {
		int y = posY/LoadImages.size;
		int x = posX/LoadImages.size;
		return fields[y][x];
	}
	
	/**
	 * Az össze Mezõjét, majd az azokon lévõ össze Dolgot kirajzolja.
	 */
	public void draw(Graphics g) {
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x< width; ++x) {
				fields[y][x].draw(g);
			}
		}
		theseus.getPath().draw(g);
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x< width; ++x) {
				fields[y][x].drawThings(g);
			}
		}
	}
	
	/**
	 * Visszaadja a Mezõi által tartalmazott összes Dolog listáját.
	 */
	public List<Thing> getAllThing(){
		List<Thing> allThings = new ArrayList<>();
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x< width; ++x) {
				for(Thing t: fields[y][x].getThings())
					allThings.add(t);
			}
		}
		return allThings;
	}
	
	/**
	 * A paraméter által meghatározott Mezõját "fal"-ra állítja.
	 */
	public void addWall(int y, int x) {
		fields[y][x].setWall(true);
	}
	
	/**
	 * A paraméter által meghatározott Mezõjet "fal nelküli"-re állítja.
	 */
	public void destroyWall(int y, int x) {
		fields[y][x].setWall(false);
	}
	
	/**
	 * A Mezõk mátrixában lévõ összes Mezõnek beállítja az egyes irányokban lévõ szomszédjait.
	 */
	public void setAllNeighbors() {
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x< width; ++x) {
				if(x < width-1) {
					fields[y][x].setNeighbor(Direction.RIGHT, fields[y][x+1]);
				} if(x >= 1) {
					fields[y][x].setNeighbor(Direction.LEFT, fields[y][x-1]);
				} if(y < height-1) {
					fields[y][x].setNeighbor(Direction.DOWN, fields[y+1][x]);
				} if(y >=1) {
					fields[y][x].setNeighbor(Direction.UP, fields[y-1][x]);
				}
			}
		}
	}
	
	/**
	 * Rekurzívan legenerálja a labirintust, a kapott kezdõpontból indulóan.
	 * Azaz a már korábban feltöltött falakba fúr megfelelõ járatokat.
	 */
	public void generateLevel(int y, int x) {
		destroyWall(y,x);
		ArrayList<Direction> directions = new ArrayList<Direction>();
		directions.add(Direction.RIGHT);
		directions.add(Direction.LEFT);
		directions.add(Direction.UP);
		directions.add(Direction.DOWN);
		Collections.shuffle(directions);
		for(Direction d: directions) {
			switch (d) {
				case RIGHT:
					if(x < width-2 && fields[y][x+2].getWall()) {
						destroyWall(y,x+1);
						generateLevel(y,x+2);
					} break;
				case LEFT:
					if(x >= 2 && fields[y][x-2].getWall()) {
						destroyWall(y,x-1);
						generateLevel(y,x-2);
					} break;
				case UP:
					if(y >= 2 && fields[y-2][x].getWall()) {
						destroyWall(y-1,x);
						generateLevel(y-2,x);
					} break;
				case DOWN:
					if(y < height-2 && fields[y+2][x].getWall()) {
						destroyWall(y+1,x);
						generateLevel(y+2,x);
					} break;
			}
		}
	}
}
