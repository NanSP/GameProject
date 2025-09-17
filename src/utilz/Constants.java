package utilz;

public class Constants {
	
	public static class Directions{
		
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		public static final int UP = 2;
		public static final int DOWN = 3;
		
	}
	
	public static class PlayerConstants{
		
		public static final int IDLE = 0;
		public static final int WALK = 1;
		public static final int RUNNING = 2;
		public static final int BACK = 3;
		public static final int HIT = 4;
		public static final int JUMP = 5;
		public static final int FALLING = 6;
		public static final int GROUND = 7;
		public static final int ATTACK = 8;
		public static final int DEAD = 9;
		
		public static int GetSpriteAmount(int player_action) {
			
			switch(player_action) {
			
			case WALK:
			case RUNNING:
			case DEAD:
			case BACK:
				return 8;
			case IDLE:
				return 5;
			case HIT:
				return 2;
			case JUMP:
			case FALLING:
			case GROUND:
				return 4;
			case ATTACK:
				return 6;
				default:
					return 1;
			
			}
		}
		
	}

}
