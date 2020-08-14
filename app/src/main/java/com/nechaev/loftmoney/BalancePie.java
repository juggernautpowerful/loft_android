package com.nechaev.loftmoney;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class BalancePie extends View {
    private float mExpences;
    private float mIncome;

    private Paint expencePaint = new Paint();
    private Paint incomePaint = new Paint();


    public BalancePie(Context context) {
        super(context);
        init();
    }

    public BalancePie(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BalancePie(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void update(float expences, float income) {
        mExpences = expences;
        mIncome = income;
        invalidate();

    }

    public BalancePie(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        expencePaint.setColor(ContextCompat.getColor(getContext(), R.color.editText_name_textColor));
        incomePaint.setColor(ContextCompat.getColor(getContext(), R.color.apple_green));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float total = mExpences + mIncome;
        float expencAngle = 360f * mExpences / total;
        float incomeAngle = 360f * mIncome / total;

        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expencAngle / 2, expencAngle, true, expencePaint);

        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomeAngle / 2, incomeAngle, true, incomePaint);

    }
}