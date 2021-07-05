package game.saveload;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import map.Direction;
import map.Field;
import map.Maze;
import things.powerups.LifePowerUp;
import things.characters.Minotaur;
import path.Path;
import path.PathBlock;
import things.weapons.Spear;
import things.powerups.SpeedPowerUp;
import things.characters.Theseus;


/**
 * A mentés megvalosításáért felel.
 * A Labirintust és a benne levõ egyes Dolgokat Stringek mátrxává alakítja, majd ebbõl állítja vissza.
 * Implementálja a Serializable-t, melynek segítségével a fájlba írást és olvasást valosítja meg.
 * A kódolás fontosságát az egyszerû Serializalas közben felmerülõ problémak (pl.: túl nagy méret) teszik indokolttá.
 */
public class SaveData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String[][] fields;
	private int height;
	private int width;
	public SaveData(Maze maze) {
		this.width = maze.getWidth();
		this.height = maze.getHeight();
		fields = new String[height][width];
	}
	
	/**
	 * A paraméterül kapott Labirintus Mezõit, valamint minden benne lévõ Dolgot egy Stringek mátrixává konvertál.
	 */
	public void convertMaze(Maze maze) {
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				fields[y][x]="";
				if(maze.getField(y, x).getWall()) {
					fields[y][x]+="w";
				} else {
					fields[y][x]+="f";
					if(maze.getField(y, x).getThings()!=null){
						for(int i = 0; i < maze.getField(y, x).getThings().size(); ++i) {
							fields[y][x] += maze.getField(y, x).getThings().get(i).codeID();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Az átkonvertált Stringek mátrixát a "game.txt" nevû fájlba írja.
	 */
	public void save(Maze maze) {
		fields = new String[height][width];
		try {
			convertMaze(maze);
			FileOutputStream fo;
			fo = new FileOutputStream("game.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fo);
			oos.writeObject(fields);
			oos.close();
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A Stringek mátrxából létrehoz egy új Labirintust, a benne lévõ Dolgokkal együtt.
	 * A mentés elõtt aktuálisan mûködõ DuratioPowerUp-ok hatásai elvesznek a visszaállítas soran.
	 * Minden más a mentés elõtti Labirintus állapotába áll vissza.
	 * Visszaadja az újonnan rekonstruált Labirintust.
	 */
	public Maze deConvertMaze() {
		Maze maze = new Maze(width, height);
		Field[][] mazeFields = new Field[height][width];
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				mazeFields[y][x]=new Field(y,x);
			}
		}
		String pathCode ="";
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				if(fields[y][x].charAt(0)=='w') {
					mazeFields[y][x].setWall(true);
				} else if(fields[y][x].charAt(0)=='f'){
					for(int i = 1; i < fields[y][x].length();++i) {
						if(fields[y][x].charAt(i)=='T') {
							Theseus theseus = new Theseus(maze, mazeFields[y][x]);
							theseus.setHp(Character.getNumericValue(fields[y][x].charAt(i+1)));
							i++;
							pathCode=fields[y][x].split(" ")[1];
							i+=pathCode.length()+2;
							mazeFields[y][x].addThing(theseus);
						} else if(fields[y][x].charAt(i)=='M') {
							Minotaur minotaur = new Minotaur(maze, mazeFields[y][x]);
							minotaur.setHp(Character.getNumericValue(fields[y][x].charAt(i+1)));
							i++;
							mazeFields[y][x].addThing(minotaur);
							maze.setMinotaur(minotaur);
						} else if(fields[y][x].charAt(i)=='A') {
							mazeFields[y][x].addThing(new Spear(maze, mazeFields[y][x]));
						} else if(fields[y][x].charAt(i)=='L') {
							mazeFields[y][x].addThing(new LifePowerUp(maze, mazeFields[y][x]));
						} else if(fields[y][x].charAt(i)=='S') {
							mazeFields[y][x].addThing(new SpeedPowerUp(maze, mazeFields[y][x]));
						}
					}
				}
			} 
		}
		maze.setFields(mazeFields);
		maze.setAllNeighbors();
		return deConvertPath(maze, pathCode);
	}
	
	/**
	 * Egy segédfüggvény a Theseus mentés elõtti Ösvényének a visszaállítasához.
	 * Visszaadja a PathBlock-ok listáját, amit a paraméterek által meghatározott új PathBlock-al bõvít.
	 */
	public List<PathBlock> addPathBlock(List<PathBlock> pb, String pathCode, int delta, Direction d) {
		if(pathCode.charAt(delta-2)=='u')
			pb.add(new PathBlock(pb.get(pathCode.length()-delta).getField().getNeighbor(d), Direction.UP));
		if(pathCode.charAt(delta-2)=='d')
			pb.add(new PathBlock(pb.get(pathCode.length()-delta).getField().getNeighbor(d), Direction.DOWN));
		if(pathCode.charAt(delta-2)=='l')
			pb.add(new PathBlock(pb.get(pathCode.length()-delta).getField().getNeighbor(d), Direction.LEFT));
		if(pathCode.charAt(delta-2)=='r')
			pb.add(new PathBlock(pb.get(pathCode.length()-delta).getField().getNeighbor(d), Direction.RIGHT));
		return pb;
	}
	
	/**
	 * A paraméterül kapott Labirintusba visszaállítja a paraméterül kapott "pathCode" alapján a mentés elõtti ösvényt.
	 * A visszaállított ösvényt beállítja a Labirintusban lévõ Theseusnak, majd visszaadja az így frissített Labirintust.
	 */
	public Maze deConvertPath(Maze maze, String pathCode){
		Field cf = maze.getTheseus().getCurrentField();
		List<PathBlock> reversePath = new ArrayList<>(); 
		if(pathCode.charAt(pathCode.length()-1)=='u') {
			 reversePath.add(new PathBlock(cf,Direction.UP));
		} else if(pathCode.charAt(pathCode.length()-1)=='l') {
			 reversePath.add(new PathBlock(cf,Direction.LEFT));
		} else if(pathCode.charAt(pathCode.length()-1)=='d') {
			 reversePath.add(new PathBlock(cf,Direction.DOWN));
		} else if(pathCode.charAt(pathCode.length()-1)=='r') {
			 reversePath.add(new PathBlock(cf,Direction.RIGHT));
		}
		for(int i = pathCode.length(); i > 1 ;--i) {
				if(pathCode.charAt(i-1)=='u') {
					reversePath = addPathBlock(reversePath,pathCode,i,Direction.DOWN);
				} else if(pathCode.charAt(i-1)=='d') {
					reversePath = addPathBlock(reversePath,pathCode,i,Direction.UP);
				} else if(pathCode.charAt(i-1)=='l') {
					reversePath = addPathBlock(reversePath,pathCode,i,Direction.RIGHT);
				} else if(pathCode.charAt(i-1)=='r') {
					reversePath = addPathBlock(reversePath,pathCode,i,Direction.LEFT);
				} 
		}
		Collections.reverse(reversePath);
		reversePath.remove(0);
		Path path = new Path(maze.getStartField(),Direction.DOWN);
		for(PathBlock p: reversePath) {
			path.add(p.getField(), p.getDirection());
		}		
		maze.getTheseus().setPath(path);
		return maze;
	}
	
	/**
	 * A "game.txt" fájlból beolvassa a kimentett Stringek mátrixává konvertált játékállapotot.
	 * A deConvertMaze() függveny segítségevel Labirintussá alakítja, majd visszaadja az így elõált Labirintust.
 	 */
	public Maze load() {
		try {
			FileInputStream fi= new FileInputStream("game.txt");
			ObjectInputStream ois = new ObjectInputStream(fi);
			fields = (String[][])ois.readObject();
			ois.close();
			fi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Maze deConvertedMaze = deConvertMaze();
		return deConvertedMaze;
	}
	
	/**
	 * Visszaadja a Mezõket egy Stringek mátrixaként.
	 * Teszteléshez szükséges.
 	 */
	public String[][] getFields(){
		return fields;
	}
}
