package model.effects;

import model.world.Champion;

public class Shock extends Effect{
	public Shock(int duration) {
		super("Shock",duration,EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		int s = c.getSpeed();
		s -= s*0.1;
		c.setSpeed(s);
		
		int d = c.getAttackDamage();
		d -= d*0.1;
		c.setAttackDamage(d);
		
		int m = c.getMaxActionPointsPerTurn();
		m-=1;
		int curr = c.getCurrentActionPoints();
		curr-=1;
		c.setMaxActionPointsPerTurn(m);
		c.setCurrentActionPoints(curr);
		
	}

	@Override
	public void remove(Champion c) {  ///// revise the remove of attack damage and speed math issue
		int s = c.getSpeed();
		s = (int)(s/1.1);
		c.setSpeed(s);
		
		int d = c.getAttackDamage();
		d = (int)((d* 11.11111)/10);
		c.setAttackDamage(d);
		
		int m = c.getMaxActionPointsPerTurn();
		m+=1;
		int curr = c.getCurrentActionPoints();
		curr+=1;
		c.setMaxActionPointsPerTurn(m);
		c.setCurrentActionPoints(curr);
		
	}
}
