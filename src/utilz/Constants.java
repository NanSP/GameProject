package utilz;

public class Constants {
	
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
			case BACK:
				return 8;
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
