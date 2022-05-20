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
		boolean first = false;
		for(int i = 0; i < c.getAppliedEffects().size() ; i++)
		{
			if(c.getAppliedEffects().get(i) instanceof Stun) {
				c.setCondition(Condition.INACTIVE);
			}
			else if (c.getAppliedEffects().get(i) instanceof Root) {
				if(first) {
					if(c.getCondition() != Condition.INACTIVE)
						c.setCondition(Condition.ROOTED);
				}
				else 
					first = true;
			}
			else {
				if(c.getCondition() != Condition.INACTIVE || c.getCondition() != Condition.ROOTED)
					c.setCondition(Condition.ACTIVE);
			}
		}
		
		
		//boolean first = false;
		/*boolean s = false;
		boolean r = false;
		for(int i = 0;i < c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i) instanceof Stun) {
				s = true;
				c.setCondition(Condition.INACTIVE);
				//break;
			}
			else if(c.getAppliedEffects().get(i) instanceof Root) {
				if(c.getCondition() != Condition.INACTIVE) {
					r = true;
					c.setCondition(Condition.ROOTED);
				}
				//break;
			}	
		}
		if(!s && !r) {
			c.setCondition(Condition.ACTIVE);
		}*/
		
		/*c.setCondition(Condition.ACTIVE);
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
			
		}*/
		
		
	}
}
