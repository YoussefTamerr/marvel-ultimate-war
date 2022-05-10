package model.effects;

import model.world.Champion;

public class Silence extends Effect {
	public Silence(int duration) {
		super("Silence", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		int m = c.getMaxActionPointsPerTurn();
		m+=2;
		int curr = c.getCurrentActionPoints();
		curr+=2;
		c.setMaxActionPointsPerTurn(m);
		c.setCurrentActionPoints(curr);
		
	}

	@Override
	public void remove(Champion c) {
		int m = c.getMaxActionPointsPerTurn();
		m-=2;
		int curr = c.getCurrentActionPoints();
		curr-=2;
		c.setMaxActionPointsPerTurn(m);
		c.setCurrentActionPoints(curr);
		
	}
}
