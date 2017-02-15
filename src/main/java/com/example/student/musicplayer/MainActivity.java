package com.example.student.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity  {

    private MediaPlayer mediaPlayer;
    public TextView songName, duration;
    private double timeElapsed = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the layout of the Activity
        setContentView(R.layout.activity_main);

        //initialize views
        initializeViews();

        //test
        //ImageButton imageButton = (ImageButton)findViewById(R.id.media_ff);
        //imageButton.setOnLongClickListener(new View.OnLongClickListener() {
          //  @Override
           // public boolean onLongClick(View view) {

              //  Log.v("MainActivity", "Long Click");
              //  return false;
          //  }
       // });

    }

    public void initializeViews(){
        songName = (TextView) findViewById(R.id.songName);
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_song);
        finalTime = mediaPlayer.getDuration();
        duration = (TextView) findViewById(R.id.songDuration);
        seekbar1 = (SeekBar) findViewById(R.id.seekBar);
        songName.setText("Sample_Song.mp3");

        seekbar1.setMax((int) finalTime);
        seekbar1.setClickable(false);

        // add
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

//                int iProgress = seekbar1.getProgress();
//                int iPos = (int)((float)i / 100.0 * (float)mediaPlayer.getDuration());
               // int iPos = (int)((float)i / 100.0);
                //mediaPlayer.seekTo(iPos);
                if(b) {
                    mediaPlayer.seekTo(i);
                    seekBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        ImageButton imageButton = (ImageButton)findViewById(R.id.media_ff);
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
         public boolean onLongClick(View view) {

          Log.v("MainActivity", "Long Click");
              if ((timeElapsed + forwardTime) <= mediaPlayer.getDuration()) {
                  timeElapsed = timeElapsed + forwardTime;

                  //seek to the exact second of the track
                  mediaPlayer.seekTo((int) timeElapsed);
              }
          return false;
          }
         });

    }

    // play mp3 song
    public void play(View view) {
        mediaPlayer.start();
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekbar1.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mediaPlayer.getCurrentPosition();
            //set seekbar progress
            seekbar1.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = finalTime - timeElapsed;
            duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));

            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };

    // pause mp3 song
    public void pause(View view) {
        mediaPlayer.pause();
    }

    // go forward at forwardTime seconds
    public void forward(View view) {
        //check if we can go forward at forwardTime seconds before song endes
        if ((timeElapsed + forwardTime) <= mediaPlayer.getDuration()) {
            timeElapsed = timeElapsed + forwardTime;

            //seek to the exact second of the track
            mediaPlayer.seekTo((int) timeElapsed);
        }
    }

    public void rewind(View view) {
        //check if we can go forward at forwardTime seconds before song endes
        if ((timeElapsed - backwardTime) < mediaPlayer.getDuration()) {
            timeElapsed = timeElapsed - backwardTime;

            //seek to the exact second of the track
            mediaPlayer.seekTo((int) timeElapsed);
        }
    }

}