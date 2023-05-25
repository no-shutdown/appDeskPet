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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class MenstruationFragment extends Fragment implements com.haibin.calendarview.CalendarView.OnCalendarSelectListener,
        com.haibin.calendarview.CalendarView.OnMonthChangeListener {

    private Activity activity;
    private TextView tvMonth;
    private CalendarView mCalendarView;
    private MenstruationDao menstruationDao;
    private Long recentlyClick;
    private final Timer timer = new Timer();
    //数据库所需更新缓存（<20230525,TRUE> 表示需要插入20230525数据）
    private final ConcurrentHashMap<Integer, Boolean> dataCache = new ConcurrentHashMap<>();


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
        new Thread(this::refreshData).start();
    }

    private void refreshData() {
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
            end = current.differ(last) > 1 ? last : current;
            //只会将经期天数至少3天的作为周期推断
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
            Calendar existsCalendar = map.get(calendar.toString());
            if (null != existsCalendar && TagEnum.PERIOD.name().equals(existsCalendar.getScheme())) {
                continue;
            }
            calendar.setScheme(TagEnum.SECURITY.name());
            map.put(calendar.toString(), calendar);
        }
        for (int i = 1; i <= 8 - days; i++) {
            Calendar calendar = Utils.calendarAdd(end, i);
            Calendar existsCalendar = map.get(calendar.toString());
            if (null != existsCalendar && TagEnum.PERIOD.name().equals(existsCalendar.getScheme())) {
                continue;
            }
            calendar.setScheme(TagEnum.SECURITY.name());
            map.put(calendar.toString(), calendar);
        }
    }

    private void putOvulation(Calendar start) {
        Calendar ovulation = Utils.calendarAdd(start, -14);
        Calendar existsCalendar = map.get(ovulation.toString());
        if (null == existsCalendar || !TagEnum.PERIOD.name().equals(existsCalendar.getScheme())) {
            ovulation.setScheme(TagEnum.OVULATION_DAY.name());
            map.put(ovulation.toString(), ovulation);
        }

        for (int i = 1; i <= 5; i++) {
            Calendar calendar = Utils.calendarAdd(ovulation, -i);
            existsCalendar = map.get(calendar.toString());
            if (null != existsCalendar && TagEnum.PERIOD.name().equals(existsCalendar.getScheme())) {
                continue;
            }
            calendar.setScheme(TagEnum.OVULATION.name());
            map.put(calendar.toString(), calendar);
        }
        for (int i = 1; i <= 4; i++) {
            Calendar calendar = Utils.calendarAdd(ovulation, i);
            existsCalendar = map.get(calendar.toString());
            if (null != existsCalendar && TagEnum.PERIOD.name().equals(existsCalendar.getScheme())) {
                continue;
            }
            calendar.setScheme(TagEnum.OVULATION.name());
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
        long currentId = System.currentTimeMillis();
        recentlyClick = currentId;
        //是否经期标记切换
        if (isClick) {
            switchPeriod(calendar, !TagEnum.PERIOD.name().equals(calendar.getScheme()));
        }
        //2s后运行，如果2s后未有任何点击则刷新数据
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentId == recentlyClick) {
                    applyDataCache();
                    refreshData();
                }
            }
        }, 2000);
    }

    private void switchPeriod(Calendar calendar, boolean tag) {
        //更新界面
        if (tag) {
            calendar.setScheme(TagEnum.PERIOD.name());
        } else {
            calendar.setScheme(null);
        }
        //记录数据库所需更新
        int id = Integer.parseInt(calendar.toString());
        Boolean cache = dataCache.get(id);
        if (null == cache) {
            dataCache.put(id, tag);
        } else if (cache != tag) {
            dataCache.remove(id);
        }
    }

    private void applyDataCache() {
        List<MenstruationDO> inserts = new ArrayList<>();
        List<MenstruationDO> deletes = new ArrayList<>();
        Set<Integer> keys = dataCache.keySet();
        for (Integer key : keys) {
            Boolean data = dataCache.get(key);
            if (Boolean.TRUE.equals(data)) {
                inserts.add(new MenstruationDO(key));
            } else {
                deletes.add(new MenstruationDO(key));
            }
        }
        menstruationDao.insertEntities(inserts);
        menstruationDao.deleteEntities(deletes);
        dataCache.clear();
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