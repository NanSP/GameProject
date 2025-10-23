package utilz;

import main.Game;

public class Constants {
	
	public static class EnemyConstants{
		public static final int SLIME = 0;
		
		public static final int IDLE = 0;
		public static final int MOVING = 1;
		public static final int DEAD = 2;
		public static final int ATTACK = 3;
		
		
		public static final int SLIME_DEFAULT_WIDTH = 32;
		public static final int SLIME_DEFAULT_HEIGHT = 32;
		
		public static final int SLIME_WIDTH = (int)(SLIME_DEFAULT_WIDTH * Game.SCALE);
		public static final int SLIME_HEIGHT = (int)(SLIME_DEFAULT_HEIGHT * Game.SCALE);
		
		public static final int SLIME_DRAWOFFSET_X = (int)(5 * Game.SCALE);
		public static final int SLIME_DRAWOFFSET_Y = (int)(20 * Game.SCALE);
		
		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			switch(enemy_type){
			case SLIME:
				switch(enemy_state) {
				case IDLE:
					return 5;
				case MOVING,ATTACK:
					return 8;
				case DEAD:
					return 6;
				}
			}
			return 0;
		}
		
		public static int GetMaxHealth(int enemy_type) {
			switch(enemy_type) {
			case SLIME:
				return 10;
			default:
				return 1;
			}
			
		}
		
		public static int GetEnemyDmg(int enemy_type) {
			switch(enemy_type) {
			case SLIME:
				return 10;
			default:
				return 0;
			}
		}
	}
	
	public static class Enviroment{
		public static final int CLOUD_BIG_WIDTH_DEFAULT = 348;
		public static final int CLOUD_BIG_HEIGHT_DEFAULT = 91;
		
		public static final int CLOUD_SMALL_WIDTH_DEFAULT = 74;
		public static final int CLOUD_SMALL_HEIGHT_DEFAULT = 24;
		
		
		public static final int CLOUD_BIG_WIDTH = (int)(CLOUD_BIG_WIDTH_DEFAULT * Game.SCALE);
		public static final int CLOUD_BIG_HEIGHT = (int)(CLOUD_BIG_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int CLOUD_SMALL_WIDTH = (int)(CLOUD_SMALL_WIDTH_DEFAULT * Game.SCALE);
		public static final int CLOUD_SMALL_HEIGHT = (int)(CLOUD_SMALL_HEIGHT_DEFAULT * Game.SCALE);
	}
	
	public static class UI{
		public static class Buttons {
			public static final int B_WIDHT_DEFAULT = 273;
			public static final int B_HEIGHT_DEFAULT = 87;
			public static final int B_WIDTH = ((int)(B_WIDHT_DEFAULT * Game.SCALE) /2);
			public static final int B_HEIGHT = ((int)(B_HEIGHT_DEFAULT * Game.SCALE)/2);
		}
		
		public static class PauseButtons{
			public static final int SOUND_SIZE_DEFAULT = 100;
			public static final int SOUND_SIZE = (int)((SOUND_SIZE_DEFAULT * Game.SCALE)/3.1);
		}
		public static class URMButtons{
			public static final int URM_DEFAULT_SIZE = 99;
			public static final int URM_SIZE = (int)((URM_DEFAULT_SIZE * Game.SCALE))/2;
		}
		public static class VolumeButtons{
			public static final int VOLUME_DEFAULT_WIDTH = 60;
			public static final int VOLUME_DEFAULT_HEIGHT = 110;
			public static final int SLIDER_DEFAULT_WIDTH = 415;
			
			public static final int VOLUME_WIDTH = (int)((VOLUME_DEFAULT_WIDTH * Game.SCALE)/2.8);
			public static final int VOLUME_HEIGHT = (int)((VOLUME_DEFAULT_HEIGHT * Game.SCALE)/2.8);
			public static final int SLIDER_WIDTH = (int)((SLIDER_DEFAULT_WIDTH * Game.SCALE)/2.2);
		}
	}
	
	public static class Directions{
		
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		public static final int DOWN = 3;
		
	}
	
	public static class PlayerConstants{
		
		public static final int IDLE = 0;
		public static final int WALK = 1;
		public static final int RUNNING = 2;
		public static final int BACK = 3;
		public static final int JUMP = 4;
		public static final int FALLING = 5;
		public static final int GROUND = 6;
		public static final int ATTACK = 7;
		public static final int DEAD = 8;
		
		public static int GetSpriteAmount(int player_action) {
			
			switch(player_action) {
			
			case WALK:
			case RUNNING:
			case DEAD:
				return 8;
			case BACK:
				return 3;
			case IDLE:
				return 5;
			case ATTACK:
				return 6;
			case JUMP:
			case FALLING:
			case GROUND:
				return 4;
				default:
					return 1;
			
			}
		}
		
	}

}
