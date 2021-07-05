package junittest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import game.saveload.LoadImages;
import game.saveload.SaveData;
import map.Field;
import map.Maze;
import things.powerups.LifePowerUp;


/**
 * A SaveData fõbb metódusait teszteli.
 */
@RunWith(Parameterized.class)
public class SaveDataTest {
	Maze maze;
	public SaveDataTest(Maze maze) {
		this.maze = maze;
		maze.init();
	}
	
	
	/**
	 * Azt teszteli, hogy a konvertálás utan helyesen kódolódnak-e a Mezõk.
	 * Falból "w", nem falból "f" lesz az elsõ karaktere az eredménynek.
	 */
	@Test
	public void testConvertMaze() {
		Field field1 = maze.getRandomEmptyField();;
		int x1 = field1.getPosX()/LoadImages.size;
		int y1 = field1.getPosY()/LoadImages.size;
		maze.addWall(y1, x1);
		Field field2 = maze.getRandomEmptyField();;
		int x2 = field2.getPosX()/LoadImages.size;
		int y2 = field2.getPosY()/LoadImages.size;
		maze.destroyWall(y2, x2);
		SaveData sd = new SaveData(maze);
		sd.convertMaze(maze);
		assertEquals(sd.getFields()[y1][x1], "w");
		assertEquals(sd.getFields()[y2][x2].charAt(0), 'f');
	}
	
	/**
	 * Azt teszteli, hogy a dekonvertálás utan visszakapott Labirintusban is annyi Dolog van-e összesen,
	 * mint amennyi az eredeti Labirintusban volt.
	 */
	@Test
	public void testDeConvertMaze() {
		SaveData sd = new SaveData(maze);
		Field f1=maze.getRandomEmptyField();
		f1.addThing(new LifePowerUp(maze,f1));
		Field f2=maze.getRandomEmptyField();
		f2.addThing(new LifePowerUp(maze,f2));
		sd.convertMaze(maze);
		assertEquals(sd.deConvertMaze().getAllThing().size(), maze.getAllThing().size());
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
