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
 * Egy D�rd�t reprezent�l a j�t�kban.
 * Theseus hozhatja l�tre, melynek hat�s�ra elkezd rep�lni, m�g valaminek neki nem �tk�zik.
 * Lesz�rmazottja a Dolog �soszt�lynak.
 */
public class Spear extends Thing{
	private boolean isReachedEnemy;
	public Spear(Maze maze, Field currentField) {
		super(maze, currentField, 8, "A");
		isReachedEnemy = false;
	}
	
	/**
	 * Az isReachededEnemy �rt�k�t igazra �ll�tja, ha eltal�lta az ellenfel�t.
	 */
	public void isReachedEnemy(){
		isReachedEnemy  = true;
	}
	
	/**
	 * Ha egy Dolognak �tk�zik, sz�l neki, hogy �tk�z�tt vele.
	 */
	public void collideWith(Thing t) {
		t.hitBy(this);
	}
	
	/**
	 * Ha a D�rd�nak Minotaurusz �tk�zik, cs�kkenti a Minotaurusz �leterej�t, majd jelzi maganak, hogy c�lba tal�lt.
	 * Tipikusan az�rt van r� sz�ks�g, mert el�fordulhat, hogy a D�rda az adott Mez�n van �ppen, mikor a Minotaurusz r�l�p.
	 * Ez el�fordulhat, hiszen mindketten mozognak.
	 */
	public void hitBy(Minotaur  m) {
		m.reduceHp();
		isReachedEnemy();
	}
	
	/**
	 * A D�rda az adott Ir�nyba megy, ameddig falba nem �tk�zik.
	 * Ez el�fordulhat �gy is, hogy r�gt�n falnak pr�b�lj�k dobni.
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
	 * Friss�ti az �llapot�t a mozg�s�nak megfelel�en, felt�ve, hogy nem �tkozik k�zben ellens�gnek.
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
	 * Kirajzolja a D�rd�t, az adott Ir�nynak megfelel�en.
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
