package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect{
	public SpeedUp(int duration) {
		super("SpeedUp",duration,EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		int m = c.getMaxActionPointsPerTurn();
		m+=1;
		int curr = c.getCurrentActionPoints();
		curr+=1;
		c.setMaxActionPointsPerTurn(m);
		c.setCurrentActionPoints(curr);
		
		int s = c.getSpeed();
		s += s*0.15;
		c.setSpeed(s);
		
	}

	@Override
	public void remove(Champion c) {
		int m = c.getMaxActionPointsPerTurn();
		m-=1;
		int curr = c.getCurrentActionPoints();
		curr-=1;
		c.setMaxActionPointsPerTurn(m);
		c.setCurrentActionPoints(curr);
		
		int s = c.getSpeed();
		s = (int)(s/1.15);
		c.setSpeed(s);
		
		
	}

}
