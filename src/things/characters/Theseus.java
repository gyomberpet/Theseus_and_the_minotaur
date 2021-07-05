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
 * A j�t�kos Theseust irany�tja a Labirintusban.
 * A Karakter lesz�rmazottja.
 * Theseus k�pes mozogni, illetve D�rd�t haj�tani.
 * C�lja, hogy meg�lje a Minotauruszt, de k�zben vigy�znia kell, hisz meg is halhat t�le.
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
	 * Fel�ldefini�lja a Karakter codeID() met�dus�t, mivel az ID �s a hp mell� az �sv�ny�nek a k�dj�t is hozz�teszi,
	 * majd ezt adja vissza egy Stringk�nt. 
	 */
	public String codeID() {
		return super.codeID()+path.toString();
	}
	
	/**
	 * Hozz�ad egy DurationPowerUp-ot a DurationPowerup-ok list�j�hoz.
	 */
	public void addPowerUp(DurationPowerUp p) {
		durationPowerUps.add(p);
	}
	
	/**
	 * T�rli a param�ter�l kapott DurationPowerUp-ot a DurationPowerUp-ok list�j�b�l.
	 */
	public void removePowerUp(DurationPowerUp p) {
		durationPowerUps.remove(p);
	}
	
	/**
	 * Visszaadja Theseus �sv�ny�t.
	 */
	public Path getPath() {
		return path;
	}
	
	/**
	 * Be�ll�tja Theseus �sv�ny�t.
	 * F�k�nt a ment�sn�l fontos.
	 */
	public void setPath(Path p) {
		path = p;
	}
	
	/**
	 * Akkor h�v�dik, ha Theseus egy Dolognak �tk�zik.
	 * Ekkor sz�l a Dolognak, hogy nekiment.
	 */
	public void collideWith(Thing t) {
		t.hitBy(this);
	}
	
	/**
	 * Amennyiben �ppen lehets�ges, letrehoz egy D�rd�t az aktualis Ir�ny�nak megfelel� Ir�nyban.
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
	 * Ha Theseusnak Minotaurusz �tk�zik, cs�kken az �letereje eggyel.
	 */
	public void hitBy(Minotaur m) {
		if(!m.getIsAlive()) {
			return;
		}
		reduceHp();
	}
	
	/**
	 * Theseus az adott Ir�nyba pr�b�l mozogni.
	 * Ha olyan Ir�nyba mozog, amerre nincsen fal, addig nem tud masik Ir�nyba l�pni,
	 * am�g a m�sik Mez�re �t nem �rt.
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
	 * Friss�ti Theseus �llapot�t.
	 * Ha Theseus �letben van, az adott Ir�nyba k�pes, mozogni.
	 * N�veli a rechargeCounter �rt�k�t.
	 * Mozgas sor�n v�ltoztatja az �sv�ny�t.
	 * cs�kkenti az �sszes DurationPowerUp-j�nak az �lettartam�t, illetve sz�l annak, amelyik lej�rt.
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
	 * Theseus mozg�s�nak az anim�ci�j�t hat�rozza meg, illetve visszaadja az annak megfelel� sorsz�mot.
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
	 * Kirajzolja Theseust, az anim�ci�j�nak megfelel�en.
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
