package game.view;

import javax.swing.JFrame;

import game.state.Game;


/**
 * A program ablaka, ezert a JFrame-b�l �r�kl�dik.
 * Fix sz�less�ge �s magass�ga van, mely a t�bbi oszt�ly sz�mara is l�that�, �s egy saj�t c�me.
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
	 * Hozz�ad mag�hoz egy �j J�t�kot, a param�terk�nt megkapott m�retekkel.
	 */
	public void run(int width, int height) {
		add(new Game(width,height));
		setVisible(true);
	}	
}
