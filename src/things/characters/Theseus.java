package things.characters;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import map.Direction;
import map.Field;
import game.saveload.LoadImages;
import map.Maze;
import things.powerups.DurationPowerUp;
import path.Path;
import things.weapons.Spear;
import things.Thing;


/**
 * A játékos Theseust iranyítja a Labirintusban.
 * A Karakter leszármazottja.
 * Theseus képes mozogni, illetve Dárdát hajítani.
 * Célja, hogy megölje a Minotauruszt, de közben vigyáznia kell, hisz meg is halhat tõle.
 */
public class Theseus extends Character{
	private final int rechargeDelay = 50;
	private int rechargeCounter;
	private boolean canAttack;
	private Path path;
	private List<DurationPowerUp> durationPowerUps;
	public Theseus(Maze maze, Field currentField) {
		super(maze, currentField, 2, 3, "T");
		this.rechargeCounter = 0;
		this.canAttack = true;
		this.path = new Path(currentField, getDirection());
		maze.setTheseus(this);
		this.durationPowerUps = new ArrayList<>();
	}
	
	/**
	 * Felüldefiniálja a Karakter codeID() metódusát, mivel az ID és a hp mellé az Ösvényének a kódját is hozzáteszi,
	 * majd ezt adja vissza egy Stringként. 
	 */
	public String codeID() {
		return super.codeID()+path.toString();
	}
	
	/**
	 * Hozzáad egy DurationPowerUp-ot a DurationPowerup-ok listájához.
	 */
	public void addPowerUp(DurationPowerUp p) {
		durationPowerUps.add(p);
	}
	
	/**
	 * Törli a paraméterül kapott DurationPowerUp-ot a DurationPowerUp-ok listájából.
	 */
	public void removePowerUp(DurationPowerUp p) {
		durationPowerUps.remove(p);
	}
	
	/**
	 * Visszaadja Theseus Ösvényét.
	 */
	public Path getPath() {
		return path;
	}
	
	/**
	 * Beállítja Theseus Ösvényét.
	 * Fõként a mentésnél fontos.
	 */
	public void setPath(Path p) {
		path = p;
	}
	
	/**
	 * Akkor hívódik, ha Theseus egy Dolognak ütközik.
	 * Ekkor szól a Dolognak, hogy nekiment.
	 */
	public void collideWith(Thing t) {
		t.hitBy(this);
	}
	
	/**
	 * Amennyiben éppen lehetséges, letrehoz egy Dárdát az aktualis Irányának megfelelõ Irányban.
	 */
	public void attack() {
		if(!canAttack || !getIsAlive()) {
			return;
		}
		Field spearStartField = getCurrentField().getNeighbor(getDirection());
		Spear spear = new Spear(getMaze(),spearStartField);
		spearStartField.addThing(spear);
		spear.move(getDirection());
		canAttack = false;
		rechargeCounter = 0;
	}
	
	/**
	 * Ha Theseusnak Minotaurusz ütközik, csökken az életereje eggyel.
	 */
	public void hitBy(Minotaur m) {
		if(!m.getIsAlive()) {
			return;
		}
		reduceHp();
	}
	
	/**
	 * Theseus az adott Irányba próbál mozogni.
	 * Ha olyan Irányba mozog, amerre nincsen fal, addig nem tud masik Irányba lépni,
	 * amíg a másik Mezõre át nem ért.
	 */
	public void move(Direction d) {
		if(!getIsAlive()) {
			return;
		}
		if(getCurrentField()==getNextField() ) {
			setDirection(d);
			if(!getCurrentField().getNeighbor(d).getWall()) {
				setNextField(getCurrentField().getNeighbor(d));
				setIsMoving(true);
			}
		}
	}
	
	/**
	 * Frissíti Theseus állapotát.
	 * Ha Theseus életben van, az adott Irányba képes, mozogni.
	 * Növeli a rechargeCounter értékét.
	 * Mozgas során változtatja az Ösvényét.
	 * csökkenti az összes DurationPowerUp-jának az élettartamát, illetve szól annak, amelyik lejárt.
	 */
	public void update(){
		if(!getIsAlive()) {
			return;
		}
		super.update();
		if(getPosX()==getNextField().getPosX() && getPosY()==getNextField().getPosY() && getIsMoving()) {
			getCurrentField().removeThing(this);			
			setCurrentField(getNextField());
			path.add(getCurrentField(),getDirection());
			getCurrentField().addThing(this);
			setIsMoving(false);
		}
		rechargeCounter++;
		if(rechargeCounter==rechargeDelay) {
			canAttack=true;
		}
		for(int i = 0; i < durationPowerUps.size(); ++i) {
			durationPowerUps.get(i).reduceDurationTime();
			if(durationPowerUps.get(i).getDurationTime()==0) {
				durationPowerUps.get(i).finished(this);
			}
		}
	}
	
	/**
	 * Theseus mozgásának az animációját határozza meg, illetve visszaadja az annak megfelelõ sorszámot.
	 */
	public int moveAnimation() {
		if(!getIsMoving())
			return 0;
		setAnimationCounter(getAnimationCounter() + 1);
		int animationSpeed = 16 ;
		int state = getAnimationCounter()/animationSpeed;
		if(getAnimationCounter() == 4 * animationSpeed) {
			setAnimationCounter(0);
		}
		return state;
	}
	
	/**
	 * Kirajzolja Theseust, az animációjának megfelelõen.
	 */
	public void draw(Graphics g) {
		int size=LoadImages.size;
		int y = 0;
		switch (getDirection()) {
			case UP: y = 2;  break;
			case LEFT:  y = 1; break;
			case DOWN: y = 0; break;
			case RIGHT: y = 3; break;
		}
		int x = moveAnimation();
		BufferedImage image = LoadImages.images.get("Theseus");
		g.drawImage(image,getPosX(),getPosY(),getPosX()+size,getPosY()+size,2*x*size,2*y*size+(y+1)*4,2*(x+1)*size,2*(y+1)*size+(y+1)*4,null);
	}
}
