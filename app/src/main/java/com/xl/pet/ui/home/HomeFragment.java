package com.xl.pet.ui.home;

import static com.xl.pet.ui.home.BuildingViewMode.MULTI_PARAM_MAP;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.xl.pet.R;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        TextView topTitle = root.findViewById(R.id.top_title);
        topTitle.setText("2023-06");

        AreaViewGroup fieldGroup = root.findViewById(R.id.layout_fields);

        GridLayout dragItemsLayout = root.findViewById(R.id.layout_dragItems);
        AbstractBuildingView buildingView = AbstractBuildingView.buildingView(root.getContext(), 0.3f, R.drawable.b_tree3);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.width = buildingView.bmpW;
        layoutParams.height = buildingView.bmpH;
        buildingView.setLayoutParams(layoutParams);
        buildingView.setOnTouchListener(new BuildingViewTouchListener(fieldGroup));
        dragItemsLayout.addView(buildingView);


        BuildingViewMode.MultiParam multiParam = MULTI_PARAM_MAP.get(R.drawable.b_room1);
        AbstractBuildingView multiBuildingView = AbstractBuildingView.buildingView(root.getContext(), 0.2f, R.drawable.b_room1, multiParam);
        RelativeLayout.LayoutParams multiLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        multiLayoutParams.width = multiBuildingView.bmpW;
        multiLayoutParams.height = multiBuildingView.bmpH;
        multiBuildingView.setLayoutParams(multiLayoutParams);
        multiBuildingView.setOnTouchListener(new BuildingViewTouchListener(fieldGroup));
        dragItemsLayout.addView(multiBuildingView);


        return root;
    }


}