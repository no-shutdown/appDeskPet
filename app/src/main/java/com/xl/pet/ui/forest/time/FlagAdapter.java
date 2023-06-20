package com.xl.pet.ui.forest.time;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xl.pet.database.entity.ForestFlagDO;
import com.xl.pet.ui.forest.constants.FlagColors;

import java.util.List;

public class FlagAdapter extends BaseAdapter {

    private final List<ForestFlagDO> itemList;
    private final Context context;

    public FlagAdapter(Context context, List<ForestFlagDO> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        ForestFlagDO flagDO = itemList.get(position);
        textView.setText(flagDO.flag);
        textView.setTextSize(16);
        textView.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // 宽度
                LinearLayout.LayoutParams.WRAP_CONTENT  // 高度
        );
        // 设置布局参数的一些属性
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(fetchColor(flagDO.id));
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    public int fetchColor(int id) {
        int index = (id - 1) % FlagColors.list.size();
        return FlagColors.list.get(index);
    }
}
