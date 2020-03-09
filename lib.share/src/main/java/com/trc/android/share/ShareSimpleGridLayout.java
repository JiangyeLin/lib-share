package com.trc.android.share;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author Unknown  on 2016/1/27.
 */
public class ShareSimpleGridLayout extends ViewGroup {
    private int column;
    private int verticalDividerWidth;
    private int horizontalDividerWidth;
    private int rowHeight;
    private int childHeight;
    private int childWidth;
    private float childHeightWidthRatio;
    private int dividerColor = Color.TRANSPARENT;
    private Paint dividerPaint;


    public ShareSimpleGridLayout(Context context) {
        this(context, null);
    }

    public ShareSimpleGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareSimpleGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShareSimpleGridLayout, defStyleAttr, 0);
        column = typedArray.getInteger(R.styleable.ShareSimpleGridLayout_grid_column, 3);
        horizontalDividerWidth = verticalDividerWidth = (int) typedArray.getDimension(R.styleable.ShareSimpleGridLayout_grid_divider_width, 0);
        horizontalDividerWidth = (int) typedArray.getDimension(R.styleable.ShareSimpleGridLayout_grid_horizontal_divider_width, 0);
        verticalDividerWidth = (int) typedArray.getDimension(R.styleable.ShareSimpleGridLayout_grid_vertical_divider_width, 0);
        childHeightWidthRatio = typedArray.getFloat(R.styleable.ShareSimpleGridLayout_grid_item_height_width_ratio, 0);
        dividerColor = typedArray.getColor(R.styleable.ShareSimpleGridLayout_grid_divider_color, Color.TRANSPARENT);
        rowHeight = (int) typedArray.getDimension(R.styleable.ShareSimpleGridLayout_grid_row_height, 1);
        typedArray.recycle();
        if (horizontalDividerWidth > 0 || verticalDividerWidth > 0 && Color.alpha(dividerColor) != 0) {
            dividerPaint = new Paint();
            dividerPaint.setColor(dividerColor);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShareSimpleGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        childWidth = getChildSize(width);
        if (childHeightWidthRatio == 0) {
            childHeight = rowHeight;
        } else {
            childHeight = (int) (childWidth * childHeightWidthRatio);
        }
        width = getPaddingLeft() + getPaddingRight() + column * (childWidth + verticalDividerWidth) - verticalDividerWidth;
        int height = getPaddingTop() + getPaddingBottom() + ((getNotGoneChildCount() - 1) / column + 1) * (childHeight + horizontalDividerWidth) - horizontalDividerWidth;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    public void setColumn(int column) {
        this.column = column;
    }

    private int getNotGoneChildCount() {
        int num = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) num++;
        }
        return num;
    }

    private int getChildSize(int width) {
        int childSpan = ((width - (column - 1) * verticalDividerWidth) - getPaddingLeft() - getPaddingRight()) / column;
        return childSpan;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != dividerPaint) {
            for (int i = 1; i < column; i++) {
                int left = getPaddingLeft() + i * childWidth + (i - 1) * verticalDividerWidth;
                canvas.drawRect(left, getPaddingTop(), left + verticalDividerWidth, getHeight() - getPaddingBottom(), dividerPaint);
            }
            int line = ((getNotGoneChildCount() - 1) / column + 1);
            for (int i = 1; i < line; i++) {
                int top = getPaddingTop() + i * childHeight + (i - 1) * horizontalDividerWidth;
                canvas.drawRect(getPaddingLeft(), top, getWidth() - getPaddingRight(), top + horizontalDividerWidth, dividerPaint);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int index = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            int left = (getPaddingLeft() + index % column * childWidth + index % column * verticalDividerWidth);
            int top = (getPaddingTop() + index / column * childHeight + index / column * horizontalDividerWidth);
            child.layout(left, top, (left + childWidth), (top + childHeight));
            index++;
        }
    }
}
