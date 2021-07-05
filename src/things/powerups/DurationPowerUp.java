package things.powerups;


import map.Field;
import map.Maze;
import things.characters.Theseus;


/**
 * Egy abstract oszt�ly, minden DurationPowerUp bel�le sz�rmazik le.
 * A PowerUp-b�l �r�kl�dik, azzal b�v�tve azt, hogy a hat�sa csak egy bizonyos ideig �rv�nyes, utana lej�r.
 */
public abstract class DurationPowerUp extends PowerUp{
	private int durationTime = 1000;
	public DurationPowerUp(Maze maze, Field currentField, String ID) {
		super(maze, currentField, ID);
	}
	
	/**
	 * Egy egys�ggel cs�kkenti a lej�rati id� sz�ml�l�j�t.
	 */
	public void reduceDurationTime() {
		durationTime--;
	}
	
	/**
	 * Visszaadja a lej�ratig h�tral�v� id�t egy egesz sz�mk�nt.
	 */
	public int getDurationTime() {
		return durationTime;
	}
	
	/**
	 * Abstarct met�dus, amit minden DurationPowerUp lesz�rmazottnak meg kell val�s�tania.
	 * Param�ter�l kap egy Theseust, mivel a hat�s csak r� �rv�nyes�lhet �s j�rhat le.
	 * Ez a met�dus akkor h�v�dik, ha lej�r a DurationPowerUp hat�sa.
	 * Els�dleges feladata a kor�bbi �r�keke vissza�ll�t�sa.
	 */
	abstract public void finished(Theseus t);

}
