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
 * A Minotaurusz egy Karakter a játékbn, ezert örökõdik Karakterbõl.
 * A Minotaurusz Thesesu ellensége, õt kell megölni a játék megnyeréséhez.
 */
public class Minotaur extends Character{
	public Minotaur(Maze maze, Field currentField) {
		super(maze, currentField, 2, 3,"M");
	}
	
	/**
	 * Akkor hívódik, amikor nekiütközik egy dolognak.
	 * Ekkor szól az adott Dolognak, hogy nekiment.  
	 */
	public void collideWith(Thing t) {
		t.hitBy(this);
	}
	
	/**
	 * Akkor hívódik, ha eltalálták egy Dárdával.
	 * Az életereje eggyel csökken, és visszaszól a Dárdának, hogy eltalált valamit.
	 */
	public void hitBy(Spear  s) {
		reduceHp();
		s.isReachedEnemy();
	}
	
	/**
	 * Akkor hívódik, ha Theseus nekimegy.
	 * Ekkor csökkenti Theseus életerejét, feltéve, hogy a Minotaurusz még életben van.
	 */
	public void hitBy(Theseus t) {
		if(!getIsAlive()) {
			return;
		}
		t.reduceHp();
	}
	
	/**
	 * Az adott Irányba próbál lépni egyet.
	 * Ez akkor történhet meg, ha meg életben van, illetve ha nem egy falba szeretne menni.
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
	 * Meghatározza a Minotaurusz következõ lépésének Irányát.
	 * Addig megy az aktuális Irányba, ameddig keresztezõdéshez nem ér, vagy falba nem ütközik.
	 * Keresztezõdés esetén Véletlenszerûen Irányt választ, de azt nem választhatja, ahonnan jött.
	 * Ha falba ütközik akkor is iranyt valt, de próbál úgy választani, hogy ne kelljen visszafordulnia.
	 * Ha zsákutcába ért, nincs mit tennie, így visszafordul.
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
	 * Elõször kiválasztja az Irányát, majd abba az Irányba frissíti az állapotát.
	 * Ha így elért egy következõ Mezõt, törli magát az aktuálisról és az újhoz adja magát. 
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
	 * Az animáció aktuális képkockájának sorszámát határozza meg, és adja vissza.
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
	 * Kirajzolja a Minotauruszt az animációnak megfelelõ pillanatban.
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
