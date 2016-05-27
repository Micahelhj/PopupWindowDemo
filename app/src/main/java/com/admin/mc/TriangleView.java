package com.admin.mc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 项目名称：MistakesCollection
 * 类描述：widget_mc_popupwindow_lay 上的三角形
 * 创建人：Michael-hj
 * 创建时间：2016/5/27  10:55
 * 修改人：Michael-hj
 * 修改时间：2016/5/27  10:55
 * 修改备注：
 */
public class TriangleView extends View {
    public TriangleView(Context context) {
        super(context);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }

    public static final int DEFAULT_WIDTH_SIZE = 30;
    public static final int DEFAULT_HEIGHT_SIZE = 15;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(DEFAULT_WIDTH_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_HEIGHT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 创建画笔
        Paint paint = new Paint();
          /*去锯齿*/
        paint.setAntiAlias(true);
          /*设置paint的颜色*/
        paint.setColor(Color.WHITE);
          /*设置paint的 style 为STROKE：空心 FILL：实心*/
        paint.setStyle(Paint.Style.FILL);
          /*设置paint的外框宽度*/
        paint.setStrokeWidth(3);
        // 绘制这个三角形,你可以绘制任意多边形
        Path path = new Path();
        path.moveTo(DEFAULT_WIDTH_SIZE / 2, 0);// 此点为多边形的起点
        path.lineTo(0, DEFAULT_HEIGHT_SIZE);
        path.lineTo(DEFAULT_WIDTH_SIZE, DEFAULT_HEIGHT_SIZE);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);
    }
}
