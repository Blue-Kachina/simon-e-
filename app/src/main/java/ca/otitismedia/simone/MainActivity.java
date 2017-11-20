package ca.otitismedia.simone;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    public static ArrayList<Integer> computerCall = null; //This will be a compilation of all of the button presses that the computer has made
    public static ArrayList<Integer> userResponse = null; //This will be a compilation of all of the button presses that the user has made
    public static Boolean callInProgress = null; //This will indicate if the computer is currently showing the user all of its selections
    public static Integer bestScoreThisSession = 0;
    public static Integer computerDemoIterator = 0;

    public static Integer soundsToUse;

    private static String[] colourNames = {"", "Green", "Red", "Yellow", "Blue"}; //Just in case we ever need to know that btn1 = "Green"
    MediaPlayer soundclip1 = null; //This is storage for the sound that btn1 will play
    MediaPlayer soundclip2 = null; //This is storage for the sound that btn2 will play
    MediaPlayer soundclip3 = null; //This is storage for the sound that btn3 will play
    MediaPlayer soundclip4 = null; //This is storage for the sound that btn4 will play
    TextView txtScore = null;
    //ProgressBar progBar = null;
    private ToggleButton btn1; //A ToggleButton to represent one of the coloured buttons
    private ToggleButton btn2; //A ToggleButton to represent one of the coloured buttons
    private ToggleButton btn3; //A ToggleButton to represent one of the coloured buttons
    private ToggleButton btn4; //A ToggleButton to represent one of the coloured buttons

    private Button btnGo = null;

    //This is used to reset the computer's colour choices
    private static void initComputerCall() {
        if (computerCall == null) {
            computerCall = new ArrayList<>();
        }
        computerCall.clear();
    }

    //This is used to reset the user's button presses
    private static void initUserResponse() {
        if (userResponse == null) {
            userResponse = new ArrayList<>();
        }
        userResponse.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        if (soundsToUse != null) {
            menu.findItem(soundsToUse).setChecked(true);
        }
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item selection
        soundsToUse = item.getItemId();
        item.setChecked(true);
        setSoundsToUse();
        MainActivity.super.recreate();
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        super.onStart();

        initializeAllViews();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            buttonPressed(1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            buttonPressed(2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            buttonPressed(3);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            buttonPressed(4);
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGo.setVisibility(View.GONE);
                txtScore.setVisibility(View.VISIBLE);

                initComputerCall();
                initUserResponse();

                nextTurnForComputer();
                illuminateAllButtonsInSequence();
            }
        });

        soundclip1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                buttonPressComplete(1);
            }
        });
        soundclip2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                buttonPressComplete(2);
            }
        });
        soundclip3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                buttonPressComplete(3);
            }
        });
        soundclip4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                buttonPressComplete(4);
            }
        });

    }

    //This function will select an integer between 1 and 4
    protected int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(4 - 1 + 1) + 1; //Should return a number between 1 and 4 (inclusive)
    }

    //This function will push a new random number to the end of ArrayList computerCall
    protected void nextTurnForComputer() {
        if (MainActivity.computerCall != null) {
            Integer turnNumber = MainActivity.computerCall.size() + 1;
        }
        Integer newNumber = getRandomNumber();
        MainActivity.computerCall.add(newNumber);

        Integer roundNumber = MainActivity.computerCall.size();
        txtScore.setText(roundNumber.toString());
        System.err.println("Computer Picked: " + colourNames[newNumber]);
    }

    //This will push a number to the end of ArrayList userResponse
    protected void logUserButtonPress(Integer buttonNumber) {


        userResponse.add(buttonNumber);
        if (MainActivity.computerCall.size() >= MainActivity.userResponse.size()) {
            Integer indexToCheck = MainActivity.userResponse.size() - 1;

            //User Pressed Correct Button
            if (MainActivity.computerCall.get(indexToCheck) == buttonNumber) {
                //System.err.println("Correct");
                Integer roundNumber = MainActivity.computerCall.size();
                //User just completed the entire sequence correctly
                if (MainActivity.computerCall.size() == MainActivity.userResponse.size()) {
                    if (MainActivity.computerCall.size() > bestScoreThisSession){
                        bestScoreThisSession = MainActivity.computerCall.size();
                    }

                    initUserResponse();

                    System.err.println("Sequence Complete!");

                    nextTurnForComputer();
                    illuminateAllButtonsInSequence();
                }
                //User got a correct answer but has not yet finished the entire sequence
                else{
                    System.err.println("Correct");
                }


            }
            //User Pressed Incorrect Button
            else {
                txtScore.setText("");
                System.err.println("Wrong Button");

                btnGo.setVisibility(View.VISIBLE);
                txtScore.setVisibility(View.GONE);

                initComputerCall();
                initUserResponse();
                //nextTurnForComputer();
                //illuminateAllButtonsInSequence();
            }

        } else {
            //User has pressed too many buttons -- I don't anticipate this ever happening
            txtScore.setText("");
            initComputerCall();
            initUserResponse();

            nextTurnForComputer();
            illuminateAllButtonsInSequence();
        }
    }

    //I would like to use this function to pause processing for a moment
    protected void waitAMoment() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //System.err.println("Currently taking a break");
            }
        }, 1000);

    }

    //This function should loop through each of the computer's selections, highlighting them so that the user knows which colours to pick in which order
    protected void illuminateAllButtonsInSequence() {

        MainActivity.computerDemoIterator = 0;
        MainActivity.callInProgress = true;
        Integer currentButtonNumber = null;

        System.err.println("Restarting");

        for (Integer i = 0; i < MainActivity.computerCall.size(); i++) {

            currentButtonNumber = MainActivity.computerCall.get(i);

            final Integer buttonNumber = currentButtonNumber;
            final Integer delayInMilliseconds = 350 * (i + 1);
            final Boolean isLastRun = MainActivity.computerCall.size() == i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    illuminateSingleButton(buttonNumber);
                    MainActivity.computerDemoIterator++;
                }
            }, delayInMilliseconds);

        }
    }

    //This should be called when a single button is to be illuminated
    protected void illuminateSingleButton(final Integer buttonNumber) {

        ToggleButton dynamicButton = getButtonFromNumber(buttonNumber);

        //Toggle It
        try {
            dynamicButton.performClick();
        } catch (NullPointerException ex) {
            System.err.println("A NullPointerException was caught: " + ex.getMessage());
        }

    }

    protected void setSoundsToUse() {
        switch (soundsToUse) {
            case R.id.useKeyboard:
                soundclip1 = MediaPlayer.create(getApplicationContext(), R.raw.low_c);
                soundclip2 = MediaPlayer.create(getApplicationContext(), R.raw.low_g);
                soundclip3 = MediaPlayer.create(getApplicationContext(), R.raw.high_a);
                soundclip4 = MediaPlayer.create(getApplicationContext(), R.raw.high_d);
                break;
            case R.id.useBeatbox:
                soundclip1 = MediaPlayer.create(getApplicationContext(), R.raw.beatbox_sound1);
                soundclip2 = MediaPlayer.create(getApplicationContext(), R.raw.beatbox_sound2);
                soundclip3 = MediaPlayer.create(getApplicationContext(), R.raw.beatbox_sound3);
                soundclip4 = MediaPlayer.create(getApplicationContext(), R.raw.beatbox_sound4);
                break;
            case R.id.useDrums:
                soundclip1 = MediaPlayer.create(getApplicationContext(), R.raw.kick);
                soundclip2 = MediaPlayer.create(getApplicationContext(), R.raw.snare);
                soundclip3 = MediaPlayer.create(getApplicationContext(), R.raw.hats);
                soundclip4 = MediaPlayer.create(getApplicationContext(), R.raw.crash);
                break;
        }
    }


    //This is used to simply initialize views
    protected void initializeAllViews(){

        initUserResponse();
        initComputerCall();

        MainActivity.callInProgress = null;
        computerDemoIterator = 0;

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

        btnGo = findViewById(R.id.btnGo);
        btnGo.setVisibility(View.VISIBLE);


        soundsToUse = (soundsToUse == null) ? R.id.useKeyboard : soundsToUse;
        setSoundsToUse();
//        MenuItem selectedMenuItem = (MenuItem)findViewById(soundsToUse);
//        selectedMenuItem.setChecked(true);

        txtScore = findViewById(R.id.txtScore);
        txtScore.setVisibility(View.GONE);

        txtScore.setText("");

        btn1.setText("");
        btn1.setTextOn("");
        btn1.setTextOff("");
        btn2.setText("");
        btn2.setTextOn("");
        btn2.setTextOff("");
        btn3.setText("");
        btn3.setTextOn("");
        btn3.setTextOff("");
        btn4.setText("");
        btn4.setTextOn("");
        btn4.setTextOff("");
    }

    //Pass this function a number between 1 and 4 in order to get the corresponding ToggleButton as a result
    private ToggleButton getButtonFromNumber(Integer buttonNumber){
        ToggleButton dynamicButton = null;


        // Pick Which Button We'll Be Working With
        switch (buttonNumber) {
            case 1:
                dynamicButton = btn1;
                break;

            case 2:
                dynamicButton = btn2;
                break;

            case 3:
                dynamicButton = btn3;
                break;

            case 4:
                dynamicButton = btn4;
                break;

        }
        return dynamicButton;
    }

    //Pass this function a number between 1 and 4 in order to get the corresponding MediaPlayer as a result
    private MediaPlayer getSoundFromNumber(Integer buttonNumber){
        MediaPlayer dynamicSound = null;


        // Pick Which Button We'll Be Working With
        switch (buttonNumber) {
            case 1:
                dynamicSound = soundclip1;
                break;

            case 2:
                dynamicSound = soundclip2;
                break;

            case 3:
                dynamicSound = soundclip3;
                break;

            case 4:
                dynamicSound = soundclip4;
                break;

        }
        return dynamicSound;
    }

    //A generic script that can be invoked from all the button click listeners
    private void buttonPressed(Integer buttonNumber){

        MediaPlayer dynamicSound = getSoundFromNumber(buttonNumber);
        dynamicSound.start();

    }

    private void buttonPressComplete(Integer buttonNumber) {

        ToggleButton dynamicButton = getButtonFromNumber(buttonNumber);
        dynamicButton.setChecked(false);

        if (MainActivity.callInProgress == null) {
            //User is pressing buttons without having started the game.  No actions required
        } else if (MainActivity.callInProgress && (MainActivity.computerDemoIterator == MainActivity.computerCall.size())) {
            MainActivity.callInProgress = false;
        } else if (MainActivity.callInProgress == false) {
            logUserButtonPress(buttonNumber);
        }
    }

}
