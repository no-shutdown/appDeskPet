package com.xl.pet.ui.forest.time;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.xl.pet.R;

import java.util.Timer;
import java.util.TimerTask;

public class ForestTimeFragment extends Fragment {

    private final Timer timer = new Timer();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forest_time, container, false);
        TextView forestTime = root.findViewById(R.id.text_forest_time);
        TextView button = root.findViewById(R.id.button_forest_time);


        ForestTimeViewModel forestTimeViewModel = ViewModelProviders.of(this).get(ForestTimeViewModel.class);
        forestTimeViewModel.getSeconds().observe(getViewLifecycleOwner(), (data) -> {
            int min = data / 60;
            int second = data - min * 60;
            forestTime.setText(String.format("%02d:%02d", min, second));
        });

        button.setOnClickListener(v -> {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(() -> {
                        forestTimeViewModel.getSeconds().setValue(forestTimeViewModel.getSeconds().getValue() + 1);
                    });
                }
            }, 0, 1000);
        });

        return root;
    }
}