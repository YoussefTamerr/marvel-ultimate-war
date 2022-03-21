package model.abilities;

public class CrowdControlAbility extends Ability{
 private Effect effect;
 public CrowdControlAbility(String name,int cost, int baseCoolDown, int castRange, AreaOfEffect area ,
			int required,Effect effect) {
	 super(name,cost,baseCoolDown,castRange,area,required);
	 this.effect=effect;
	 //el variable el 2smo effect el type Effect nafso mesh mawgood w na2es fa lama yt3emel 7ayb2a fadel bas n3mel el getter bta3 el variable dah
 }
}
