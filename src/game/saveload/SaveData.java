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
 * A ment�s megvalos�t�s��rt felel.
 * A Labirintust �s a benne lev� egyes Dolgokat Stringek m�trx�v� alak�tja, majd ebb�l �ll�tja vissza.
 * Implement�lja a Serializable-t, melynek seg�ts�g�vel a f�jlba �r�st �s olvas�st valos�tja meg.
 * A k�dol�s fontoss�g�t az egyszer� Serializalas k�zben felmer�l� probl�mak (pl.: t�l nagy m�ret) teszik indokoltt�.
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
	 * A param�ter�l kapott Labirintus Mez�it, valamint minden benne l�v� Dolgot egy Stringek m�trix�v� konvert�l.
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
	 * Az �tkonvert�lt Stringek m�trix�t a "game.txt" nev� f�jlba �rja.
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
	 * A Stringek m�trx�b�l l�trehoz egy �j Labirintust, a benne l�v� Dolgokkal egy�tt.
	 * A ment�s el�tt aktu�lisan m�k�d� DuratioPowerUp-ok hat�sai elvesznek a vissza�ll�tas soran.
	 * Minden m�s a ment�s el�tti Labirintus �llapot�ba �ll vissza.
	 * Visszaadja az �jonnan rekonstru�lt Labirintust.
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
	 * Egy seg�df�ggv�ny a Theseus ment�s el�tti �sv�ny�nek a vissza�ll�tas�hoz.
	 * Visszaadja a PathBlock-ok list�j�t, amit a param�terek �ltal meghat�rozott �j PathBlock-al b�v�t.
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
	 * A param�ter�l kapott Labirintusba vissza�ll�tja a param�ter�l kapott "pathCode" alapj�n a ment�s el�tti �sv�nyt.
	 * A vissza�ll�tott �sv�nyt be�ll�tja a Labirintusban l�v� Theseusnak, majd visszaadja az �gy friss�tett Labirintust.
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
	 * A "game.txt" f�jlb�l beolvassa a kimentett Stringek m�trix�v� konvert�lt j�t�k�llapotot.
	 * A deConvertMaze() f�ggveny seg�ts�gevel Labirintuss� alak�tja, majd visszaadja az �gy el��lt Labirintust.
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
	 * Visszaadja a Mez�ket egy Stringek m�trixak�nt.
	 * Tesztel�shez sz�ks�ges.
 	 */
	public String[][] getFields(){
		return fields;
	}
}
