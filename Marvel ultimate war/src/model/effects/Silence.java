package model.effects;

public class Silence extends Effect {
	int Silence;
	public Silence(String name, int duration, EffectType type,int Silence) {
		super(name, duration, EffectType.DEBUFF);
		this.Silence=Silence;
	}

	public Silence(int Silence) {
		super();
		this.Silence=Silence;
	}
}
