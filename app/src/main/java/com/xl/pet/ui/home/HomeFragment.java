package com.xl.pet.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
        int widthPixels = root.getResources().getDisplayMetrics().widthPixels;
        int n = 8; //n*n
        float scale = computeScale(widthPixels, n, 297); //计算缩放比例 297是图片长宽的斜边dp，即x轴长度
        int offset_top = (int) (dipToPx(60 * scale)); //偏移量 60固定偏移量才能刚好重合
        int offset_left = (int) (dipToPx(103 * scale)); //偏移量 103固定偏移量才能刚好重合

        RelativeLayout layout = root.findViewById(R.id.layout_fields);
        int startMarginLeft = (n - 1) * offset_left;
        for (int i = 0; i < n; i++) {
            int baseTop = offset_top * i;
            int baseLeft = startMarginLeft - offset_left * i;
            for (int j = 0; j < n; j++) {
                FieldView fieldView = new FieldView(root.getContext(), scale);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.topMargin = baseTop + offset_top * j;
                layoutParams.leftMargin = baseLeft + offset_left * j;
                layoutParams.width = fieldView.getBmpW();
                layoutParams.height = fieldView.getBmpH();
                fieldView.setLayoutParams(layoutParams);
                layout.addView(fieldView);
            }
        }
    }


    private float computeScale(int parentWidth, int n, int viewDp) {
        //100px伸缩空间
        return 1.0f * (parentWidth - 100) / n / viewDp / 2;
    }

    private int dipToPx(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}