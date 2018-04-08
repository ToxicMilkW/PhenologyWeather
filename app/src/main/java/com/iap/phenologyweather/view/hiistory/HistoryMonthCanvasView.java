package com.iap.phenologyweather.view.hiistory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.MonthForecast;
import com.iap.phenologyweather.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public class HistoryMonthCanvasView extends View {
    private List<MonthForecast> mList = new ArrayList<>();
    private Context mContext;
    private Dimens mDimens;
    private Paint mRainfallImgPaint, mHighTempTextPaint, mLowTempTextPaint, mRainfallTextPaint, mMonthTextPaint,
            mRainfallTitleTextPaint, mDivisionLinePaint, mHighTempPaint, mLowTempPaint,
            mHighTempShadowPaint, mLowTempShadowPaint,
            mRainDayLinePaint, mRainDayNumPaint
            ;
    private Path highLinePath, lowLinePath, rainDayPath;
    private Path highShadowPath, lowShadowPath;
    private Path dividerLinePath;
    private int lowTemp, highTemp, tempDiff, highRainDay, lowRainDay, rainDayDiff;
    private float maxRainfall;
    private List<Point> mHighTempPoint = new ArrayList<>();
    private List<Point> mLowTempPoint = new ArrayList<>();
    private List<Point> mRainDayPoint = new ArrayList<>();

    private Paint mPointPaint;

    public HistoryMonthCanvasView(Context context) {
        super(context);
        init(context);
    }

    public HistoryMonthCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public HistoryMonthCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private int dp2Px(float dp) {
        return CommonUtils.dip2px(mContext, dp);
    }

    private void initShadow() {
        mDimens.tempRegionHeight = mDimens.viewHeight - mDimens.tempBottomPadding
                - mDimens.rainfallRegionHeight - mDimens.bottomPadding - mDimens.centerPaddingHeight
                - mDimens.rainTitleBottomPadding - mDimens.rainTitleTextSize
                - mDimens.rainTitlePaddingTop
                - mDimens.rainSecondTitleTextSize - mDimens.rainSecondTitleBottomPadding
                - mDimens.rainSecondTitlePaddingTop;
        Shader HighShader = new LinearGradient(0, 0, 0, mDimens.viewHeight
                - mDimens.rainfallRegionHeight - mDimens.rainDayTextSize
                - mDimens.rainTitleBottomPadding - mDimens.rainTitleTextSize
                - mDimens.rainTitlePaddingTop
                - mDimens.rainSecondTitleTextSize - mDimens.rainSecondTitleBottomPadding
                - mDimens.rainSecondTitlePaddingTop,
                new int[]{getResources().getColor(R.color.hourly_card_high_red_15),
                        getResources().getColor(R.color.white_10)}, null, Shader.TileMode.CLAMP);
        mHighTempShadowPaint.setShader(HighShader);
        Shader lowShader = new LinearGradient(0, 0, 0, mDimens.viewHeight
                - mDimens.rainfallRegionHeight - mDimens.rainDayTextSize
                - mDimens.rainTitleBottomPadding - mDimens.rainTitleTextSize
                - mDimens.rainTitlePaddingTop
                - mDimens.rainSecondTitleTextSize - mDimens.rainSecondTitleBottomPadding
                - mDimens.rainSecondTitlePaddingTop,
                new int[]{getResources().getColor(R.color.hourly_card_low_blue_20),
                        getResources().getColor(R.color.white_40)}, null, Shader.TileMode.CLAMP);
        mLowTempShadowPaint.setShader(lowShader);
    }

    private class Dimens {
        int rainTitleTextSize;
        int rainSecondTitleTextSize;
        int rainTitleBottomPadding;
        int rainSecondTitleBottomPadding;
        int rainTitlePaddingTop;
        int rainSecondTitlePaddingTop;
        int rainTitlePaddingLeft;
        int rainTitlePaddingRight;
        int monthTextSize;
        int highTempTextSize;
        int lowTempTextSize;
        int rainDayTextSize;

        int viewHeight;

        int itemWidth;

        /**
         * 曲线部分的高度，计算而得
         */
        int tempRegionHeight;
        int rainDayRegionHeight;
        int rainfallRegionHeight;
        int leftLineWidth;
        int bottomLineWidth;
        int centerLineWidth;
        int dashLineWidth;
        int leftPadding;
        int leftLowHighTempSize;
        int centerPaddingHeight;
        int rainDayBottomPadding;
        int tempBottomPadding;
        int bottomPadding;

        /**
         * 一度有多高
         */
        float oneTempHeight;
        float oneRainDayHeight;

        /**
         * 降水量1ml有多高
         */
        float oneMlHeight;
        int dataPadding;

        /**
         * 温度标题bottompadding
         */
        int tempTextBottomPadding;

        public void init() {
            viewHeight = dp2Px(360);
            leftPadding = 0;
            monthTextSize = dp2Px(10);
            highTempTextSize = dp2Px(12);
            lowTempTextSize = dp2Px(12);
            rainDayTextSize = dp2Px(12);
            leftLowHighTempSize = dp2Px(8);
            rainTitleTextSize = dp2Px(12);
            rainSecondTitleTextSize = dp2Px(9);
            rainTitlePaddingTop = dp2Px(14);
            rainSecondTitlePaddingTop = dp2Px(1);
            rainTitlePaddingLeft = dp2Px(8);
            rainTitlePaddingRight = dp2Px(8);
            rainTitleBottomPadding = dp2Px(3);
            rainSecondTitleBottomPadding = dp2Px(8);
            bottomLineWidth = dp2Px(1);
            leftLineWidth = dp2Px(2);
            centerLineWidth = dp2Px(2);
            dashLineWidth = 1;
            tempBottomPadding = dp2Px(40);
            tempTextBottomPadding = dp2Px(4);
            rainDayRegionHeight = dp2Px(44);
            rainfallRegionHeight = dp2Px(60);
            bottomPadding = monthTextSize + dp2Px(6);
            dataPadding = 0;
            rainDayBottomPadding = dp2Px(4);
            centerPaddingHeight = rainTitlePaddingTop + rainTitleTextSize + rainTitleBottomPadding
                    + rainSecondTitlePaddingTop + rainSecondTitleTextSize + rainSecondTitleBottomPadding
                    + rainDayRegionHeight + rainDayBottomPadding;
        }
    }

    private void init(Context context) {
        mContext = context;
        this.mDimens = new Dimens();
        mDimens.init();

        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mDimens.viewHeight));


        mRainfallImgPaint = new Paint();
        mRainfallImgPaint.setAntiAlias(true);
        ColorFilter iconColorFilter = new PorterDuffColorFilter(
                getResources().getColor(R.color.monthly_card_rain_day_blue), PorterDuff.Mode.MULTIPLY);
        mRainfallImgPaint.setColorFilter(iconColorFilter);
        mRainfallImgPaint.setColor(getResources().getColor(R.color.monthly_card_rain_day_blue));

        //高温文字paint
        mHighTempTextPaint = new Paint();
        mHighTempTextPaint.setAntiAlias(true);
        mHighTempTextPaint.setTextSize(mDimens.highTempTextSize);

        //低温文字paint
        mLowTempTextPaint = new Paint();
        mLowTempTextPaint.setAntiAlias(true);
        mLowTempTextPaint.setTextSize(mDimens.lowTempTextSize);

        //高温paint
        mHighTempPaint = new Paint();
        mHighTempPaint.setAntiAlias(true);
        mHighTempPaint.setColor(getResources().getColor(R.color.hourly_card_high_red));
        mHighTempPaint.setStyle(Paint.Style.STROKE);
        mHighTempPaint.setStrokeWidth(dp2Px(1f));

        //低温paint
        mLowTempPaint = new Paint();
        mLowTempPaint.setAntiAlias(true);
        mLowTempPaint.setColor(getResources().getColor(R.color.hourly_card_low_blue));
        mLowTempPaint.setStyle(Paint.Style.STROKE);
        mLowTempPaint.setStrokeWidth(dp2Px(1f));

        //降雨天数曲线Paint
        mRainDayLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRainDayLinePaint.setColor(getResources().getColor(R.color.monthly_card_rain_day_gray));
        mRainDayLinePaint.setStyle(Paint.Style.STROKE);
        mRainDayLinePaint.setStrokeWidth(dp2Px(1f));

        //降水天数文字paint
        mRainDayNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRainDayNumPaint.setTextSize(mDimens.rainDayTextSize);
        mRainDayNumPaint.setColor(getResources().getColor(R.color.black_70));

        mRainfallTextPaint = new Paint();
        mRainfallTextPaint.setAntiAlias(true);
        mRainfallTextPaint.setColor(getResources().getColor(R.color.black_50));
        mRainfallTextPaint.setTextSize(mDimens.rainDayTextSize);

        mMonthTextPaint = new Paint();
        mMonthTextPaint.setAntiAlias(true);
        mMonthTextPaint.setColor(getResources().getColor(R.color.black_40));
        mMonthTextPaint.setTextSize(mDimens.monthTextSize);

        mRainfallTitleTextPaint = new Paint();
        mRainfallTitleTextPaint.setAntiAlias(true);
        mRainfallTitleTextPaint.setColor(getResources().getColor(R.color.black_50));
        mRainfallTitleTextPaint.setTextSize(mDimens.rainTitleTextSize);

        mDivisionLinePaint = new Paint();
        mDivisionLinePaint.setAntiAlias(true);
        mDivisionLinePaint.setColor(getResources().getColor(R.color.black_50));
        mDivisionLinePaint.setStyle(Paint.Style.STROKE);
        mDivisionLinePaint.setStrokeWidth(dp2Px(1));

        mHighTempShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLowTempShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);

        highLinePath = new Path();
        lowLinePath = new Path();
        highShadowPath = new Path();
        lowShadowPath = new Path();

        rainDayPath = new Path();

        dividerLinePath = new Path();

        initShadow();
    }

    public void fillData(List<MonthForecast> list, int highTemp, int lowTemp,
                         int highRainDay, int lowRainDay, float maxRainfall) {
        mList = list;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.maxRainfall = maxRainfall;
        this.highRainDay = highRainDay;
        this.lowRainDay = lowRainDay;
        this.rainDayDiff = highRainDay - lowRainDay;
        this.tempDiff = highTemp - lowTemp;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mList != null && mList.size() > 0) {
            mDimens.itemWidth = (int) ((getWidth() - mDimens.leftPadding) * 1.0 / 12);
            mDimens.oneTempHeight = (mDimens.tempRegionHeight - mDimens.dataPadding * 2) / tempDiff;
            mDimens.oneRainDayHeight = (mDimens.rainDayRegionHeight - mDimens.highTempTextSize
                    - mDimens.tempTextBottomPadding - mDimens.dataPadding * 2) / rainDayDiff;
            mDimens.oneMlHeight = (mDimens.rainfallRegionHeight - + mDimens.rainDayTextSize - dp2Px(7)) / (maxRainfall);
            resetList();
            for (MonthForecast mf : mList) {
                //init coordinate
                mf.setTempTop((int) ((highTemp - mf.getHighTemp()) * mDimens.oneTempHeight + mDimens.dataPadding + mDimens.monthTextSize + mDimens.tempTextBottomPadding + mDimens.highTempTextSize));
                mf.setTempBottom((int) ((highTemp - mf.getLowTemp()) * mDimens.oneTempHeight + mDimens.dataPadding + mDimens.monthTextSize + mDimens.tempTextBottomPadding));

                mf.setLeft(mDimens.leftPadding + mf.getId() * mDimens.itemWidth);
                mf.setRight(mDimens.leftPadding + mf.getId() * mDimens.itemWidth + mDimens.itemWidth);

                mf.setRainDayY((int) ((highRainDay - mf.getRainDay()) * mDimens.oneRainDayHeight
                                        + mDimens.tempRegionHeight + mDimens.tempBottomPadding
                                        + mDimens.rainTitleTextSize + mDimens.rainTitlePaddingTop
                                        + mDimens.lowTempTextSize + mDimens.tempTextBottomPadding
                                         + mDimens.rainTitleBottomPadding
                                        + mDimens.rainSecondTitlePaddingTop + mDimens.rainSecondTitleTextSize + mDimens.rainSecondTitleBottomPadding));

                mf.setRanfallTop((int) (mDimens.tempRegionHeight + mDimens.tempBottomPadding + mDimens.centerPaddingHeight
                        + mDimens.rainTitlePaddingTop + mDimens.rainTitleTextSize + mDimens.rainTitleBottomPadding
                        + mDimens.rainSecondTitlePaddingTop + mDimens.rainSecondTitleTextSize + mDimens.rainSecondTitleBottomPadding
                        + mDimens.rainDayTextSize + dp2Px(7)
                        + mDimens.dataPadding * 2 + (maxRainfall - mf.getPrecip()) * mDimens.oneMlHeight));
                mf.setRanfallBottom((int) (mDimens.tempRegionHeight + mDimens.tempBottomPadding + mDimens.centerPaddingHeight
                        + mDimens.rainTitlePaddingTop + mDimens.rainTitleTextSize + mDimens.rainTitleBottomPadding
                        + mDimens.rainSecondTitlePaddingTop + mDimens.rainSecondTitleTextSize + mDimens.rainSecondTitleBottomPadding
                        + mDimens.rainDayTextSize + dp2Px(7)
                        + mDimens.dataPadding * 2 + (maxRainfall) * mDimens.oneMlHeight));
                drawItem(canvas, mf);
                getBezierPoints(mf);
            }
            //绘制高低温曲线h和阴影
            getBezierPath(highLinePath, mHighTempPoint);
            highShadowPath.reset();
            highShadowPath.addPath(highLinePath);
            highShadowPath.lineTo(getWidth(), mDimens.viewHeight
                    - mDimens.rainfallRegionHeight - mDimens.rainDayTextSize
                    - mDimens.rainTitleBottomPadding - mDimens.rainTitleTextSize
                    - mDimens.rainTitlePaddingTop
                    - mDimens.rainSecondTitleTextSize - mDimens.rainSecondTitleBottomPadding
                    - mDimens.rainSecondTitlePaddingTop);
            highShadowPath.lineTo(0, mDimens.viewHeight
                    - mDimens.rainfallRegionHeight - mDimens.rainDayTextSize
                    - mDimens.rainTitleBottomPadding - mDimens.rainTitleTextSize
                    - mDimens.rainTitlePaddingTop
                    - mDimens.rainSecondTitleTextSize - mDimens.rainSecondTitleBottomPadding
                    - mDimens.rainSecondTitlePaddingTop);
            highShadowPath.close();
            drawShadow(canvas, highShadowPath, mHighTempShadowPaint);
            canvas.drawPath(highLinePath, mHighTempPaint);

            getBezierPath(lowLinePath, mLowTempPoint);
            lowShadowPath.reset();
            lowShadowPath.addPath(lowLinePath);
            lowShadowPath.lineTo(getWidth(), mDimens.viewHeight
                    - mDimens.rainfallRegionHeight - mDimens.rainDayTextSize
                    - mDimens.rainTitleBottomPadding - mDimens.rainTitleTextSize
                    - mDimens.rainTitlePaddingTop
                    - mDimens.rainSecondTitleTextSize - mDimens.rainSecondTitleBottomPadding
                    - mDimens.rainSecondTitlePaddingTop);
            lowShadowPath.lineTo(0, mDimens.viewHeight
                    - mDimens.rainfallRegionHeight - mDimens.rainDayTextSize
                    - mDimens.rainTitleBottomPadding - mDimens.rainTitleTextSize
                    - mDimens.rainTitlePaddingTop
                    - mDimens.rainSecondTitleTextSize - mDimens.rainSecondTitleBottomPadding
                    - mDimens.rainSecondTitlePaddingTop);
            lowShadowPath.close();
            drawShadow(canvas, lowShadowPath, mLowTempShadowPaint);
            canvas.drawPath(lowLinePath, mLowTempPaint);

            getBezierPath(rainDayPath, mRainDayPoint);
            canvas.drawPath(rainDayPath, mRainDayLinePaint);

            drawLineDotAndTempText(canvas);


            //画框架线
            drawLine(canvas);
        }
    }

    private void resetList() {
        mHighTempPoint.clear();
        mLowTempPoint.clear();
    }

    private void getBezierPoints(MonthForecast mf) {
        //获取B曲线的点
        mHighTempPoint.add(new Point(mf.getLeft() + mDimens.itemWidth / 2, mf.getTempTop()));
        mLowTempPoint.add(new Point(mf.getLeft() + mDimens.itemWidth / 2, mf.getTempBottom()));
        mRainDayPoint.add(new Point(mf.getLeft() + mDimens.itemWidth / 2, mf.getRainDayY()));
    }


    private void drawLine(Canvas canvas) {
        //to get text high
        canvas.drawText(getResources().getString(R.string.monthly_rain_fall_title).toUpperCase(), mDimens.rainTitlePaddingLeft,
                mDimens.tempRegionHeight + mDimens.tempBottomPadding + mDimens.centerPaddingHeight
                        + mDimens.rainTitlePaddingTop + mDimens.rainTitleTextSize
                        + mDimens.dataPadding * 2, mRainfallTitleTextPaint);
        canvas.drawText(getResources().getString(R.string.monthly_rain_day_title).toUpperCase(), mDimens.rainTitlePaddingLeft,
                mDimens.tempRegionHeight + mDimens.tempBottomPadding
                        + mDimens.rainTitlePaddingTop + mDimens.rainTitleTextSize
                        + mDimens.dataPadding * 2, mRainfallTitleTextPaint);
        canvas.drawText(getResources().getString(R.string.monthly_temp_title).toUpperCase(), mDimens.rainTitlePaddingLeft,
                mDimens.monthTextSize, mRainfallTitleTextPaint);
        mRainfallTitleTextPaint.setTextSize(mDimens.rainSecondTitleTextSize);
        canvas.drawText(getResources().getString(R.string.monthly_rain_fall_unit).toUpperCase(), mDimens.rainTitlePaddingLeft,
                mDimens.tempRegionHeight + mDimens.tempBottomPadding + mDimens.centerPaddingHeight
                        + mDimens.rainTitlePaddingTop + mDimens.rainTitleTextSize + mDimens.rainTitleBottomPadding
                        + mDimens.rainSecondTitlePaddingTop + mDimens.rainSecondTitleTextSize
                        + mDimens.dataPadding * 2, mRainfallTitleTextPaint);
        canvas.drawText(getResources().getString(R.string.monthly_rain_day_unit).toUpperCase(), mDimens.rainTitlePaddingLeft,
                mDimens.tempRegionHeight + mDimens.tempBottomPadding
                        + mDimens.rainTitlePaddingTop + mDimens.rainTitleTextSize + mDimens.rainTitleBottomPadding
                        + mDimens.rainSecondTitlePaddingTop + mDimens.rainSecondTitleTextSize
                        + mDimens.dataPadding * 2, mRainfallTitleTextPaint);

        //draw bottom line
        mDivisionLinePaint.setStrokeWidth(mDimens.leftLineWidth);
        canvas.drawLine(0, 0, 0, mDimens.tempRegionHeight + mDimens.tempBottomPadding
                + mDimens.rainfallRegionHeight + mDimens.centerPaddingHeight
                + mDimens.rainTitlePaddingTop + mDimens.rainTitleBottomPadding + mDimens.rainTitleTextSize
                + mDimens.rainSecondTitlePaddingTop + mDimens.rainSecondTitleTextSize + mDimens.rainSecondTitleBottomPadding
                , mDivisionLinePaint);

        mDivisionLinePaint.setStrokeWidth(mDimens.bottomLineWidth);
        canvas.drawLine(0, mDimens.tempRegionHeight + mDimens.tempBottomPadding
                        + mDimens.rainfallRegionHeight + mDimens.centerPaddingHeight
                        + mDimens.rainTitlePaddingTop + mDimens.rainTitleBottomPadding + mDimens.rainTitleTextSize
                        + mDimens.rainSecondTitlePaddingTop + mDimens.rainSecondTitleTextSize + mDimens.rainSecondTitleBottomPadding
                        + mDimens.bottomLineWidth / 2,
                getWidth(), mDimens.tempRegionHeight + mDimens.tempBottomPadding
                        + mDimens.rainfallRegionHeight + mDimens.centerPaddingHeight
                        + mDimens.rainTitlePaddingTop + mDimens.rainTitleBottomPadding + mDimens.rainTitleTextSize
                        + mDimens.rainSecondTitlePaddingTop + mDimens.rainSecondTitleTextSize + mDimens.rainSecondTitleBottomPadding
                        + mDimens.bottomLineWidth / 2, mDivisionLinePaint);
    }

    private void drawItem(Canvas canvas, MonthForecast mf) {

        //temp divide line
        dividerLinePath.reset();
        dividerLinePath.moveTo(mf.getLeft(), dp2Px(4));
        dividerLinePath.lineTo(mf.getLeft(), mf.getRanfallBottom());

        RectF frameRect;
        if (mf.getRainDay() != 0) {
            frameRect = new RectF(mf.getLeft() + mDimens.leftLineWidth / 2, mf.getRanfallTop(), mf.getRight() + mDimens.leftLineWidth / 2 - 1.5f, mf.getRanfallBottom());
            canvas.drawRoundRect(frameRect, 0, 0, mRainfallImgPaint);
        }

        //降水量文字
        canvas.drawText(
                mf.getRainfallString(), mf.getLeft() - mRainfallTextPaint.measureText(mf.getRainfallString()) / 2 + mDimens.itemWidth / 2,
                mf.getRanfallTop() - dp2Px(7),
                mRainfallTextPaint
        );

        //月份文字
        String monthName = mf.getMonthName().toUpperCase();
        canvas.drawText(monthName, mf.getLeft() - mMonthTextPaint.measureText(monthName) / 2 + mDimens.itemWidth / 2,
                mf.getRanfallBottom() + mDimens.monthTextSize + dp2Px(4), mMonthTextPaint);
    }

    private List<Point> getMidPoints(List<Point> points) {
        List<Point> mMidPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point midPoint;
            if (i != points.size() - 1) {
                midPoint = new Point((points.get(i).x + points.get(i + 1).x) / 2, (points.get(i).y + points.get(i + 1).y) / 2);
                mMidPoints.add(midPoint);
            }
        }
        return mMidPoints;
    }

    private List<Point> getMidMidPoints(List<Point> midPoints) {
        List<Point> mMidMidPoints = new ArrayList<>();
        for (int i = 0; i < midPoints.size(); i++) {
            Point midMidPoint;
            if (i != midPoints.size() - 1) {
                midMidPoint = new Point((midPoints.get(i).x + midPoints.get(i + 1).x) / 2, (midPoints.get(i).y + midPoints.get(i + 1).y) / 2);
                mMidMidPoints.add(midMidPoint);
            }
        }
        return mMidMidPoints;
    }

    private List<Point> getControlPoints(List<Point> points, List<Point> midPoints, List<Point> midMidPoints) {
        List<Point> controlPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            if (i != 0 && i != points.size() - 1) {
                Point before = new Point();
                Point after = new Point();
                before.x = points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i - 1).x;
                before.y = points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i - 1).y;
                after.x = points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i).x;
                after.y = points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i).y;
                controlPoints.add(before);
                controlPoints.add(after);
            }
        }
        return controlPoints;
    }

    /**
     * 生成B曲线
     */
    private void getBezierPath(Path path, List<Point> points) {
        // 重置路径
        path.reset();
        points.add(0, new Point(-mDimens.itemWidth, points.get(0).y));
        points.add(new Point(getWidth() + mDimens.itemWidth, points.get(points.size() - 1).y));
        List<Point> midPoints = getMidPoints(points);
        List<Point> midMidPoints = getMidMidPoints(midPoints);
        List<Point> controlPoints = getControlPoints(points, midPoints, midMidPoints);
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {// 第一条为二阶贝塞尔
                path.moveTo(points.get(i).x, points.get(i).y);// 起点
                path.quadTo(controlPoints.get(i).x, controlPoints.get(i).y,// 控制点
                        points.get(i + 1).x, points.get(i + 1).y);
            } else if (i < points.size() - 2) {// 三阶贝塞尔
                path.cubicTo(controlPoints.get(2 * i - 1).x, controlPoints.get(2 * i - 1).y,// 控制点
                        controlPoints.get(2 * i).x, controlPoints.get(2 * i).y,// 控制点
                        points.get(i + 1).x, points.get(i + 1).y);// 终点
            } else if (i == points.size() - 2) {// 最后一条为二阶贝塞尔
                path.quadTo(controlPoints.get(controlPoints.size() - 1).x, controlPoints.get(controlPoints.size() - 1).y,
                        points.get(i + 1).x, points.get(i + 1).y);// 终点
            }
        }
        points.remove(0);
        points.remove(points.size() - 1);
    }


    /**
     * 生成阴影
     */
    private void drawShadow(Canvas canvas, Path path, Paint paint) {
        canvas.drawPath(path, paint);
    }

    /**
     * 生成曲线上面的点和温度
     */
    private void drawLineDotAndTempText(Canvas canvas) {
        for (int i = 0; i < mHighTempPoint.size(); i++) {
            MonthForecast mf = mList.get(i);
            mPointPaint.setColor(getResources().getColor(R.color.hourly_card_high_red));
            canvas.drawCircle(mHighTempPoint.get(i).x, mHighTempPoint.get(i).y, dp2Px(2), mPointPaint);
            mPointPaint.setColor(getResources().getColor(R.color.hourly_card_low_blue));
            canvas.drawCircle(mLowTempPoint.get(i).x, mLowTempPoint.get(i).y, dp2Px(2), mPointPaint);
            mPointPaint.setColor(Color.WHITE);
            canvas.drawCircle(mHighTempPoint.get(i).x, mHighTempPoint.get(i).y, dp2Px(1), mPointPaint);
            canvas.drawCircle(mLowTempPoint.get(i).x, mLowTempPoint.get(i).y, dp2Px(1), mPointPaint);
            mPointPaint.setColor(getResources().getColor(R.color.monthly_card_rain_day_gray));
            canvas.drawCircle(mRainDayPoint.get(i).x, mRainDayPoint.get(i).y, dp2Px(2), mPointPaint);
            mPointPaint.setColor(Color.WHITE);
            canvas.drawCircle(mRainDayPoint.get(i).x, mRainDayPoint.get(i).y, dp2Px(1), mPointPaint);

            if (mf != null) {

                int mHighTempColor = getResources().getColor(R.color.black_80);
                int mLowTempColor = getResources().getColor(R.color.black_50);
                if (mf.getHighTemp() == highTemp) {
                    mHighTempColor = getResources().getColor(R.color.hourly_card_high_red);
                }
                if (mf.getLowTemp() == lowTemp) {
                    mLowTempColor = getResources().getColor(R.color.hourly_card_low_blue);
                }
                mHighTempTextPaint.setColor(mHighTempColor);
                mLowTempTextPaint.setColor(mLowTempColor);

                //最高温度文字
                canvas.drawText(
                        mf.getHighTempString(mContext),
                        mf.getLeft() - mHighTempTextPaint.measureText(mf.getHighTempString(mContext)) / 2 + mDimens.itemWidth / 2,
                        mf.getTempTop() - dp2Px(7),
                        mHighTempTextPaint);

                //最低温度文字
                canvas.drawText(
                        mf.getLowTempString(mContext),
                        mf.getLeft() - mLowTempTextPaint.measureText(mf.getLowTempString(mContext)) / 2 + mDimens.itemWidth / 2,
                        mf.getTempBottom() + mDimens.highTempTextSize + dp2Px(6),
                        mLowTempTextPaint);

                //降水天数文字
                canvas.drawText(
                        mf.getRainString(),
                        mf.getLeft() - mRainDayNumPaint.measureText(mf.getRainString()) / 2 + mDimens.itemWidth / 2,
                        mf.getRainDayY() - dp2Px(7),
                        mRainDayNumPaint);

            }
        }
    }

}
