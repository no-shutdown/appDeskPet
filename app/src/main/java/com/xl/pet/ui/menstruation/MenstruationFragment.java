package com.xl.pet.ui.menstruation;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.xl.pet.R;
import com.xl.pet.database.dao.MenstruationDao;
import com.xl.pet.database.entity.MenstruationDO;
import com.xl.pet.ui.menstruation.constants.TagEnum;
import com.xl.pet.utils.DatabaseHelper;
import com.xl.pet.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenstruationFragment extends Fragment implements com.haibin.calendarview.CalendarView.OnCalendarSelectListener,
        com.haibin.calendarview.CalendarView.OnMonthChangeListener {

    private Activity activity;
    private TextView tvMonth;
    private CalendarView mCalendarView;
    private MenstruationDao menstruationDao;


    private final Map<String, Calendar> map = new HashMap<>();
    private final long ONE_DAY = 24 * 60 * 60 * 1000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menstruation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        init();
        initData();
    }

    private void init() {
        tvMonth = activity.findViewById(R.id.tv_month);
        mCalendarView = activity.findViewById(R.id.calendarView);
        menstruationDao = DatabaseHelper.menstruationDao();

        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
    }

    private void initData() {
        int y = mCalendarView.getCurYear();//获取年
        int m = mCalendarView.getCurMonth();//获取月
        tvMonth.setText(y + "年" + m + "月");
        mCalendarView.setRange(m < 6 ? y - 1 : y, m < 6 ? 12 : m - 6, 1,
                m == 12 ? y + 1 : y, m == 12 ? 1 : m + 1, 31);//限制选择范围(限制只显示前6个月后1个月)
        mCalendarView.scrollToCurrent();//滚动到今天
        new Thread(this::setDatas).start();
    }

    private void setDatas() {
        map.clear();
        Calendar start = null, end = null, last = null;
        List<MenstruationDO> all = menstruationDao.findAll();
        for (int index = 0; index < all.size(); index++) {
            MenstruationDO i = all.get(index);
            Calendar current = getSchemeCalendar(i.year, i.month, i.day, TagEnum.PERIOD.name());
            map.put(current.toString(), current);
            //第一次循环
            if (last == null) {
                start = current;
                last = current;
                continue;
            }
            //第N次循环
            if (index != all.size() - 1 && current.differ(last) == 1) {
                last = current;
                continue;
            }
            end = index == all.size() - 1 ? current : last;
            //只会将经期天数大于2天的作为周期推断
            int days = end.differ(start);
            if (days > 2) {
                //安全期：经期第一天前7后8
                putSecurity(start, end, days);
                //排卵日：经期第一天往前推14天 | 排卵期：排卵日前5后4
                putOvulation(start);
            }
            start = current;
            last = current;
        }
        mCalendarView.setSchemeDate(map);
    }

    private void putSecurity(Calendar start, Calendar end, int days) {
        for (int i = 1; i <= 7; i++) {
            Calendar calendar = Utils.calendarAdd(start, -i);
            calendar.setScheme(TagEnum.SECURITY.name());
            map.put(calendar.toString(), calendar);
        }
        for (int i = 1; i <= 8 - days; i++) {
            Calendar calendar = Utils.calendarAdd(end, i);
            calendar.setScheme(TagEnum.SECURITY.name());
            map.put(calendar.toString(), calendar);
        }
    }

    private void putOvulation(Calendar start) {
        Calendar ovulation = Utils.calendarAdd(start, -14);
        ovulation.setScheme(TagEnum.OVULATION.name());
        map.put(ovulation.toString(), ovulation);

        for (int i = 1; i <= 5; i++) {
            Calendar calendar = Utils.calendarAdd(ovulation, -i);
            calendar.setScheme(TagEnum.FERTILE.name());
            map.put(calendar.toString(), calendar);
        }
        for (int i = 1; i <= 4; i++) {
            Calendar calendar = Utils.calendarAdd(ovulation, i);
            calendar.setScheme(TagEnum.FERTILE.name());
            map.put(calendar.toString(), calendar);
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, String type) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setScheme(type);
        return calendar;
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //是否经期标记切换，Room操作数据库强制不能在主线程
        if (isClick) {
            System.out.println(calendar.getScheme());
            new Thread(() -> switchPeriod(calendar, !TagEnum.PERIOD.name().equals(calendar.getScheme()))).start();
        }
    }

    private void switchPeriod(Calendar calendar, boolean tag) {
        if (tag) {
            calendar.setScheme(TagEnum.PERIOD.name());
        } else {
            calendar.setScheme(null);
        }

        //数据库更新
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        if (tag) {
            MenstruationDO menstruationDO = new MenstruationDO();
            menstruationDO.year = year;
            menstruationDO.month = month;
            menstruationDO.day = day;
            menstruationDao.insert(menstruationDO);
        } else {
            menstruationDao.deleteTag(year, month, day);
        }
    }

    @Override
    public void onMonthChange(int year, int month) {//月份改变事件
        tvMonth.setText(year + "年" + month + "月");
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {//超出日期选择范围
        Toast.makeText(activity, "超出日期选择范围", Toast.LENGTH_SHORT).show();
    }
}