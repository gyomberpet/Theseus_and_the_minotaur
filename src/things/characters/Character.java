package things.characters;

import java.awt.Graphics;

import map.Direction;
import map.Field;
import map.Maze;
import things.Thing;

/**
 * A Karakter egy abstract osztály, ami szintén leszármazik az abstract Dolog osztályból.
 * Feladata, hogy a ténylegesen élõ egységeket együtt lehessen kezelni, illetve könnyen bövíteni azokat.
 * A legtobb atributumát örokli, de kibõvül az eleterõ és isAlive változókkal.
 */
public abstract class Character extends Thing{
	private int hp;	
	private boolean isAlive;
	public Character(Maze maze, Field currentField, int speed, int hp, String ID) {
		super(maze, currentField, speed, ID);
		this.hp = hp;
		this.isAlive = true;
	}
	
	/**
	 * Visszaadja isAlive értékét.
	 */
	public boolean getIsAlive() {
		return isAlive;
	}
	
	/**
	 * Ez a metodus fut le, amikor meghal egy Karakter.
	 * Az isAlive hamisra állítódik, valamint mozgási képességét is elveszti.
	 */
	public void die() {
		isAlive = false;
		setIsMoving(false);
	}
	
	/**
	 * Visszaad egy Stringet, ami kódolja a Karakter állapotát.
	 * A String elsõ karaktere a Karakter egybetûs azonosítója,
	 * a második karaktere pedig az (egyjegyû) életereje (pl.: M2).
	 */
	public String codeID() {
		return getID()+hp;
	}
	
	/**
	 * Visszaadja a Karakter aktuális életerejét.
	 */
	public int getHp() {
		return hp;
	}
	
	/**
	 * A Karakter életerejét a paraméterül kapott értékre állítja, feltéve, hogy nem lesz nagyobb, mint 9.
	 * Ha az így beállított érték nulla, vagy annál kisebb, a Karakter meghal.
	 */
	public void setHp(int h) {
		if(h>=10) {
			return;
		}
		hp=h;
		if(hp<=0) {
			die();
		}
	}
	
	/**
	 * A Karakter életerejét egy értékkel növeli, feltéve, hogy az így nem lesz nagyobb, mint 9.
	 */
	public void addHp() {
		if(hp>=9) {
			return;
		}
		hp++;
	}
	
	/**
	 * A Karakter életerejét egy értékkel csokkenti.
	 * Ha az így lecsökkent életerõ nulla, vagy annál kisebb, a Karakter meghal.
	 */
	public void reduceHp() {
		hp--;
		if(hp<=0) {
			die();
		}
	}
	
	/**
	 * Abstarct metódus, amit minden Karakter leszarmazottnak meg kell valósítania.
	 * A paraméterül kapott irányba próbálja meg léptetni az adott Karaktert, ami kulonbozo logikak alapjan valósulhat meg.
	 */
	abstract public void move(Direction d) ;
	
	/**
	 * Abstarct metódus, amit minden Karakter leszarmazottnak meg kell valósítania.
	 * A Karakter mozgásához szükséges animáció meghatározása a feladata.
	 * Visszatéris értéke egy egész szám, ami az aktuálisan kirajzolandó képkocka sorszáma.
	 */
	abstract public int moveAnimation();
	
	/**
	 * Abstarct metódus, amit minden Karakter leszarmazottnak meg kell valósítania.
	 * Kirajzolja a Karaktert a képernyõre.
	 */
	abstract public void draw(Graphics g);
}
