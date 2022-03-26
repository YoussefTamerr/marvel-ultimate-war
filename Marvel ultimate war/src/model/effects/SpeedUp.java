package model.effects;

public class SpeedUp extends Effect{
	int SpeedUp;

	public SpeedUp(String name, int duration, EffectType type,int SpeedUp) {
		super(name, duration, EffectType.BUFF);
		this.SpeedUp=SpeedUp;
	}

	public SpeedUp(int speedUp) {
		super("SpeedUp",13,EffectType.BUFF);
		SpeedUp = speedUp;
	}
	

}
