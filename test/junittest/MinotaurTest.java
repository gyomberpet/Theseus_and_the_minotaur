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
import map.Field;
import map.Maze;
import things.characters.Minotaur;
import things.weapons.Spear;

/**
 * A Minotaurusz f�bb met�dusait teszteli.
 */
@RunWith(Parameterized.class)
public class MinotaurTest {
	Maze maze;
	Field field;
	public MinotaurTest(Maze maze) {
		this.maze = maze;
		maze.init();
		this.field = maze.getRandomEmptyField();
	}
	
	
	/**
	 * Azt teszteli, hogy  ha eltal�ljuk egy D�rd�val, t�nyleg lecsokken-e az �lete.
	 */
	@Test
	public void testHitBySpear() {
		Minotaur m = new Minotaur(maze, field);
		m.hitBy(new Spear(maze,field));
		assertEquals(m.getHp(),2);
	}
	
	/**
	 * A Minotaurusz Ir�ny�ban l�v� Mez�t falra �ll�tjuk, majd l�ptetj�k egyet az �llatot.
	 * Elvarjuk ezutan, hogy az Iranya megv�ltozzon.
	 */
	@Test
	public void testNextStep() {
		Minotaur m = new Minotaur(maze, field);
		Direction d = m.getDirection();
		maze.addWall(m.getCurrentField().getNeighbor(d).getPosY()/LoadImages.size,
					 m.getCurrentField().getNeighbor(d).getPosX()/LoadImages.size);
		m.nextStep();
		assertNotEquals(m.getDirection(),d);
	}
	
	/**
	 * A Minotaurusz �leterej�t nullara �ll�tjuk.
	 * Elvarjuk hogy ne legyen �letben, �s ne is mozogjon.
	 */
	@Test
	public void testDie() {
		Minotaur m = new Minotaur(maze, field);
		m.setHp(0);
		assertEquals(m.getIsAlive(),false);
		assertEquals(m.getIsMoving(),false);
	}
	
	@Parameters
	public static List<Object[]> parameters() {
	List<Object[]> params = new ArrayList<Object[]>();
	params.add(new Object[] {new Maze(25,25)});
	params.add(new Object[] {new Maze(33,12)});
	params.add(new Object[] {new Maze(43,23)});
	params.add(new Object[] {new Maze(6,43)});
	params.add(new Object[] {new Maze(65,3)});
	params.add(new Object[] {new Maze(5,6)});
	return params;
	}
}
