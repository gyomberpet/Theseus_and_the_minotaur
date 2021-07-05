package things.powerups;


import map.Field;
import map.Maze;
import things.Thing;
import things.characters.Theseus;

/**
 * A PowerUp egy abstarct oszt�ly, ami minden k�pess�gn�vel� Dolgot magaba foglal.
 * Lesz�rmazottja a Dolog �soszt�lynak.
 */
public abstract class PowerUp extends Thing{
	private int timeToLive = 5000;
	public PowerUp(Maze maze, Field currentField, String ID) {
		super(maze, currentField, 0, ID);
	}
	
	/**
	 * Frissiti a PowerUp �llapot�t.
	 */
	public void update() {
		reduceTimeToLive();
	}
	
	/**
	 * Cs�kkenti a timeToLive �rt�k�t, ami az �letidej�t hat�rozza meg.
	 * Ha �letideje el�ri a null�t, elt�nteti magat a j�t�kb�l.
	 */
	public void reduceTimeToLive() {
		timeToLive--;
		if(timeToLive==0) {
			getCurrentField().removeThing(this);
		}
	}
	
	/**
	 * Ha Theseus nekimegy, elt�nteti mag�t a j�t�kb�l.
	 */
	public void hitBy(Theseus t) {
		getCurrentField().removeThing(this);
	}
}
