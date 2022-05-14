package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect{
	public Root(int duration) {
		super("Root", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		if(c.getCondition() != Condition.INACTIVE) {
			c.setCondition(Condition.ROOTED);
		}
		
	}

	@Override
	public void remove(Champion c) {
		for(int i = c.getAppliedEffects().size(); i>=0 ; i--)
		{
			if(c.getAppliedEffects().get(i) instanceof Stun) {
				c.setCondition(Condition.INACTIVE);
			}
			else if (c.getAppliedEffects().get(i) instanceof Root) {
				c.setCondition(Condition.ROOTED);
			}
			else {
				c.setCondition(Condition.ACTIVE);
			}
		}
		
	}
}
