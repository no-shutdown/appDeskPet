package com.xl.pet.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.xl.pet.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        createFields(root);
        return root;
    }

    private void createFields(View root) {
        RelativeLayout layout = root.findViewById(R.id.layout_fields);
        for (int i = 0; i < 7; i++) {
            int baseTop = dipToPx(18) * i; //数值越大，越靠下
            int baseLeft = dipToPx(186) - dipToPx(31) * i; //数值越大，越靠左
            for (int j = 0; j < 7; j++) {
                FieldView fieldView = new FieldView(root.getContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.topMargin = baseTop + dipToPx(18) * j;
                layoutParams.leftMargin = baseLeft + dipToPx(31) * j;
                layoutParams.width = dipToPx(63);
                layoutParams.height = dipToPx(75);
                fieldView.setLayoutParams(layoutParams);
                layout.addView(fieldView);
            }
        }
    }


    /**
     * dp转px
     */
    private int dipToPx(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}