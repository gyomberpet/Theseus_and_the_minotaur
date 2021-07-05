package game;

import game.view.GameFrame;

/**
 * Létrehoz egy GameFrame-et, majd a megadott méretekkel futtatja rajta a Játékot.
 */
public class Main {
	public static void main(String[] args) {
		GameFrame gameFrame = new GameFrame();
		gameFrame.run(25,25);
	}
}
