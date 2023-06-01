package com.xl.pet.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

        FieldViewGroup fieldGroup = root.findViewById(R.id.layout_fields);

        LinearLayout topLayout = root.findViewById(R.id.layout_dragItems);
        BuildingView buildingView = new BuildingView(root.getContext(), fieldGroup.getScale());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.width = buildingView.getBmpW();
        layoutParams.height = buildingView.getBmpH();
        buildingView.setLayoutParams(layoutParams);
        topLayout.addView(buildingView);

        fieldGroup.setOnTouchListener(new FieldViewGroupTouchListener(fieldGroup, buildingView));


        return root;
    }


}