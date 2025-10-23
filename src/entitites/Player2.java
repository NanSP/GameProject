package entitites;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.Playing;
import main.Game;
import utilz.LoadSave;


public class Player2 extends Entity{
	
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 30;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean  left, right, down, jump;
	private float playerSpeed = 1.3f * Game.SCALE;
	private int[][] lvlData;
	private float xDrawOffSet = 35 * Game.SCALE;
	private float yDrawOffSet = 18 * Game.SCALE;
	
	//JUMP AND GRAVITY SETTINGS
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	
	private BufferedImage statusBarImg, redBarImg, yellowBarImg;
	
	private int hpBarWidth = (int)(116 * Game.SCALE);
	private int hpBarHeight = (int)(64 * Game.SCALE);
	private int hpBarX = (int)(705 * Game.SCALE);
	private int hpBarY = (int)(10 * Game.SCALE);
	
	private int healthBarWidth = (int)(50 * Game.SCALE);
	private int healthBarHeight = (int)(6 * Game.SCALE);
	private int healthBarXStart = (int)(4 * Game.SCALE);
	private int healthBarYStart = (int)(54 * Game.SCALE);
	
	private int maxHealth = 100;
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;
	
	private Rectangle2D.Float attackBox;
	
	private int flipX = 0;
	private int flipW = 1;
	private boolean attackChecked;
	private Playing playing;
	
	
	public Player2(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimations();
		initHitbox(x, y, (int)(12 * Game.SCALE),(int)( 45 * Game.SCALE));//43
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int)(30 * Game.SCALE), (int)(30 * Game.SCALE));		
	}

	public void update() {
		updateHealthBar();
		if(currentHealth <= 0) {
			
			if(playerAction != DEAD) {
				playerAction = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
			} else if(aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= aniSpeed - 1) {
				playing.setGameOver(true);
			} else
				updateAnimationTick();
			
			return;
		}
		updateAttackBox();
	
		updatePos();
		if(attacking)
			checkAttack();
		updateAnimationTick();
		setAnimation();
	}
	
	private void updateAttackBox() {
		if(right) {
			attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
		}else if(left) {
			attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 20);
		}
		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	private void updateHealthBar() {
		
		healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
	}
	
	public void render(Graphics g, int xLvlOffset) {
		
		g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x - xDrawOffSet) - xLvlOffset + flipX, (int)(hitbox.y - yDrawOffSet), width * flipW, height, null);
		//drawHitbox(g, xLvlOffset);
		//drawAttackBox(g, xLvlOffset);
		drawUI(g);
	}
	
	private void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.blue);
		g.drawRect((int)attackBox.x - xLvlOffset, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
	}
	
	private void drawUI(Graphics g) {
		
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + hpBarX, healthBarYStart + hpBarY, healthWidth, healthBarHeight);
		
		g.drawImage(redBarImg, (int)(757 * Game.SCALE), (int)(11 * Game.SCALE), (int)(61 * Game.SCALE), (int)(61 * Game.SCALE), null);
		g.drawImage(yellowBarImg, (int)(724 * Game.SCALE), (int)(57 * Game.SCALE), (int)(32 * Game.SCALE), (int)(5 * Game.SCALE), null);
		g.drawImage(statusBarImg, hpBarX, hpBarY, hpBarWidth, hpBarHeight, null);
	}
	
	private void updateAnimationTick() {
		
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(playerAction)) {
				
				aniIndex = 0;
				attacking = false;	
				attackChecked = false;
			}	
		}
	}
	
	private void setAnimation() {
		
		int startAni = playerAction;

		if(moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;
		if(inAir) {
			if(airSpeed < 0)
				playerAction = JUMP;
			else
				playerAction = FALLING;
		}
		
		if(attacking) {
			playerAction = ATTACK;
			aniSpeed = 10;
			
			if(startAni != ATTACK) {
				aniIndex = 4;
				aniTick = 0;
				return;
			}
		} else {
			aniSpeed = 30;
		}
		if(startAni != playerAction)
			resetAniTick();
	}
	
	private void resetAniTick() {
			
		aniTick = 0;
		aniIndex = 0;
		
	}

	private void updatePos() {
		
		moving = false;
		
		if(jump)
			jump();
		
		if(!inAir)
			if((!left && !right)||(right && left))
			return;
		
		float xSpeed = 0;
	
		if (left) {
			xSpeed -= playerSpeed;
			flipX = 0;
			flipW = 1;
		}
		if (right) {
			xSpeed += playerSpeed;
			flipX = width;
			flipW = -1;
		}
		
		if(!inAir)
			if(!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;
		
		if(inAir) {
			
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0)
					resetInAir();
				else 
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
				
			}
			
		} else 
			updateXPos(xSpeed);
		moving = true;
	}
	
	private void jump() {
		
		if(inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
		
	}

	private void resetInAir() {

		inAir = false;
		airSpeed = 0;
		
	}

	private void updateXPos(float xSpeed) {

		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}
		
	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		
		if(currentHealth <= 0) {
			currentHealth = 0;
				//gameOver();
		}else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	private void loadAnimations() {
		
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER2_ATLAS);
			
			animations =new BufferedImage[9][8];
			
			for(int j = 0; j < animations.length; j++) 
				 for(int i = 0; i < animations[j].length; i++)
					 animations[j][i] = img.getSubimage(i*80, j*64, 80, 64);
		
			statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.HP2_SPRITE);
			redBarImg = LoadSave.GetSpriteAtlas(LoadSave.REDBAR2);
			yellowBarImg = LoadSave.GetSpriteAtlas(LoadSave.YELLOW_BAR);
	}
	
	public void loadLvlData(int[][] lvlData) {
		
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		
	}
	

	public void resetDirBooleans() {
		
		left = false;
		right = false;
		down = false;
		
	}
	
	public void setAttacking(boolean attacking) {
		
		this.attacking = attacking;
		
	}
	
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	
	public Rectangle2D.Float getHitbox() {
	    return hitbox;
	}

	public int getCurrentHealth() {
	    return currentHealth;
	}
	
	public void reserAll() {

		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		playerAction = IDLE;
		currentHealth = maxHealth;
		
		hitbox.x = x;
		hitbox.y = y;
		
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}
	
	private void checkAttack() {
	    if(attackChecked || aniIndex != 4)
	        return;
	    attackChecked = true;
	    
	    playing.checkEnemyHit(attackBox);
	    playing.checkPlayerHitPlayer(attackBox, playing.getPlayer());
	}
	
}


