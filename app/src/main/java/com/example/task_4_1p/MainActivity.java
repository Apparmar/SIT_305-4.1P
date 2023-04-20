//         SIT 305 - 4.1P
//  Date            Author              Status
// 19-Apr-2023      Amit Parmar         Initial Version
//
//

// Load Packages
package com.example.task_4_1p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Define different variables
    TextView timerText,numberOfSets, setDuration, restDuration, setsCompleted, setsToGo, statusText ;
    Button startButton, pauseButton, stopButton, setButton;
    ProgressBar progress;
    long exeTimeLeft, restTimeLeft, maxProgress;
    CountDownTimer timeCounter;
    //boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign elements to variables.
        numberOfSets = findViewById(R.id.numberOfSets);
        setDuration = findViewById(R.id.setDuration);
        restDuration = findViewById(R.id.restDuration);
        setsCompleted = findViewById(R.id.completedText);
        setsToGo = findViewById(R.id.remainText);
        timerText = findViewById(R.id.timerText);
        statusText = findViewById(R.id.statusText);
        statusText.setText("Go!");

        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        setButton = findViewById(R.id.setButton);

        // By default set few buttons to disabled.
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        setsCompleted.setEnabled(false);
        setsToGo.setEnabled(false);
        timerText.setEnabled(false);

        //... Progress bar
        progress = findViewById(R.id.progressBar);

        // Assign onclick listener to butons
        setButton.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View v)
            {
                setTimer();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View v)
            {
                startTimer();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View v)
            {
                pauseTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View v)
            {
                stopTimer();
            }
        });
    }

    // Set timer method
    public void setTimer(){

        // if any of the primary inputs are not provided, return
        if (TextUtils.isEmpty(setDuration.getText().toString()))
            return;
        if (TextUtils.isEmpty(numberOfSets.getText().toString()))
            return;
        if (TextUtils.isEmpty(restDuration.getText().toString()))
            return;

        // Disable the settings part and enable the control buttons as well as timer
        setButton.setEnabled(false);
        numberOfSets.setEnabled(false);
        setDuration.setEnabled(false);
        restDuration.setEnabled(false);
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        setsCompleted.setEnabled(true);
        setsToGo.setEnabled(true);
        timerText.setEnabled(true);

        // Set the default values for exercise and rest timers
        // 1000 milliseconds = 1 second.
        setsCompleted.setText("0");
        setsToGo.setText(numberOfSets.getText().toString());
        timerText.setText("00:00");
        exeTimeLeft = Long.parseLong(setDuration.getText().toString()) * 1000 * 60;
        maxProgress = exeTimeLeft;
        restTimeLeft = Long.parseLong(restDuration.getText().toString()) * 1000 * 60;
        progress.setProgress(100);
    }

    // Star the timer
    public void startTimer(){
        timeCounter = new CountDownTimer(exeTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the time left variable
                updateTimer(exeTimeLeft);
                exeTimeLeft = millisUntilFinished;
                // Update the progress bar
                progress.setProgress((int)exeTimeLeft * 100 / (int)maxProgress);
            }

            @Override
            public void onFinish() {
                if (Integer.parseInt(setsToGo.getText().toString()) == 0) {
                    // When all number of sets finished, stop the app and set to initial state
                    stopTimer();
                    return;
                }

                // Update the timer and text view values.
                timerText.setText("00:00");
                exeTimeLeft = Long.parseLong(setDuration.getText().toString()) * 1000 * 60;
                int ctr = Integer.parseInt(setsToGo.getText().toString()) - 1;
                setsToGo.setText(String.valueOf(ctr));
                ctr = Integer.parseInt(setsCompleted.getText().toString() ) + 1;
                setsCompleted.setText(String.valueOf(ctr));

                // Update the rest timer values and start the rest timer.
                statusText.setText("Rest..");
                restTimeLeft = Long.parseLong(restDuration.getText().toString()) * 1000 * 60;
                maxProgress = restTimeLeft;
                progress.setMax(100);
                progress.setProgress(100);
                startRest();
            }
        }.start();

        // Disable start, enable pause and stop while timer is running
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    };

    public void startRest(){
        timeCounter = new CountDownTimer(restTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the time left variable
                updateTimer(restTimeLeft);
                restTimeLeft = millisUntilFinished;
                // Update progress bar
                progress.setProgress((int)restTimeLeft * 100 / (int)maxProgress);
            }

            @Override
            public void onFinish() {
                // When finishes, check if any more sets left.
                // If no sets left, bring app to initial state and return
                statusText.setText("Go!");
                timerText.setText("00:00");
                exeTimeLeft = Long.parseLong(setDuration.getText().toString()) * 1000 * 60;
                if (Integer.parseInt(setsToGo.getText().toString()) == 0) {
                    stopTimer();
                    return;
                }

                // If more sets left, start the timer
                maxProgress = exeTimeLeft;
                progress.setMax(100);
                progress.setProgress(100);
                startTimer();
            }
        }.start();
    };
    public void pauseTimer(){
        // Toggle the buttons when pause button is hit.
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(true);

        timeCounter.cancel();
    };
    public void stopTimer(){
        // Set everything to initial state, except settings
        timeCounter.cancel();
        timerText.setText("00:00");
        progress.setProgress(0);
        setButton.setEnabled(true);
        numberOfSets.setEnabled(true);
        setDuration.setEnabled(true);
        restDuration.setEnabled(true);
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        setsCompleted.setEnabled(false);
        setsToGo.setEnabled(false);
        timerText.setEnabled(false);
    };

    public void updateTimer(long currentTimeLeft)
    {
        // Calculate the time values and update the text
        int mins = (int) (currentTimeLeft / 1000) / 60;
        int sec = (int) (currentTimeLeft / 1000) % 60;
        timerText.setText(String.format(Locale.getDefault(),"%02d:%02d",mins,sec));
    }
}