package game;

import game.view.GameFrame;

/**
 * L�trehoz egy GameFrame-et, majd a megadott m�retekkel futtatja rajta a J�t�kot.
 */
public class Main {
	public static void main(String[] args) {
		GameFrame gameFrame = new GameFrame();
		gameFrame.run(25,25);
	}
}
