package model.effects;

import model.abilities.DamagingAbility;
import model.world.Champion;

public class PowerUp extends Effect{

	public PowerUp(int duration) {
		super("PowerUp",duration,EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		for(int i=0; i< c.getAbilities().size();i++) {
			
			if(c.getAbilities().get(i) instanceof DamagingAbility)
			{
				DamagingAbility d = (DamagingAbility)c.getAbilities().get(i);
				d.setDamageAmount();
				
			}
		}
		
	}

	@Override
	public void remove(Champion c) {
		// TODO Auto-generated method stub
		
	}

}
