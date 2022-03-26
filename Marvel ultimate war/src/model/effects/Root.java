package model.effects;

public class Root extends Effect{
	int Root;
	public Root(String name, int duration, EffectType type,int Root) {
		super(name, duration, EffectType.DEBUFF);
		this.Root=Root;
		// TODO Auto-generated constructor stub
	}

	public Root() {
		// TODO Auto-generated constructor stub
	}

}
