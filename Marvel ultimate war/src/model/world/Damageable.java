package model.world;
import java.awt.Point;

public interface Damageable {
	public Point getLocation();
	public int getCurrentHP();
	void setCurrentHP(int hp);
}
