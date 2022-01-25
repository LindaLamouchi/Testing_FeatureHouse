

import java.awt.BasicStroke; 
import java.awt.Color; 
import java.awt.GradientPaint; 
import java.awt.Graphics2D; 
import java.awt.Paint; 
import java.awt.Point; 
import java.awt.Shape; 
import java.awt.geom.Rectangle2D; 
import java.util.ArrayList; 


public  class  ApoGameLevel {
	
private String levelName;

	
	
	private String[] instructions;

	
	
	private byte[][][] level;

	
	
	private ArrayList<ApoGameEntity>[] entities;

	
	
	private ApoGamePlayer[] players;

	
	
	private boolean[] visibleEntities;

	
	
	private boolean bShouldTest;

	
	
	private boolean bCheat;

	
	
	private boolean bShouldChange;

	
	
	public ApoGameLevel(final byte[][][] level, final String[] instructions, final String levelName) {
		this.level = level;
		this.instructions = instructions;
		this.levelName = levelName;
		this.bShouldChange = false;
		this.bCheat = false;
		this.restart();
	}

	
	
	public String getLevelName() {
		return this.levelName;
	}

	

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	

	public String[] getInstructions() {
		return this.instructions;
	}

	

	public void setInstructions(String[] instructions) {
		this.instructions = instructions;
	}

	

	public byte[][][] getLevel() {
		return this.level;
	}

	

	public void setLevel(byte[][][] level) {
		this.level = level;
	}

	

	public boolean isCheat() {
		return this.bCheat;
	}

	

	public void setCheat(boolean cheat) {
		this.bCheat = cheat;
	}

	

	@SuppressWarnings("unchecked")
	public void restart() {
		this.entities = new ArrayList[this.level.length];
		this.players = new ApoGamePlayer[this.level.length];
		this.visibleEntities = new boolean[this.level.length];
		for (int i = 0; i < this.level.length; i++) {
			this.visibleEntities[i] = true;
			this.entities[i] = new ArrayList<ApoGameEntity>();
			for (int y = 0; y < this.level[0].length; y++) {
				for (int x = 0; x < this.level[0][0].length; x++) {
					if (this.level[i][y][x] == ApoGameConstants.LEVEL_FIXED) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_FIXED, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_NO_MOVEMENT, true));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_UP) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_UP, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_UP, true));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_DOWN) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_DOWN, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_DOWN, true));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_RIGHT) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_RIGHT, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_RIGHT, true));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_LEFT) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_LEFT, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_LEFT, true));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_PLAYER) {
						this.players[i] = new ApoGamePlayer(ApoGameImages.ORIGINAL_PLAYER, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE);
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_FINISH) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_FINISH, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_FINISH, true));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_VISIBLE_TRUE) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_VISIBLE_TRUE, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_CHANGEVISIBLE_UP, true));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_VISIBLE_FALSE) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_VISIBLE_FALSE, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_CHANGEVISIBLE_UP, false));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_STEP) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_STEP, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_STEP, true));
					} else if (this.level[i][y][x] == ApoGameConstants.LEVEL_STEP_FINISH) {
						this.entities[i].add(new ApoGameEntity(ApoGameImages.ORIGINAL_STEP_FINISH, x * ApoGameConstants.TILE_SIZE, y * ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.TILE_SIZE, ApoGameConstants.PLAYER_DIRECTION_STEP_FINISH, true));
					}
				}
			}
		}
		this.bShouldTest = false;
	}

	
	
	public int getLayer() {
		return this.entities.length;
	}

	
	
	public boolean changeDirection(int direction) {
		boolean bChange = false;
		if ((direction == ApoGameConstants.PLAYER_DIRECTION_LEFT) ||
			(direction == ApoGameConstants.PLAYER_DIRECTION_RIGHT) ||
			(direction == ApoGameConstants.PLAYER_DIRECTION_UP) ||
			(direction == ApoGameConstants.PLAYER_DIRECTION_DOWN)) {
			for (int i = 0; i < this.entities.length; i++) {
				if (this.visibleEntities[i]) {
					if (this.players[i] != null) {
						if (this.players[i].isBVisible()) {
							if (this.players[i].isMoving()) {
								return false;
							}
							this.players[i].setNextDirection(direction);
							this.players[i].moveNextDirection();
							if (this.players[i].isMoving()) {
								bChange = true;
							}
						}
					}
					if (bChange) {
						this.bShouldChange = false;
						for (int e = 0; e < this.entities[i].size(); e++) {
							this.entities[i].get(e).setMoving();
							if ((direction == ApoGameConstants.PLAYER_DIRECTION_UP) ||
								(direction == ApoGameConstants.PLAYER_DIRECTION_DOWN)) {
								if ((this.entities[i].get(e).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_UP) ||
									(this.entities[i].get(e).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_DOWN) ||
									(this.entities[i].get(e).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_CHANGEVISIBLE_UP)) {
									this.entities[i].get(e).moveNextDirection();
								}
							}
							if ((direction == ApoGameConstants.PLAYER_DIRECTION_LEFT) ||
								(direction == ApoGameConstants.PLAYER_DIRECTION_RIGHT)) {
								if ((this.entities[i].get(e).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_LEFT) ||
									(this.entities[i].get(e).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_RIGHT)) {
									this.entities[i].get(e).moveNextDirection();
								}
							}
						}
					}
				}
			}
			return bChange;
		}
		return false;
	}

	
	
	public boolean think(int delta) {
		boolean bMove = false;
		this.bShouldTest = false;
		if (this.entities != null) {
			for (int i = 0; i < this.entities.length; i++) {
				if (this.visibleEntities[i]) {
					for (int j = 0; j < this.entities[i].size(); j++) {
						this.entities[i].get(j).think(delta);
					}
					if (this.players[i] != null) {
						boolean bMoveOld = this.players[i].isMoving();
						this.players[i].think(delta);
						if (this.players[i].isMoving()) {
							bMove = true;
						}
						if (bMoveOld != this.players[i].isMoving()) {
							this.bShouldTest = true;
						}
					}
				}
			}
		}
		return bMove;
	}

	
	
	public void changeLevelVisible(int level) {
		this.visibleEntities[level] = false;
	}

	
	
	public boolean isLayerVisible(int layer) {
		return this.visibleEntities[layer];
	}

	
	
	public boolean isVisible() {
		for (int i = 0; i < this.visibleEntities.length; i++) {
			if (this.visibleEntities[i]) {
				return true;
			}
		}
		return false;
	}

	
	
	public boolean shouldChange() {
		return this.bShouldChange;
	}

	

	public int[][] checkMovement(int delta) {
		int[][] result = new int[this.players.length][this.entities.length];
		for (int p = 0; p < result.length; p++) {
			for (int e = 0; e < result[0].length; e++) {
				result[p][e] = -1;
			}
		}
		if (!this.bShouldTest) {
			return result;
		} else {
//			for (int p = 0; p < this.players.length; p++) {
				for (int i = 0; i < this.entities.length; i++) {
					if (this.visibleEntities[i]) {
						for (int size = 0; size < this.entities[i].size(); size++) {
							if ((this.entities[i].get(size).isBVisible()) && (!this.entities[i].get(size).isMoving())) {
								if (this.players[i].intersects(this.entities[i].get(size))) {
									if (this.entities[i].get(size).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_FINISH) {
//										if (i == i) {
											result[i][i] = this.entities[i].get(size).getFixedMovement();											
//										}
									} else if (this.entities[i].get(size).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_STEP) {
//										if (i == i) {
											this.entities[i].get(size).setBVisible(false);
											this.changeVisibleStepFinish();
//										}
									} else {
										result[i][i] = this.entities[i].get(size).getFixedMovement();
									}
								}
							}
						}
					}
//				}
			}
		}
		return result;
	}

	
	
	private void changeVisibleStepFinish() {
		for (int i = 0; i < this.entities.length; i++) {
			for (int size = 0; size < this.entities[i].size(); size++) {
				if (this.entities[i].get(size).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_STEP_FINISH) {
					this.entities[i].get(size).setBVisible(!this.entities[i].get(size).isBVisible());	
				}
			}
		}
	}

	
	
	public int getChangeY() {
		int changeY = ApoGameConstants.GAME_HEIGHT/2 - ApoGameConstants.LEVEL_HEIGHT/2;
		if (this.entities.length == 2) {
			changeY -= (int)(2.5 * ApoGameConstants.TILE_SIZE);
		}
		return changeY;
	}

	
	
	public void renderBackground(Graphics2D g, int changeX, int changeY) {
		changeY += this.getChangeY();
		for (int i = 0; i < this.entities.length; i++) {
			Paint paint = g.getPaint();
			GradientPaint grafientPaint = new GradientPaint(8, changeY - 2, Color.LIGHT_GRAY, 8, changeY - 2 + ApoGameConstants.LEVEL_HEIGHT/2, new Color(230, 230, 230), true);
			g.setPaint(grafientPaint);
			g.fillRect(8, changeY - 2, ApoGameConstants.LEVEL_WIDTH + 4, ApoGameConstants.LEVEL_HEIGHT + 4);
			
			g.setPaint(paint);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(5));
			g.drawRoundRect(6, changeY - 4, ApoGameConstants.LEVEL_WIDTH + 8, ApoGameConstants.LEVEL_HEIGHT + 8, 15, 15);
			
			changeY += (int)(5 * ApoGameConstants.TILE_SIZE);
		}
	}

	

	public void render(Graphics2D g, int changeX, int changeY, boolean bEditor) {
		int realChangeY = changeY;
		final Shape clip = g.getClip();
		if (this.entities != null) {
			changeY += this.getChangeY();
			final int startChangeY = changeY;
			for (int i = 0; i < this.entities.length; i++) {
				if (this.visibleEntities[i]) {
					if ((this.isCheat()) && (i > 0)) {
						changeY = startChangeY;
					}
					Shape recClip = new Rectangle2D.Double(10, changeY, ApoGameConstants.LEVEL_WIDTH, ApoGameConstants.LEVEL_HEIGHT);
					g.setClip(recClip);
					Point[][] levelArray = new Point[3][10];
					for (int j = 0; j < this.entities[i].size(); j++) {
						if (this.entities[i].get(j).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_NO_MOVEMENT) {
							this.entities[i].get(j).render(g, 10, changeY);
							levelArray[(int)(this.entities[i].get(j).getY()/ApoGameConstants.TILE_SIZE)][(int)(this.entities[i].get(j).getX()/ApoGameConstants.TILE_SIZE)] = new Point(i, j);
						}
					}
					for (int j = 0; j < this.entities[i].size(); j++) {
						if (this.entities[i].get(j).getFixedMovement() != ApoGameConstants.PLAYER_DIRECTION_NO_MOVEMENT) {
							boolean bVisible = this.entities[i].get(j).isBVisible();
							if (bEditor) {
								this.entities[i].get(j).setBVisible(true);
							}
							if (this.entities[i].get(j).isBVisible()) {
								boolean bClip = false;
								if ((!this.entities[i].get(j).isMoving()) && (!this.entities[i].get(j).isAnotherEntityOver())) {
									if (levelArray[(int)(this.entities[i].get(j).getY()/ApoGameConstants.TILE_SIZE)][(int)(this.entities[i].get(j).getX()/ApoGameConstants.TILE_SIZE)] != null) {
										Point p = levelArray[(int)(this.entities[i].get(j).getY()/ApoGameConstants.TILE_SIZE)][(int)(this.entities[i].get(j).getX()/ApoGameConstants.TILE_SIZE)];
										if (((this.entities[p.x].get(p.y).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_UP) && (this.entities[i].get(j).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_DOWN)) ||
											((this.entities[p.x].get(p.y).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_DOWN) && (this.entities[i].get(j).getFixedMovement() == ApoGameConstants.PLAYER_DIRECTION_UP))) {
											this.entities[p.x].get(p.y).setOverAnotherEntity(0);
											this.entities[i].get(j).setOverAnotherEntity(ApoGameEntity.MAX_TIME);
											this.bShouldChange = true;
										}
									}
								}
//								if (!this.entities[i].get(j).isMoving()) {
//									this.bShouldChange = true;
//									
//									if (levelArray[(int)(this.entities[i].get(j).getY()/ApoGameConstants.TILE_SIZE)][(int)(this.entities[i].get(j).getX()/ApoGameConstants.TILE_SIZE)] > 0) {
//										g.setClip(new Rectangle2D.Double(this.entities[i].get(j).getX() + 10 , this.entities[i].get(j).getY() + changeY, ApoGameConstants.TILE_SIZE/2, ApoGameConstants.TILE_SIZE));
//										bClip = true;
//									}
//								}
								this.entities[i].get(j).render(g, 10, changeY);
								if (levelArray[(int)(this.entities[i].get(j).getY()/ApoGameConstants.TILE_SIZE)][(int)(this.entities[i].get(j).getX()/ApoGameConstants.TILE_SIZE)] != null) {
									levelArray[(int)(this.entities[i].get(j).getY()/ApoGameConstants.TILE_SIZE)][(int)(this.entities[i].get(j).getX()/ApoGameConstants.TILE_SIZE)].y = j;									
								} else {
									levelArray[(int)(this.entities[i].get(j).getY()/ApoGameConstants.TILE_SIZE)][(int)(this.entities[i].get(j).getX()/ApoGameConstants.TILE_SIZE)] = new Point(i, j);
								}
								if (bClip) {
									g.setClip(recClip);
								}
							}
							this.entities[i].get(j).setBVisible(bVisible);
						}
					}
					if (this.players[i] != null) {
						this.players[i].render(g, 10, changeY);
					}
					g.setClip(clip);
				}
				changeY += (int)(5 * ApoGameConstants.TILE_SIZE);
			}
		}
		changeY = realChangeY;
		if (this.instructions != null) {
			// render instructions
			changeY += ApoGameConstants.GAME_HEIGHT/2 - ApoGameConstants.LEVEL_HEIGHT/2;
			if (this.entities.length == 2) {
				changeY -= (int)(2.5 * ApoGameConstants.TILE_SIZE);
			}
			g.setColor(Color.BLACK);
			int add = (int)(4 * ApoGameConstants.TILE_SIZE);
			if (this.instructions.length == 3) {
				add = (int)(4.5 * ApoGameConstants.TILE_SIZE);
			}
			g.setFont(ApoGameConstants.FONT_LEVELNAME);
			if (this.instructions.length == 2) {
				String s = this.levelName;
				int w = g.getFontMetrics().stringWidth(s);
				g.drawString(s, ApoGameConstants.LEVEL_WIDTH/2 + 10 - w/2, 50); 
			}
			g.setFont(ApoGameConstants.FONT_INSTRUCTIONS);
			for (int i = 0; i < this.instructions.length; i++) {
				String s = this.instructions[i];
				if (s != null) {
					int w = g.getFontMetrics().stringWidth(s);
					g.drawString(s, ApoGameConstants.LEVEL_WIDTH/2 + 10 - w/2, changeY - 14); 
				}
				changeY += add;
			}
		}
	}


}
