package things.characters;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import map.Direction;
import map.Field;
import game.saveload.LoadImages;
import map.Maze;
import things.weapons.Spear;
import things.Thing;


/**
 * A Minotaurusz egy Karakter a j�t�kbn, ezert �r�k�dik Karakterb�l.
 * A Minotaurusz Thesesu ellens�ge, �t kell meg�lni a j�t�k megnyer�s�hez.
 */
public class Minotaur extends Character{
	public Minotaur(Maze maze, Field currentField) {
		super(maze, currentField, 2, 3,"M");
	}
	
	/**
	 * Akkor h�v�dik, amikor neki�tk�zik egy dolognak.
	 * Ekkor sz�l az adott Dolognak, hogy nekiment.  
	 */
	public void collideWith(Thing t) {
		t.hitBy(this);
	}
	
	/**
	 * Akkor h�v�dik, ha eltal�lt�k egy D�rd�val.
	 * Az �letereje eggyel cs�kken, �s visszasz�l a D�rd�nak, hogy eltal�lt valamit.
	 */
	public void hitBy(Spear  s) {
		reduceHp();
		s.isReachedEnemy();
	}
	
	/**
	 * Akkor h�v�dik, ha Theseus nekimegy.
	 * Ekkor cs�kkenti Theseus �leterej�t, felt�ve, hogy a Minotaurusz m�g �letben van.
	 */
	public void hitBy(Theseus t) {
		if(!getIsAlive()) {
			return;
		}
		t.reduceHp();
	}
	
	/**
	 * Az adott Ir�nyba pr�b�l l�pni egyet.
	 * Ez akkor t�rt�nhet meg, ha meg �letben van, illetve ha nem egy falba szeretne menni.
	 */
	public void move(Direction d) {
		if(!getIsAlive()) {
			return;
		}
		setNextField(getCurrentField().getNeighbor(d));
		if(!getNextField().getWall()) {
			setDirection(d);
			setIsMoving(true);
		}
		
	}
	
	/**
	 * Meghat�rozza a Minotaurusz k�vetkez� l�p�s�nek Ir�ny�t.
	 * Addig megy az aktu�lis Ir�nyba, ameddig keresztez�d�shez nem �r, vagy falba nem �tk�zik.
	 * Keresztez�d�s eset�n V�letlenszer�en Ir�nyt v�laszt, de azt nem v�laszthatja, ahonnan j�tt.
	 * Ha falba �tk�zik akkor is iranyt valt, de pr�b�l �gy v�lasztani, hogy ne kelljen visszafordulnia.
	 * Ha zs�kutc�ba �rt, nincs mit tennie, �gy visszafordul.
	 */
	public void nextStep() {
		if(!getCurrentField().getNeighbor(getDirection()).getWall() && !getCurrentField().isCrossRoad()) {
			move(getDirection()); 
			return;
		}
		ArrayList<Direction> directions = new ArrayList<Direction>();
		if(!getDirection().equals(Direction.LEFT)) directions.add(Direction.RIGHT);
		if(!getDirection().equals(Direction.RIGHT)) directions.add(Direction.LEFT);
		if(!getDirection().equals(Direction.DOWN)) directions.add(Direction.UP);
		if(!getDirection().equals(Direction.UP)) directions.add(Direction.DOWN);
		Collections.shuffle(directions);
		for(Direction d: directions) {
			if(!getIsMoving()) {
				move(d);
			} 
		}
		if(!getIsMoving()) {
			if(getDirection().equals(Direction.LEFT)) move(Direction.RIGHT);
			if(getDirection().equals(Direction.RIGHT)) move(Direction.LEFT);
			if(getDirection().equals(Direction.DOWN)) move(Direction.UP);
			if(getDirection().equals(Direction.UP)) move(Direction.DOWN);
		}
	}
	
	/**
	 * El�sz�r kiv�lasztja az Ir�ny�t, majd abba az Ir�nyba friss�ti az �llapot�t.
	 * Ha �gy el�rt egy k�vetkez� Mez�t, t�rli mag�t az aktu�lisr�l �s az �jhoz adja mag�t. 
	 */
	public void update() {
		if(!getIsAlive()) {
			return;
		}
		nextStep();
		super.update();
		if(getPosX()==getNextField().getPosX() && getPosY()==getNextField().getPosY()) {
			getCurrentField().removeThing(this);
			setCurrentField(getNextField());
			getCurrentField().addThing(this);
			setIsMoving(false);
		}
	}
	
	/**
	 * Az anim�ci� aktu�lis k�pkock�j�nak sorsz�m�t hat�rozza meg, �s adja vissza.
	 */
	public int moveAnimation() {
		if(!getIsMoving())
			return 0;
		setAnimationCounter(getAnimationCounter() + 1);
		int animationSpeed = 15 ;
		int state = getAnimationCounter()/animationSpeed;
		if(getAnimationCounter() == 4 * animationSpeed-1) {
			setAnimationCounter(0);
		}
		if(state==3) {
			return 1;
		}
		return state;
	}
	
	/**
	 * Kirajzolja a Minotauruszt az anim�ci�nak megfelel� pillanatban.
	 */
	public void draw(Graphics g) {
		int size=LoadImages.size;
		int y = 0;
		switch (getDirection()) {
			case UP: y = 0;  break;
			case LEFT:  y = 3; break;
			case DOWN: y = 2; break;
			case RIGHT: y = 1; break;
		}
		int x = moveAnimation();
		BufferedImage image = LoadImages.images.get("Minotaur");
		g.drawImage(image,getPosX(),getPosY()-2,getPosX()+size,getPosY()+size+2,x*size,y*size,(x+1)*size,(y+1)*size,null);
	}
}
