package things.weapons;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import map.Direction;
import map.Field;
import game.saveload.LoadImages;
import map.Maze;
import things.Thing;
import things.characters.Minotaur;

/**
 * Egy Dárdát reprezentál a játékban.
 * Theseus hozhatja létre, melynek hatására elkezd repülni, míg valaminek neki nem ütközik.
 * Leszármazottja a Dolog õsosztálynak.
 */
public class Spear extends Thing{
	private boolean isReachedEnemy;
	public Spear(Maze maze, Field currentField) {
		super(maze, currentField, 8, "A");
		isReachedEnemy = false;
	}
	
	/**
	 * Az isReachededEnemy értékét igazra állítja, ha eltalálta az ellenfelét.
	 */
	public void isReachedEnemy(){
		isReachedEnemy  = true;
	}
	
	/**
	 * Ha egy Dolognak ütközik, szól neki, hogy ütközött vele.
	 */
	public void collideWith(Thing t) {
		t.hitBy(this);
	}
	
	/**
	 * Ha a Dárdának Minotaurusz ütközik, csökkenti a Minotaurusz életerejét, majd jelzi maganak, hogy célba talált.
	 * Tipikusan azért van rá szükség, mert elõfordulhat, hogy a Dárda az adott Mezõn van éppen, mikor a Minotaurusz rálép.
	 * Ez elõfordulhat, hiszen mindketten mozognak.
	 */
	public void hitBy(Minotaur  m) {
		m.reduceHp();
		isReachedEnemy();
	}
	
	/**
	 * A Dárda az adott Irányba megy, ameddig falba nem ütközik.
	 * Ez elõfordulhat úgy is, hogy rögtön falnak próbálják dobni.
	 */
	public void move(Direction d) {
		setDirection(d);
		if(getCurrentField().getWall()) {
			getCurrentField().removeThing(this);
			setIsMoving(false);
			return;
		} 
		setNextField(getCurrentField().getNeighbor(d));
		if(getNextField().getWall()) {
			getCurrentField().removeThing(this);
			setIsMoving(false);
			return;
		}
		setIsMoving(true);
	}
	
	/**
	 * Frissíti az állapotát a mozgásának megfelelõen, feltéve, hogy nem ütkozik közben ellenségnek.
	 */
	public void update() {
		move(getDirection());
		super.update();
		if(getPosX()==getNextField().getPosX() && getPosY()==getNextField().getPosY()) {
			getCurrentField().removeThing(this);
			setCurrentField(getNextField());
			if(!getCurrentField().getNeighbor(getDirection()).getWall() && !isReachedEnemy)
				getCurrentField().addThing(this);
			setIsMoving(false);
		}
		
	}
	
	/**
	 * Kirajzolja a Dárdát, az adott Iránynak megfelelõen.
	 */
	public void draw(Graphics g) {
		int size=LoadImages.size;
		int x = 0;
		switch (getDirection()) {
			case UP: x = 0;  break;
			case LEFT:  x = 3; break;
			case DOWN: x = 2; break;
			case RIGHT: x = 1; break;
		}
		BufferedImage image = LoadImages.images.get("Spear");
		g.drawImage(image,getPosX(),getPosY(),getPosX()+size,getPosY()+size,x*size,0,(x+1)*size,size,null);
	}
}
