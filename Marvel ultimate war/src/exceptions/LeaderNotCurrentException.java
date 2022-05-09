package exceptions;

public class LeaderNotCurrentException extends  GameActionException {
	public LeaderNotCurrentException() {
		super();
	}
	LeaderNotCurrentException(String s){
		super(s);
	}
}
