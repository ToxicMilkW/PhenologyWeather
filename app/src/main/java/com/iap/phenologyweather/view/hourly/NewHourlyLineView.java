package com.iap.phenologyweather.view.hourly;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.HourForecast;
import com.iap.phenologyweather.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxueqing on 2016/9/1.
 */

public class NewHourlyLineView extends View {
    private Context mContext;
    private List<HourForecast> hours = new ArrayList<>();

    private Paint tempPaint, tempLinePaint, dotInPaint, dotOutPaint, verticalLinePaint, bottomLinePaint, timePaint;
    private Path verticalLinePath;
//    private Typeface oSwaldRegular;

    private int itemWidth;
    private int viewHeight;
    private NewHourlyView hourlyView;

    public NewHourlyLineView(Context context) {
        super(context);
        init(context);
    }

    public NewHourlyLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewHourlyLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        initTypeface();
        initPaint();
        initPath();
    }

    private void initPath() {
        verticalLinePath = new Path();
    }

    private void initTypeface() {
//        TypefaceLoader loader = TypefaceLoader.getInstance(mContext);
//        oSwaldRegular = loader.getTypefaceByName(TypefaceLoader.TYPEFACE_OSWALD_REGULAR);//number A
    }

    private void initPaint() {
        tempPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        tempPaint.setTypeface(oSwaldRegular);

        tempLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tempLinePaint.setColor(getResources().getColor(R.color.hourly_card_temp_line_gray));
        tempLinePaint.setStyle(Paint.Style.STROKE);

        dotInPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        dotOutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        verticalLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        verticalLinePaint.setColor(getResources().getColor(R.color.black_30));
        verticalLinePaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{CommonUtils.dip2px(mContext, 3),
                CommonUtils.dip2px(mContext, 2), CommonUtils.dip2px(mContext, 3), CommonUtils.dip2px(mContext, 2)}, 0);
        verticalLinePaint.setPathEffect(effects);

        bottomLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomLinePaint.setColor(getResources().getColor(R.color.hourly_card_temp_line_gray));
        bottomLinePaint.setStyle(Paint.Style.STROKE);

        timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timePaint.setColor(getResources().getColor(R.color.black_80));
//        timePaint.setTypeface(oSwaldRegular);
    }

    public void fillData(NewHourlyView view, List<HourForecast> hourForecasts) {
        hours.clear();
        hours.addAll(hourForecasts);

        this.hourlyView = view;

        itemWidth = hourlyView.getOverviewItemWidth();
        viewHeight = hourlyView.getContentHeight();

        tempPaint.setTextSize((4 / 3f) * hourlyView.getOverTempHeight());

        tempLinePaint.setStrokeWidth(hourlyView.getTempLineStrokeWidth());

        dotInPaint.setColor(getResources().getColor(R.color.white_100));

        dotOutPaint.setColor(getResources().getColor(R.color.hourly_card_temp_line_gray));

        verticalLinePaint.setStrokeWidth(hourlyView.getVerticalLineStrokeWidth());

        bottomLinePaint.setStrokeWidth(hourlyView.getBottomLineStrokeWidth());

        timePaint.setTextSize((4 / 3f) * hourlyView.getTimeHeight());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(itemWidth * hours.size(), viewHeight);
        setLayoutParams(params);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (hours.isEmpty()) {
            return;
        }
        boolean isRightRead = false;
        for (int i = 0; i < hours.size(); i++) {
            drawItem(canvas, hours, i, isRightRead);
        }

    }

    private void drawItem(Canvas canvas, List<HourForecast> hourList, int i, boolean isRightRead) {
        HourForecast hour = hourList.get(i);
        boolean isNow = hour.getNowId() == HourForecast.NOW_ID_IS_NOW;
        float centerX, nextCenterX, tempX, timeX;
        if (i == 0 || (i > 0 && hour.getIconResId()
                != hourList.get(i - 1).getIconResId())
                || i == hourList.size() - 1) {
            verticalLinePath.reset();
            if (isRightRead) {
                verticalLinePath.moveTo(itemWidth * (hourList.size() - i - 0.5f), hour.getY());
                verticalLinePath.lineTo(itemWidth * (hourList.size() - i - 0.5f), getHeight() - hourlyView.getOverPaddingBottom()
                        - hourlyView.getTimeHeight() - hourlyView.getTimeTopPadding());
            } else {
                verticalLinePath.moveTo(itemWidth * (i + 0.5f), hour.getY());
                verticalLinePath.lineTo(itemWidth * (i + 0.5f), getHeight() - hourlyView.getOverPaddingBottom()
                        - hourlyView.getTimeHeight() - hourlyView.getTimeTopPadding());
            }
            canvas.drawPath(verticalLinePath, verticalLinePaint);
        }

        if (isRightRead) {
            centerX = itemWidth * (hourList.size() - i - 0.5f);
            nextCenterX = itemWidth * (hourList.size() - i - 1.5f);
            tempX = (itemWidth - tempPaint.measureText(hour.getTemp())) / 2 + itemWidth * (hourList.size() - 1 - i);
            timeX = (itemWidth - tempPaint.measureText(hour.getTime())) / 2 + itemWidth * (hourList.size() - 1 - i);
        } else {
            centerX = itemWidth * (i + 0.5f);
            nextCenterX = itemWidth * (i + 1.5f);
            tempX = (itemWidth - tempPaint.measureText(hour.getTemp())) / 2 + itemWidth * i;
            timeX = (itemWidth - tempPaint.measureText(hour.getTime())) / 2 + itemWidth * i;
        }
        if (hour.isHighest()) {
            tempPaint.setColor(getResources().getColor(R.color.hourly_card_high_red));
            dotOutPaint.setColor(getResources().getColor(R.color.hourly_card_high_red));
            dotInPaint.setColor(getResources().getColor(R.color.white_100));
        } else if (hour.isLowest()) {
            tempPaint.setColor(getResources().getColor(R.color.hourly_card_low_blue));
            dotOutPaint.setColor(getResources().getColor(R.color.hourly_card_low_blue));
            dotInPaint.setColor(getResources().getColor(R.color.white_100));
        } else {
            tempPaint.setColor(getResources().getColor(R.color.black_60));
            dotOutPaint.setColor(getResources().getColor(R.color.hourly_card_temp_line_gray));
            dotInPaint.setColor(getResources().getColor(R.color.white_100));
        }

        //tempLine
        if (i != hourList.size() - 1) {
            canvas.drawLine(centerX, hour.getY(), nextCenterX, hourList.get(i + 1).getY(), tempLinePaint);
        }
        //dot
        if (isNow) {
            canvas.drawCircle(centerX, hour.getY(), dp2px(4.5f), dotInPaint);
            canvas.drawCircle(centerX, hour.getY(), dp2px(3f), dotOutPaint);
        } else {
            canvas.drawCircle(centerX, hour.getY(), hourlyView.getDotOutRadius(), dotOutPaint);
            canvas.drawCircle(centerX, hour.getY(), hourlyView.getDotInRadius(), dotInPaint);
        }
        //temp
        canvas.drawText(hour.getTemp(), tempX, hour.getY() - hourlyView.getOverTempPaddingBottom(), tempPaint);
        //bottom line
        float bottomLineY = getHeight() - hourlyView.getOverPaddingBottom() - hourlyView.getTimeHeight()
                - hourlyView.getTimeTopPadding() - hourlyView.getBottomLineStrokeWidth() / 2f;
        canvas.drawLine(itemWidth * 0.5f, bottomLineY, getWidth() - itemWidth * 0.5f, bottomLineY, bottomLinePaint);
        //time
        canvas.drawText(isNow ? getResources().getString(R.string.now) : hour.getTime(),
                timeX, getHeight() - hourlyView.getOverPaddingBottom(), timePaint);
    }

    private int dp2px(float dp) {
        return CommonUtils.dip2px(mContext, dp);
    }


}
