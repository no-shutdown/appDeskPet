package com.xl.pet.ui.forest.time;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xl.pet.R;
import com.xl.pet.database.dao.ForestDao;
import com.xl.pet.database.dao.ForestFlagDao;
import com.xl.pet.database.entity.ForestDO;
import com.xl.pet.database.entity.ForestFlagDO;
import com.xl.pet.ui.forest.constants.FlagColors;
import com.xl.pet.ui.forest.constants.TreeImages;
import com.xl.pet.utils.DatabaseHelper;

import java.util.Timer;
import java.util.TimerTask;

public class ForestTimeFragment extends Fragment {

    private static final long MIN = 60 * 1000;

    private Timer mTimer;
    private Chronometer chronometer;
    private Button startPauseButton;
    private ForestDao forestDao;
    private ForestFlagDao forestFlagDao;

    private boolean isRunning = false;
    private Integer selectResId = R.drawable.forest_tree_1;

    private String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
    private String selectedItem;

    private ForestTimeViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forest_time, container, false);
        forestDao = DatabaseHelper.forestDao();
        forestFlagDao = DatabaseHelper.forestFlagDao();

        chronometer = root.findViewById(R.id.chronometer);
        startPauseButton = root.findViewById(R.id.startPauseButton);
        TextView textView = root.findViewById(R.id.textView_forest_flag);
        ImageView image = root.findViewById(R.id.imageView_forest_time);
        //树种弹窗
        BottomSheetDialog bottomSheetDialog = createForestDialog();
        ImageAdapter treeAdapter = new ImageAdapter(this.getContext(), TreeImages.list);
        GridView gridView = bottomSheetDialog.findViewById(R.id.gridView);
        gridView.setAdapter(treeAdapter);
        //标签弹窗
        Dialog popupDialog = createPopupDialog();
        EditText editText = popupDialog.findViewById(R.id.editText);
        ListView listView = popupDialog.findViewById(R.id.listView);

        //viewModel
        viewModel = ViewModelProviders.of(this).get(ForestTimeViewModel.class);
        viewModel.getResId().observe(getViewLifecycleOwner(), image::setImageResource);
        viewModel.getFlag().observe(getViewLifecycleOwner(), (data) -> {
            textView.setText(data.flag);
            textView.setBackgroundColor(fetchColor(data.id));
        });

        //button点击事件
        startPauseButton.setOnClickListener(v -> {
            if (isRunning) {
                pauseChronometer();
            } else {
                startChronometer();
            }
        });

        //显示弹窗
        image.setOnClickListener(v -> bottomSheetDialog.show());
        //树种点击事件
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (isRunning) {
                return;
            }
            selectResId = TreeImages.list.get(position);
            viewModel.setResId(selectResId);
            bottomSheetDialog.hide();
        });

        //显示弹窗
        textView.setOnClickListener(v -> popupDialog.show());
        //标签点击事件
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 根据列表项点击打印对应的内容
            String selectedItem = (String) listView.getItemAtPosition(position);
            System.out.println(selectedItem);
        });


        //显示默认选择的树种
        viewModel.setResId(selectResId);
        //显示默认标签
        new Thread(() -> {
            ForestFlagDO firstFlag = findFirstFlag();
            getActivity().runOnUiThread(() -> viewModel.setFlag(firstFlag));
        }).start();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mTimer) {
            mTimer.cancel();
        }
    }

    private BottomSheetDialog createForestDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this.getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.forest_bottom_dialog, null);
        bottomSheetDialog.setContentView(dialogView);
        return bottomSheetDialog;
    }

    // 弹窗创建方法
    private Dialog createPopupDialog() {
        // 创建弹窗对象
        Dialog popupDialog = new Dialog(this.getContext());
        // 设置弹窗的布局
        popupDialog.setContentView(R.layout.forest_flag_dialog);
        return popupDialog;
    }

    private void startChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        startPauseButton.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.attention));
        startPauseButton.setText("结束");
        isRunning = true;

        //先设置为小树苗，然后根据时间升级树种
        viewModel.setResId(R.drawable.forest_tree_seedlings1);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                //超过30分钟显示全树
                if (elapsedMillis >= 30 * MIN) {
                    getActivity().runOnUiThread(() -> viewModel.setResId(selectResId));
                    cancel();
                } else if (elapsedMillis >= 20 * MIN) {
                    getActivity().runOnUiThread(() -> viewModel.setResId(R.drawable.forest_tree_seedlings5));
                } else if (elapsedMillis >= 10 * MIN) {
                    getActivity().runOnUiThread(() -> viewModel.setResId(R.drawable.forest_tree_seedlings4));
                } else if (elapsedMillis >= 5 * MIN) {
                    getActivity().runOnUiThread(() -> viewModel.setResId(R.drawable.forest_tree_seedlings3));
                } else if (elapsedMillis >= 3 * MIN) {
                    getActivity().runOnUiThread(() -> viewModel.setResId(R.drawable.forest_tree_seedlings2));
                }
            }
        }, 0, MIN);
    }

    private void pauseChronometer() {
        chronometer.stop();
        startPauseButton.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimary));
        startPauseButton.setText("开始");
        isRunning = false;
        mTimer.cancel();
        new Thread(() -> insertData(chronometer.getBase(), SystemClock.elapsedRealtime(), viewModel.getResId().getValue(), viewModel.getFlag().getValue().flag))
                .start();
    }

    private ForestFlagDO findFirstFlag() {
        ForestFlagDO first = forestFlagDao.findFirst();
        if (null == first) {
            forestFlagDao.insert(new ForestFlagDO("学习"));
            first = forestFlagDao.findFirst();
        }
        return first;
    }

    public int fetchColor(int id) {
        int index = (id - 1) % FlagColors.list.size();
        return FlagColors.list.get(index);
    }

    private void insertData(long startTime, long endTime, int restId, String flag) {
        //不够1分钟不算
        if (endTime - startTime < MIN) {
            return;
        }
        //时间不够是枯树
        if (restId != selectResId) {
            restId = R.drawable.forest_tree_decayed;
        }
        ForestDO data = new ForestDO();
        data.startTime = startTime;
        data.endTime = endTime;
        data.resId = restId;
        data.flag = flag;
        forestDao.insert(data);
    }
}