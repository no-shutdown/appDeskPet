package com.xl.pet.ui.forest.time;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.xl.pet.R;

public class ForestTimeFragment extends Fragment {

    private Chronometer chronometer;
    private Button startPauseButton;

    private boolean isRunning = false;
    private long pauseOffset = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forest_time, container, false);
        chronometer = root.findViewById(R.id.chronometer);
        startPauseButton = root.findViewById(R.id.startPauseButton);

        startPauseButton.setOnClickListener(v -> {
            if (isRunning) {
                pauseChronometer();
            } else {
                startChronometer();
            }
        });
        return root;
    }



    private void startChronometer() {
        pauseOffset = 0;
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer.start();
        startPauseButton.setText("结束");
        isRunning = true;
    }

    private void pauseChronometer() {
        chronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        startPauseButton.setText("开始");
        isRunning = false;
    }
}