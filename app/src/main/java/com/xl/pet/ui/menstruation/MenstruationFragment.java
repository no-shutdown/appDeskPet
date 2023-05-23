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
import com.xl.pet.ui.menstruation.constants.TagEnum;
import com.xl.pet.utils.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

public class MenstruationFragment extends Fragment implements com.haibin.calendarview.CalendarView.OnCalendarSelectListener,
        com.haibin.calendarview.CalendarView.OnMonthChangeListener {

    private Activity activity;
    private TextView tvMonth;
    private CalendarView mCalendarView;
    private MenstruationDao menstruationDao;


    private final Map<String, Calendar> map = new HashMap<>();


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
        setDatas(y, m);
    }

    private void setDatas(int y, int m) {
        map.put(getSchemeCalendar(y, m, 1, "SECURITY").toString(),
                getSchemeCalendar(y, m, 1, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 2, "SECURITY").toString(),
                getSchemeCalendar(y, m, 2, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 3, "SECURITY").toString(),
                getSchemeCalendar(y, m, 3, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 4, "SECURITY").toString(),
                getSchemeCalendar(y, m, 4, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 5, "SECURITY").toString(),
                getSchemeCalendar(y, m, 5, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 6, "FERTILE").toString(),
                getSchemeCalendar(y, m, 6, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 7, "FERTILE").toString(),
                getSchemeCalendar(y, m, 7, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 8, "FERTILE").toString(),
                getSchemeCalendar(y, m, 8, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 9, "FERTILE").toString(),
                getSchemeCalendar(y, m, 9, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 10, "OVULATION").toString(),
                getSchemeCalendar(y, m, 10, "OVULATION"));
        map.put(getSchemeCalendar(y, m, 11, "FERTILE").toString(),
                getSchemeCalendar(y, m, 11, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 12, "FERTILE").toString(),
                getSchemeCalendar(y, m, 12, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 13, "FERTILE").toString(),
                getSchemeCalendar(y, m, 13, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 14, "FERTILE").toString(),
                getSchemeCalendar(y, m, 14, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 15, "FERTILE").toString(),
                getSchemeCalendar(y, m, 15, "FERTILE"));
        map.put(getSchemeCalendar(y, m, 16, "SECURITY").toString(),
                getSchemeCalendar(y, m, 16, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 17, "SECURITY").toString(),
                getSchemeCalendar(y, m, 17, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 18, "SECURITY").toString(),
                getSchemeCalendar(y, m, 18, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 19, "SECURITY").toString(),
                getSchemeCalendar(y, m, 19, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 20, "SECURITY").toString(),
                getSchemeCalendar(y, m, 20, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 21, "SECURITY").toString(),
                getSchemeCalendar(y, m, 21, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 22, "SECURITY").toString(),
                getSchemeCalendar(y, m, 22, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 23, "SECURITY").toString(),
                getSchemeCalendar(y, m, 23, "SECURITY"));
        map.put(getSchemeCalendar(y, m, 24, "PERIOD").toString(),
                getSchemeCalendar(y, m, 24, "PERIOD"));
        map.put(getSchemeCalendar(y, m, 25, "PERIOD").toString(),
                getSchemeCalendar(y, m, 25, "PERIOD"));
        map.put(getSchemeCalendar(y, m, 26, "PERIOD").toString(),
                getSchemeCalendar(y, m, 26, "PERIOD"));
        map.put(getSchemeCalendar(y, m, 27, "PERIOD").toString(),
                getSchemeCalendar(y, m, 27, "PERIOD"));
        map.put(getSchemeCalendar(y, m, 28, "PERIOD").toString(),
                getSchemeCalendar(y, m, 28, "PERIOD"));
        switch (m) {
            case 2://二月
                if (y % 4 == 0 && y % 100 != 0 || y % 400 == 0) {//闰年
                    map.put(getSchemeCalendar(y, m, 29, "SECURITY").toString(),
                            getSchemeCalendar(y, m, 29, "SECURITY"));
                }
                break;
            case 1://有31天
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                map.put(getSchemeCalendar(y, m, 29, "SECURITY").toString(),
                        getSchemeCalendar(y, m, 29, "SECURITY"));
                map.put(getSchemeCalendar(y, m, 30, "SECURITY").toString(),
                        getSchemeCalendar(y, m, 30, "SECURITY"));
                map.put(getSchemeCalendar(y, m, 31, "SECURITY").toString(),
                        getSchemeCalendar(y, m, 31, "SECURITY"));
                break;
            default://只有30天
                map.put(getSchemeCalendar(y, m, 29, "SECURITY").toString(),
                        getSchemeCalendar(y, m, 29, "SECURITY"));
                map.put(getSchemeCalendar(y, m, 30, "SECURITY").toString(),
                        getSchemeCalendar(y, m, 30, "SECURITY"));
                break;
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
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

//        //数据库更新
//        int year = calendar.getYear();
//        int month = calendar.getMonth();
//        int day = calendar.getDay();
//        if (tag) {
//            MenstruationDO menstruationDO = new MenstruationDO();
//            menstruationDO.year = year;
//            menstruationDO.month = month;
//            menstruationDO.day = day;
//            menstruationDao.insert(menstruationDO);
//        } else {
//            menstruationDao.deleteTag(year, month, day);
//        }
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