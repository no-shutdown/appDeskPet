package com.xl.pet.ui.forest.time;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ForestTimeFragment extends Fragment {

    private static final long MIN = 1000;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Timer mTimer;
    private Chronometer chronometer;
    private Button startPauseButton;
    private ForestDao forestDao;
    private ForestFlagDao forestFlagDao;

    private boolean isRunning = false;
    private Integer selectResId = R.drawable.forest_tree_1;

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
        Button delButton = popupDialog.findViewById(R.id.button_del);
        Button okButton = popupDialog.findViewById(R.id.button_ok);
        delButton.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.attention));

        //viewModel
        viewModel = ViewModelProviders.of(this).get(ForestTimeViewModel.class);
        viewModel.getResId().observe(getViewLifecycleOwner(), image::setImageResource);
        viewModel.getFlag().observe(getViewLifecycleOwner(), (data) -> {
            textView.setText(data.flag);
            textView.setBackgroundColor(fetchColor(data.id));
        });

        //开始/结束button点击事件
        startPauseButton.setOnClickListener(v -> {
            if (isRunning) {
                pauseChronometer();
            } else {
                startChronometer();
            }
        });
        //flag ok button点击事件
        okButton.setOnClickListener(v -> {
            String inputStr = trimStr(editText.getText().toString());
            new Thread(() -> {
                ForestFlagDO byFlag = forestFlagDao.findByFlag(inputStr);
                if (null == byFlag) {
                    forestFlagDao.insert(new ForestFlagDO(inputStr));
                    refreshFlagList(listView);
                }
            }).start();
        });
        //flag del button点击事件
        delButton.setOnClickListener(v -> {
            String inputStr = trimStr(editText.getText().toString());
            new Thread(() -> {
                List<ForestFlagDO> flags = forestFlagDao.findAll();
                if (!(flags.size() == 1 && flags.get(0).flag.equals(inputStr))) {
                    forestFlagDao.deleteByFlag(inputStr);
                    refreshFlagList(listView);
                } else {
                    message("至少保留一个标签噢");
                }
            }).start();
        });

        //图片点击事件显示弹窗
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

        //标签点击事件显示弹窗
        textView.setOnClickListener(v -> {
            new Thread(() -> refreshFlagList(listView)).start();
            popupDialog.show();
        });
        //标签列表点击事件
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ForestFlagDO selectedItem = (ForestFlagDO) listView.getItemAtPosition(position);
            viewModel.setFlag(selectedItem);
            popupDialog.hide();
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
        handler.removeCallbacksAndMessages(null);
    }

    private void refreshFlagList(ListView listView) {
        List<ForestFlagDO> flags = forestFlagDao.findAll();
        FlagAdapter flagAdapter = new FlagAdapter(this.getContext(), flags);
        this.getActivity().runOnUiThread(() -> listView.setAdapter(flagAdapter));
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
        new Thread(() -> insertData(chronometer.getBase(), SystemClock.elapsedRealtime(),
                viewModel.getResId().getValue(), viewModel.getFlag().getValue().flag))
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
        int time = (int) ((endTime - startTime) / MIN);
        message(String.format("本次时长%d分钟", time));
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

    private void message(String message) {
        handler.post(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }

    private String trimStr(String str) {
        return str.replaceAll("\\s+", "").replaceAll("[\\r\\n]", "");
    }
}