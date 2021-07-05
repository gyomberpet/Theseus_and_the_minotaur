package junittest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import map.Direction;
import map.Field;
import map.Maze;
import path.Path;
import things.characters.Minotaur;
import things.characters.Theseus;
import things.powerups.SpeedPowerUp;


/**
 * Theseus fõbb metódusait teszteli.
 */
@RunWith(Parameterized.class)
public class TheseusTest {
	Maze maze;
	Field field;
	public TheseusTest(Maze maze) {
		this.maze = maze;
		maze.init();
		this.field = maze.getRandomEmptyField();
	}
	
	
	/**
	 * Azt teszteli, hogy ha nekimegy a Minotaurusz, tényleg lecsökken-e az élete.
	 */
	@Test
	public void testHitByMinotaur() {
		Theseus t = new Theseus(maze, field);
		t.hitBy(new Minotaur(maze,field));
		assertEquals(t.getHp(),2);
	}
	
	/**
	 * Azt teszteli, hogy ha Theseus egy SpeedPowerUp-nak megy, tenylge 2-szeres lesz-e a sebessége.
	 */
	@Test
	public void testCollideWidthSpeedPowerUP() {
		Theseus t = new Theseus(maze, field);
		int speed = t.getSpeed();
		t.collideWith(new SpeedPowerUp(maze,field));
		assertEquals(speed,t.getSpeed()/2);
	}
	
	/**
	 * Theseus életerejét nullára állítjuk.
	 * Elvárjuk, hogy ne legyen életben, és ne is mozogjon.
	 */
	@Test
	public void testDie() {
		Theseus t = new Theseus(maze, field);
		t.setHp(0);
		assertEquals(t.getIsAlive(),false);
		assertEquals(t.getIsMoving(),false);
	}
	
	/**
	 * Létrehozunk egy Ösvenyt, majd beállítjuk Theseus Ösvényének.
	 * Elvárjuk, hogy a kódda alakítása az alábbi legyen.
	 */
	@Test
	public void testCodeID() {
		Theseus t = new Theseus(maze, field);
		Path p = new Path(new Field(1,1),Direction.DOWN);
		p.add(new Field(1,2), Direction.UP);
		p.add(new Field(11,22), Direction.LEFT);
		p.add(new Field(3,7), Direction.LEFT);
		p.add(new Field(5,34), Direction.RIGHT);
		t.setPath(p);
		assertEquals(t.codeID(), "T3 dullr ");
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