package game.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import map.Maze;
import game.saveload.LoadImages;
import game.saveload.SaveData;
import game.view.GameFrame;

/**
 * A Játék Menüje.
 * Segítségével indítható új játék, menthetõ el az aktuális, illetve tölthetõ be a legutoljára mentett.
 * Tartalmazza a Game-et, amihez tartozik, továbbá egy SaveData-t, ami a mentéshez szükséges.
 * Implementálja a MouseListenert, mivel egérkattintással válszthatunk a Menü opciói közül. 
 */

public class Menu implements MouseListener{
	private Rectangle playButton = new Rectangle(65,100,330,80);
	private Rectangle continueButton = new Rectangle(85,190,290,80);
	private Rectangle loadButton = new Rectangle(150,280,150,80);
	private Rectangle saveButton = new Rectangle(150,370,150,80);
	private Rectangle exitButton = new Rectangle(150,460,150,80);
	private SaveData saveData;
	private Game game;
	public Menu(Game game) {
		this.game=game;
		saveData = new SaveData(game.getMaze());
	}
	
	/**
	 * Kirajzolja a Menü hátterét, és elhelyezi rajta az egyes gombokat.
	 */
	public void draw(Graphics g) {
		BufferedImage menuImage = LoadImages.images.get("Menu");
		g.drawImage(menuImage,0, 0, GameFrame.WIDTH, GameFrame.HEIGHT, null);
		Graphics2D g2d=(Graphics2D) g;
		g2d.draw(playButton);
		g2d.draw(continueButton);
		g2d.draw(loadButton);
		g2d.draw(saveButton);
		g2d.draw(exitButton);
		Font font = new Font("arial",Font.BOLD,60);
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString("New Game", playButton.x+12, playButton.y+58);
		g.drawString("Continue", continueButton.x+12, continueButton.y+58);
		g.drawString("Load", loadButton.x+5, loadButton.y+58);
		g.drawString("Save", saveButton.x+5, saveButton.y+58);
		g.drawString("Exit", exitButton.x+15, exitButton.y+58);
	}
	
	/**
	 * Új játékot idít és "PLAY" állapotba vált.
	 */
	public void newgame() {
		Maze newMaze = new Maze(game.getMaze().getWidth(),game.getMaze().getHeight());
		newMaze.init();
		game.setMaze(newMaze);
		Game.state = State.PLAY;
	}
	
	/**
	 * Folytatja az aktuális játékot, azaz "PLAY" állapotba vált.
	 */
	public void continueGame() {
		Game.state = State.PLAY;
	}
	
	/**
	 * Betölti a legutoljára elmentett játékot, és "PLAY" állapotba vált.
	 */
	public void load() {
		game.setMaze(saveData.load());
		Game.state = State.PLAY;
	}
	
	/**
	 * Elmenti az aktuális játékot.
	 */
	public void save() {
		saveData.save(game.getMaze());
	}
	
	/**
	 * Kilép a játékból.
	 */
	public void exit() {
		System.exit(0);
	}
	public void mouseClicked(MouseEvent e) {
		
	}
	
	/**
	 * A megfelelõ gombokra kattintva meghívja az egyes eseményeket végrehajtó függvényeket.
	 */
	public void mousePressed(MouseEvent e) {
		if(Game.state != State.MENU)
			return;
		int x = e.getX();
		int y = e.getY();
		if(x > playButton.x && x < playButton.x+330 && y > playButton.y && y < playButton.y+80) {
			newgame();
		} else if(x > continueButton.x && x < continueButton.x+290 && y > continueButton.y && y < continueButton.y+80) {
			continueGame();
		} else if(x > loadButton.x && x < loadButton.x+150 && y > loadButton.y && y < loadButton.y+80) {
			load();
		} else if(x > saveButton.x && x < saveButton.x+150 && y > saveButton.y && y < saveButton.y+80) {
			save();
		} else if(x > exitButton.x && x < exitButton.x+150 && y > exitButton.y && y < exitButton.y+80) {
			exit();
		}
	}
	public void mouseReleased(MouseEvent e) {
		
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
}
