package model.effects;

import model.world.Champion;

public class Embrace extends Effect {
	public Embrace(int duration) {
		super("Embrace", duration, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		int max=c.getMaxHP();
		c.setCurrentHP((int)(c.getCurrentHP()+(0.2*max)));
		int mana=c.getMana();
		c.setMana((int)(mana+(mana*0.2)));
		c.setAttackDamage((int)(c.getAttackDamage()+(c.getAttackDamage()*0.2)));
		c.setSpeed((int)(c.getSpeed()+(c.getSpeed()*0.2)));
	}

	@Override
	public void remove(Champion c) {
		
		c.setAttackDamage((int)(c.getAttackDamage()/1.2));
		c.setSpeed((int)(c.getSpeed()/1.2));
	}

}
