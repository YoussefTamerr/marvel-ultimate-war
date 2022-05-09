package model.effects;

import model.world.Champion;

public class Dodge extends Effect {
	public Dodge(int duration) {
		super("Dodge", duration, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		int s = c.getSpeed();
		s += s*0.05;
		c.setSpeed(s);
	}

	@Override
	public void remove(Champion c) {
		int s = c.getSpeed();
		s = (int) (s/1.05);
		c.setSpeed(s);
		
	}
}
