package entitites;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class Player extends Entity{

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 30;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean  left, right, down, jump;
	private float playerSpeed = 2.0f;
	private int[][] lvlData;
	private float xDrawOffSet = 33 * Game.SCALE;
	private float yDrawOffSet = 17 * Game.SCALE;
	
	//JUMP AND GRAVITY SETTINGS
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	
	
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y, 12 * Game.SCALE, 45 * Game.SCALE);//43
	
	}
	
	public void update() {
		
		updatePos();
		updateAnimationTick();
		setAnimation();
	}
	
	public void render(Graphics g) {
		
		g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x - xDrawOffSet), (int)(hitbox.y - yDrawOffSet), width, height, null);
		//drawHitbox(g);
		
	}
	

	private void updateAnimationTick() {
		
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(playerAction)) {
				
				aniIndex = 0;
				attacking = false;	
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
		if(!left && !right && !inAir)
			return;
		
		float xSpeed = 0;
	
		if (left) 
			xSpeed -= playerSpeed;
		if (right) 
			xSpeed += playerSpeed;
		
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

	private void loadAnimations() {
		
			BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			
			animations =new BufferedImage[9][8];
			
			for(int j = 0; j < animations.length; j++) 
				 for(int i = 0; i < animations[j].length; i++)
					 animations[j][i] = img.getSubimage(i*80, j*64, 80, 64);
		
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
	
}

