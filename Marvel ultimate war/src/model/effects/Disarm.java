package model.effects;

import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect{

	public Disarm(int duration) {
		super("Disarm",duration,EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		
		DamagingAbility d = new DamagingAbility("Punch",0,1,1,AreaOfEffect.SINGLETARGET,1,50);
		c.getAbilities().add(d);
	}

	@Override
	public void remove(Champion c) {
		
		c.getAbilities().remove(c.getAbilities().size()-1);
	}

}
