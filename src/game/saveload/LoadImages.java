package game.saveload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


/**
 * A képek betöltéséért felel.
 * Tartalmazza továbbá a játékban elõforduló egységes, fix méretet is.
 * Változói elérhetõek a többi osztályból.
 */
public class LoadImages {
	public static final Map <String, BufferedImage> images = new HashMap<>();
	public static final int size=64;
	static{
		try {
			images.put("Theseus",ImageIO.read(new File("images/avatar.png")));
			images.put("Wall", ImageIO.read(new File("images/wall.png")));
			images.put("Grass", ImageIO.read(new File("images/grass.png")));
			images.put("Minotaur", ImageIO.read(new File("images/minotaur.png")));
			images.put("Spear", ImageIO.read(new File("images/my_spear.png")));
			images.put("Rope", ImageIO.read(new File("images/rope.png")));
			images.put("Princess", ImageIO.read(new File("images/princess.png")));
			images.put("Heart", ImageIO.read(new File("images/heart.png")));
			images.put("Speed", ImageIO.read(new File("images/speed.png")));
			images.put("Menu", ImageIO.read(new File("images/menu.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
