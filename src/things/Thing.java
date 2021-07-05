package things;

import java.awt.Graphics;

import map.Direction;
import map.Field;
import game.saveload.LoadImages;
import map.Maze;
import things.characters.Minotaur;
import things.characters.Theseus;
import things.powerups.PowerUp;
import things.weapons.Spear;

/**
 * A játékban lévõ Dolgok abstract osztálya.
 * Minden Dolog ebbõl szarmazik, így rendelkezik a Dolog tulajdonságaival, amit a játékban elvárunk.
 */
public abstract class Thing{
	private String ID;
	private Field currentField;
	private int posX;
	private int posY;
	private Direction direction;
	private Maze maze;
	private int speed;
	private Field nextField;
	private boolean isMoving;
	private int animationCounter;
	public Thing(Maze maze, Field currentField, int speed, String ID) {
		this.posX = currentField.getPosX();
		this.posY = currentField.getPosY();
		this.direction = Direction.DOWN;
		this.speed = speed;
		this.currentField = currentField;
		this.nextField = currentField;
		this.maze = maze;
		this.isMoving = false;
		this.animationCounter = 0;
		this.ID = ID;
	}
	
	
	/**
	 * Visszaad egy Stringet, ami azonosítja a Dolgot.
	 * Néhány leszarmazott osztaly feluldefiniálja.
	 */
	public String codeID() {
		return ID;
	}
	
	/**
	 * Visszaadja a következõ Mezõt.
	 */
	public Field getNextField() {
		return nextField;
	}
	
	/**
	 * Beállítja a következõ Mezõt.
	 */
	public void setNextField(Field f) {
		nextField = f;
	}
	
	/**
	 * Visszaadja az animaciószámláló aktuáis értékét.
	 */
	public int getAnimationCounter() {
		return animationCounter;
	}
	
	/**
	 * Beállítja az animaciószámláló aktuáis értékét.
	 */
	public void setAnimationCounter(int ac) {
		animationCounter = ac;
	}
	/**
	 * Visszaadja az Irányát.
	 */
	public Direction getDirection(){
		return direction;
	}
	
	/**
	 * Beállítja az Irányát.
	 */
	public void setDirection(Direction d){
		direction = d;
	}
	
	/**
	 * Visszaadja az aktuális Mezõjét.
	 */
	public Field getCurrentField(){
		return currentField;
	}
	
	/**
	 * Beállítja az aktuális Mezõjét.
	 */
	public void setCurrentField(Field f){
		currentField = f;
	}
	
	/**
	 * Visszaadja az azonosítóját egy Stringként.
	 */
	public String getID() {
		return ID;
	}
	
	/**
	 * Visszaadja a Labirintust.
	 */
	public Maze getMaze() {
		return maze;
	}
	
	/**
	 * Igaz értékkel tér vissza, ha a Dolog éppen mozog.
	 */
	public boolean getIsMoving() {
		return isMoving;
	}
	
	/**
	 * Beállítja isMoving értékét.
	 */
	public void setIsMoving(boolean b) {
		isMoving = b;
	}
	
	/**
	 * Visszaadja a sebességet.
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Beállítja a sebességet.
	 */
	public void setSpeed(int s) {
		speed = s;
	}
	
	/**
	 * Visszaadja a pozíciójának X koordinátáját.
	 */
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Visszaadja a pozíciójának Y koordinátáját.
	 */
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Frissítii a dolog állapotát.
	 * Amennyiben lehetséges, a Dolgot az Irányának megfelelõen a sebességével arányosan változtatja a pozícióját.
	 */
	public void update() {
		if(!isMoving) {			
			return;
		}
		switch (direction) {
			case UP: if(!(maze.getFieldAtPosition(posY-speed,posX).getWall() ||
					maze.getFieldAtPosition(posY-speed,posX+LoadImages.size-1).getWall())) {
				posY -= speed;
			} break;
			case LEFT: if(!(maze.getFieldAtPosition(posY,posX-speed).getWall() ||
					maze.getFieldAtPosition(posY+LoadImages.size-1,posX-speed).getWall())) {
				posX -= speed; 
			} break;
			case DOWN: if(!(maze.getFieldAtPosition(posY+LoadImages.size,posX).getWall() ||
					maze.getFieldAtPosition(posY+LoadImages.size,posX+LoadImages.size-1).getWall())) {
				posY += speed;
			} break;
			case RIGHT: if(!(maze.getFieldAtPosition(posY,posX+LoadImages.size).getWall() ||
					maze.getFieldAtPosition(posY+LoadImages.size-1,posX+LoadImages.size).getWall())) {
				posX += speed;
			} break;
		}	
	}
	
	/**
	 * Ez hívódik, ha egy Dolognak Theseus ütközik.
	 */
	public void hitBy(Theseus t) {}
	
	/**
	 * Ez hívódik, ha egy Dolognak Minotaurusz ütközik.
	 */
	public void hitBy(Minotaur  m) {}
	
	/**
	 * Ez hívódik, ha egy Dolognak Dárda ütközik.
	 */
	public void hitBy(Spear  s) {}
	
	/**
	 * Ez hívódik, ha egy Dolognak egy PowerUp ütközik.
	 */
	public void hitBy(PowerUp  p) {}
	
	/**
	 * Ez hívódik, ha két Dolog ütközik egymással.
	 */
	public void collideWith(Thing t) {}
	
	/**
	 * Kirajzolja a Dolgot.
	 * Minden dolognak meg kell valositania ezt az abstract metódust.
	 */
	abstract public void draw(Graphics g);
}
