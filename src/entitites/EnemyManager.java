package entitites;

import static utilz.Constants.EnemyConstants.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] slimeArr;
	private ArrayList<Slime> slimes = new ArrayList<>();
	private float xDrawOffset = 6 * Game.SCALE;
	private float yDrawOffset = 20 * Game.SCALE;
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();
	}

	private void addEnemies() {

		slimes = LoadSave.getSlimes();
	}

	public void update(int[][] lvlData, Player player, Player2 player2) {
		for(Slime s : slimes)
			if(s.isActive())
				s.update(lvlData, player, player2);
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		drawSlimes(g, xLvlOffset);
	}
	
	private void drawSlimes(Graphics g, int xLvlOffset) {
		for(Slime s : slimes) 
		
		if(s.isActive()){
			g.drawImage(slimeArr[s.getEnemyState()][s.getAniIndex()], (int)(s.getHitbox().x - xDrawOffset) - xLvlOffset + s.flipX(), (int)(s.getHitbox().y - yDrawOffset), SLIME_WIDTH * s.flipW(), SLIME_HEIGHT, null);
			//s.drawHitbox(g, xLvlOffset);
			//s.drawAttackBox(g, xLvlOffset);
		}
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for(Slime s : slimes)
			if(s.isActive())
				if(s.currentHealth > 0)
				if(attackBox.intersects(s.getHitbox())) {
					s.hurt(10);
					return;
				}
	}
	
	private void loadEnemyImgs() {
		slimeArr = new BufferedImage[4][8];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
		for(int j = 0; j < slimeArr.length; j++)
			for (int i = 0; i < slimeArr[j].length; i++)
			slimeArr[j][i] = temp.getSubimage(i * SLIME_DEFAULT_WIDTH, j * SLIME_DEFAULT_HEIGHT, SLIME_DEFAULT_WIDTH, SLIME_DEFAULT_HEIGHT);
	}

	public void resetAllEnemie() {
		for(Slime s : slimes)
			s.resetEnemy();
		
	}
	
	
}
