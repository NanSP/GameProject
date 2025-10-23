package audio;

import javax.sound.sampled.*;

public class DefaultClipAdapter implements ClipAdapter {
    private final Clip clip;
    private BooleanControl muteControl;
    private FloatControl gainControl;
    private float previousGain;

    public DefaultClipAdapter(Clip clip) {
        this.clip = clip;

        if (clip.isControlSupported(BooleanControl.Type.MUTE)) {
            muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
        }

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            previousGain = gainControl.getValue();
        }
    }

    @Override
    public void setMute(boolean mute) {
        if (muteControl != null) {
            muteControl.setValue(mute);
        } else if (gainControl != null) {
            if (mute) {
                previousGain = gainControl.getValue();
                gainControl.setValue(gainControl.getMinimum());
            } else {
                gainControl.setValue(previousGain);
            }
        } else {
            System.out.println("Nenhum controle de mute/volume suportado para este clip.");
        }
    }

    @Override
    public void setVolume(float volume) {
        if (gainControl != null) {
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    @Override
    public void play() {
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    @Override
    public void stop() {
        clip.stop();
    }

    @Override
    public Clip getClip() {
        return clip;
    }
    
    @Override
    public void setMicrosecondPosition(long pos) {
        clip.setMicrosecondPosition(pos);
    }

    @Override
    public void start() {
        clip.start();
    }
}
