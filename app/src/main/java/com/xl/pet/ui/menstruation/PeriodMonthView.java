package com.xl.pet.ui.menstruation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.xl.pet.ui.menstruation.constants.TagEnum;

/**
 * 下标标记的日历控件
 */
public class PeriodMonthView extends MonthView {
    private final int mPadding;
    private final int mH;
    private final int mW;
    private final Paint mPointPaint = new Paint();//标记画笔
    private final Paint mClearPaint = new Paint();//清空画笔

    private static final String TODAY = "今天";

    public PeriodMonthView(Context context) {
        super(context);
        mPadding = dipToPx(getContext(), 4);
        mH = dipToPx(getContext(), 2);
        mW = dipToPx(getContext(), 8);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);
        mSelectedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mClearPaint.setColor(Color.TRANSPARENT);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        //如果不是经期就绘制选中效果
        if (!TagEnum.PERIOD.name().equals(calendar.getScheme())) {
            canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
        }
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        //绘制标记
        TagEnum tagEnum;
        if (null == calendar.getScheme() || calendar.getScheme().isEmpty()) {
            tagEnum = TagEnum.NULL;
        } else {
            tagEnum = TagEnum.valueOf(calendar.getScheme());
        }
        switch (tagEnum) {
            case PERIOD: //主题标记月经期
                canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSchemePaint);
                break;
            case OVULATION_DAY: //特别标记排卵日（底部横线表示）
                mPointPaint.setColor(0xFFE4CFFB);
                canvas.drawRect(x + mItemWidth / 2 - mW / 2,
                        y + mItemHeight - mH * 2 - mPadding,
                        x + mItemWidth / 2 + mW / 2,
                        y + mItemHeight - mH - mPadding, mPointPaint);
                break;
            default: //默认无标记
                canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mClearPaint);
        }
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme,
                              boolean isSelected) {
        //绘制文本
        TagEnum tagEnum;
        if (null == calendar.getScheme() || calendar.getScheme().isEmpty()) {
            tagEnum = TagEnum.NULL;
        } else {
            tagEnum = TagEnum.valueOf(calendar.getScheme());
        }
        switch (tagEnum) {
            case PERIOD: //月经期，所有文本颜色都是白色
                mSchemeTextPaint.setColor(0xFFFFFFFF);//标记的文本画笔
                mSchemeLunarTextPaint.setColor(0xFFFFFFFF);//标记农历文本画笔
                mCurMonthTextPaint.setColor(0xFFFFFFFF);//当前月份日期文本画笔
                mCurMonthLunarTextPaint.setColor(0xFFFFFFFF);//当前月份农历文本画笔
                mOtherMonthTextPaint.setColor(0xFFFFFFFF);//其它月份日期文本画笔
                mOtherMonthLunarTextPaint.setColor(0xFFFFFFFF);//其它月份农历文本画笔
                break;
            case SECURITY://安全期，所有的文本颜色都是绿色
                mCurMonthTextPaint.setColor(0xff8CD97C);
                mCurMonthLunarTextPaint.setColor(0xff8CD97C);
                mSchemeTextPaint.setColor(0xff8CD97C);
                mSchemeLunarTextPaint.setColor(0xff8CD97C);
                mOtherMonthTextPaint.setColor(0xff8CD97C);
                mOtherMonthLunarTextPaint.setColor(0xff8CD97C);
                break;
            case OVULATION_DAY://排卵日
            case OVULATION://排卵期所有的文本颜色都是粉色
                mCurMonthTextPaint.setColor(0xffE4BBF8);
                mCurMonthLunarTextPaint.setColor(0xffE4BBF8);
                mSchemeTextPaint.setColor(0xffE4BBF8);
                mSchemeLunarTextPaint.setColor(0xffE4BBF8);
                mOtherMonthTextPaint.setColor(0xffE4BBF8);
                mOtherMonthLunarTextPaint.setColor(0xffE4BBF8);
                break;
            default://如果没有设置标识，那就是文本是黑色，农历是灰色
                mCurMonthTextPaint.setColor(0xff000000);
                mCurMonthLunarTextPaint.setColor(0xffACACAC);
                mSchemeTextPaint.setColor(0xff000000);
                mSchemeLunarTextPaint.setColor(0xffACACAC);
                mOtherMonthTextPaint.setColor(0xff000000);
                mOtherMonthLunarTextPaint.setColor(0xffACACAC);
                break;
        }

        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;
        if (isSelected) {
            //如果选中
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSchemeTextPaint);//日期
            canvas.drawText(calendar.isCurrentDay() ? TODAY : calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSchemeLunarTextPaint);//农历
        } else if (hasScheme) {
            //如果有标记
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);//日期
            canvas.drawText(calendar.isCurrentDay() ? TODAY : calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSchemeLunarTextPaint);//农历
        } else {
            //默认
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);//日期
            canvas.drawText(calendar.isCurrentDay() ? TODAY : calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);//农历
        }
    }


    /**
     * dp转px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
