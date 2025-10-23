package entitites;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;


import static utilz.Constants.Directions.*;

import main.Game;

public abstract class Enemy extends Entity{
	
	protected int aniIndex, enemyState, enemyType;
	protected int aniTick, aniSpeed = 25;
	protected boolean firstUpdate = true;
	protected boolean inAir;
	protected float fallSpeed;
	protected float gravity = 0.04f * Game.SCALE;
	protected float walkSpeed = 0.2f * Game.SCALE;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected int maxHealth;
	protected int currentHealth;
	protected boolean active = true;
	protected boolean attackChecked;
	protected Game game;
	

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x, y, width, height);
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
	}
	
	protected void firstUpdateCheck(int[][] lvlData) {
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}
	
	protected void updateInAir(int[][] lvlData) {
		if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += fallSpeed;
			fallSpeed += gravity;
		}else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed)-50;
			tileY = (int)(hitbox.y / Game.TILES_SIZE);
		}
	}
	
	public void move(int[][] lvlData) {
		float xSpeed = 0;
		
		if(walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;
			
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if(IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}
		changeWalkDir();
	}
	
	protected void turnTowardsPlayer(Player player, Player2 player2) {
		float distToP1 = Math.abs(player.hitbox.x - hitbox.x);
	    float distToP2 = Math.abs(player2.hitbox.x - hitbox.x);
	    
	    if (distToP1 <= distToP2) {
	        
	        if (player.hitbox.x > hitbox.x)
	            walkDir = RIGHT;
	        else
	            walkDir = LEFT;
	    } else {
	        
	        if (player2.hitbox.x > hitbox.x)
	            walkDir = RIGHT;
	        else
	            walkDir = LEFT;
	    }
	}
	
	
	
	protected boolean canSeePlayer(int[][] lvlData, Player player, Player2 player2) {
		int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
		int player2TileY = (int)(player2.getHitbox().y / Game.TILES_SIZE);
		if(Math.abs(playerTileY - tileY) <= 2 || Math.abs(player2TileY - tileY) <= 2) {
			if(isPlayerRange(player)){
				if(IsSightClear(lvlData, hitbox, player.hitbox, tileY) || IsSightClear(lvlData, hitbox, player2.hitbox, tileY)) {
					return true;
				}
			}
			if(isPlayer2Range(player2)) {
				if(IsSightClear(lvlData, hitbox, player2.hitbox, tileY)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	protected boolean isPlayerRange(Player player) {
		
		int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
		
	} 
	
	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance ;
	}
	
	protected boolean isPlayer2Range(Player2 player2) {
		
		int absValue = (int)Math.abs(player2.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
		
	} 
	
	protected boolean isPlayer2CloseForAttack(Player2 player2) {
		int absValue = (int)Math.abs(player2.hitbox.x - hitbox.x);
		return absValue <= attackDistance ;
	}

	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	public void hurt(int amount) {
		currentHealth -= amount;
		
		if(currentHealth <= 0)
			newState(DEAD);
	}
	
	protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player, Player2 player2) {
		if(attackBox.intersects(player.hitbox)) {
			player.changeHealth(-GetEnemyDmg(enemyType));
		}
		if(attackBox.intersects(player2.hitbox)) {
			player2.changeHealth(-GetEnemyDmg(enemyType));
		}
		attackChecked = true;
	} 
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
				
				switch(enemyState) {
				case ATTACK -> enemyState = IDLE;
				case DEAD -> active = false;
				}
			}
		}
	}
	
	protected void changeWalkDir() {
		if(walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		fallSpeed = 0;
	}

	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getEnemyType() {
		return enemyType;
	}
	
	public int getEnemyState() {
		return enemyState;
	}

	public boolean isActive() {
		return active;
	}
}
