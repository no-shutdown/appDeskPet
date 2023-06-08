package com.xl.pet.ui.forest;

import com.xl.pet.R;
import com.xl.pet.ui.forest.mode.BuildingMode;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTB {


    public static List<BuildingMode.Mode> findModesFromDatabase() {
        List<BuildingMode.Mode> MODES = new ArrayList<>();
        MODES.add(new BuildingMode.Mode(0, 0, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(0, 1, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(0, 2, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(0, 3, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(0, 4, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(0, 5, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(1, 0, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(1, 1, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(1, 2, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(1, 3, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(1, 4, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(1, 5, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(2, 0, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(2, 1, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(2, 2, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(2, 3, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(2, 4, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(2, 5, R.drawable.b_forest2));
        MODES.add(new BuildingMode.Mode(3, 0, R.drawable.b_forest2));
        MODES.add(new BuildingMode.Mode(3, 1, R.drawable.b_forest2));
        MODES.add(new BuildingMode.Mode(3, 2, R.drawable.b_forest2));
        MODES.add(new BuildingMode.Mode(3, 3, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(3, 4, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(3, 5, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(4, 0, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(4, 1, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(4, 2, R.drawable.b_forest1));
        MODES.add(new BuildingMode.Mode(4, 3, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(4, 4, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(4, 5, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(5, 0, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(5, 1, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(5, 2, R.drawable.b_forest));
        MODES.add(new BuildingMode.Mode(5, 3, R.drawable.b_forest2));
        MODES.add(new BuildingMode.Mode(5, 4, R.drawable.b_forest2));
        MODES.add(new BuildingMode.Mode(5, 5, R.drawable.b_forest2));

        return MODES;
    }

}
