package junittest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import game.saveload.LoadImages;
import map.Direction;
import map.Maze;
import things.powerups.LifePowerUp;

/**
 * A Labirrintus f�bb met�dusait teszteli.
 */
@RunWith(Parameterized.class)
public class MazeTest {
	int x;
	int y;
	public MazeTest(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Azt teszteli, hogy a t�nyleges koordin�t�k alapj�n a helyes Mez�t adja-e vissza.
	 */
	@Test
	public void testGetFieldAtPosition() {
		Maze maze = new Maze(55,55);
		maze.init();
		assertEquals(maze.getFieldAtPosition(y*LoadImages.size, x*LoadImages.size),maze.getField(y, x));
	}
	
	/**
	 * A k�l�nb�z� Ir�nyokban l�v� szomsz�dokat teszteli.
	 */
	@Test
	public void testSetAllNeighbors() {
		Maze maze = new Maze(55,55);
		maze.init();
		maze.setAllNeighbors();
		assertEquals(maze.getField(y, x).getNeighbor(Direction.RIGHT),maze.getField(y, x+1));
		assertEquals(maze.getField(y, x).getNeighbor(Direction.LEFT),maze.getField(y, x-1));
		assertEquals(maze.getField(y, x).getNeighbor(Direction.UP),maze.getField(y-1, x));
		assertEquals(maze.getField(y, x).getNeighbor(Direction.DOWN),maze.getField(y+1, x));
	}
	
	/**
	 * Egy kisebb m�ret� labirintus 5 Mez�j�hez hozz�ad egy Dolgot, majd megn�zi,
	 * hogy egy v�letlenszer�en v�lasztott �res Mez� lehet-e nem �res.
	 */
	@Test
	public void testGetRandomEmptyField() {
		Maze maze = new Maze(13,13);
		maze.init();
		maze.getField(y%13, x%13).addThing(new LifePowerUp(maze,maze.getField(y%13, x%13)));
		maze.getField(x%13, y%13).addThing(new LifePowerUp(maze,maze.getField(x%13, y%13)));
		maze.getField(x%13, x%13).addThing(new LifePowerUp(maze,maze.getField(x%13, x%13)));
		maze.getField(y%13, y%13).addThing(new LifePowerUp(maze,maze.getField(y%13, y%13)));
		maze.getField((x+y)%13, (x+y)%13).addThing(new LifePowerUp(maze,maze.getField((x+y)%13, (x+y)%13)));
		assertNotEquals(maze.getRandomEmptyField(),maze.getField(y%13, x%13));
		assertNotEquals(maze.getRandomEmptyField(),maze.getField(x%13, y%13));
		assertNotEquals(maze.getRandomEmptyField(),maze.getField(x%13, x%13));
		assertNotEquals(maze.getRandomEmptyField(),maze.getField(y%13, y%13));
		assertNotEquals(maze.getRandomEmptyField(),maze.getField((x+y)%13, (x+y)%13));

	}
	
	/**
	 * A Labirintus kezdetben 2 Dolgot (Theseus, Minotaurusz) tartalmaz,
	 * majd 3 tetsz�leges Mez�h�z hozz�adunk egy LifePowerUp-ot.
	 * Elv�rjuk, hogy �sszesen 5 Dolog legyen a Labirintusban.
	 */
	@Test
	public void testGetAllThing() {
		Maze maze = new Maze(55,55);
		maze.init();
		maze.getField(y, x).addThing(new LifePowerUp(maze,maze.getField(y, x)));
		maze.getField(y, x).addThing(new LifePowerUp(maze,maze.getField(x, y)));
		maze.getField(y, x).addThing(new LifePowerUp(maze,maze.getField((y*x)%55, (y*x)%55)));
		assertEquals(maze.getAllThing().size(),5);
	}
	
	@Parameters
	public static List<Object[]> parameters() {
	List<Object[]> params = new ArrayList<Object[]>();
	params.add(new Object[] {3, 5});
	params.add(new Object[] {11, 4});
	params.add(new Object[] {12, 26});
	params.add(new Object[] {23, 1});
	params.add(new Object[] {32, 5});
	params.add(new Object[] {1, 7});
	params.add(new Object[] {45, 45});
	return params;
	}

}
