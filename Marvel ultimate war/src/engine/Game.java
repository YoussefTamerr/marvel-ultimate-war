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
		prepareChampionTurns();
	}

	public Champion getCurrentChampion() {
		return (Champion) turnOrder.peekMin();
	}

	public Player checkGameOver() {
		boolean f = false;
		boolean s = false;
		for (int i = 0; i < firstPlayer.getTeam().size(); i++) {

			if (firstPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
				f = true;
		}
		for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
			if (secondPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
				s = true;
		}

		if (!f) {
			return secondPlayer;
		} else if (!s) {
			return firstPlayer;
		} else
			return null;

	}

	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		Champion current = this.getCurrentChampion();

		if (current.getCondition() != Condition.ACTIVE) { //// keep it like this or check appliedEffects arrayList ????
			throw new UnallowedMovementException();
		}

		else if (current.getCurrentActionPoints() < 1) {

			throw new NotEnoughResourcesException();
		} else {

			if (d == Direction.UP) {
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
			} else if (d == Direction.DOWN) {
				int yD = current.getLocation().x;
				int xD = current.getLocation().y;
				if (yD == 0)
					throw new UnallowedMovementException();
				else if (board[yD - 1][xD] == null) {
					board[yD - 1][xD] = current;
					board[yD][xD] = null;
					current.setLocation(new Point(yD - 1, xD));
					current.setCurrentActionPoints(current.getCurrentActionPoints() - 1);
				} else
					throw new UnallowedMovementException();
			} else if (d == Direction.LEFT) {
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
			} else {
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

	public Damageable getTarget(int height, int width, Direction d, int range) {
		for (int i = 1; i <= range; i++) {
			if (d == Direction.UP) {
				if (height + i <= 4 && board[height + i][width] != null) {
					if(board[height + i][width] instanceof Champion) {
						if(!checkTeammate(this.getCurrentChampion(),(Champion) board[height + i][width]))
							return (Damageable) board[height + i][width];
					}
					else 
						return (Damageable) board[height + i][width];
				}
			} else if (d == Direction.DOWN) {
				if (height - i >= 0 && board[height - i][width] != null) {
					if(board[height - i][width] instanceof Champion) {
						if(!checkTeammate(this.getCurrentChampion(),(Champion) board[height - i][width]))
							return (Damageable) board[height - i][width];
					}
					else 
						return (Damageable) board[height - i][width];
				}
			} else if (d == Direction.RIGHT) {
				if ((width + i) <= 4 && board[height][width + i] != null) {

					if(board[height][width + i] instanceof Champion) {
						if(!checkTeammate(this.getCurrentChampion(),(Champion) board[height][width + i]))
							return (Damageable) board[height][width + i];
					}
					else 
						return (Damageable) board[height][width + i];
				}
			} else {
				if (width - i >= 0 && board[height][width - i] != null) {
					if(board[height][width - i] instanceof Champion) {
						if(!checkTeammate(this.getCurrentChampion(),(Champion) board[height][width - i]))
							return (Damageable) board[height][width - i];
					}
					else 
						return (Damageable) board[height][width - i];
				}
			}

		}
		return null;

	}

	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException {
		Champion c = this.getCurrentChampion();
		boolean isHero = false;
		boolean isVillain = false;
		boolean isAnti = false;
		if (c instanceof Hero) {
			isHero = true;
		} else if (c instanceof Villain) {
			isVillain = true;
		} else
			isAnti = true;
		int range = c.getAttackRange();
		int dmg = c.getAttackDamage();
		int height = c.getLocation().x;
		int width = c.getLocation().y;
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if (c.getAppliedEffects().get(i) instanceof Disarm) {
				throw new ChampionDisarmedException();
			}
		}
		if (c.getCurrentActionPoints() < 2) {

			throw new NotEnoughResourcesException();

		} else {
			c.setCurrentActionPoints(c.getCurrentActionPoints() - 2);
			Damageable target = getTarget(height, width, d, range);
			if (target instanceof Cover) {
				Cover attackedC = (Cover) target;
				int newHeight = attackedC.getLocation().x;
				int newWidth = attackedC.getLocation().y;
				attackedC.setCurrentHP(attackedC.getCurrentHP() - c.getAttackDamage());
				if (attackedC.getCurrentHP() == 0) {
					board[newHeight][newWidth] = null;
				}

			} else if (target instanceof Champion) {
				Champion attackedCH = (Champion) target;
				int newHeight = attackedCH.getLocation().x;
				int newWidth = attackedCH.getLocation().y;
				boolean isShield = false;
				boolean isDodge = false;
				for (int i = 0; i < attackedCH.getAppliedEffects().size(); i++) {
					if (attackedCH.getAppliedEffects().get(i) instanceof Shield) {
						isShield = true;
						attackedCH.getAppliedEffects().get(i).remove(attackedCH);
						attackedCH.getAppliedEffects().remove(i);
						i = i - 1;
					} else if (attackedCH.getAppliedEffects().get(i) instanceof Dodge) {
						isDodge = true;
						break;
					}
				}
				if (isShield) {
					dmg = 0;
				}
				if (isDodge) {
					Random r = new Random();
					if (r.nextBoolean())
						dmg = 0;
				}
				if (isHero) {
					if (attackedCH instanceof Hero) {
						attackedCH.setCurrentHP(attackedCH.getCurrentHP() - dmg);
					} else {
						dmg = (int) (dmg * 1.5);
						attackedCH.setCurrentHP((int) (attackedCH.getCurrentHP() - dmg));
					}

				} else if (isVillain) {
					if (attackedCH instanceof Villain) {
						attackedCH.setCurrentHP(attackedCH.getCurrentHP() - dmg);
					} else {
						dmg = (int) (dmg * 1.5);
						attackedCH.setCurrentHP((int) (attackedCH.getCurrentHP() - dmg));
					}

				} else {
					if (attackedCH instanceof AntiHero) {
						attackedCH.setCurrentHP(attackedCH.getCurrentHP() - dmg);
					} else {
						dmg = (int) (dmg * 1.5);
						attackedCH.setCurrentHP((int) (attackedCH.getCurrentHP() - dmg));
					}

				}
				if (attackedCH.getCurrentHP() == 0) {
					attackedCH.setCondition(Condition.KNOCKEDOUT);
					board[newHeight][newWidth] = null;
					removeFromTeam(attackedCH);
				}	
					/*Stack<Champion> temp = new Stack<Champion>();
					while (!turnOrder.isEmpty()) {
						if ((Champion) turnOrder.peekMin() != attackedCH) {
							temp.add((Champion) turnOrder.remove());
							
						} else {
							turnOrder.remove();
						}
					}
					while (!temp.isEmpty()) {
						turnOrder.insert(temp.pop());
					}
					if(firstPlayer.getTeam().contains(attackedCH)) {
						firstPlayer.getTeam().remove(attackedCH);
					}
					else 
						secondPlayer.getTeam().remove(attackedCH);*/
					
				
					
					/*Stack<Comparable> temp = new Stack<Comparable>();
					for (int i = 0; i < turnOrder.size(); i++) {
						if (turnOrder.peekMin() != attackedCH) {
							temp.add(turnOrder.remove());
						}
						turnOrder.remove();
					}
					while (!temp.isEmpty()) {
						turnOrder.insert(temp.pop());
					}*/
					
					/*if (this.getFirstPlayer().getTeam().contains(attackedCH)) {
						for (Champion ch : this.getFirstPlayer().getTeam()) {
							if (ch == attackedCH) {
								this.getFirstPlayer().getTeam().remove(attackedCH);
							}
						}
					} else {
						for (Champion ch : this.getSecondPlayer().getTeam()) {
							if (ch == attackedCH) {
								this.getSecondPlayer().getTeam().remove(attackedCH);
							}
						}
					}*/
					
					
				
			}
		}

	}

	public void castAbility(Ability a)
			throws CloneNotSupportedException, NotEnoughResourcesException, AbilityUseException {
		Champion c = getCurrentChampion();
		ArrayList<Damageable> e = new ArrayList<Damageable>();
		int range = a.getCastRange();

		if (a.getCurrentCooldown() != 0) {
			throw new AbilityUseException();
		}

		int y =  c.getLocation().y;
		int x =  c.getLocation().x;
		// Champion attackedCH = null;
		// int attackedX = -1;
		// int attackedY = -1;
		int distance = 0;
		if (a.getManaCost() > c.getMana()) {
			// throw new AbilityUseException();
			throw new NotEnoughResourcesException();
		}
		for (int j = 0; j < c.getAppliedEffects().size(); j++) {
			if (c.getAppliedEffects().get(j) instanceof Silence) {
				throw new AbilityUseException();
			}
		}
		if (a.getCastArea() == AreaOfEffect.DIRECTIONAL || a.getCastArea() == AreaOfEffect.SINGLETARGET) {
			throw new AbilityUseException();
		}
		if (a.getRequiredActionPoints() > c.getCurrentActionPoints()) {
			throw new NotEnoughResourcesException();
		}
		switch (a.getCastArea()) {
		case TEAMTARGET:
			if (a instanceof CrowdControlAbility) {
				CrowdControlAbility ac = (CrowdControlAbility) a;
				if (ac.getEffect().getType() == EffectType.DEBUFF) {
					if (firstPlayer.getTeam().contains(c)) {
						for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
							int xs =  secondPlayer.getTeam().get(i).getLocation().x;
							int ys =  secondPlayer.getTeam().get(i).getLocation().y;
							distance = Math.abs(x - xs) + Math.abs(y - ys);
							// boolean flag = false;
							if (distance <= range) {
								/*
								 * for (int j = 0; j < secondPlayer.getTeam().get(i).getAppliedEffects().size();
								 * j++) { if (secondPlayer.getTeam().get(i).getAppliedEffects() .get(j)
								 * instanceof Shield) { flag = true;
								 * secondPlayer.getTeam().get(i).getAppliedEffects().get(j)// bagib el // effect
								 * // wa ba call el // remove el fi // subclass bta3 // effect wa // badiha //
								 * el champion // el // 3ayz a removo .remove(secondPlayer.getTeam().get(i));
								 * secondPlayer.getTeam().get(i).getAppliedEffects().remove(j); j = j -1; //
								 * byshil // el // effect // nafso // men // applied // effect } }
								 */
								// if (flag == false) {

								e.add((Damageable) secondPlayer.getTeam().get(i));
								// }
							}
						}
						// a.execute(e);
					} else {
						for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
							int xs = firstPlayer.getTeam().get(i).getLocation().x;
							int ys = firstPlayer.getTeam().get(i).getLocation().y;
							distance = Math.abs(x - xs) + Math.abs(y - ys);
							boolean flag = false;
							if (distance <= range) {
								/*
								 * for (int j = 0; j < firstPlayer.getTeam().get(i).getAppliedEffects().size();
								 * j++) { if (firstPlayer.getTeam().get(i).getAppliedEffects().get(j) instanceof
								 * Shield) { flag = true;
								 * firstPlayer.getTeam().get(i).getAppliedEffects().get(j)
								 * .remove(firstPlayer.getTeam().get(i));
								 * firstPlayer.getTeam().get(i).getAppliedEffects().remove(j); j = j -1; } }
								 */
								if (flag == false) {

									e.add((Damageable) firstPlayer.getTeam().get(i));
								}
							}
							// a.execute(e);// ta2riban mara wa7da bas eli tetkatb
						}
					}

				} else {
					if (firstPlayer.getTeam().contains(c)) {
						for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
							int xs =  firstPlayer.getTeam().get(i).getLocation().x;
							int ys =  firstPlayer.getTeam().get(i).getLocation().y;
							distance = Math.abs(x - xs) + Math.abs(y - ys);
							if (distance <= range) {

								e.add((Damageable) firstPlayer.getTeam().get(i));
							}
						}
						// a.execute(e);
					} else {
						for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
							int xs =  secondPlayer.getTeam().get(i).getLocation().x;
							int ys =  secondPlayer.getTeam().get(i).getLocation().y;
							distance = Math.abs(x - xs) + Math.abs(y - ys);
							if (distance <= range) {

								e.add((Damageable) secondPlayer.getTeam().get(i));
							}
						}
						// a.execute(e);
					}
				}
				/*a.execute(e);
				a.setCurrentCooldown(a.getBaseCooldown());
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());*/

			} else if (a instanceof HealingAbility) {
				if (firstPlayer.getTeam().contains(c)) {
					for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
						int xs =  firstPlayer.getTeam().get(i).getLocation().x;
						int ys =  firstPlayer.getTeam().get(i).getLocation().y;
						distance = Math.abs(x - ys) + Math.abs(y - xs);
						if (distance <= range) {

							e.add((Damageable) firstPlayer.getTeam().get(i));
						}
					}
					// a.execute(e);
				} else {
					for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
						int xs = secondPlayer.getTeam().get(i).getLocation().x;
						int ys =  secondPlayer.getTeam().get(i).getLocation().y;
						distance = Math.abs(x - ys) + Math.abs(y - xs);
						if (distance <= range) {

							e.add((Damageable) secondPlayer.getTeam().get(i));
						}
					}
					// a.execute(e);
				}
				/*a.execute(e);
				a.setCurrentCooldown(a.getBaseCooldown());
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());*/

			} else {
				if(this.getFirstPlayer().getTeam().contains(c) ) {
					
					for(int i = 0; i < this.getSecondPlayer().getTeam().size(); i++) {
						//Champion t = this.getSecondPlayer().getTeam().get(i);
						boolean isShield = false;
						int xc = this.getSecondPlayer().getTeam().get(i).getLocation().x;
						int yc = this.getSecondPlayer().getTeam().get(i).getLocation().y;
						distance = Math.abs(x - xc) + Math.abs(y - yc);
						if(distance <= range) {
							for(int j = 0; j < this.getSecondPlayer().getTeam().get(i).getAppliedEffects().size(); j++) {
								if(this.getSecondPlayer().getTeam().get(i).getAppliedEffects().get(j) instanceof Shield) {
									this.getSecondPlayer().getTeam().get(i).getAppliedEffects().get(j).remove(this.getSecondPlayer().getTeam().get(i));
									this.getSecondPlayer().getTeam().get(i).getAppliedEffects().remove(j);
									j = j - 1;
									isShield = true;
								}
							}
							
							if(!isShield) {
								if(this.getSecondPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
									e.add(this.getSecondPlayer().getTeam().get(i));
							}
						}
					}
				}
				else {
					for(int i = 0; i < this.getFirstPlayer().getTeam().size(); i++) {
						//Champion t = this.getFirstPlayer().getTeam().get(i);
						boolean isShield = false;
						int xc = this.getFirstPlayer().getTeam().get(i).getLocation().x;
						int yc = this.getFirstPlayer().getTeam().get(i).getLocation().y;
						distance = Math.abs(x - xc) + Math.abs(y - yc);
						if(distance <= range) {
							for(int j = 0; j < this.getFirstPlayer().getTeam().get(i).getAppliedEffects().size(); j++) {
								if(this.getFirstPlayer().getTeam().get(i).getAppliedEffects().get(j) instanceof Shield) {
									this.getFirstPlayer().getTeam().get(i).getAppliedEffects().get(j).remove(this.getFirstPlayer().getTeam().get(i));
									this.getFirstPlayer().getTeam().get(i).getAppliedEffects().remove(j);
									j = j - 1;
									isShield = true;
								}
							}
							if(!isShield) {
								if(this.getFirstPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
									e.add(this.getFirstPlayer().getTeam().get(i));
							}
						}
					}
				}
				/*if (firstPlayer.getTeam().contains(c)) {
					//boolean outRange = false;
					for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
						Champion f= secondPlayer.getTeam().get(i);
						boolean s = false;
						double xc =  f.getLocation().getX();
						double yc =  f.getLocation().getY();
						distance = Math.abs(x - xc) + Math.abs(y - yc);
						if (distance <= range) {
							for (int j = 0; j < f.getAppliedEffects().size(); j++) {
								if (f.getAppliedEffects().get(j) instanceof Shield) {
									f.getAppliedEffects().get(j)
											.remove(f);
									f.getAppliedEffects().remove(j);
									j = j - 1;
									s = true;
								}
							}
							if (!s)
								e.add((Damageable)f);
						} 
						
					}

				}

				else {
					//boolean outRange = false;
					for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
						Champion f= firstPlayer.getTeam().get(i);
						boolean s = false;
						double xc =  f.getLocation().getX();
						double yc =  f.getLocation().getY();
						distance = Math.abs(x - xc) + Math.abs(y - yc);
						if (distance <= range) {
							for (int j = 0; j < f.getAppliedEffects().size(); j++) {
								
								if (f.getAppliedEffects().get(j) instanceof Shield) {
									f.getAppliedEffects().get(j)
											.remove(f);
									f.getAppliedEffects().remove(j);
									j = j - 1;
									s = true;
								}
							}
						
							if (!s)
								e.add((Damageable)f);
						}

					}
				}*/
				/*a.execute(e);
				a.setCurrentCooldown(a.getBaseCooldown());
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());

				for (int i = 0; i < e.size(); i++) {
					if (e.get(i) instanceof Cover) {
						Cover t = (Cover) e.get(i);
						if (t.getCurrentHP() == 0) {
							board[t.getLocation().x][t.getLocation().y] = null;
						}
					} else if (e.get(i) instanceof Champion) {
						Champion t = (Champion) e.get(i);
						if (t.getCurrentHP() == 0) {
							board[t.getLocation().x][t.getLocation().y] = null;
							t.setCondition(Condition.KNOCKEDOUT);
							removeFromTeam(t);
						}
					}
				}*/
			}
			a.execute(e);
			a.setCurrentCooldown(a.getBaseCooldown());
			c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
			c.setMana(c.getMana() - a.getManaCost());

			for (int i = 0; i < e.size(); i++) {
				if (e.get(i) instanceof Cover) {
					Cover t = (Cover) e.get(i);
					if (t.getCurrentHP() == 0) {
						board[t.getLocation().x][t.getLocation().y] = null;
					}
				} else if (e.get(i) instanceof Champion) {
					Champion t = (Champion) e.get(i);
					if (t.getCurrentHP() == 0) {
						board[t.getLocation().x][t.getLocation().y] = null;
						t.setCondition(Condition.KNOCKEDOUT);
						removeFromTeam(t);
						/*Stack<Champion> temp = new Stack<Champion>();
						while (!turnOrder.isEmpty()) {
							if ((Champion) turnOrder.peekMin() != t) {
								temp.add((Champion) turnOrder.remove());
								
							} else {
								turnOrder.remove();
							}
						}
						while (!temp.isEmpty()) {
							turnOrder.insert(temp.pop());
						}
						if(firstPlayer.getTeam().contains(t)) {
							firstPlayer.getTeam().remove(t);
						}
						else 
							secondPlayer.getTeam().remove(t);*/
					}
				}
			}
			break;
		case SELFTARGET:
			e.add((Damageable) c);
			a.execute(e);
			a.setCurrentCooldown(a.getBaseCooldown());
			c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
			c.setMana(c.getMana() - a.getManaCost());

			break;
		case SURROUND:
			if (a instanceof HealingAbility) {
				for (int j = y - 1; j < y + 2; j++) {
					for (int i = x - 1; i < x + 2; i++) {
						if (i >= 0 && i <= 4 && j >= 0 && j <= 4 && !(i == x && j == y)) {
							if (board[i][j] instanceof Champion) {
								Champion CH = (Champion) board[i][j];
								if (secondPlayer.getTeam().contains(c) == secondPlayer.getTeam().contains(CH)) {
									// teammem = secondPlayer.getTeam().get(i);
									e.add((Damageable) CH);

								}
							}
						}
					}
				}
				a.execute(e);
				a.setCurrentCooldown(a.getBaseCooldown());
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());
			} else if (a instanceof DamagingAbility) {
				Champion CH = null;
				Cover C = null;
				boolean flag = false;
				for (int j = y - 1; j < y + 2; j++) {
					for (int i = x - 1; i < x + 2; i++) {
						if (i >= 0 && i <= 4 && j >= 0 && j <= 4 && !(i == x && j == y)) { //// x w y ????
							if (board[i][j] instanceof Champion) {
								CH = (Champion) board[i][j];
								flag = false;
								if (firstPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(CH)) {
									for (int k = 0; k < CH.getAppliedEffects().size(); k++) {
										if (CH.getAppliedEffects().get(k) instanceof Shield) {
											flag = true;
											CH.getAppliedEffects().get(k).remove(CH);
											CH.getAppliedEffects().remove(k);
											k = k - 1;
										}
									}
									if (flag == false) {
										// teammem = secondPlayer.getTeam().get(i);
										e.add((Damageable) CH);
									}

								} else if (firstPlayer.getTeam().contains(CH) && secondPlayer.getTeam().contains(c)) {
									for (int k = 0; k < CH.getAppliedEffects().size(); k++) {
										if (CH.getAppliedEffects().get(k) instanceof Shield) {
											flag = true;
											CH.getAppliedEffects().get(k).remove(CH);
											CH.getAppliedEffects().remove(k);
											k = k - 1;
										}
									}
									if (flag == false) {
										// teammem = firstPlayer.getTeam().get(i);
										e.add((Damageable) CH);
									}
								}

							} else if (board[i][j] instanceof Cover) {
								C = (Cover) board[i][j];
								e.add((Damageable) C);
							}

						}
					}
				}
				a.execute(e);
				a.setCurrentCooldown(a.getBaseCooldown());
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());
				for (int i = 0; i < e.size(); i++) {
					if (e.get(i) instanceof Cover) {
						Cover t = (Cover) e.get(i);
						if (t.getCurrentHP() == 0) {
							board[t.getLocation().x][t.getLocation().y] = null;
						}
					} else if (e.get(i) instanceof Champion) {
						Champion t = (Champion) e.get(i);
						if (t.getCurrentHP() == 0) {
							board[t.getLocation().x][t.getLocation().y] = null;
							t.setCondition(Condition.KNOCKEDOUT);
							removeFromTeam(t);
						}
					}
				}
			} else if (a instanceof CrowdControlAbility) {
				CrowdControlAbility ac = (CrowdControlAbility) a;
				Champion CH = null;
				boolean flag = false;
				// int y = (int) this.getCurrentChampion().getLocation().getX();
				// int x = (int) this.getCurrentChampion().getLocation().getY();
				for (int j = y - 1; j < y + 2; j++) {
					for (int i = x - 1; i < x + 2; i++) {
						if (i >= 0 && i <= 4 && j >= 0 && j <= 4 && !(i == x && j == y)) {
							if (board[i][j] instanceof Champion) {
								CH = (Champion) board[i][j];
								if (ac.getEffect().getType() == EffectType.DEBUFF) {
									flag = false;
									if (firstPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(CH)) {
										if (flag == false) {
											// teammem = secondPlayer.getTeam().get(i);
											e.add((Damageable) CH);
										}
									} else if (secondPlayer.getTeam().contains(c)
											&& firstPlayer.getTeam().contains(CH)) {
										if (flag == false) {
											// teammem = firstPlayer.getTeam().get(i);
											e.add((Damageable) CH);
										}
									}
								} else {
									flag = false;
									if (firstPlayer.getTeam().contains(c) && firstPlayer.getTeam().contains(CH)) {
										if (flag == false) {
											// teammem = firstPlayer.getTeam().get(i);
											e.add((Damageable) CH);
										}
									}
									if (secondPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(CH)) {
										if (flag == false) {
											// teammem = secondPlayer.getTeam().get(i);
											e.add((Damageable) CH);
										}
									}
								}
							}
						}
					}
				}
				a.execute(e);
				a.setCurrentCooldown(a.getBaseCooldown());
				c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
				c.setMana(c.getMana() - a.getManaCost());
			}
			break;
		default:
			break;
		}

	}

//	public void castAbility(Ability a, Direction d)
//			throws CloneNotSupportedException, AbilityUseException, NotEnoughResourcesException {
//		Champion currentchamp = this.getCurrentChampion();
//		int Xposition = (int) currentchamp.getLocation().getX();// revise
//		int Yposition = (int) currentchamp.getLocation().getY();// revise
//		int range = a.getCastRange();
//		ArrayList<Damageable> e = new ArrayList<Damageable>();
//		if (a.getManaCost() > currentchamp.getMana()) {
//			throw new AbilityUseException();
//		}
//		for (int j = 0; j < currentchamp.getAppliedEffects().size(); j++) {
//			if (currentchamp.getAppliedEffects().get(j) instanceof Silence) {
//				throw new AbilityUseException();
//			}
//		}
//		if (a.getCastArea() == AreaOfEffect.SINGLETARGET || a.getCastArea() == AreaOfEffect.SELFTARGET
//				|| a.getCastArea() == AreaOfEffect.SURROUND || a.getCastArea() == AreaOfEffect.TEAMTARGET) {
//			throw new AbilityUseException();
//		}
//		if (a.getRequiredActionPoints() > currentchamp.getCurrentActionPoints()
//				&& a.getManaCost() > currentchamp.getMana()) {
//			throw new NotEnoughResourcesException();
//		}
//
//		switch (d) {
//		case UP: {
//			int allowableRange = 0;
//			if (range + Xposition <= 4)
//				allowableRange = range;
//			else
//				allowableRange = 4 - Xposition;
//			if (a instanceof DamagingAbility) {
//
//				for (int i = 1; i <= allowableRange; i++) {
//					if (board[Xposition + i][Yposition] instanceof Champion) {
//						Champion x = (Champion) board[Xposition + i][Yposition];
//
//						if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam().contains(currentchamp)) {
//							boolean shield = false;
//							for (int z = 0; z < x.getAppliedEffects().size(); z++) {
//								if (x.getAppliedEffects().get(z) instanceof Shield) {
//									shield = true;
//									break;
//								} else
//									shield = false;
//							}
//							if (shield == true) {
//								for (int z = 0; z < x.getAppliedEffects().size(); z++) {
//									if (x.getAppliedEffects().get(z) instanceof Shield) {
//										x.getAppliedEffects().remove(z);
//										break;
//									}
//								}
//							} else
//								e.add(x);
//						}
//					}
//
//					else {
//						if (board[Xposition + i][Yposition] instanceof Cover) {
//							Cover c = (Cover) board[Xposition + i][Yposition];
//							e.add(c);
//						}
//					}
//				}
//
//				a.execute(e);
//			}
//
//			else {
//				if (a instanceof HealingAbility) {
//					for (int i = 1; i <= allowableRange; i++) {
//						if (board[Xposition + i][Yposition] instanceof Champion) {
//							Champion x = (Champion) board[Xposition + i][Yposition];
//
//							if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam().contains(currentchamp))
//								e.add(x);
//						}
//					}
//					a.execute(e);
//				} else {
//					if (a instanceof CrowdControlAbility) {
//						CrowdControlAbility cc = (CrowdControlAbility) a;
//						if (cc.getEffect().getType() == EffectType.BUFF) {
//							for (int i = 1; i <= allowableRange; i++) {
//								if (board[Xposition + i][Yposition] instanceof Champion) {
//									Champion x = (Champion) board[Xposition + i][Yposition];
//
//									if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam()
//											.contains(currentchamp))
//										e.add(x);
//
//								}
//
//							}
//
//						} else if (cc.getEffect().getType() == EffectType.DEBUFF) {
//							for (int i = 1; i <= allowableRange; i++) {
//								if (board[Xposition + i][Yposition] instanceof Champion) {
//									Champion x = (Champion) board[Xposition + i][Yposition];
//
//									if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam()
//											.contains(currentchamp))
//										e.add(x);
//
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		case DOWN: {
//			int allowableRange = 0;
//			if (Xposition - range >= 0)
//				allowableRange = range;
//			else
//				allowableRange = Xposition;
//			if (a instanceof DamagingAbility) {
//
//				for (int i = 1; i <= allowableRange; i++) {
//					if (board[Xposition - i][Yposition] instanceof Champion) {
//						Champion x = (Champion) board[Xposition - i][Yposition];
//
//						if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam().contains(currentchamp)) {
//							boolean shield = false;
//							for (int z = 0; z < x.getAppliedEffects().size(); z++) {
//								if (x.getAppliedEffects().get(z) instanceof Shield) {
//									shield = true;
//									break;
//								} else
//									shield = false;
//							}
//							if (shield == true) {
//								for (int z = 0; z < x.getAppliedEffects().size(); z++) {
//									if (x.getAppliedEffects().get(z) instanceof Shield) {
//										x.getAppliedEffects().remove(z);
//										break;
//									}
//								}
//							} else
//								e.add(x);
//						}
//					}
//
//					else {
//						if (board[Xposition - i][Yposition] instanceof Cover) {
//							Cover c = (Cover) board[Xposition - i][Yposition];
//							e.add(c);
//						}
//					}
//				}
//
//				a.execute(e);
//			}
//
//			else {
//				if (a instanceof HealingAbility) {
//					for (int i = 1; i <= allowableRange; i++) {
//						if (board[Xposition - i][Yposition] instanceof Champion) {
//							Champion x = (Champion) board[Xposition - i][Yposition];
//
//							if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam().contains(currentchamp))
//								e.add(x);
//						}
//					}
//					a.execute(e);
//				} else {
//					if (a instanceof CrowdControlAbility) {
//						CrowdControlAbility cc = (CrowdControlAbility) a;
//						if (cc.getEffect().getType() == EffectType.BUFF) {
//							for (int i = 1; i <= allowableRange; i++) {
//								if (board[Xposition - i][Yposition] instanceof Champion) {
//									Champion x = (Champion) board[Xposition - i][Yposition];
//
//									if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam()
//											.contains(currentchamp))
//										e.add(x);
//
//								}
//
//							}
//
//						} else if (cc.getEffect().getType() == EffectType.DEBUFF) {
//							for (int i = 1; i <= allowableRange; i++) {
//								if (board[Xposition - i][Yposition] instanceof Champion) {
//									Champion x = (Champion) board[Xposition - i][Yposition];
//
//									if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam()
//											.contains(currentchamp))
//										e.add(x);
//
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		case RIGHT: {
//
//			int allowableRange = 0;
//			if (range + Yposition <= 4)
//				allowableRange = range;
//			else
//				allowableRange = 4 - Yposition;
//			if (a instanceof DamagingAbility) {
//
//				for (int i = 1; i <= allowableRange; i++) {
//					if (board[Xposition][Yposition + i] instanceof Champion) {
//						Champion x = (Champion) board[Xposition][Yposition + i];
//
//						if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam().contains(currentchamp)) {
//							boolean shield = false;
//							for (int z = 0; z < x.getAppliedEffects().size(); z++) {
//								if (x.getAppliedEffects().get(z) instanceof Shield) {
//									shield = true;
//									break;
//								} else
//									shield = false;
//							}
//							if (shield == true) {
//								for (int z = 0; z < x.getAppliedEffects().size(); z++) {
//									if (x.getAppliedEffects().get(z) instanceof Shield) {
//										x.getAppliedEffects().remove(z);
//										break;
//									}
//								}
//							} else
//								e.add(x);
//						}
//					}
//
//					else {
//						if (board[Xposition][Yposition + i] instanceof Cover) {
//							Cover c = (Cover) board[Xposition][Yposition + i];
//							e.add(c);
//						}
//					}
//				}
//
//				a.execute(e);
//			}
//
//			else {
//				if (a instanceof HealingAbility) {
//					for (int i = 1; i <= allowableRange; i++) {
//						if (board[Xposition][Yposition + i] instanceof Champion) {
//							Champion x = (Champion) board[Xposition][Yposition + i];
//
//							if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam().contains(currentchamp))
//								e.add(x);
//						}
//					}
//					a.execute(e);
//				} else {
//					if (a instanceof CrowdControlAbility) {
//						CrowdControlAbility cc = (CrowdControlAbility) a;
//						if (cc.getEffect().getType() == EffectType.BUFF) {
//							for (int i = 1; i <= allowableRange; i++) {
//								if (board[Xposition][Yposition + i] instanceof Champion) {
//									Champion x = (Champion) board[Xposition][Yposition + i];
//
//									if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam()
//											.contains(currentchamp))
//										e.add(x);
//
//								}
//
//							}
//
//						} else if (cc.getEffect().getType() == EffectType.DEBUFF) {
//							for (int i = 1; i <= allowableRange; i++) {
//								if (board[Xposition][Yposition + i] instanceof Champion) {
//									Champion x = (Champion) board[Xposition][Yposition + i];
//
//									if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam()
//											.contains(currentchamp))
//										e.add(x);
//
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		case LEFT: {
//			int allowableRange = 0;
//			if (Yposition - range >= 0)
//				allowableRange = range;
//			else
//				allowableRange = Yposition;
//			if (a instanceof DamagingAbility) {
//
//				for (int i = 1; i <= allowableRange; i++) {
//					if (board[Xposition][Yposition - i] instanceof Champion) {
//						Champion x = (Champion) board[Xposition][Yposition - i];
//
//						if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam().contains(currentchamp)) {
//							boolean shield = false;
//							for (int z = 0; z < x.getAppliedEffects().size(); z++) {
//								if (x.getAppliedEffects().get(z) instanceof Shield) {
//									shield = true;
//									break;
//								} else
//									shield = false;
//							}
//							if (shield == true) {
//								for (int z = 0; z < x.getAppliedEffects().size(); z++) {
//									if (x.getAppliedEffects().get(z) instanceof Shield) {
//										x.getAppliedEffects().remove(z);
//										break;
//									}
//								}
//							} else
//								e.add(x);
//						}
//					}
//
//					else {
//						if (board[Xposition][Yposition - i] instanceof Cover) {
//							Cover c = (Cover) board[Xposition][Yposition - i];
//							e.add(c);
//						}
//					}
//				}
//
//				a.execute(e);
//			}
//
//			else {
//				if (a instanceof HealingAbility) {
//					for (int i = 1; i <= allowableRange; i++) {
//						if (board[Xposition][Yposition - i] instanceof Champion) {
//							Champion x = (Champion) board[Xposition][Yposition - i];
//
//							if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam().contains(currentchamp))
//								e.add(x);
//						}
//					}
//					a.execute(e);
//				} else {
//					if (a instanceof CrowdControlAbility) {
//						CrowdControlAbility cc = (CrowdControlAbility) a;
//						if (cc.getEffect().getType() == EffectType.BUFF) {
//							for (int i = 1; i <= allowableRange; i++) {
//								if (board[Xposition][Yposition - i] instanceof Champion) {
//									Champion x = (Champion) board[Xposition][Yposition - i];
//
//									if (firstPlayer.getTeam().contains(x) == firstPlayer.getTeam()
//											.contains(currentchamp))
//										e.add(x);
//
//								}
//
//							}
//
//						} else if (cc.getEffect().getType() == EffectType.DEBUFF) {
//							for (int i = 1; i <= allowableRange; i++) {
//								if (board[Xposition][Yposition - i] instanceof Champion) {
//									Champion x = (Champion) board[Xposition][Yposition - i];
//
//									if (firstPlayer.getTeam().contains(x) != firstPlayer.getTeam()
//											.contains(currentchamp))
//										e.add(x);
//
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		}
//	}

//	a.execute(targets);
//	removeCosts(c,a);

//	for(int i = 0;i < targets.size();i++) {
//		if(targets.get(i).getCurrentHP() == 0) {
//			board[targets.get(i).getLocation().x][targets.get(i).getLocation().y] = null;
//			if(targets.get(i) instanceof Champion) {
//				Champion temp = (Champion) targets.get(i);
//				temp.setCondition(Condition.KNOCKEDOUT);
//				Stack<Champion> s = new Stack<Champion>();
//				while(!turnOrder.isEmpty()) {
//					if((Champion) turnOrder.peekMin() == temp) {
//						turnOrder.remove();
//					}
//					else {
//						s.push((Champion) turnOrder.remove());
//					}
//				}
//				while(!s.isEmpty()) {
//					turnOrder.insert(s.pop());
//				}
//			}
//		}
//	}

	public void castAbility(Ability a, Direction d) throws CloneNotSupportedException, AbilityUseException,
			NotEnoughResourcesException, InvalidTargetException {
		Champion c = this.getCurrentChampion();

		if (a.getCurrentCooldown() != 0) {
			throw new AbilityUseException();
		}
		if (c.getCurrentActionPoints() < a.getRequiredActionPoints() || c.getMana() < a.getManaCost()) {
			throw new NotEnoughResourcesException();
		}

		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if (c.getAppliedEffects().get(i) instanceof Silence) {
				throw new AbilityUseException();
			}
		}
		ArrayList<Damageable> targets = getMuliTargets(c.getLocation().x, c.getLocation().y, d, a.getCastRange());
		if (a instanceof DamagingAbility) {
			for (int i = 0; i < targets.size(); i++) {
				if (targets.get(i) instanceof Champion) {
					Champion v = (Champion) targets.get(i);
					for (int j = 0; j < v.getAppliedEffects().size(); j++) {
						if (v.getAppliedEffects().get(j) instanceof Shield) {
							targets.remove(i);
							v.getAppliedEffects().get(j).remove(v);
							v.getAppliedEffects().remove(j);
							i = i - 1;
							j = j - 1;
						}
					}
					if (checkTeammate(c, v)) {
						targets.remove(i);
						i = i - 1;
						// throw new InvalidTargetException();
					}
				}

			}
			a.execute(targets);
			removeCosts(c, a);
			for (int i = 0; i < targets.size(); i++) {
				if (targets.get(i) instanceof Cover) {
					Cover t = (Cover) targets.get(i);
					if (t.getCurrentHP() == 0) {
						board[t.getLocation().x][t.getLocation().y] = null;
					}
				} else if (targets.get(i) instanceof Champion) {
					Champion t = (Champion) targets.get(i);
					if (t.getCurrentHP() == 0) {
						board[t.getLocation().x][t.getLocation().y] = null;
						t.setCondition(Condition.KNOCKEDOUT);
						removeFromTeam(t);
					}
				}
			}
		} else if (a instanceof HealingAbility) {
			for (int i = 0; i < targets.size(); i++) {
				if (targets.get(i) instanceof Cover) {
					targets.remove(i);
					i = i - 1;
				} else {
					Champion v = (Champion) targets.get(i);
					if (!checkTeammate(c, v)) {
						targets.remove(i);
						i = i - 1;
						// throw new InvalidTargetException();
					}
				}
			}
			a.execute(targets);
			removeCosts(c, a);
		} else {
			CrowdControlAbility ac = (CrowdControlAbility) a;
			for (int i = 0; i < targets.size(); i++) {
				if (targets.get(i) instanceof Cover) {
					targets.remove(i);
					i = i - 1;
					// throw new InvalidTargetException();
				} else {
					Champion v = (Champion) targets.get(i);
					if (ac.getEffect().getType() == EffectType.BUFF) {
						if (!checkTeammate(c, v)) {
							targets.remove(i);
							i = i - 1;
							// throw new InvalidTargetException();
						}
					} else {
						if (checkTeammate(c, v)) {
							targets.remove(i);
							i = i - 1;
							// throw new InvalidTargetException();
						}
					}
				}
			}

			a.execute(targets);
			removeCosts(c, a);

		}

	}

	public ArrayList<Damageable> getMuliTargets(int height, int width, Direction d, int range) {
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		for (int i = 1; i <= range; i++) {
			if (d == Direction.UP) {
				if (height + i <= 4 && board[height + i][width] != null) {
					targets.add((Damageable) board[height + i][width]);
				}
			} else if (d == Direction.DOWN) {
				if (height - i >= 0 && board[height - i][width] != null) {
					targets.add((Damageable) board[height - i][width]);
				}
			} else if (d == Direction.RIGHT) {
				if ((width + i) <= 4 && board[height][width + i] != null) {

					targets.add((Damageable) board[height][width + i]);
				}
			} else {
				if (width - i >= 0 && board[height][width - i] != null) {

					targets.add((Damageable) board[height][width - i]);
				}
			}

		}
		return targets;
	}

	public void removeCosts(Champion c, Ability a) {
		c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
		c.setMana(c.getMana() - a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
	}

	public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, AbilityUseException,
			InvalidTargetException, CloneNotSupportedException {
		Champion c = this.getCurrentChampion();
		if (a.getManaCost() > c.getMana() || a.getRequiredActionPoints() > c.getCurrentActionPoints()) {
			throw new NotEnoughResourcesException();
		}
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		int range = a.getCastRange();
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if (c.getAppliedEffects().get(i) instanceof Silence) {
				throw new AbilityUseException();
			}
		}
		if (a.getCurrentCooldown() != 0) {
			throw new AbilityUseException();
		}
		if (x > 4 || x < 0 || y > 4 || y < 0) {
			throw new InvalidTargetException();
		}
		if (board[x][y] == null) {
			throw new InvalidTargetException();
		}

		/*
		 * if (a.getCastArea() == AreaOfEffect.DIRECTIONAL || a.getCastArea() ==
		 * AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.SURROUND ||
		 * a.getCastArea() == AreaOfEffect.TEAMTARGET) { throw new
		 * AbilityUseException(); }
		 */
		int x1 = c.getLocation().x;
		int y1 = c.getLocation().y;

		int distance = Math.abs(x - x1) + Math.abs(y - y1);
		if (range < distance) {
			throw new AbilityUseException();
		}

		if (a instanceof DamagingAbility) {
			if (board[x][y] instanceof Cover) {
				Cover q = (Cover) board[x][y];
				targets.add((Damageable) board[x][y]);

			} else {
				boolean isShield = false;
				Champion v = (Champion) board[x][y];
				if(c == v) {
					throw new InvalidTargetException();
				}
				if (checkTeammate(c, v)) {
					throw new InvalidTargetException();
				} else {
					for (int i = 0; i < v.getAppliedEffects().size(); i++) {
						if (v.getAppliedEffects().get(i) instanceof Shield) {
							v.getAppliedEffects().get(i).remove(v);
							v.getAppliedEffects().remove(i);
							i = i - 1;
							isShield = true;
						}
					}
				}

				if (!isShield)
					targets.add(v);
			}
			a.execute(targets);
			removeCosts(c, a);
			for (int i = 0; i < targets.size(); i++) {
				if (targets.get(i) instanceof Cover) {
					Cover t = (Cover) targets.get(i);
					if (t.getCurrentHP() == 0) {
						board[t.getLocation().x][t.getLocation().y] = null;
					}
				} else if (targets.get(i) instanceof Champion) {
					Champion t = (Champion) targets.get(i);
					if (t.getCurrentHP() == 0) {
						board[t.getLocation().x][t.getLocation().y] = null;
						t.setCondition(Condition.KNOCKEDOUT);
						removeFromTeam(t);
					}
				}
			}
		} else if (a instanceof HealingAbility) {
			if (board[x][y] instanceof Cover) {
				throw new InvalidTargetException();
			} else {
				Champion v = (Champion) board[x][y];
				if(c == v) 
					targets.add(v);
				else {
					if (!checkTeammate(c, v)) {
						throw new InvalidTargetException();
					} else {
						
						targets.add(v);
	
					}
				}
				a.execute(targets);
				removeCosts(c, a);
			}
		} else {
			if (board[x][y] instanceof Cover) {
				throw new InvalidTargetException();
			}
			Champion v = (Champion) board[x][y];
			CrowdControlAbility cc = (CrowdControlAbility) a;
			if (c == v) {
				if (cc.getEffect().getType() == EffectType.BUFF)
					targets.add(v);
				else
					throw new InvalidTargetException();

			} else {

				if (cc.getEffect().getType() == EffectType.BUFF) {

					if (!checkTeammate(c, v)) {
						throw new InvalidTargetException();
					}

					targets.add(v);

				} else {

					if (checkTeammate(c, v)) {
						throw new InvalidTargetException();
					}

					targets.add(v);

				}
			}
			a.execute(targets);
			removeCosts(c, a);
		}

	}

	public void removeFromTeam(Champion t) {
		/*Stack<Champion> temp = new Stack<Champion>();
		while (!turnOrder.isEmpty()) {
			if ((Champion) turnOrder.peekMin() != t) {
				temp.add((Champion) turnOrder.remove());
				
			} else {
				turnOrder.remove();
			}
		}
		while (!temp.isEmpty()) {
			turnOrder.insert(temp.pop());
		}*/
		
		/*Stack<Comparable> temp = new Stack<Comparable>();
		for (int i = 0; i < turnOrder.size(); i++) {
			if (turnOrder.peekMin() != t) {
				temp.add(turnOrder.remove());
			}
			turnOrder.remove();
		}
		while (!temp.isEmpty()) {
			turnOrder.insert(temp.pop());
		}*/
		
		/*if (this.getFirstPlayer().getTeam().contains(t)) {
			for (Champion c : this.getFirstPlayer().getTeam()) {
				if (c == t) {
					this.getFirstPlayer().getTeam().remove(t);
				}
			}
		} else {
			for (Champion c : this.getSecondPlayer().getTeam()) {
				if (c == t) {
					this.getSecondPlayer().getTeam().remove(t);
				}
			}*/
			
			Stack<Champion> temp = new Stack<Champion>();
			while (!turnOrder.isEmpty()) {
				if ((Champion) turnOrder.peekMin() != t) {
					temp.add((Champion) turnOrder.remove());
					
				} else {
					turnOrder.remove();
				}
			}
			while (!temp.isEmpty()) 
				turnOrder.insert(temp.pop());
			
			if(firstPlayer.getTeam().contains(t)) {
				firstPlayer.getTeam().remove(t);
			}
			else 
				secondPlayer.getTeam().remove(t);
		

		
		
		
	}

	public boolean checkTeammate(Champion attacker, Champion victim) {
		if (firstPlayer.getTeam().contains(attacker)) {
			if (firstPlayer.getTeam().contains(victim))
				return true;
			else
				return false;
		} else {
			if (secondPlayer.getTeam().contains(victim))
				return true;
			else
				return false;
		}
	}

	public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException {
		Champion c = this.getCurrentChampion();
		if (this.getFirstPlayer().getLeader() != c && this.getSecondPlayer().getLeader() != c)
			throw new LeaderNotCurrentException();
		else if ((this.getFirstPlayer().getLeader() == c && firstLeaderAbilityUsed)
				|| (this.getSecondPlayer().getLeader() == c && secondLeaderAbilityUsed))
			throw new LeaderAbilityAlreadyUsedException();
		else {
			ArrayList<Champion> targets = new ArrayList<Champion>();
			if (c instanceof Hero) {
				if (this.getFirstPlayer().getLeader() == c) {

					for (int i = 0; i < this.getFirstPlayer().getTeam().size(); i++) {
						targets.add(this.getFirstPlayer().getTeam().get(i));

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
			} else if (c instanceof Villain) {
				if (firstPlayer.getTeam().contains(c)) {
					for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
						if (secondPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT && secondPlayer
								.getTeam().get(i).getCurrentHP() < secondPlayer.getTeam().get(i).getMaxHP() * 0.3)
							targets.add(secondPlayer.getTeam().get(i));
					}
					this.firstLeaderAbilityUsed = true;
				} else {
					for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
						if (firstPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT && firstPlayer.getTeam()
								.get(i).getCurrentHP() < firstPlayer.getTeam().get(i).getMaxHP() * 0.3)
							targets.add(firstPlayer.getTeam().get(i));
					}
					this.secondLeaderAbilityUsed = true;
				}
			} else if (c instanceof AntiHero) {
				for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
					if ((this.getFirstPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
							&& (this.getFirstPlayer().getTeam().get(i) != this.getFirstPlayer().getLeader())) {
						targets.add(this.getFirstPlayer().getTeam().get(i));
					}
				}
				for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
					if ((this.getSecondPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
							&& (this.getSecondPlayer().getTeam().get(i) != this.getSecondPlayer().getLeader())) {
						targets.add(this.getSecondPlayer().getTeam().get(i));
					}

				}
				if (firstPlayer.getTeam().contains(c)) {
					this.firstLeaderAbilityUsed = true;

				} else
					this.secondLeaderAbilityUsed = true;

			}

			c.useLeaderAbility(targets);
			
			for (int i = 0; i < targets.size(); i++) {
				 if (targets.get(i) instanceof Champion) {
					Champion t = (Champion) targets.get(i);
					if (t.getCurrentHP() == 0) {
						board[t.getLocation().x][t.getLocation().y] = null;
						t.setCondition(Condition.KNOCKEDOUT);
						removeFromTeam(t);
					}
				}
			}
			
			

		}
	}

	public void endTurn() { // do not understand whether to return the inactive champions to the turnorder
		// or not
		turnOrder.remove();
		if (turnOrder.isEmpty()) {
			prepareChampionTurns();
		}
		Champion c = (Champion) turnOrder.peekMin();
		while (c.getCondition() == Condition.INACTIVE) {

			for (int i = 0; i < c.getAppliedEffects().size(); i++) { /// hwar stun da ghareeb es2l el shabab
				c.getAppliedEffects().get(i).setDuration(c.getAppliedEffects().get(i).getDuration() - 1); ////// revise
				if (c.getAppliedEffects().get(i).getDuration() == 0) {
					c.getAppliedEffects().get(i).remove(c);
					c.getAppliedEffects().remove(i);
					i = i - 1;
				}
				/*
				 * if(c.getAppliedEffects().get(i) instanceof Stun) {
				 * c.getAppliedEffects().get(i).setDuration(c.getAppliedEffects().get(i).
				 * getDuration() - 1); ////// revise
				 * if(c.getAppliedEffects().get(i).getDuration() == 0) {
				 * c.getAppliedEffects().get(i).remove(c); c.getAppliedEffects().remove(i); } }
				 */
			}
			for (int i = 0; i < c.getAbilities().size(); i++) {
				c.getAbilities().get(i).setCurrentCooldown(c.getAbilities().get(i).getCurrentCooldown() - 1); //// revise
			}
			turnOrder.remove();
			if(turnOrder.isEmpty()) 
				this.prepareChampionTurns();
			c = (Champion) turnOrder.peekMin();
		}
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
		for (int i = 0; i < c.getAbilities().size(); i++) {
			c.getAbilities().get(i).setCurrentCooldown(c.getAbilities().get(i).getCurrentCooldown() - 1); //// revise
		}
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			c.getAppliedEffects().get(i).setDuration(c.getAppliedEffects().get(i).getDuration() - 1); ////// revise
			if (c.getAppliedEffects().get(i).getDuration() == 0) {
				c.getAppliedEffects().get(i).remove(c);
				c.getAppliedEffects().remove(i);
				i = i - 1;
			}

		}

	}

	private void prepareChampionTurns() {
		for (int i = 0; i < this.getFirstPlayer().getTeam().size(); i++) {
			if (this.getFirstPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT) {
				turnOrder.insert(this.getFirstPlayer().getTeam().get(i));
			}

		}
		for (int i = 0; i < this.getSecondPlayer().getTeam().size(); i++) {
			if (this.getSecondPlayer().getTeam().get(i).getCondition() != Condition.KNOCKEDOUT) {
				turnOrder.insert(this.getSecondPlayer().getTeam().get(i));
			}
		}

		/*
		 * for(int i = 0; i< 5; i++) { for(int j = 0;i <5; j++) { if(board[i][j] != null
		 * ) { if(board[i][j] instanceof Champion) { turnOrder.insert((Champion)
		 * board[i][j]); } } } }
		 */
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
