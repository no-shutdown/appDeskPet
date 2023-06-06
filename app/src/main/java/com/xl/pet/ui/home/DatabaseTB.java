package com.xl.pet.ui.home;

import static com.xl.pet.ui.home.BuildingViewMode.MULTI_PARAM_MAP;

import com.xl.pet.R;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTB {


    public static List<BuildingViewMode.Mode> findModesFromDatabase() {
        List<BuildingViewMode.Mode> MODES = new ArrayList<>();
        MODES.add(new BuildingViewMode.Mode(0, 0, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(0, 1, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(0, 2, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(0, 3, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(0, 4, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(0, 5, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(1, 0, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(1, 1, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(1, 2, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(1, 3, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(1, 4, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(1, 5, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(2, 0, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(2, 1, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(2, 2, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(2, 3, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(2, 4, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(2, 5, R.drawable.b_forest2));
        MODES.add(new BuildingViewMode.Mode(3, 0, R.drawable.b_forest2));
        MODES.add(new BuildingViewMode.Mode(3, 1, R.drawable.b_forest2));
        MODES.add(new BuildingViewMode.Mode(3, 2, R.drawable.b_forest2));
        MODES.add(new BuildingViewMode.Mode(3, 3, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(3, 4, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(3, 5, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(4, 0, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(4, 1, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(4, 2, R.drawable.b_forest1));
        MODES.add(new BuildingViewMode.Mode(4, 3, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(4, 4, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(4, 5, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(5, 0, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(5, 1, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(5, 2, R.drawable.b_forest));
        MODES.add(new BuildingViewMode.Mode(5, 3, R.drawable.b_forest2));
        MODES.add(new BuildingViewMode.Mode(5, 4, R.drawable.b_forest2));
        MODES.add(new BuildingViewMode.Mode(5, 5, R.drawable.b_forest2));

        return MODES;
    }

}
