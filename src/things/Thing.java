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
 * A j�t�kban l�v� Dolgok abstract oszt�lya.
 * Minden Dolog ebb�l szarmazik, �gy rendelkezik a Dolog tulajdons�gaival, amit a j�t�kban elv�runk.
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
	 * Visszaad egy Stringet, ami azonos�tja a Dolgot.
	 * N�h�ny leszarmazott osztaly feluldefini�lja.
	 */
	public String codeID() {
		return ID;
	}
	
	/**
	 * Visszaadja a k�vetkez� Mez�t.
	 */
	public Field getNextField() {
		return nextField;
	}
	
	/**
	 * Be�ll�tja a k�vetkez� Mez�t.
	 */
	public void setNextField(Field f) {
		nextField = f;
	}
	
	/**
	 * Visszaadja az animaci�sz�ml�l� aktu�is �rt�k�t.
	 */
	public int getAnimationCounter() {
		return animationCounter;
	}
	
	/**
	 * Be�ll�tja az animaci�sz�ml�l� aktu�is �rt�k�t.
	 */
	public void setAnimationCounter(int ac) {
		animationCounter = ac;
	}
	/**
	 * Visszaadja az Ir�ny�t.
	 */
	public Direction getDirection(){
		return direction;
	}
	
	/**
	 * Be�ll�tja az Ir�ny�t.
	 */
	public void setDirection(Direction d){
		direction = d;
	}
	
	/**
	 * Visszaadja az aktu�lis Mez�j�t.
	 */
	public Field getCurrentField(){
		return currentField;
	}
	
	/**
	 * Be�ll�tja az aktu�lis Mez�j�t.
	 */
	public void setCurrentField(Field f){
		currentField = f;
	}
	
	/**
	 * Visszaadja az azonos�t�j�t egy Stringk�nt.
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
	 * Igaz �rt�kkel t�r vissza, ha a Dolog �ppen mozog.
	 */
	public boolean getIsMoving() {
		return isMoving;
	}
	
	/**
	 * Be�ll�tja isMoving �rt�k�t.
	 */
	public void setIsMoving(boolean b) {
		isMoving = b;
	}
	
	/**
	 * Visszaadja a sebess�get.
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Be�ll�tja a sebess�get.
	 */
	public void setSpeed(int s) {
		speed = s;
	}
	
	/**
	 * Visszaadja a poz�ci�j�nak X koordin�t�j�t.
	 */
	public int getPosX() {
		return posX;
	}
	
	/**
	 * Visszaadja a poz�ci�j�nak Y koordin�t�j�t.
	 */
	public int getPosY() {
		return posY;
	}
	
	/**
	 * Friss�tii a dolog �llapot�t.
	 * Amennyiben lehets�ges, a Dolgot az Ir�ny�nak megfelel�en a sebess�g�vel ar�nyosan v�ltoztatja a poz�ci�j�t.
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
	 * Ez h�v�dik, ha egy Dolognak Theseus �tk�zik.
	 */
	public void hitBy(Theseus t) {}
	
	/**
	 * Ez h�v�dik, ha egy Dolognak Minotaurusz �tk�zik.
	 */
	public void hitBy(Minotaur  m) {}
	
	/**
	 * Ez h�v�dik, ha egy Dolognak D�rda �tk�zik.
	 */
	public void hitBy(Spear  s) {}
	
	/**
	 * Ez h�v�dik, ha egy Dolognak egy PowerUp �tk�zik.
	 */
	public void hitBy(PowerUp  p) {}
	
	/**
	 * Ez h�v�dik, ha k�t Dolog �tk�zik egym�ssal.
	 */
	public void collideWith(Thing t) {}
	
	/**
	 * Kirajzolja a Dolgot.
	 * Minden dolognak meg kell valositania ezt az abstract met�dust.
	 */
	abstract public void draw(Graphics g);
}
