package ca.otitismedia.simone;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ToggleButton;


import ca.otitismedia.simone.R;

/**
 * Created by Matt Leering on 2017-11-18.
 */


public class ColourToggleButton extends ToggleButton {

    Enum beatBoxSounds;

    private MediaPlayer mySoundClip = null; //This is storage for the sound that btn1 will play
    private ToggleButton myLayoutObject; //A ToggleButton to represent one of the coloured buttons

    private Integer buttonNumber = null;
    private Integer soundsToUse;
    private Context context;

    public ColourToggleButton(Context context, Integer buttonNumber) {
        super(context);
        this.context = context;

        if (buttonNumber < 1 || buttonNumber > 4) {
            System.err.println("Invalid buttonNumber provided in ca.otitismedia.simone.ColourToggleButton Constructor");
            return;
        }
        this.buttonNumber = buttonNumber;
        myLayoutObject = findViewById(getResources().getIdentifier("btn" + buttonNumber.toString(), "id", context.getPackageName()));
        mySoundClip = getSound();

    }


    private MediaPlayer getSound() {
        MediaPlayer dynamicSound = null;

        Integer[] beatBoxSounds = {R.raw.beatbox_sound1, R.raw.beatbox_sound2, R.raw.beatbox_sound3, R.raw.beatbox_sound4};
        Integer[] drumSounds = {R.raw.kick, R.raw.snare, R.raw.hats, R.raw.crash};
        Integer[] keyboardSounds = {R.raw.beatbox_sound1, R.raw.beatbox_sound2, R.raw.beatbox_sound3, R.raw.beatbox_sound4};

        Integer[] currentSoundBank = null;
        Integer rawSoundID = null;

        // Retrieve the proper sound from the soundbank
        switch (this.soundsToUse) {
            case R.id.useBeatbox:
                currentSoundBank = beatBoxSounds;
                break;

            case R.id.useDrums:
                currentSoundBank = drumSounds;
                break;

            case R.id.useKeyboard:
                currentSoundBank = keyboardSounds;
                break;
        }

        try {
            rawSoundID = currentSoundBank[this.buttonNumber];
        } catch (NullPointerException ex) {
            System.err.println("Null pointer exception encountered");
        }

        return MediaPlayer.create(this.context.getApplicationContext(), rawSoundID);
    }

    private void playSound() {
        mySoundClip.start();
    }

    public void updateSounds(Integer newSoundsToUse) {
        switch (newSoundsToUse) {
            case R.id.useBeatbox:
            case R.id.useDrums:
            case R.id.useKeyboard:
                getSound();
                break;
            default:
                break;
        }
    }
}



