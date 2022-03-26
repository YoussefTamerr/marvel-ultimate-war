package model.effects;

public class Stun extends Effect{
	public Stun(int stun) {
		super("Stun",83,EffectType.DEBUFF);
		this.Stun = stun;
	}
	int Stun;
	public Stun(String name, int duration, EffectType type,int Stun) {
		super(name, duration, EffectType.DEBUFF);
		this.Stun=Stun;
	}
	

}
