package com.xl.pet.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.xl.pet.R;

public class SegmentView extends ViewGroup implements View.OnClickListener {
    private static final int SPACE = 1;

    private final float r = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

    private int bgColor = 0xff0072c6;
    private int fgColor = Color.WHITE;
    private float mTextSize = 3f * r;
    private String[] mText = {"item1", "item2", "item3"};

    private int checkedItem = 1;
    private OnItemClickListener listener;

    public SegmentView(Context context) {
        super(context);
        initFromAttributes(context, null);
        initalize();
    }

    public SegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
        initalize();
    }

    protected void initFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SegmentView);
        String content = a.getString(R.styleable.SegmentView_data);
        if (!isEmpty(content)) {
            mText = content.split(",");
        }
        checkedItem = a.getInt(R.styleable.SegmentView_index, checkedItem);
        mTextSize = a.getDimension(R.styleable.SegmentView_textSize, mTextSize);
        bgColor = a.getColor(R.styleable.SegmentView_bgColor, bgColor);
        fgColor = a.getColor(R.styleable.SegmentView_fgColor, fgColor);
        a.recycle();
    }

    public void initalize() {
        int length = mText.length;
        for (int i = 0; i < length; i++) {
            View view = new ItemView(getContext(), mText[i], getGravity(i, length), i == checkedItem);
            view.setOnClickListener(this);
            addView(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int count = getChildCount();
        int childWidthMeasureSpec = widthMeasureSpec;
        int childWidth = 0;
        int childHeight = 0;
        if (widthSize >= 0) {
            childWidth = widthSize / (count + SPACE); //预留SPACE空间不占满整个屏幕宽度
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, widthMode);
        }

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, childWidthMeasureSpec, heightMeasureSpec);
            childHeight = Math.max(childHeight, child.getMeasuredHeight());
        }

        setMeasuredDimension(childWidth * count, childHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) return;
        int count = getChildCount();
        int left = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.layout(left, 0, left + child.getMeasuredWidth(), child.getMeasuredHeight());
            left += child.getMeasuredWidth();
        }
    }

    private int getGravity(int i, int len) {
        if (i == 0) {
            if (i == len - 1)
                return ItemView.GRAVITY_SINGLE;
            else
                return ItemView.GRAVITY_LEFT;
        } else if (i == len - 1) {
            return ItemView.GRAVITY_RIGHT;
        } else
            return ItemView.GRAVITY_CENTER;
    }

    @Override
    public void onClick(View v) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (v.equals(child)) {
                checkedItem = i;
                ((ItemView) child).setChecked(true);
            } else {
                ((ItemView) child).setChecked(false);
            }
            child.postInvalidate();
        }
        if (listener != null) {
            listener.onItemClick((ItemView) v, checkedItem);
        }
    }

    /**
     * segment子集item
     */
    class ItemView extends View {
        public final static int GRAVITY_SINGLE = 1 << 0;
        public final static int GRAVITY_LEFT = 1 << 1;
        public final static int GRAVITY_CENTER = 1 << 2;
        public final static int GRAVITY_RIGHT = 1 << 3;

        private GradientDrawable drawable;
        private int gravity;
        private boolean isChecked;
        private String text;

        private Paint mTextPaint;
        private Rect mTextBound = new Rect();

        private ItemView(Context context, String text, int gravity, boolean isChecked) {
            super(context);
            this.text = text;
            this.gravity = gravity;
            this.isChecked = isChecked;
            init();
        }

        private void init() {
            mTextPaint = new Paint();

            mTextPaint.setTextSize(mTextSize);
            mTextPaint.getTextBounds(text, 0, text.length(), mTextBound);

            drawable = new GradientDrawable();
            setItemGravity(gravity);
            setChecked(isChecked);
        }

        public void setItemGravity(int gravity) {
            this.gravity = gravity;
            switch (gravity) {
                case GRAVITY_SINGLE:
                    drawable.setCornerRadii(new float[]{r, r, r, r, r, r, r, r});
                    break;
                case GRAVITY_LEFT:
                    drawable.setCornerRadii(new float[]{r, r, 0, 0, 0, 0, r, r});
                    break;
                case GRAVITY_CENTER:
                    drawable.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
                    break;
                case GRAVITY_RIGHT:
                    drawable.setCornerRadii(new float[]{0, 0, r, r, r, r, 0, 0});
                    break;
            }
        }

        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
            mTextPaint.setColor(isChecked ? fgColor : bgColor);
            drawable.setColor(isChecked ? bgColor : fgColor);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            Rect rect = canvas.getClipBounds();
            drawable.setBounds(new Rect(rect));
            drawable.draw(canvas);
            int l = (rect.width() - mTextBound.width()) / 2;
            int b = (rect.height() + mTextBound.height()) / 2;
            canvas.drawText(text, l, b, mTextPaint);
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(ItemView item, int checkedItem);
    }

    public static boolean isEmpty(String str) {
        return null == str || str.trim().length() == 0;
    }
}
