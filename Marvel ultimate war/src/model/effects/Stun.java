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
		for(int i = 0;i < c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i) instanceof Stun) {
				c.setCondition(Condition.INACTIVE);
				break;
			}
			else if(c.getAppliedEffects().get(i) instanceof Root) {
				if(c.getCondition() != Condition.INACTIVE) {
					c.setCondition(Condition.ROOTED);
				}
				break;
			}
			
		}
		
	}
}
