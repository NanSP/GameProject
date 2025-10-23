package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entitites.Slime;
import main.Game;
import static utilz.Constants.EnemyConstants.SLIME;

public class LoadSave {
	
	public static final String PLAYER_ATLAS = "playerImgRight.png";
	public static final String PLAYER2_ATLAS = "playerImgLeft1.png";
	public static final String LEVEL_ATLAS = "FloorTiles.png";
	public static final String LEVEL_DATA = "level_one_data2.png";
	public static final String MENU_BUTTONS = "button_atlas1.png";
	public static final String MENU_BACKGROUND = "menu1.png";
	public static final String PAUSED_BACKGROUND = "pause_menu1.png";
	public static final String SOUND_BUTTONS = "sound_button1.png";
	public static final String URM_BUTTONS = "urm_buttons1.png";
	public static final String VOLUME_BUTTONS = "slideBar1.png";
	public static final String PLAYING_BACKGROUND_1 = "background1.png";
	public static final String PLAYING_BACKGROUND_2 = "background2.png";
	public static final String PLAYING_BACKGROUND_3 = "background3.png";
	public static final String PLAYING_BACKGROUND_4 = "background4.png";
	public static final String PLAYING_BACKGROUND_5 = "background5.png";
	public static final String PLAYING_SUN = "sun.png";
	public static final String PLAYING_BIRDS = "birds1.png";
	public static final String PLAYING_CLOUD_BIG = "cloudBig.png";
	public static final String PLAYING_CLOUD_SMALL = "cloudSmall.png";
	public static final String SLIME_SPRITE = "slimeBlue1.png";
	public static final String HP_SPRITE = "Hpbar.png";
	public static final String HP2_SPRITE = "Hpbar2.PNG";
	public static final String REDBAR = "redbar.png";
	public static final String REDBAR2 = "redbar2.png";
	public static final String YELLOW_BAR = "yellowbar.png";
	public static final String DUEL_COMPLETED = "endGame1.png";
	public static final String OPTIONS_BG = "opcoes1.png";
	public static final String BG_MENU = "bgMenu.png";

	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/imgs/" + fileName);
		
		try {
			img = ImageIO.read(is);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static ArrayList<Slime> getSlimes(){
		BufferedImage img = GetSpriteAtlas(LEVEL_DATA);
		ArrayList<Slime> list = new ArrayList<>();
		for(int j = 0; j < img.getHeight(); j++)
			for(int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if(value == SLIME)
					list.add(new Slime( i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;
	}
	
	public static int[][] GetLevelData(){

		BufferedImage img = GetSpriteAtlas(LEVEL_DATA);
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		
		for(int j = 0; j < img.getHeight(); j++)

			for(int i = 0; i < img.getWidth(); i++) {

				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if(value >= 162)
					value = 0;
				lvlData[j][i] = value;
		}	

		return lvlData;

	} 
}
