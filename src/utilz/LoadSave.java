package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadSave {
	
	public static final String PLAYER_ATLAS = "playerImgRight.png";
	public static final String PLAYER2_ATLAS = "playerImgLeft.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String LEVEL_ONE_DATA = "lvl_test.png";
	public static final String LEVEL_DATA = "lvl_test_long.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSED_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String PLAYING_BACKGROUND_1 = "background1.png";
	public static final String PLAYING_BACKGROUND_2 = "background2.png";
	public static final String PLAYING_BACKGROUND_3 = "background3.png";
	public static final String PLAYING_BACKGROUND_4 = "background4.png";
	public static final String PLAYING_BACKGROUND_5 = "background5.png";
	public static final String PLAYING_SUN = "sun.png";
	public static final String PLAYING_BIRDS = "birds1.png";
	public static final String PLAYING_CLOUD_BIG = "cloudBig.png";
	public static final String PLAYING_CLOUD_SMALL = "cloudSmall.png";
	
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
