package model.abilities;

public class Ability {
private String name;
private int manaCost;
private int baseCooldown;
private int currentCooldown;
private int castRange;
private int requiredActionPoints;
AreaOfEffect castArea;

public Ability(String name,int cost, int baseCoolDown, int castRange, AreaOfEffect area ,
		int required) {
	this.manaCost=cost;
	this.baseCooldown=baseCoolDown;
	this.castRange=castRange;
	this.castArea=area;
	this.requiredActionPoints=required;

}

private String getName() {
	return name;
}

private int getManacost() {
	return manaCost;
}

private int getBaseCooldown() {
	return baseCooldown;
}

public int getCurrentCooldown() {
	return currentCooldown;
}

public void setCurrentCooldown(int currentCooldown) {
	this.currentCooldown = currentCooldown;
}

private int getCastRange() {
	return castRange;
}

private int getRequiredActionPoints() {
	return requiredActionPoints;
}

AreaOfEffect getCastArea() {
	return castArea;
}



}
