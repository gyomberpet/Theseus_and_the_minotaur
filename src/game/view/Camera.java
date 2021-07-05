package game.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.saveload.LoadImages;
import things.characters.Minotaur;
import things.characters.Theseus;

/**
 * A kamera felel�s az�rt, hogy a j�t�kban �ppen mely obiektumok jelenjenek meg a k�perny�n.
 */

public class Camera {
	private int x;
	private int y;
	private int width;
	private int height;
	private Theseus theseus;
	public Camera(Theseus theseus, int x, int y, int width, int height) {
		this.theseus = theseus;
		this.x = x;
		this.y = y;
		this.width = width*LoadImages.size;
		this.height = height*LoadImages.size;
	}
	
	/**
	 * Visszaadja X-et.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Visszaadja Y-t.
	 */
	public int getY() {
		return y;
	}

	/**
	 * A kamer�t Theseusra f�kusz�lja.
	 * Ha Theseus a p�lya sz�l�hez k�zel van, nem mozdul, egy�bk�nt Theseust k�veti.
	 */
	public void setFocus() {
		int posX = theseus.getPosX();
		int posY = theseus.getPosY();
		if(posX>6*LoadImages.size) {
			x=posX-6*LoadImages.size;
		} else {
			x=0;
		} if(posY>4*LoadImages.size) {
			y=posY-4*LoadImages.size;
		} else {
			y=0;
		} if (posX>width-(1+6)*LoadImages.size) {
			x=width-(1+2*6)*LoadImages.size;
		} if (posY>height-(1+4)*LoadImages.size) {
			y=height-(1+2*4)*LoadImages.size;
		}
	}

	/**
	 * Megjelen�ti Theseus �leteit a k�perny� jobb fels� sark�ban.
	 * J�t�k v�ge eset�n jelzi, hogy gy�zelemmel vagy veres�ggel fejez�d�tt be.
	 */
	public void draw(Graphics g, Minotaur m) {
		int size = LoadImages.size;
		BufferedImage heartImage = LoadImages.images.get("Heart");
		for(int i = 0;i < theseus.getHp();++i) {
			g.drawImage(heartImage,x+680+i*40,y+10,x+size+670+i*40,y+size,0,0,size,size,null);
		}
		Font font = new Font("arial",Font.BOLD,100);
		g.setFont(font);
		if(!theseus.getIsAlive()) {
			g.setColor(Color.red);
			g.drawString("You Lose!",x+200,y+300);
		} else if(!m.getIsAlive()) {
			g.setColor(Color.blue);
			g.drawString("You Win!",x+200,y+300);
		}
	}
}
