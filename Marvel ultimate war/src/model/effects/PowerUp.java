package model.effects;

import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
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
				int temp = d.getDamageAmount();
				temp += temp*0.2;
				d.setDamageAmount(temp);
				
			}
			else if(c.getAbilities().get(i) instanceof HealingAbility)
			{
				HealingAbility d = (HealingAbility)c.getAbilities().get(i);
				int temp = d.getHealAmount();
				temp += temp*0.2;
				d.setHealAmount(temp);
			}
		}
		
	}

	@Override
	public void remove(Champion c) {
		for(int i=0; i< c.getAbilities().size();i++) {
			
			if(c.getAbilities().get(i) instanceof DamagingAbility)
			{
				DamagingAbility d = (DamagingAbility)c.getAbilities().get(i);
				int temp = d.getDamageAmount();
				temp = (int)(temp/1.2);
				d.setDamageAmount(temp);
				
			}
			else if(c.getAbilities().get(i) instanceof HealingAbility)
			{
				HealingAbility d = (HealingAbility)c.getAbilities().get(i);
				int temp = d.getHealAmount();
				temp = (int)(temp/1.2);
				d.setHealAmount(temp);
			}
		}
		
	}

}
