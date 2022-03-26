package model.effects;

public class Shock extends Effect{
	int Shock;
	public Shock(int Shock) {
		super("Shock",65,EffectType.DEBUFF);
		this.Shock = Shock;
	}
	public Shock(String name, int duration, EffectType type) {
		super(name, duration, EffectType.DEBUFF);	
	}
}
