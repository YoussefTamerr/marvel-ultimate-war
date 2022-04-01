package model.abilities;

public class DamagingAbility extends Ability {//set damage amount
	private int damageAmount;

	public DamagingAbility(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required,int damageAmount) {
		super(name, cost, baseCoolDown, castRange, area, required);
		this.setDamageAmount(damageAmount);
	}

	public int getDamageAmount() {
		return damageAmount;
	}

	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
}
