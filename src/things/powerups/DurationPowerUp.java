package things.powerups;


import map.Field;
import map.Maze;
import things.characters.Theseus;


/**
 * Egy abstract osztály, minden DurationPowerUp belõle származik le.
 * A PowerUp-ból öröklõdik, azzal bõvítve azt, hogy a hatása csak egy bizonyos ideig érvényes, utana lejár.
 */
public abstract class DurationPowerUp extends PowerUp{
	private int durationTime = 1000;
	public DurationPowerUp(Maze maze, Field currentField, String ID) {
		super(maze, currentField, ID);
	}
	
	/**
	 * Egy egységgel csökkenti a lejárati idõ számlálóját.
	 */
	public void reduceDurationTime() {
		durationTime--;
	}
	
	/**
	 * Visszaadja a lejáratig hátralévõ idõt egy egesz számként.
	 */
	public int getDurationTime() {
		return durationTime;
	}
	
	/**
	 * Abstarct metódus, amit minden DurationPowerUp leszármazottnak meg kell valósítania.
	 * Paraméterül kap egy Theseust, mivel a hatás csak rá érvényesülhet és járhat le.
	 * Ez a metódus akkor hívódik, ha lejár a DurationPowerUp hatása.
	 * Elsõdleges feladata a korábbi érékeke visszaállítása.
	 */
	abstract public void finished(Theseus t);

}
