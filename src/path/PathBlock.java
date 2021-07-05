package path;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import map.Direction;
import map.Field;
import game.saveload.LoadImages;


/**
 * A Path egy blokkja.
 * Rendelkezik egy Mezõvel és egy Iránnyal.
 */
public class PathBlock {
	private Field field;
	private Direction direction;
	public PathBlock(Field field, Direction direction) {
		this.field = field;
		this.direction = direction;
	}
	
	/**
	 * Visszaadj a Mezõjet.
	 */
	public Field getField() {
		return field;
	}
	
	/**
	 * Visszaadj az Irányát.
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Kirajzolja a PathBlocknak megfelelõ elemét, figyelembe véve a követtkezõ PathBlock-ot.
	 */
	public void draw(Graphics g, PathBlock next) {
		int size=LoadImages.size;
		BufferedImage ropeImage = LoadImages.images.get("Rope");
		int x = 0;
		int y = 0;
		switch (direction) {
			case UP: x = 0;  
				if(next.getDirection().equals(Direction.RIGHT)) {
					y = 1;
				} else if(next.getDirection().equals(Direction.LEFT)) {
					y = 2;
				} break;
			case LEFT:  x = 3;
				if(next.getDirection().equals(Direction.UP)) {
					y = 1;
				} else if(next.getDirection().equals(Direction.DOWN)) {
					y = 2;
				} break;
			case DOWN: x = 2;
				if(next.getDirection().equals(Direction.LEFT)) {
					y = 1;
				} else if(next.getDirection().equals(Direction.RIGHT)) {
					y = 2;
				} break;
			case RIGHT: x = 1;
				if(next.getDirection().equals(Direction.DOWN)) {
					y = 1;
				} else if(next.getDirection().equals(Direction.UP)) {
					y = 2;
				} break;
		}
		int posX = field.getPosX();
		int posY = field.getPosY();
		g.drawImage(ropeImage,posX,posY,posX+size,posY+size,x*size,y*size,(x+1)*size,(y+1)*size,null);
	}
	
	/**
	 * Visszaadja a PathBlock Irányát egy egybetûs Stringben (u: up, d: down, r: right, l: left).
	 */
	public String toString() {
		String str = new String();
		switch(direction) {
			case UP: str+="u"; break;
			case LEFT: str+="l"; break;
			case DOWN: str+="d"; break;
			case RIGHT: str+="r"; break;
		}
		return str;
	}
}
