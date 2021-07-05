package game.view;

import javax.swing.JFrame;

import game.state.Game;


/**
 * A program ablaka, ezert a JFrame-bõl öröklõdik.
 * Fix szélessége és magassága van, mely a többi osztály számara is látható, és egy saját címe.
 */

public class GameFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 845;
	public static final int HEIGHT = 610;
	private String title = "Theseus and the Minotaur";
	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
	}
	
	/**
	 * Hozzáad magához egy új Játékot, a paraméterként megkapott méretekkel.
	 */
	public void run(int width, int height) {
		add(new Game(width,height));
		setVisible(true);
	}	
}
