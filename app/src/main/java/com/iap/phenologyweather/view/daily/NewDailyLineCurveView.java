package com.iap.phenologyweather.view.daily;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.DayForecast;

/**
 * Created by chenxueqing on 2016/8/17.
 */

public class NewDailyLineCurveView extends View {

    private Context mContext;
    private boolean isSetAllAttr;


    private Paint highTempPaint, lowTempPaint, lineHighPaint, lineLowPaint,
            highDotOutPaint, lowDotOutPaint, highDotInPaint, lowDotInPaint,
            highShadowPaint, lowShadowPaint;
    private Path highPath, lowPath, highShaderPath, lowShaderPath;
//    private Typeface oswaldRegular;

    private int itemWidth, itemHeight;
    private int lineStrokeWidth;
    private int dotOutRadius, dotInRadius;
    private int centerX;
    private int highTempY, lowTempY, highDotY, lowDotY;
    private String highTempStr, lowTempStr;

    public NewDailyLineCurveView(Context context) {
        super(context);
        init(context);
    }

    public NewDailyLineCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewDailyLineCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Path getHighPath() {
        return highPath;
    }

    public Path getLowPath() {
        return lowPath;
    }

    private void init(Context context) {
        this.mContext = context;
//        initTypeface();
        initPaint();
        initPath();
    }

    private void initPath() {
        highPath = new Path();
        lowPath = new Path();
        highShaderPath = new Path();
        lowShaderPath = new Path();
    }

//    private void initTypeface() {
//        TypefaceLoader loader = TypefaceLoader.getInstance(mContext);
//        oswaldRegular = loader.getTypefaceByName(TypefaceLoader.TYPEFACE_OSWALD_REGULAR);
//    }

    private void initPaint() {
        highTempPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        highTempPaint.setTypeface(oswaldRegular);

        lowTempPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        lowTempPaint.setTypeface(oswaldRegular);

        highDotOutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        lowDotOutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        highDotInPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        lowDotInPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        lineHighPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineHighPaint.setColor(getResources().getColor(R.color.hourly_card_high_red));
        lineHighPaint.setStyle(Paint.Style.STROKE);

        lineLowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineLowPaint.setColor(getResources().getColor(R.color.hourly_card_low_blue));
        lineLowPaint.setStyle(Paint.Style.STROKE);

        highShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lowShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSetAllAttr) {
            //shader
            highShaderPath.reset();
            lowShaderPath.reset();
            highShaderPath.addPath(highPath);
            lowShaderPath.addPath(lowPath);

            highShaderPath.lineTo(itemWidth, itemHeight + lineStrokeWidth);
            highShaderPath.lineTo(0, itemHeight + lineStrokeWidth);
            highShaderPath.close();
            canvas.drawPath(highShaderPath, highShadowPaint);

            lowShaderPath.lineTo(itemWidth, itemHeight + lineStrokeWidth);
            lowShaderPath.lineTo(0, itemHeight + lineStrokeWidth);
            lowShaderPath.close();
            canvas.drawPath(lowShaderPath, lowShadowPaint);

            //curve
            canvas.drawPath(highPath, lineHighPaint);
            canvas.drawPath(lowPath, lineLowPaint);
            //dot
            canvas.drawCircle(centerX, highDotY, dotOutRadius, highDotOutPaint);
            canvas.drawCircle(centerX, lowDotY, dotOutRadius, lowDotOutPaint);
            canvas.drawCircle(centerX, highDotY, dotInRadius, highDotInPaint);
            canvas.drawCircle(centerX, lowDotY, dotInRadius, lowDotInPaint);
            //temp
            canvas.drawText(highTempStr, (itemWidth - highTempPaint.measureText(highTempStr)) / 2, highTempY, highTempPaint);
            canvas.drawText(lowTempStr, (itemWidth - lowTempPaint.measureText(lowTempStr)) / 2, lowTempY, lowTempPaint);


        }
    }

    public void refreshView() {
        isSetAllAttr = true;
        invalidate();
    }

    public void setSizeAndPosition(NewDailyView dailyView, DayForecast day) {
        itemWidth = dailyView.getCurveViewItemWidth();
        itemHeight = dailyView.getCurveViewHeight();
        lineStrokeWidth = dailyView.getLineStrokeWidth();
        dotOutRadius = dailyView.getDotOutRadius();
        dotInRadius = dailyView.getDotInRadius();
        centerX = dailyView.getCurveViewItemWidth() / 2;
        highDotY = day.getHighY();
        highTempY = highDotY - dailyView.getHighTempBottomPadding();
        lowDotY = day.getLowY();
        lowTempY = lowDotY + dailyView.getLowTempTopPadding() + dailyView.getLowTempHeight();
        highTempStr = day.getHighTempStr();
        lowTempStr = day.getLowTempStr();

        highTempPaint.setTextSize((4 / 3f) * dailyView.getHighTempHeight());
        lowTempPaint.setTextSize((4 / 3f) * dailyView.getLowTempHeight());
        lineHighPaint.setStrokeWidth(dailyView.getLineStrokeWidth());
        lineLowPaint.setStrokeWidth(dailyView.getLineStrokeWidth());

        if (day.isToday()) {
            highDotOutPaint.setColor(getResources().getColor(R.color.white_100));
            highDotInPaint.setColor(getResources().getColor(R.color.hourly_card_high_red));
            lowDotOutPaint.setColor(getResources().getColor(R.color.white_100));
            lowDotInPaint.setColor(getResources().getColor(R.color.hourly_card_low_blue));
        } else {
            highDotOutPaint.setColor(getResources().getColor(R.color.hourly_card_high_red));
            highDotInPaint.setColor(getResources().getColor(R.color.white_100));
            lowDotOutPaint.setColor(getResources().getColor(R.color.hourly_card_low_blue));
            lowDotInPaint.setColor(getResources().getColor(R.color.white_100));
        }
        if (day.isYesterday()) {
            highTempPaint.setColor(getResources().getColor(R.color.black_40));
            lowTempPaint.setColor(getResources().getColor(R.color.black_25));
        } else {
            highTempPaint.setColor(getResources().getColor(R.color.black_80));
            lowTempPaint.setColor(getResources().getColor(R.color.black_50));
        }


        Shader HighShader = new LinearGradient(0, 0, 0, itemHeight,
                new int[]{getResources().getColor(R.color.hourly_card_high_red_15),
                        getResources().getColor(R.color.white_10)}, null, Shader.TileMode.REPEAT);
        highShadowPaint.setShader(HighShader);
        Shader lowShader = new LinearGradient(0, 0, 0, itemHeight,
                new int[]{getResources().getColor(R.color.hourly_card_low_blue_20),
                        getResources().getColor(R.color.white_40)}, null, Shader.TileMode.REPEAT);
        lowShadowPaint.setShader(lowShader);
    }
}
