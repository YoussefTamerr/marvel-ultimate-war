package model.abilities;

public class HealingAbility extends Ability{
	private int healAmount;
	public HealingAbility(String name,int cost, int baseCoolDown, int castRange, AreaOfEffect area ,
		int required,int healAmount ) {
		super(name,cost,baseCoolDown,castRange,area,required);
		this.setHealAmount(healAmount);
	}
	private int getHealAmount() {
		return healAmount;
	}
	private void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}
}
