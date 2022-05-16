package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class Game {
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player first, Player second) {
		firstPlayer = first;

		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
		for (int i = 0; i < 3; i++) {
			turnOrder.insert(firstPlayer.getTeam().get(i));
			turnOrder.insert(secondPlayer.getTeam().get(i));
		}
	}

	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		Champion current = this.getCurrentChampion();
		

		if (current.getCondition() != Condition.ACTIVE) { //// keep it like this or check appliedEffects arrayList ????
			throw new UnallowedMovementException();
		}

		else if (current.getCurrentActionPoints() < 1) {

			throw new NotEnoughResourcesException();
		} else {

			

			if(d == Direction.UP) {
				int y = current.getLocation().x;
				int x = current.getLocation().y;
				if (y == 4)
					throw new UnallowedMovementException();
				else if (board[y + 1][x] == null) {
					board[y + 1][x] = current;
					board[y][x] = null;
					current.setLocation(new Point(y + 1, x));
					current.setCurrentActionPoints(current.getCurrentActionPoints() - 1);
				} else
					throw new UnallowedMovementException();
			}
			else if(d == Direction.DOWN) {
				int yD =  current.getLocation().x;
				int xD =  current.getLocation().y;
				if (yD == 0)
					throw new UnallowedMovementException();
				else if (board[yD - 1][xD] == null) {
					board[yD - 1][xD] = current;
					board[yD][xD] = null;
					current.setLocation(new Point(yD - 1, xD));
					current.setCurrentActionPoints(current.getCurrentActionPoints() - 1);
				} else
					throw new UnallowedMovementException();
			}
			else if(d == Direction.LEFT) {
				int yL = current.getLocation().x;
				int xL = current.getLocation().y;
				if (xL == 0)
					throw new UnallowedMovementException();
				else if (board[yL][xL - 1] == null) {
					board[yL][xL - 1] = current;
					board[yL][xL] = null;
					current.setLocation(new Point(yL, xL - 1));
					current.setCurrentActionPoints(current.getCurrentActionPoints() - 1);
				} else
					throw new UnallowedMovementException();
			}
			else {
				int yR = current.getLocation().x;
				int xR = current.getLocation().y;
				if (xR == 4)
					throw new UnallowedMovementException();
				else if (board[yR][xR + 1] == null) {
					board[yR][xR + 1] = current;
					board[yR][xR] = null;
					current.setLocation(new Point(yR, xR + 1));
					current.setCurrentActionPoints(current.getCurrentActionPoints() - 1);
				} else
					throw new UnallowedMovementException();
			}
		}

	}
	public void castAbility(Ability a) throws CloneNotSupportedException , NotEnoughResourcesException, AbilityUseException
	/// not finished me7tag kol ability wa crowd wa
	/// wa1st
// waqla 2nd player champion SINGLETARGET, TEAMTARGET damaging wala
// heaaling,DIRECTIONAL,SELFTARGET,SURROUND; damaging wala heaaling contains
// boolean
// wa est5dam el cast range fa el manhatten x1 da ana x0 da eli la2ito
// int distance = Math.abs(x1-x0) + Math.abs(y1-y0); a7sab el distanse ma beny
// wa bein el pont wa hal 2a2al men el cast range team target wa self target wa
// surround
// firstPlayer.getteam hal wa single mediny x wa y ##shield
// law cc mat3malsh 7aga la cover
// law ha dont heal cover
// law da damage cover 3ady
{
Champion c = this.getCurrentChampion();
Champion teammem = null;
ArrayList<Damageable> e = new ArrayList<Damageable>();
int range = a.getCastRange();

int y = (int) c.getLocation().getX();
int x = (int) c.getLocation().getY();
// Champion attackedCH = null;
// int attackedX = -1;
// int attackedY = -1;
int distance = 0;
if(a.getManaCost() > c.getMana()) {
	throw new AbilityUseException();
}
for (int j = 0; j < c.getAppliedEffects().size(); j++) {
	if (c.getAppliedEffects().get(j) instanceof Silence) {
		throw new AbilityUseException();
	}}
if(a.getCastArea()== AreaOfEffect.DIRECTIONAL ||a.getCastArea()== AreaOfEffect.SINGLETARGET) {
	throw new AbilityUseException();
}

switch (a.getCastArea()) {
case TEAMTARGET:
	if (a.getRequiredActionPoints() <= c.getCurrentActionPoints() && a.getManaCost() <= c.getMana()) {
		c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
		c.setMana(c.getMana() - a.getManaCost());
		if (a instanceof CrowdControlAbility) {
			CrowdControlAbility ac = (CrowdControlAbility) a;
			if (ac.getEffect().getType() == EffectType.DEBUFF) {
				if (firstPlayer.getTeam().contains(c)) {
					for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
						int xs = (int) secondPlayer.getTeam().get(i).getLocation().getY();
						int ys = (int) secondPlayer.getTeam().get(i).getLocation().getX();
						distance = Math.abs(x - xs) + Math.abs(y - ys);
						boolean flag = false;
						if (distance <= range) {
							
							if (flag == false) {
								teammem = secondPlayer.getTeam().get(i);
								e.add(teammem);
							}
						}
					}
					a.execute(e);
				} else {
					for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
						int xs = (int) firstPlayer.getTeam().get(i).getLocation().getY();
						int ys = (int) firstPlayer.getTeam().get(i).getLocation().getX();
						distance = Math.abs(x - xs) + Math.abs(y - ys);
						boolean flag = false;
						if (distance <= range) {
							if (flag == false) {
								teammem = firstPlayer.getTeam().get(i);
								e.add(teammem);
							}
						}
						a.execute(e);// ta2riban mara wa7da bas eli tetkatb
					}
				}
			} else {
				if (firstPlayer.getTeam().contains(c)) {
					for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
						int xs = (int) firstPlayer.getTeam().get(i).getLocation().getY();
						int ys = (int) firstPlayer.getTeam().get(i).getLocation().getX();
						distance = Math.abs(x - xs) + Math.abs(y - ys);
						if (distance <= range) {
							teammem = firstPlayer.getTeam().get(i);
							e.add(teammem);
						}
					}
					a.execute(e);
				} else {
					for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
						int xs = (int) secondPlayer.getTeam().get(i).getLocation().getY();
						int ys = (int) secondPlayer.getTeam().get(i).getLocation().getX();
						distance = Math.abs(x - xs) + Math.abs(y - ys);
						if (distance <= range) {
							teammem = secondPlayer.getTeam().get(i);
							e.add(teammem);
						}
					}
					a.execute(e);
				}
			}
		} else if (a instanceof HealingAbility) {
			if (firstPlayer.getTeam().contains(c)) {
				for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
					int xs = (int) firstPlayer.getTeam().get(i).getLocation().getY();
					int ys = (int) firstPlayer.getTeam().get(i).getLocation().getX();
					distance = Math.abs(x - xs) + Math.abs(y - ys);
					if (distance <= range) {
						teammem = firstPlayer.getTeam().get(i);
						e.add(teammem);
					}
				}
				a.execute(e);
			} else {
				for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
					int xs = (int) secondPlayer.getTeam().get(i).getLocation().getY();
					int ys = (int) secondPlayer.getTeam().get(i).getLocation().getX();
					distance = Math.abs(x - xs) + Math.abs(y - ys);
					if (distance <= range) {
						teammem = secondPlayer.getTeam().get(i);
						e.add(teammem);
					}
				}
				a.execute(e);
			}

		} else {
			// Cover C = null;
			Champion CH = null;
			if (firstPlayer.getTeam().contains(c)) {
				for (int i = 0; i < getBoardwidth(); i++) {
					for (int j = 0; j < getBoardheight(); j++) {
						if (board[i][j] != null) {
							// if (board[i][j] instanceof Cover) {//momken tgib errors
							// C = (Cover) board[i][j];
							// int xs = (int) C.getLocation().getY();
							// int ys = (int) C.getLocation().getX();
							// distance = Math.abs(xs - x) + Math.abs(ys - y);
							// if (distance <= range)
							// e.add(C);

							if (board[i][j] instanceof Champion) {
								CH = (Champion) board[i][j];
								if (secondPlayer.getTeam().contains(CH)) {
									int xs = (int) secondPlayer.getTeam().get(i).getLocation().getY();
									int ys = (int) secondPlayer.getTeam().get(i).getLocation().getX();
									distance = Math.abs(xs - x) + Math.abs(ys - y);
									if (distance <= range)
										e.add(CH);
								}

							}
						}
					}
				}
				a.execute(e);
			} else {
				for (int i = 0; i < getBoardwidth(); i++) {
					for (int j = 0; j < getBoardheight(); j++) {
						if (board[i][j] != null) {
							// if (board[i][j] instanceof Cover) {//momken tgib errors
							// C = (Cover) board[i][j];
							// int xs = (int) C.getLocation().getY();
							// int ys = (int) C.getLocation().getX();
							// distance = Math.abs(xs - x) + Math.abs(ys - y);
							// if (distance <= range)
							// e.add(C);

							if (board[i][j] instanceof Champion) {
								CH = (Champion) board[i][j];
								if (firstPlayer.getTeam().contains(CH)) {
									int xs = (int) firstPlayer.getTeam().get(i).getLocation().getY();
									int ys = (int) firstPlayer.getTeam().get(i).getLocation().getX();
									distance = Math.abs(xs - x) + Math.abs(ys - y);
									if (distance <= range)
										e.add(CH);
								}

							}
						}
					}
				}

			}
		}
	} else {
		throw new NotEnoughResourcesException();
	}
	break;
case SELFTARGET:
	if (a.getRequiredActionPoints() <= c.getCurrentActionPoints() && a.getManaCost() <= c.getMana()) {
		c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
		c.setMana(c.getMana() - a.getManaCost());
		e.add(c);
		a.execute(e);
	} else
		throw new NotEnoughResourcesException();
	break;
case SURROUND:
	if (a.getRequiredActionPoints() <= c.getCurrentActionPoints() && a.getManaCost() <= c.getMana()) {
		c.setCurrentActionPoints(a.getRequiredActionPoints() - c.getCurrentActionPoints());
		if (a instanceof CrowdControlAbility) {
			CrowdControlAbility ac = (CrowdControlAbility) a;
			Champion CH = null;
			boolean flag = false;
			// int y = (int) this.getCurrentChampion().getLocation().getX();
			// int x = (int) this.getCurrentChampion().getLocation().getY();
			for (int j = y - 1; j < y + 2; j++) {
				for (int i = x - 1; i < x + 2; i++) {
					if (i >= 0 && i <= 4 && j >= 0 && j <= 4 && i != x && j != y) {
						if (board[j][i] instanceof Champion) {
							CH = (Champion) board[j][i];
							if (ac.getEffect().getType() == EffectType.DEBUFF) {
								flag = false;
								if (firstPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(CH)) {
									for (int k = 0; k < secondPlayer.getTeam().get(i).getAppliedEffects()
											.size(); k++) {
										if (secondPlayer.getTeam().get(i).getAppliedEffects()
												.get(k) instanceof Shield) {
											flag = true;
											secondPlayer.getTeam().get(i).getAppliedEffects().get(k)
													.remove(secondPlayer.getTeam().get(i));
											secondPlayer.getTeam().get(i).getAppliedEffects().remove(k);
										}
									}
									if (flag == false) {
										teammem = secondPlayer.getTeam().get(i);
										e.add(teammem);
									}
								} else if (firstPlayer.getTeam().contains(CH)
										&& secondPlayer.getTeam().contains(c)) {
									for (int k = 0; k < firstPlayer.getTeam().get(i).getAppliedEffects()
											.size(); k++) {
										if (firstPlayer.getTeam().get(i).getAppliedEffects()
												.get(k) instanceof Shield) {
											flag = true;
											firstPlayer.getTeam().get(i).getAppliedEffects().get(k)
													.remove(firstPlayer.getTeam().get(i));
											firstPlayer.getTeam().get(i).getAppliedEffects().remove(k);
										}
									}
									if (flag == false) {
										teammem = firstPlayer.getTeam().get(i);
										e.add(teammem);
									}
								}
							} else {
								flag = false;
								if (firstPlayer.getTeam().contains(c) && firstPlayer.getTeam().contains(CH)) {
									if (flag == false) {
										teammem = firstPlayer.getTeam().get(i);
										e.add(teammem);
									}
								}
								if (secondPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(CH)) {
									if (flag == false) {
										teammem = secondPlayer.getTeam().get(i);
										e.add(teammem);
									}
								}
							}
							a.execute(e);
						}

					}
				}
			}
		} else if (a instanceof HealingAbility) {
			Champion CH = null;
			for (int j = y - 1; j < y + 2; j++) {
				for (int i = x - 1; i < x + 2; i++) {
					if (i >= 0 && i <= 4 && j >= 0 && j <= 4 && i != x && j != y) {
						if (board[j][i] instanceof Champion) {
							CH = (Champion) board[j][i];
							if (secondPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(CH)) {
								teammem = secondPlayer.getTeam().get(i);
								e.add(teammem);
							} else if (firstPlayer.getTeam().contains(c)
									&& firstPlayer.getTeam().contains(CH)) {
								teammem = firstPlayer.getTeam().get(i);
								e.add(teammem);
							}
						}
					}
				}
			}
		} else {
			Champion CH = null;
			Cover C = null;
			boolean flag = false;
			for (int j = y - 1; j < y + 2; j++) {
				for (int i = x - 1; i < x + 2; i++) {
					if (i >= 0 && i <= 4 && j >= 0 && j <= 4 && i != x && j != y) {
						if (board[j][i] instanceof Champion) {
							CH = (Champion) board[j][i];
							flag = false;
							if (firstPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(CH)) {
								for (int k = 0; k < secondPlayer.getTeam().get(i).getAppliedEffects()
										.size(); k++) {
									if (secondPlayer.getTeam().get(i).getAppliedEffects()
											.get(k) instanceof Shield) {
										flag = true;
										secondPlayer.getTeam().get(i).getAppliedEffects().get(k)
												.remove(secondPlayer.getTeam().get(i));
										secondPlayer.getTeam().get(i).getAppliedEffects().remove(k);
									}
								}
								if (flag == false) {
									teammem = secondPlayer.getTeam().get(i);
									e.add(teammem);
								}

							} else if (firstPlayer.getTeam().contains(CH)
									&& secondPlayer.getTeam().contains(c)) {
								for (int k = 0; k < firstPlayer.getTeam().get(i).getAppliedEffects()
										.size(); i++) {
									if (firstPlayer.getTeam().get(i).getAppliedEffects()
											.get(k) instanceof Shield) {
										flag = true;
										firstPlayer.getTeam().get(i).getAppliedEffects().get(k)
												.remove(firstPlayer.getTeam().get(i));
										firstPlayer.getTeam().get(i).getAppliedEffects().remove(k);
									}
								}
								if (flag == false) {
									teammem = firstPlayer.getTeam().get(i);
									e.add(teammem);
								}
							}

						} else if (board[j][i] instanceof Cover) {
							C = (Cover) board[j][i];
							e.add(C);
						}
						a.execute(e);
					}
				}
			}
		}
	} else
		throw new NotEnoughResourcesException();
	break;
}

}


public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, CloneNotSupportedException, InvalidTargetException, AbilityUseException {
Champion c = this.getCurrentChampion();
Champion teammem = null;
ArrayList<Damageable> e = new ArrayList<Damageable>();
Cover cov=null;
int range = a.getCastRange();
int y1 = (int) c.getLocation().getX();
int x1 = (int) c.getLocation().getY();
int distance = 0;
boolean flag= false;
if(a.getManaCost() > c.getMana()) {
	throw new AbilityUseException();
}
for (int j = 0; j < c.getAppliedEffects().size(); j++) {
	if (c.getAppliedEffects().get(j) instanceof Silence) {
		throw new AbilityUseException();
	}}
if(a.getCastArea()== AreaOfEffect.DIRECTIONAL ||a.getCastArea()== AreaOfEffect.SELFTARGET||a.getCastArea()== AreaOfEffect.SURROUND||a.getCastArea()== AreaOfEffect.TEAMTARGET) {
	throw new AbilityUseException();
}
if (a.getRequiredActionPoints() > c.getCurrentActionPoints() && a.getManaCost() > c.getMana()) {
	throw new NotEnoughResourcesException();
} else {
	distance = Math.abs(x - x1) + Math.abs(y - y1);
	if (a instanceof CrowdControlAbility) {
		CrowdControlAbility ac = (CrowdControlAbility) a;
		
		if (ac.getEffect().getType() == EffectType.DEBUFF) {
			if (board[x][y] instanceof Champion) {
				Champion checkchamp = (Champion) board[x][y];
				if (firstPlayer.getTeam().contains(c)) {

					if (secondPlayer.getTeam().contains(checkchamp)) {
						if (distance <= range) {
							for(int i=0;i<checkchamp.getAppliedEffects()
										.size(); i++) {
							if(checkchamp.getAppliedEffects()
											.get(i) instanceof Shield) {
								flag =true;
								checkchamp.getAppliedEffects().get(i)
								.remove(checkchamp);
								checkchamp.getAppliedEffects().remove(i);
							}
							}
							if (flag == false) {
								e.add(checkchamp);
							}
							
							a.execute(e);
						}

					}
				} else {
					if (secondPlayer.getTeam().contains(checkchamp)) {
						if (distance <= range) {
							for(int i=0;i<checkchamp.getAppliedEffects()
									.size(); i++) {
						if(checkchamp.getAppliedEffects()
										.get(i) instanceof Shield) {
							flag =true;
							checkchamp.getAppliedEffects().get(i)
							.remove(checkchamp);
							checkchamp.getAppliedEffects().remove(i);
						}
						}
							e.add(checkchamp);
							a.execute(e);
						}
					}
				}
			}
			else throw new InvalidTargetException();
		}
		else {
			if (ac.getEffect().getType() == EffectType.BUFF) 
				    {

				if (board[x][y] instanceof Champion) {
					Champion checkchamp = (Champion) board[x][y];
					if (firstPlayer.getTeam().contains(c)) {

						if (firstPlayer.getTeam().contains(checkchamp)) {
							if (distance <= range) {
								e.add(checkchamp);
								a.execute(e);
							}

						}
					} else {
						if (secondPlayer.getTeam().contains(checkchamp)) {
							if (distance <= range) {
								e.add(checkchamp);
								a.execute(e);
							}
						}
					}
				}
				else throw new InvalidTargetException();
			
				}
			}
	} else if(a instanceof HealingAbility) {
		distance = Math.abs(x - x1) + Math.abs(y - y1);
				if (board[x][y] instanceof Champion) {
					Champion checkchamp = (Champion) board[x][y];
					if (firstPlayer.getTeam().contains(c)) {

						if (firstPlayer.getTeam().contains(checkchamp)) {
							if (distance <= range) {
								e.add(checkchamp);
								a.execute(e);
							}

						}
					} else {
						if (secondPlayer.getTeam().contains(checkchamp)) {
							if (distance <= range) {
								e.add(checkchamp);
								a.execute(e);
							}
						}
					}
				}
				else throw new InvalidTargetException();
			
				
			
	}
	else {
	if (board[x][y] instanceof Champion) {
		Champion checkchamp = (Champion) board[x][y];
		if (firstPlayer.getTeam().contains(c)) {

			if (firstPlayer.getTeam().contains(checkchamp)) {
				if (distance <= range) {
					e.add(checkchamp);
					a.execute(e);
				}

			}
		} else {
			if (secondPlayer.getTeam().contains(checkchamp)) {
				if (distance <= range) {
					e.add(checkchamp);
					a.execute(e);
				}
			}
		}
	}
	else if(board[x][y] instanceof Cover) {
		cov=(Cover) board[x][y];
		if (distance <= range) {
			e.add(cov);
			a.execute(e);
		}
	}
	else throw new InvalidTargetException();
		
	
	}
}

}

	
public void castAbility(Ability a, Direction d) throws CloneNotSupportedException, AbilityUseException, NotEnoughResourcesException {
	Champion currentchamp = this.getCurrentChampion();
	int Xposition = (int) currentchamp.getLocation().getX();// revise
	int Yposition = (int) currentchamp.getLocation().getY();// revise
	int range = a.getCastRange();
	ArrayList<Damageable> e = new ArrayList<Damageable>();
	if(a.getManaCost() > currentchamp.getMana()) {
		throw new AbilityUseException();
	}
	for (int j = 0; j < currentchamp.getAppliedEffects().size(); j++) {
		if (currentchamp.getAppliedEffects().get(j) instanceof Silence) {
			throw new AbilityUseException();
		}}
	if(a.getCastArea()== AreaOfEffect.SINGLETARGET ||a.getCastArea()== AreaOfEffect.SELFTARGET||a.getCastArea()== AreaOfEffect.SURROUND||a.getCastArea()== AreaOfEffect.TEAMTARGET) {
		throw new AbilityUseException();
	}
	if (a.getRequiredActionPoints() > currentchamp.getCurrentActionPoints() && a.getManaCost() > currentchamp.getMana()) {
		throw new NotEnoughResourcesException();
	}

	switch (d) {
	case UP: {
		int allowableRange = 0;
		if (range + Xposition <= 4)
			allowableRange = range;
		else
			allowableRange = 4 - Xposition;
		if (a instanceof DamagingAbility) {

			for (int i = 1; i <= allowableRange; i++) {
				if (board[Xposition+i][Yposition] instanceof Champion) {
					Champion x = (Champion) board[Xposition+i][Yposition];

					if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam().contains(currentchamp)) {
						boolean shield = false;
						for (int z = 0; z < x.getAppliedEffects().size(); z++) {
							if (x.getAppliedEffects().get(z) instanceof Shield) {
								shield = true;
								break;
							} else
								shield = false;
						}
						if (shield == true) {
							for (int z = 0; z < x.getAppliedEffects().size(); z++) {
								if (x.getAppliedEffects().get(z) instanceof Shield) {
									x.getAppliedEffects().remove(z);
									break;
								}
							}
						} else
							e.add(x);
					}
				}

				else {
					if (board[Xposition+i][Yposition] instanceof Cover) {
						Cover c = (Cover) board[Xposition+i][Yposition];
						e.add(c);
					}
				}
			}

			a.execute(e);
		}

		else {
			if (a instanceof HealingAbility) {
				for (int i = 1; i <= allowableRange; i++) {
					if (board[Xposition+i][Yposition] instanceof Champion) {
						Champion x = (Champion) board[Xposition+i][Yposition];

						if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam().contains(currentchamp))
							e.add(x);
					}
				}
				a.execute(e);
			} else {
				if (a instanceof CrowdControlAbility) {
					CrowdControlAbility cc = (CrowdControlAbility) a;
					if (cc.getEffect().getType() == EffectType.BUFF) {
						for (int i = 1; i <= allowableRange; i++) {
							if (board[Xposition+i][Yposition] instanceof Champion) {
								Champion x = (Champion) board[Xposition+i][Yposition];

								if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam()
										.contains(currentchamp))
									e.add(x);

							}

						}

					} else if (cc.getEffect().getType() == EffectType.DEBUFF) {
						for (int i = 1; i <= allowableRange; i++) {
							if (board[Xposition+i][Yposition] instanceof Champion) {
								Champion x = (Champion) board[Xposition+i][Yposition];

								if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam()
										.contains(currentchamp))
									e.add(x);

							}
						}
					}
				}
			}
		}
	}
	case DOWN: {
		int allowableRange = 0;
		if (Xposition - range >= 0)
			allowableRange = range;
		else
			allowableRange = Xposition;
		if (a instanceof DamagingAbility) {

			for (int i = 1; i <= allowableRange; i++) {
				if (board[Xposition-i][Yposition] instanceof Champion) {
					Champion x = (Champion) board[Xposition-i][Yposition];

					if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam().contains(currentchamp)) {
						boolean shield = false;
						for (int z = 0; z < x.getAppliedEffects().size(); z++) {
							if (x.getAppliedEffects().get(z) instanceof Shield) {
								shield = true;
								break;
							} else
								shield = false;
						}
						if (shield == true) {
							for (int z = 0; z < x.getAppliedEffects().size(); z++) {
								if (x.getAppliedEffects().get(z) instanceof Shield) {
									x.getAppliedEffects().remove(z);
									break;
								}
							}
						} else
							e.add(x);
					}
				}

				else {
					if (board[Xposition-i][Yposition] instanceof Cover) {
						Cover c = (Cover) board[Xposition-i][Yposition];
						e.add(c);
					}
				}
			}

			a.execute(e);
		}

		else {
			if (a instanceof HealingAbility) {
				for (int i = 1; i <= allowableRange; i++) {
					if (board[Xposition-i][Yposition] instanceof Champion) {
						Champion x = (Champion) board[Xposition-i][Yposition];

						if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam().contains(currentchamp))
							e.add(x);
					}
				}
				a.execute(e);
			} else {
				if (a instanceof CrowdControlAbility) {
					CrowdControlAbility cc = (CrowdControlAbility) a;
					if (cc.getEffect().getType() == EffectType.BUFF) {
						for (int i = 1; i <= allowableRange; i++) {
							if (board[Xposition-i][Yposition] instanceof Champion) {
								Champion x = (Champion) board[Xposition-i][Yposition];

								if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam()
										.contains(currentchamp))
									e.add(x);

							}

						}

					} else if (cc.getEffect().getType() == EffectType.DEBUFF) {
						for (int i = 1; i <= allowableRange; i++) {
							if (board[Xposition-i][Yposition] instanceof Champion) {
								Champion x = (Champion) board[Xposition-i][Yposition];

								if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam()
										.contains(currentchamp))
									e.add(x);

							}
						}
					}
				}
			}
		}
	}
	case RIGHT: {

		int allowableRange = 0;
		if (range + Yposition <= 4)
			allowableRange = range;
		else
			allowableRange = 4 - Yposition;
		if (a instanceof DamagingAbility) {

			for (int i = 1; i <= allowableRange; i++) {
				if (board[Xposition][Yposition+i] instanceof Champion) {
					Champion x = (Champion) board[Xposition][Yposition+i];

					if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam().contains(currentchamp)) {
						boolean shield = false;
						for (int z = 0; z < x.getAppliedEffects().size(); z++) {
							if (x.getAppliedEffects().get(z) instanceof Shield) {
								shield = true;
								break;
							} else
								shield = false;
						}
						if (shield == true) {
							for (int z = 0; z < x.getAppliedEffects().size(); z++) {
								if (x.getAppliedEffects().get(z) instanceof Shield) {
									x.getAppliedEffects().remove(z);
									break;
								}
							}
						} else
							e.add(x);
					}
				}

				else {
					if (board[Xposition][Yposition+i] instanceof Cover) {
						Cover c = (Cover) board[Xposition][Yposition+i];
						e.add(c);
					}
				}
			}

			a.execute(e);
		}

		else {
			if (a instanceof HealingAbility) {
				for (int i = 1; i <= allowableRange; i++) {
					if (board[Xposition][Yposition+i] instanceof Champion) {
						Champion x = (Champion) board[Xposition][Yposition+i];

						if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam().contains(currentchamp))
							e.add(x);
					}
				}
				a.execute(e);
			} else {
				if (a instanceof CrowdControlAbility) {
					CrowdControlAbility cc = (CrowdControlAbility) a;
					if (cc.getEffect().getType() == EffectType.BUFF) {
						for (int i = 1; i <= allowableRange; i++) {
							if (board[Xposition][Yposition+i] instanceof Champion) {
								Champion x = (Champion) board[Xposition][Yposition+i];

								if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam()
										.contains(currentchamp))
									e.add(x);

							}

						}

					} else if (cc.getEffect().getType() == EffectType.DEBUFF) {
						for (int i = 1; i <= allowableRange; i++) {
							if (board[Xposition][Yposition+i] instanceof Champion) {
								Champion x = (Champion) board[Xposition][Yposition+i];

								if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam()
										.contains(currentchamp))
									e.add(x);

							}
						}
					}
				}
			}
		}
	}
	case LEFT: {
		int allowableRange = 0;
		if (Yposition - range >= 0)
			allowableRange = range;
		else
			allowableRange = Yposition;
		if (a instanceof DamagingAbility) {

			for (int i = 1; i <= allowableRange; i++) {
				if (board[Xposition][Yposition- i] instanceof Champion) {
					Champion x = (Champion) board[Xposition][Yposition- i];

					if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam().contains(currentchamp)) {
						boolean shield = false;
						for (int z = 0; z < x.getAppliedEffects().size(); z++) {
							if (x.getAppliedEffects().get(z) instanceof Shield) {
								shield = true;
								break;
							} else
								shield = false;
						}
						if (shield == true) {
							for (int z = 0; z < x.getAppliedEffects().size(); z++) {
								if (x.getAppliedEffects().get(z) instanceof Shield) {
									x.getAppliedEffects().remove(z);
									break;
								}
							}
						} else
							e.add(x);
					}
				}

				else {
					if (board[Xposition][Yposition- i] instanceof Cover) {
						Cover c = (Cover) board[Xposition][Yposition- i];
						e.add(c);
					}
				}
			}

			a.execute(e);
		}

		else {
			if (a instanceof HealingAbility) {
				for (int i = 1; i <= allowableRange; i++) {
					if (board[Xposition][Yposition- i] instanceof Champion) {
						Champion x = (Champion) board[Xposition][Yposition- i];

						if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam().contains(currentchamp))
							e.add(x);
					}
				}
				a.execute(e);
			} else {
				if (a instanceof CrowdControlAbility) {
					CrowdControlAbility cc = (CrowdControlAbility) a;
					if (cc.getEffect().getType() == EffectType.BUFF) {
						for (int i = 1; i <= allowableRange; i++) {
							if (board[Xposition][Yposition- i] instanceof Champion) {
								Champion x = (Champion) board[Xposition][Yposition- i];

								if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam()
										.contains(currentchamp))
									e.add(x);

							}

						}

					} else if (cc.getEffect().getType() == EffectType.DEBUFF) {
						for (int i = 1; i <= allowableRange; i++) {
							if (board[Xposition][Yposition- i] instanceof Champion) {
								Champion x = (Champion) board[Xposition][Yposition- i];

								if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam()
										.contains(currentchamp))
									e.add(x);

							}
						}
					}
				}
			}
		}
	}
	}
}
	
	
	
	public Damageable getTarget(int height,int width,Direction d,int range) {
		for(int i=1;i<=range;i++){ 
			if(d==Direction.UP){
				if(height+i<=4 && board[height + i][width] != null){
					return (Damageable) board[height+i][width];
				}
			}
			else if(d==Direction.DOWN) {
				if (height - i >= 0 && board[height - i][width] != null) {
					return (Damageable) board[height-i][width];
				}
			}
			else if(d==Direction.RIGHT) {
				if ((width + i) <= 4 && board[height][width + i] != null) {
					
					return (Damageable) board[height][width + i];
				}
			} else {
				if (width - i >= 0 && board[height][width -i] != null) {
					
					return (Damageable) board[height][width - i];
				}	
			}
				
			
			
		}
		return null;

	}

	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException  {
		Champion c = this.getCurrentChampion();
		boolean isHero = false;
		boolean isVillain = false;
		boolean isAnti = false;
		if(c instanceof Hero)
		{
			isHero = true;
		}
		else if(c instanceof Villain)
		{
			isVillain = true;
		}
		else
			isAnti = true;
		int range = c.getAttackRange();
		int dmg = c.getAttackDamage();
		int height =c.getLocation().x;
		int width = c.getLocation().y;
		for(int i = 0; i<c.getAppliedEffects().size(); i++)
		{
			if(c.getAppliedEffects().get(i) instanceof Disarm)
			{
				throw new ChampionDisarmedException();
			}
		}
		if (c.getCurrentActionPoints() < 2) {
			
			throw new NotEnoughResourcesException();
			
		} else {
			c.setCurrentActionPoints(c.getCurrentActionPoints() - 2);
			Damageable target = getTarget(height, width, d, range);
			if(target instanceof Cover)
			{
				Cover attackedC = (Cover) target;
				int newHeight = attackedC.getLocation().x;
				int newWidth = attackedC.getLocation().y;
				attackedC.setCurrentHP(attackedC.getCurrentHP()-c.getAttackDamage());
				if(attackedC.getCurrentHP() == 0)
				{
					board[newHeight][newWidth] = null;
				}
				
			}
			else if(target instanceof Champion)
			{
				Champion attackedCH = (Champion) target;
				int newHeight = attackedCH.getLocation().x;
				int newWidth = attackedCH.getLocation().y;
				boolean isShield = false;
				boolean isDodge = false;
				for(int i =0;i<attackedCH.getAppliedEffects().size();i++)
				{
					if(attackedCH.getAppliedEffects().get(i) instanceof Shield)
					{
						isShield = true;
						attackedCH.getAppliedEffects().get(i).remove(attackedCH);
						attackedCH.getAppliedEffects().remove(i);
						
						break;
					}
					else if(attackedCH.getAppliedEffects().get(i) instanceof Dodge)
					{
						isDodge = true;
						break;
					}
				}
				if(isShield)
				{
					dmg = 0;
				}
				if(isDodge)
				{
					Random r = new Random();
					if(r.nextBoolean())
						dmg = 0;
				}
				if (isHero) {
					if (attackedCH instanceof Hero) {
						attackedCH.setCurrentHP(attackedCH.getCurrentHP() - dmg);
					}
					else {
						dmg = (int) (dmg *1.5);
						attackedCH.setCurrentHP((int)(attackedCH.getCurrentHP() - dmg));
					}

				}
				else if (isVillain) {
					if (attackedCH instanceof Villain) {
						attackedCH.setCurrentHP(attackedCH.getCurrentHP() - dmg);
					}
					else {
						dmg = (int) (dmg *1.5);
						attackedCH.setCurrentHP((int)(attackedCH.getCurrentHP() - dmg));					}

				}
				else {
						if (attackedCH instanceof AntiHero) {
							attackedCH.setCurrentHP(attackedCH.getCurrentHP() - dmg);
						}
						else {
							dmg = (int) (dmg *1.5);
							attackedCH.setCurrentHP((int)(attackedCH.getCurrentHP() - dmg));						}

					
				}
				if(attackedCH.getCurrentHP() == 0)
				{
					attackedCH.setCondition(Condition.KNOCKEDOUT);
					board[newHeight][newWidth] = null;
					Stack<Comparable> temp = new Stack<Comparable>();
					for(int i =0; i< turnOrder.size(); i++)
					{
						if(turnOrder.peekMin() != attackedCH) {
							temp.add(turnOrder.remove());
						}
						turnOrder.remove();
					}
					while(!temp.isEmpty()) {
						turnOrder.insert(temp.pop());
					}
				}
			}
		}
			
	}

	public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException  {
		Champion c = this.getCurrentChampion();
		if (this.getFirstPlayer().getLeader() != c && this.getSecondPlayer().getLeader() != c)
			throw new LeaderNotCurrentException();
		else if ((this.getFirstPlayer().getLeader()==c && firstLeaderAbilityUsed) || (this.getSecondPlayer().getLeader() == c && secondLeaderAbilityUsed))
			throw new LeaderAbilityAlreadyUsedException();
		else {
			ArrayList<Champion> targets = new ArrayList<Champion>();
			if (c instanceof Hero) {
				if (this.getFirstPlayer().getLeader() == c) {
					
						for (int i = 0; i < this.getFirstPlayer().getTeam().size(); i++) {
							if (this.getFirstPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT) {
								targets.add(this.getFirstPlayer().getTeam().get(i));
							}
						}
						this.firstLeaderAbilityUsed = true;
				} else if (this.getSecondPlayer().getLeader() == c) {
					for (int i = 0; i < this.getSecondPlayer().getTeam().size(); i++) {
						if (this.getSecondPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT) {
							targets.add(this.getSecondPlayer().getTeam().get(i));
						}
					}
					this.secondLeaderAbilityUsed = true;

				}
			}
			else if(c instanceof Villain) {
				if(firstPlayer.getTeam().contains(c)) {
					for(int i = 0; i<3 ; i++) {
						if(secondPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT && secondPlayer.getTeam().get(i).getCurrentHP()<secondPlayer.getTeam().get(i).getMaxHP()*0.3)
							targets.add(secondPlayer.getTeam().get(i));
					}
					this.firstLeaderAbilityUsed = true;
				}
				else {
					for(int i = 0; i<3 ; i++) {
						if(firstPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT && firstPlayer.getTeam().get(i).getCurrentHP()<firstPlayer.getTeam().get(i).getMaxHP()*0.3)
							targets.add(firstPlayer.getTeam().get(i));
					}
					this.secondLeaderAbilityUsed = true;
				}
			}
			else {
				for(int i = 0; i<3; i++)
				{
					if ((this.getFirstPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT) && (this.getFirstPlayer().getTeam().get(i) != this.getFirstPlayer().getLeader())) {
						targets.add(this.getFirstPlayer().getTeam().get(i));
					}
					if ((this.getSecondPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT) && (this.getSecondPlayer().getTeam().get(i) != this.getSecondPlayer().getLeader())) {
						targets.add(this.getSecondPlayer().getTeam().get(i));
					}
					if(firstPlayer.getTeam().contains(c)) {
						this.firstLeaderAbilityUsed = true;
						
					}
					else 
						this.secondLeaderAbilityUsed = true;
				}
				
			}
			
			c.useLeaderAbility(targets);
			
		}
	}

	public void endTurn() { // do not understand whether to return the inactive champions to the turnorder
							// or not
		turnOrder.remove();
		if (turnOrder.isEmpty()) {
			prepareChampionTurns();
		} else {
			ArrayList<Object> temp = new ArrayList<Object>();
			Champion c = (Champion) turnOrder.peekMin();
			while (c.getCondition() != Condition.ACTIVE) {
				
				for(int i = 0; i< c.getAppliedEffects().size();i++) { /// hwar stun da ghareeb es2l el shabab
					c.getAppliedEffects().get(i).setDuration(c.getAppliedEffects().get(i).getDuration() - 1); ////// revise
					if(c.getAppliedEffects().get(i).getDuration() == 0) {
						c.getAppliedEffects().get(i).remove(c);
						c.getAppliedEffects().remove(i);
					}
					/*if(c.getAppliedEffects().get(i) instanceof Stun) {
						c.getAppliedEffects().get(i).setDuration(c.getAppliedEffects().get(i).getDuration() - 1); ////// revise
						if(c.getAppliedEffects().get(i).getDuration() == 0) {
							c.getAppliedEffects().get(i).remove(c);
							c.getAppliedEffects().remove(i);
						}
					}*/
				}
				for (int i = 0; i < c.getAbilities().size(); i++) {
					c.getAbilities().get(i).setCurrentCooldown(c.getAbilities().get(i).getCurrentCooldown() - 1); //// revise
				}
				temp.add(turnOrder.remove());
				c = (Champion) turnOrder.peekMin();
			}
			c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
			for (int i = 0; i < c.getAbilities().size(); i++) {
				c.getAbilities().get(i).setCurrentCooldown(c.getAbilities().get(i).getCurrentCooldown() - 1); //// revise
			}
			for (int i = 0; i < c.getAppliedEffects().size(); i++) {
				c.getAppliedEffects().get(i).setDuration(c.getAppliedEffects().get(i).getDuration() - 1); ////// revise
				if(c.getAppliedEffects().get(i).getDuration() == 0) {
					c.getAppliedEffects().get(i).remove(c);
					c.getAppliedEffects().remove(i);
				}

			}
		}
	}

	private void prepareChampionTurns() {
		for (int i = 0; i < 3; i++) {
			if (this.getFirstPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT) {
				turnOrder.insert(this.getFirstPlayer().getTeam().get(i));
			}
			if (this.getSecondPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT) {
				turnOrder.insert(this.getSecondPlayer().getTeam().get(i));
			}
		}
	}

	public Champion getCurrentChampion() {
		return (Champion) turnOrder.peekMin();
	}

	public Player checkGameOver() {
		int f = 0;
		int s = 0;
		for (int i = 0; i < 3; i++) {

			if (firstPlayer.getTeam().get(i).getCondition() == Condition.KNOCKEDOUT)
				f++;
		}
		if (f == 3)
			return secondPlayer;
		for (int i = 0; i < 3; i++) {
			if (secondPlayer.getTeam().get(i).getCondition() == Condition.KNOCKEDOUT)
				s++;
		}
		if (s == 3)
			return firstPlayer;
		return null;
		
		
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}

	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
}
