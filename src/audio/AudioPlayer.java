package audio;

import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioPlayer {

	public static int MENU = 0;
	public static int LVL = 1;

	public static int DIE = 0;
	public static int JUMP = 1;
	public static int GAMEOVER = 2;
	public static int WALK = 3;
	public static int ATTACK= 4;
	public static int ATTACK_IN_AIR = 5;
	public static int BUTTONS = 6;

	private Clip[] songs;
	private ClipAdapter[] effects;
	private int currentSongId;
	private float volume = 1f;
	private boolean songMute, effectMute;
	private Random rand = new Random();

	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU);
	}

	private void loadSongs() {
		String[] names = {"menuSong1", "lvl1"};
		songs = new Clip[names.length];
		for (int i = 0; i < songs.length; i++)
			songs[i] = getClip(names[i]);
	}

	private void loadEffects() {
		String[] effectNames = {  "dying1", "jump1", "endGame1", "walk1", "hitSword1", "swordInAir1", "buttonssound1"};
		effects = new ClipAdapter[effectNames.length];

		for (int i = 0; i < effects.length; i++) {
			Clip clip = getClip(effectNames[i]);
			effects[i] = new DefaultClipAdapter(clip);
		}

		updateEffectsVolume();
	}

	private Clip getClip(String name) {
	    String path = "/sounds/" + name + ".wav";
	    URL url = getClass().getResource(path);
	    //System.out.println("Tentando carregar: " + path + " → " + url);

	    if (url == null) {
	      //System.err.println("Arquivo não encontrado: " + path);
	        return null;
	    }

	    try (AudioInputStream audio = AudioSystem.getAudioInputStream(url)) {
	        Clip c = AudioSystem.getClip();
	        c.open(audio);
	        return c;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectsVolume();
	}

	public void stopSong() {
		if (songs[currentSongId].isActive())
			songs[currentSongId].stop();
	}

	public void setLevelSong(int lvlIndex) {
			playSong(LVL);	
	}

	public void playAttackSound() {
		int start = 4;
		playEffect(start);
	}
	
	public void playSlimeAttackSound() {
		int start = 4;
		playEffect(start);
	}

	public void playEffect(int effect) {
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();
	}

	public void playSong(int song) {
		stopSong();

		currentSongId = song;
		updateSongVolume();
		songs[currentSongId].setMicrosecondPosition(0);
		songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void toggleSongMute() {
		this.songMute = !songMute;
		for (Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}

	public void toggleEffectMute() {
	    this.effectMute = !effectMute;
	    for (ClipAdapter adapter : effects) {
	        adapter.setMute(effectMute);
	    }
	    if (!effectMute)
	        playEffect(JUMP);
	}

	private void updateSongVolume() {

		FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);

	}

	private void updateEffectsVolume() {
	    for (ClipAdapter adapter : effects) {
	        Clip c = adapter.getClip();
	        if (c == null) continue;

	        FloatControl gainControl = null;
	        if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
	            gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
	        } else if (c.isControlSupported(FloatControl.Type.VOLUME)) {
	            gainControl = (FloatControl) c.getControl(FloatControl.Type.VOLUME);
	        }

	        if (gainControl != null) {
	            float range = gainControl.getMaximum() - gainControl.getMinimum();
	            float gain = (range * volume) + gainControl.getMinimum();
	            gainControl.setValue(gain);
	        } else {
	            System.out.println("Nenhum controle de volume suportado para este clip.");
	        }
	    }
	}

}
