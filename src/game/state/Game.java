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
 * A játék motorja, mely kezeli a felhasználói inputokat és a játék állapotait.
 * Folyamatosan frissíti az egyes Dolgokat és kirajzolj azokat a képernyõre.
 * JPanel-bõl öröklõdik így a GameFrame-hez hozzáadható.
 * Implementálja a KeyListener és ActionListener interfészeket,
 * melyek billentyûlenyomasokért és az idözítõ használhatóságáért szükségesek.
 * A Játék osztály tárolja a Labirintust, a Kamerát, a Menüt, az egyes irányokat és a játek állapotát. 
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
	 * Visszaadja az aktuális Labirintust.
	 */
	public Maze getMaze(){
		return maze;
	}
	
	/**
	 * Beállítja a Labirintust a paraméterként kapott Labirintusra.
	 */
	public void setMaze(Maze m){
		maze=m;
		camera = new Camera(maze.getTheseus(),0,0,maze.getWidth(),maze.getHeight());
	}
	
	/**
	 * Ha a játék állapota "PLAY", kirajzolja a Labirintust (ami az összes benne lévõ Dolgot) és a Kamerát.
	 * Ha a játék állapota "MENU", a Menüt jeleníti meg.
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
	 * Figyeli a billentyûlenyomásokat, és ennek megfelelõen beállítja a megfelelõ irányokat.
	 * Innen hívódik meg Theseus támadasa, valamit a Menü megjelenítése is.
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
	 * Figyeli a billentyûfelengedéseket, ées ennek megfelelõen beállítja a megfelelo irányokat.
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
	 * Az idõzítõ hatására folyamatos idõközönként meghívódik.
	 * Az aktuális irányok alapján mozgatja Theseust.
	 * Minden Dolog állapotát frissíti a Labirintusban.
	 * Meghívja a Labirintus addPowerUp függvényét, ami idõnkent új PowerUp-ot ad magához.
	 * Frissíti a Kamera fókuszát is.
	 * A frissítések után újból meghívja a saját kirajzolófüggvényét, hogy láthatóak legyenek a változások.
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
