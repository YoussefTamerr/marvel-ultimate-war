package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
		
	}
	


	@Override
	public void apply(Champion c) { // set duration to 0 to eliminate effect (point 3)
		int s = c.getSpeed();
		s += s*0.02;
		c.setSpeed(s);
		
		
		
		
		
	}

	@Override
	public void remove(Champion c) { 
		int s = c.getSpeed();
		s = (int)(s/1.02);
		c.setSpeed(s);
		
		
		
	}

}
