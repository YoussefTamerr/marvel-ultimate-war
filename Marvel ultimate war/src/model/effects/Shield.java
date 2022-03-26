package model.effects;

public class Shield extends Effect{
	int Shield;
	public Shield(String name, int duration, EffectType type,int Shield) {
		super(name, duration, EffectType.BUFF);
		this.Shield=Shield;
	}

	public Shield(int Shield) {
		this.Shield=Shield;
	}

}
