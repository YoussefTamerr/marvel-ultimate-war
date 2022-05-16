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
		for(int i = c.getAppliedEffects().size()-1;i>=0;i++) {
			if(c.getAppliedEffects().get(i) instanceof Stun) {
				c.setCondition(Condition.INACTIVE);
				break;
			}
			else if(c.getAppliedEffects().get(i) instanceof Root) {
				c.setCondition(Condition.ROOTED);
				break;
			}
			
		}
		
	}
}
