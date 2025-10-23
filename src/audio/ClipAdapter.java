package audio;

import javax.sound.sampled.Clip;

public interface ClipAdapter {
    void setMute(boolean mute);
    void setVolume(float volume);
    void play();
    void stop();
    Clip getClip();
    void setMicrosecondPosition(long pos);
    void start();
}
