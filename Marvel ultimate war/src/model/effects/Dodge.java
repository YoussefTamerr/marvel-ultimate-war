package model.effects;

public class Dodge extends Effect {
	int Dodge;
	public Dodge(String name, int duration, EffectType type,int Dodge) {
		super(name, duration, EffectType.BUFF);
		this.Dodge=Dodge;
	}
	public Dodge(int Dodge) {
		super("Dodge",6,EffectType.BUFF);
		this.Dodge=Dodge;
	}

}
