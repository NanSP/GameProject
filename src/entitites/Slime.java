package entitites;

import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;

import main.Game;

public class Slime extends Enemy{
	
	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX;

	public Slime(float x, float y) {
		super(x, y, SLIME_WIDTH, SLIME_HEIGHT , SLIME);
		initHitbox(x, y, (int)(20 * Game.SCALE), (int)(10 * Game.SCALE));
		initAttackBox();
	}
	
	private void initAttackBox() {

		attackBox = new Rectangle2D.Float(x, y, (int)(24 * Game.SCALE), (int)(10 * Game.SCALE));
		attackBoxOffsetX = (int)(Game.SCALE * 2);
		
	}

	public void update(int[][] lvlData, Player player, Player2 player2) {
		updateBehavior(lvlData, player, player2);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	private void updateBehavior(int[][] lvlData, Player player, Player2 player2) {
		if(firstUpdate) {
			firstUpdateCheck(lvlData);
		}
		
		if(inAir)
			updateInAir(lvlData);
		else {
			switch(enemyState) {
			case IDLE:
				newState(MOVING);
				break;
			case MOVING:
				
				if(aniIndex == 1)
					attackChecked = false;
				
				if(canSeePlayer(lvlData, player, player2)) {
					turnTowardsPlayer(player, player2);
				if(isPlayerCloseForAttack(player)|| isPlayer2CloseForAttack(player2))
					newState(ATTACK);
				}
				move(lvlData);
				break;
			case ATTACK:
				if(aniIndex == 0)
					attackChecked = false;
				
				if(aniIndex == 2 && !attackChecked)
					checkEnemyHit(attackBox, player, player2);
				break;
			}
		}
	}
	
	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.black);
		g.drawRect((int)(attackBox.x - xLvlOffset), (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
	}
	
	public int flipX() {
		if(walkDir == RIGHT)
			return width;
		else
			return 0;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return -1;
		else
			return 1;
	}
}
