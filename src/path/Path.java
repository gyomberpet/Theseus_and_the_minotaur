package path;


import java.awt.Graphics;
import java.util.Stack;

import map.Direction;
import map.Field;

/**
 * A játékban egy Ösvényt (útvonalat) reprezentál.
 * Tárolja a megfelelõ Mezõket a megadott sorrendben, illetve azt is, hogy azt melyik irányból jövet érték el.
 * Ehhez a PathBlock osztályt használja fel, ami képes az elemeket egyszerûbben kezelni.
 */
public class Path {
	private Stack<PathBlock> fields;
	public Path(Field startingField, Direction d) {
		this.fields = new Stack<PathBlock>();
		fields.push(new PathBlock(startingField,d));
	}
	
	/**
	 * Új elem hozzáadása a többihez.
	 * Ha az elemszám egy, mindenképp hozzáadódik.
	 * Egyebként megnézi, hogy a következõ elem nem-e az elõzõ (ahonnan jött az Ösvény),
	 * mert ekkor hozzáadas hellyett törli az aktuálisat az elemek közül.
	 */
	public void add(Field f, Direction d) {
		try {
			if(fields.size()==1) {
				fields.push(new PathBlock(f,d));
				return;
			}
			if(f.equals(getLastPathElement().getField())) {
				fields.pop();
				return;
			}
			fields.push(new PathBlock(f,d));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Visszaadj az elõzõ elemet (az utolsó olyat, ami nem az utolsó).
	 */
	public PathBlock getLastPathElement() throws Exception{
		return fields.get(fields.size()-2);
	}
	
	/**
	 * Kirajzolja az Ösvényt, kivéve a a legutolsó elemet (amin a Karakter van).
	 */
	public void draw(Graphics g) {
		PathBlock before = fields.get(0);
		for(int i = 1; i<fields.size(); ++i) {
			before.draw(g,fields.get(i));
			before = fields.get(i);
		}
	}
	
	/**
	 * Stringgé kódolja az Ösvényt, melyben minden karakter az adott elem
	 * aktuális irányának felel meg (u: up, d: down, r: right, l: left).
	 */
	public String toString() {
		String str = new String();
		str =" ";
		for(int i = 0; i < fields.size(); ++i) {
			str+=fields.get(i).toString();
		}
		str+=" ";
		return str;
	}
}
