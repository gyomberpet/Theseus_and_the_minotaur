package things.characters;

import java.awt.Graphics;

import map.Direction;
import map.Field;
import map.Maze;
import things.Thing;

/**
 * A Karakter egy abstract oszt�ly, ami szint�n lesz�rmazik az abstract Dolog oszt�lyb�l.
 * Feladata, hogy a t�nylegesen �l� egys�geket egy�tt lehessen kezelni, illetve k�nnyen b�v�teni azokat.
 * A legtobb atributum�t �rokli, de kib�v�l az eleter� �s isAlive v�ltoz�kkal.
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
	 * Visszaadja isAlive �rt�k�t.
	 */
	public boolean getIsAlive() {
		return isAlive;
	}
	
	/**
	 * Ez a metodus fut le, amikor meghal egy Karakter.
	 * Az isAlive hamisra �ll�t�dik, valamint mozg�si k�pess�g�t is elveszti.
	 */
	public void die() {
		isAlive = false;
		setIsMoving(false);
	}
	
	/**
	 * Visszaad egy Stringet, ami k�dolja a Karakter �llapot�t.
	 * A String els� karaktere a Karakter egybet�s azonos�t�ja,
	 * a m�sodik karaktere pedig az (egyjegy�) �letereje (pl.: M2).
	 */
	public String codeID() {
		return getID()+hp;
	}
	
	/**
	 * Visszaadja a Karakter aktu�lis �leterej�t.
	 */
	public int getHp() {
		return hp;
	}
	
	/**
	 * A Karakter �leterej�t a param�ter�l kapott �rt�kre �ll�tja, felt�ve, hogy nem lesz nagyobb, mint 9.
	 * Ha az �gy be�ll�tott �rt�k nulla, vagy ann�l kisebb, a Karakter meghal.
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
	 * A Karakter �leterej�t egy �rt�kkel n�veli, felt�ve, hogy az �gy nem lesz nagyobb, mint 9.
	 */
	public void addHp() {
		if(hp>=9) {
			return;
		}
		hp++;
	}
	
	/**
	 * A Karakter �leterej�t egy �rt�kkel csokkenti.
	 * Ha az �gy lecs�kkent �leter� nulla, vagy ann�l kisebb, a Karakter meghal.
	 */
	public void reduceHp() {
		hp--;
		if(hp<=0) {
			die();
		}
	}
	
	/**
	 * Abstarct met�dus, amit minden Karakter leszarmazottnak meg kell val�s�tania.
	 * A param�ter�l kapott ir�nyba pr�b�lja meg l�ptetni az adott Karaktert, ami kulonbozo logikak alapjan val�sulhat meg.
	 */
	abstract public void move(Direction d) ;
	
	/**
	 * Abstarct met�dus, amit minden Karakter leszarmazottnak meg kell val�s�tania.
	 * A Karakter mozg�s�hoz sz�ks�ges anim�ci� meghat�roz�sa a feladata.
	 * Visszat�ris �rt�ke egy eg�sz sz�m, ami az aktu�lisan kirajzoland� k�pkocka sorsz�ma.
	 */
	abstract public int moveAnimation();
	
	/**
	 * Abstarct met�dus, amit minden Karakter leszarmazottnak meg kell val�s�tania.
	 * Kirajzolja a Karaktert a k�perny�re.
	 */
	abstract public void draw(Graphics g);
}
