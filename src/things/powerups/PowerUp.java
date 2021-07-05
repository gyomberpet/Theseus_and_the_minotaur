package things.powerups;


import map.Field;
import map.Maze;
import things.Thing;
import things.characters.Theseus;

/**
 * A PowerUp egy abstarct osztály, ami minden képességnövelõ Dolgot magaba foglal.
 * Leszármazottja a Dolog õsosztálynak.
 */
public abstract class PowerUp extends Thing{
	private int timeToLive = 5000;
	public PowerUp(Maze maze, Field currentField, String ID) {
		super(maze, currentField, 0, ID);
	}
	
	/**
	 * Frissiti a PowerUp állapotát.
	 */
	public void update() {
		reduceTimeToLive();
	}
	
	/**
	 * Csökkenti a timeToLive értékét, ami az életidejét határozza meg.
	 * Ha életideje eléri a nullát, eltünteti magat a játékból.
	 */
	public void reduceTimeToLive() {
		timeToLive--;
		if(timeToLive==0) {
			getCurrentField().removeThing(this);
		}
	}
	
	/**
	 * Ha Theseus nekimegy, eltünteti magát a játékból.
	 */
	public void hitBy(Theseus t) {
		getCurrentField().removeThing(this);
	}
}
