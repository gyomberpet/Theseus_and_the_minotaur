package path;


import java.awt.Graphics;
import java.util.Stack;

import map.Direction;
import map.Field;

/**
 * A j�t�kban egy �sv�nyt (�tvonalat) reprezent�l.
 * T�rolja a megfelel� Mez�ket a megadott sorrendben, illetve azt is, hogy azt melyik ir�nyb�l j�vet �rt�k el.
 * Ehhez a PathBlock oszt�lyt haszn�lja fel, ami k�pes az elemeket egyszer�bben kezelni.
 */
public class Path {
	private Stack<PathBlock> fields;
	public Path(Field startingField, Direction d) {
		this.fields = new Stack<PathBlock>();
		fields.push(new PathBlock(startingField,d));
	}
	
	/**
	 * �j elem hozz�ad�sa a t�bbihez.
	 * Ha az elemsz�m egy, mindenk�pp hozz�ad�dik.
	 * Egyebk�nt megn�zi, hogy a k�vetkez� elem nem-e az el�z� (ahonnan j�tt az �sv�ny),
	 * mert ekkor hozz�adas hellyett t�rli az aktu�lisat az elemek k�z�l.
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
	 * Visszaadj az el�z� elemet (az utols� olyat, ami nem az utols�).
	 */
	public PathBlock getLastPathElement() throws Exception{
		return fields.get(fields.size()-2);
	}
	
	/**
	 * Kirajzolja az �sv�nyt, kiv�ve a a legutols� elemet (amin a Karakter van).
	 */
	public void draw(Graphics g) {
		PathBlock before = fields.get(0);
		for(int i = 1; i<fields.size(); ++i) {
			before.draw(g,fields.get(i));
			before = fields.get(i);
		}
	}
	
	/**
	 * Stringg� k�dolja az �sv�nyt, melyben minden karakter az adott elem
	 * aktu�lis ir�ny�nak felel meg (u: up, d: down, r: right, l: left).
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
