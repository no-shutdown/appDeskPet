package com.xl.pet.ui.home;

import static com.xl.pet.ui.home.BuildingViewMode.MULTI_PARAM_MAP;

import com.xl.pet.R;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTB {


    public static List<BuildingViewMode.Mode> findModesFromDatabase() {
        List<BuildingViewMode.Mode> MODES = new ArrayList<>();
        MODES.add(new BuildingViewMode.Mode(0, 0, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(0, 1, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(0, 2, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(0, 3, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(0, 4, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(1, 0, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(1, 1, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(2, 0, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(2, 1, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(3, 0, R.drawable.b_tree3));
        MODES.add(new BuildingViewMode.Mode(3, 1, R.drawable.b_tree3));


        MODES.add(new BuildingViewMode.Mode(1, 2, R.drawable.b_room1, MULTI_PARAM_MAP.get(R.drawable.b_room1)));
        MODES.add(new BuildingViewMode.Mode(4, 0, R.drawable.b_car, MULTI_PARAM_MAP.get(R.drawable.b_car)));

        return MODES;
    }

}
