package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect{
	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		c.setCondition(Condition.INACTIVE);
		
	}

	@Override
	public void remove(Champion c) { /// active ?????
		c.setCondition(Condition.ACTIVE);
		boolean first = false;
		for(int i = 0;i < c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i) instanceof Stun) {
				if(first) {
					c.setCondition(Condition.INACTIVE);
					//break;
				}
				else 
					first = true;
			}
			else if(c.getAppliedEffects().get(i) instanceof Root) {
				if(c.getCondition() != Condition.INACTIVE) {
					c.setCondition(Condition.ROOTED);
				}
				//break;
			}
			
		}
		
	}
}
