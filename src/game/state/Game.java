package game.state;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import map.Direction;
import map.Maze;
import things.Thing;
import game.view.Camera;

import javax.swing.JPanel;


/**
 * A j�t�k motorja, mely kezeli a felhaszn�l�i inputokat �s a j�t�k �llapotait.
 * Folyamatosan friss�ti az egyes Dolgokat �s kirajzolj azokat a k�perny�re.
 * JPanel-b�l �r�kl�dik �gy a GameFrame-hez hozz�adhat�.
 * Implement�lja a KeyListener �s ActionListener interf�szeket,
 * melyek billenty�lenyomasok�rt �s az id�z�t� haszn�lhat�s�g��rt sz�ks�gesek.
 * A J�t�k oszt�ly t�rolja a Labirintust, a Kamer�t, a Men�t, az egyes ir�nyokat �s a j�tek �llapot�t. 
 */
public class Game extends JPanel implements KeyListener, ActionListener{
	private static final long serialVersionUID = 1L;
	private Maze maze;
	private Camera camera;
	private Timer timer;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	public static State state = State.MENU;
	private Menu menu;
	public Game(int width, int height) {
		maze = new Maze(width,height);		
		maze.init();
		camera = new Camera(maze.getTheseus(),0,0,width,height);
		menu = new Menu(this);
		addKeyListener(this);
		addMouseListener(menu);
		setFocusable(true);
		timer = new Timer(10,this);
		timer.start();
	}
	
	/**
	 * Visszaadja az aktu�lis Labirintust.
	 */
	public Maze getMaze(){
		return maze;
	}
	
	/**
	 * Be�ll�tja a Labirintust a param�terk�nt kapott Labirintusra.
	 */
	public void setMaze(Maze m){
		maze=m;
		camera = new Camera(maze.getTheseus(),0,0,maze.getWidth(),maze.getHeight());
	}
	
	/**
	 * Ha a j�t�k �llapota "PLAY", kirajzolja a Labirintust (ami az �sszes benne l�v� Dolgot) �s a Kamer�t.
	 * Ha a j�t�k �llapota "MENU", a Men�t jelen�ti meg.
	 */
	public void paintComponent(Graphics g) {
		if(state == State.PLAY) {
			g.translate(-camera.getX(), -camera.getY());
			super.paintComponent(g);
			maze.draw(g);
			camera.draw(g,maze.getMinotaur());
			g.translate(camera.getX(), camera.getY());
		} else if(state == State.MENU) {
			menu.draw(g);
		}
	}
	public void keyTyped(KeyEvent e) {
		
	}
	/**
	 * Figyeli a billenty�lenyom�sokat, �s ennek megfelel�en be�ll�tja a megfelel� ir�nyokat.
	 * Innen h�v�dik meg Theseus t�madasa, valamit a Men� megjelen�t�se is.
	 */
	public void keyPressed(KeyEvent e) {
		if(state == State.PLAY) {
			if(e.getKeyCode()==KeyEvent.VK_W) {
				up=true;
			} if(e.getKeyCode()==KeyEvent.VK_A) {
				left=true;
			} if(e.getKeyCode()==KeyEvent.VK_S) {
				down=true;
			} if(e.getKeyCode()==KeyEvent.VK_D) {
				right=true;
			} if(e.getKeyCode()==KeyEvent.VK_SPACE) {
				maze.getTheseus().attack();
			} if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
				state = State.MENU;
			}
		}
	}
	
	/**
	 * Figyeli a billenty�felenged�seket, �es ennek megfelel�en be�ll�tja a megfelelo ir�nyokat.
	 */
	public void keyReleased(KeyEvent e) {
		if(state == State.PLAY) {
			if(e.getKeyCode()==KeyEvent.VK_W) {
				up=false;
			} if(e.getKeyCode()==KeyEvent.VK_A) {
				left=false;
			} if(e.getKeyCode()==KeyEvent.VK_S) {
				down=false;
			} if(e.getKeyCode()==KeyEvent.VK_D) {
				right=false;
			} 
		}
	}
	
	/**
	 * Az id�z�t� hat�s�ra folyamatos id�k�z�nk�nt megh�v�dik.
	 * Az aktu�lis ir�nyok alapj�n mozgatja Theseust.
	 * Minden Dolog �llapot�t friss�ti a Labirintusban.
	 * Megh�vja a Labirintus addPowerUp f�ggv�ny�t, ami id�nkent �j PowerUp-ot ad mag�hoz.
	 * Friss�ti a Kamera f�kusz�t is.
	 * A friss�t�sek ut�n �jb�l megh�vja a saj�t kirajzol�f�ggv�ny�t, hogy l�that�ak legyenek a v�ltoz�sok.
	 */
	public void actionPerformed(ActionEvent e) {
		if(state == State.PLAY) {
			if(up) {
				maze.getTheseus().move(Direction.UP);
			} if(left) {
				maze.getTheseus().move(Direction.LEFT);
			} if(down) {
				maze.getTheseus().move(Direction.DOWN);
			} if(right) {
				maze.getTheseus().move(Direction.RIGHT);
			} 
			for(Thing t: maze.getAllThing()) {
				t.update();
			}
			maze.addPowerUp();
			camera.setFocus();
		}
		repaint();
	}
}
