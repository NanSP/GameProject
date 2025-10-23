package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entitites.EnemyManager;
import entitites.Player;
import entitites.Player2;
import levels.LevelManager;
import main.Game;
import ui.DuelCompleteOverlay;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Enviroment.*;

public class Playing extends State implements StateMethods{

	private Player player;
	private Player2 player2;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private PauseOverlay  pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private DuelCompleteOverlay duelCompleteOverlay;
	private boolean paused = false;
	
	private int xLvlOffset;
	private int leftBorder = (int)(0.2 * Game.GAME_WIDTH);//no caso é a altura no nosso jogo não se esqueça
	private int rightBorder = (int)(0.3 * Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
	
	private BufferedImage backgroundImg1, backgroundImg2, backgroundImg3, backgroundImg4, backgroundImg5, 
							cloudBig, cloudSmall, sun, birds;
	private int[] cloudSmallPos;
	private Random rnd = new Random();
	private boolean gameOver;
	private boolean lvlCompleted;
	private boolean playerDying;
	
	public Playing(Game game) {
		super(game);
		initClasses();
		iniBackground();
	
		cloudBig = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_CLOUD_BIG);
		cloudSmall = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_CLOUD_SMALL);
		
		cloudSmallPos = new int[8];
		for(int i = 0; i < cloudSmallPos.length; i++)
			cloudSmallPos[i] = (int)(40 * Game.SCALE) + rnd.nextInt((int)(150 * Game.SCALE));
	}

	private void iniBackground() {
		backgroundImg1 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_1);
		backgroundImg2 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_2);
		backgroundImg3 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_3);
		backgroundImg4 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_4);
		backgroundImg5 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_5);
		sun = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_SUN);
		birds = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BIRDS);
	}

	private void initClasses() {
		
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		player = new Player(450, 200,(int)(80 * Game.SCALE),(int)(64 * Game.SCALE), this);
		player2 = new Player2(1220, 200,(int)(80 * Game.SCALE),(int)(64 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player2.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		duelCompleteOverlay = new DuelCompleteOverlay(this);

	}

	@Override
	public void update() {
		if(paused) {
			pauseOverlay.update();
		} else if(lvlCompleted) {
			duelCompleteOverlay.update();
		} else if(gameOver) {
			gameOverOverlay.update();
		} else if(playerDying) {	
			player.update();
			player2.update();
		} else {
			levelManager.update();
			player.update();
			player2.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player, player2);
			checkCloseToBorder();
		}
	}

	private void checkCloseToBorder() {
		int playerX = (int)player.getHitbox().x;
		int diff = playerX - xLvlOffset;
		
		int player2X = (int)player2.getHitbox().x;
		int diff2 = player2X - xLvlOffset;
		
		if(diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if(diff < leftBorder)
			xLvlOffset += diff - leftBorder;
		
		if(xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if(xLvlOffset < 0)
			xLvlOffset = 0;
		
		/*if(diff2 > rightBorder)
			xLvlOffset += diff2 - rightBorder;
		else if(diff2 < leftBorder)
			xLvlOffset += diff2 - leftBorder;
		
		if(xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if(xLvlOffset < 0)
			xLvlOffset = 0;*/
	}

	@Override
	public void draw(Graphics g) {
		
		drawBackgound(g);
		drawClouds(g);
		
		levelManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		player2.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		
		if(paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			
			pauseOverlay.draw(g);
		}else if(gameOver)
			gameOverOverlay.draw(g);
		else if(lvlCompleted)
			duelCompleteOverlay.draw(g);
	}

	private void drawBackgound(Graphics g) {
		g.drawImage(backgroundImg1, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(birds, 900, 150, (int)(50 * Game.SCALE), (int)(50 * Game.SCALE), null);
		g.drawImage(backgroundImg2, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImg3, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(sun, 100, 150, (int)(50 * Game.SCALE), (int)(50 * Game.SCALE), null);
		g.drawImage(backgroundImg4, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImg5, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
	}

	private void drawClouds(Graphics g) {
		
		for(int i = 0; i < 3; i++) 
			g.drawImage(cloudBig, 0 + i * CLOUD_BIG_WIDTH - (int)(xLvlOffset * 0.3), (int)(10 * Game.SCALE), CLOUD_BIG_WIDTH, CLOUD_BIG_HEIGHT, null);
		
		for(int i = 0; i < cloudSmallPos.length; i++)
			g.drawImage(cloudSmall, CLOUD_SMALL_WIDTH * 4 * i - (int)(xLvlOffset * 0.7), cloudSmallPos[i], CLOUD_SMALL_WIDTH, CLOUD_SMALL_HEIGHT, null);
	}
	
	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		player.reserAll();
		player2.reserAll();
		enemyManager.resetAllEnemie();
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver= gameOver;
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void mouseDragged(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.MouseDragged(e);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mousePressed(e);
			else if(lvlCompleted)
				duelCompleteOverlay.mousePressed(e);
		}else {
			gameOverOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseReleased(e);
			else if(lvlCompleted)
				duelCompleteOverlay.mouseReleased(e);
		}else {
			gameOverOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseMoved(e);
			else if(lvlCompleted)
				duelCompleteOverlay.mouseMoved(e);
		}else 
			gameOverOverlay.mouseMoved(e);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gameOver)
			gameOverOverlay.keyPressed(e);
		else
		switch(e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_W:
			player.setJump(true);
			break;
		case KeyEvent.VK_S:
			player.setDown(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_SPACE:
			player.setAttacking(true);
			break;
		case KeyEvent.VK_LEFT:
			player2.setLeft(true);
			break;
		case KeyEvent.VK_UP:
			player2.setJump(true);
			break;
		case KeyEvent.VK_DOWN:
			player2.setDown(true);
			break;
		case KeyEvent.VK_RIGHT:
			player2.setRight(true);
			break;
		case KeyEvent.VK_ENTER:
			player2.setAttacking(true);
			break;
		case KeyEvent.VK_ESCAPE:
			paused = !paused;
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!gameOver)
			
		switch(e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_W:
			player.setJump(false);
			break;
		case KeyEvent.VK_S:
			player.setDown(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		case KeyEvent.VK_LEFT:
			player2.setLeft(false);
			break;
		case KeyEvent.VK_UP:
			player2.setJump(false);
			break;
		case KeyEvent.VK_DOWN:
			player2.setDown(false);
			break;
		case KeyEvent.VK_RIGHT:
			player2.setRight(false);
			break;
		case KeyEvent.VK_ENTER:
			player2.setAttacking(false);
			break;
			
		}
		
	}
	
	public Player getPlayer() {
		
		return player;
		
	}
	
	public Player2 getPlayer2() {
		
		return player2;
		
	}
	
	public void unpauseGame() {
		paused = false;
	}
	public void setLvlCompleted(boolean lvlCompleted) {
		this.lvlCompleted = lvlCompleted;
	}

	public void windowFocusLost() {

		player.resetDirBooleans();
		player2.resetDirBooleans();
		
	}
	
	public void checkPlayerHitPlayer(Rectangle2D.Float attackBox, Player player) {
	    if(attackBox.intersects(player.getHitbox())) {
	        player.changeHealth(-10);
	    }
	}
	
	public void checkPlayerHitPlayerI(Rectangle2D.Float attackBox, Player2 player2) {
	    if(attackBox.intersects(player2.getHitbox())) {
	        player2.changeHealth(-10);
	    }
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}
	
}
