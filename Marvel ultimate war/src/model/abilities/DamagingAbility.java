package model.abilities;

public class DamagingAbility extends Ability {
	private int damageAmount;

	public DamagingAbility(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required,
			int damageAmount) {
		super(name, cost, baseCoolDown, castRange, area, required);
		this.setDamageAmount(damageAmount);
	}

	int getDamageAmount() {
		return damageAmount;
	}

	void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
}
