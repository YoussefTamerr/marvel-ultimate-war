package model.world;
import java.awt.Point;

public class Cover {
	
	private int currentHP;
	private Point location;
	
	public Cover(int x, int y)
	{
		int min = 100;
		int max = 999;
		
		this.currentHP= (int)Math.floor(Math.random()*(max-min+1)+min);
		location = new Point(x,y);
		
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}

	public Point getLocation() {
		return location;
	}
	
}
