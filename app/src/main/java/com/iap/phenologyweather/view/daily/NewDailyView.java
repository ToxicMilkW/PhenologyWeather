package com.iap.phenologyweather.view.daily;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.DayForecast;
import com.iap.phenologyweather.data.loader.WeatherInfoLoader;
import com.iap.phenologyweather.utils.AmberSdkConstants;
import com.iap.phenologyweather.utils.CommonUtils;
import com.iap.phenologyweather.utils.Preferences;
import com.iap.phenologyweather.utils.WeatherDataFormatUtils;
import com.iap.phenologyweather.view.custom.DottedLineView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by chenxueqing on 2016/8/17.
 */

public class NewDailyView extends LinearLayout {

    private List<DayForecast> dayList = new ArrayList<>();
    private boolean isRightRead = false;
    private Context mContext;
    private RecyclerView mVerticalRecycler;
    private View mHeaderView;
    private NewDailyView dailyView;
    private RecyclerView mHeaderRecycler;

    //-----Bezier curve for daily
    //High
    private List<Point> mHTempPoints = new ArrayList<>();
    private List<Point> mHMidPoints = new ArrayList<>();
    private List<Point> mHMidMidPoints = new ArrayList<>();
    private List<Point> mHControlPoints = new ArrayList<>();
    //Low
    private List<Point> mLTempPoints = new ArrayList<>();
    private List<Point> mLMidPoints = new ArrayList<>();
    private List<Point> mLMidMidPoints = new ArrayList<>();
    private List<Point> mLControlPoints = new ArrayList<>();


    private int highestTemp;
    private int tempDiff;
    private int curveViewItemWidth;
    private int curveViewHeight;
    private int curveViewTopPadding;
    private int curveViewBottomPadding;
    private int highTempHeight;
    private int highTempBottomPadding;
    private int lowTempHeight;
    private int lowTempTopPadding;
    private int dotOutRadius, dotInRadius;
    private int lineStrokeWidth;

    private boolean hasYesterday;
    private DailyHorizontalAdapter horizontalAdapter;
    private DailyVerticalAdapter verticalAdapter;


    public NewDailyView(Context context) {
        super(context);
        dailyView = this;
        this.mContext = context;
        initDimens();
        initViews();
    }

    private void initDimens() {
        curveViewItemWidth = dp2px(60);
        curveViewHeight = dp2px(140);
        curveViewTopPadding = dp2px(10);
        curveViewBottomPadding = dp2px(16);
        highTempHeight = dp2px(12);
        highTempBottomPadding = dp2px(12);
        lowTempHeight = dp2px(12);
        lowTempTopPadding = dp2px(12);
        dotOutRadius = dp2px(2f);
        dotInRadius = dp2px(1f);
        lineStrokeWidth = dp2px(0.7f);

    }


    private void initViews() {
        View.inflate(mContext, R.layout.card_tab_daily, this);
        mVerticalRecycler = (RecyclerView) findViewById(R.id.rv_daily_vertical);
        mVerticalRecycler.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVerticalRecycler.setLayoutManager(layoutManager);
        initHeaderView();
    }

    public RecyclerView getRecyclerView() {
        return mVerticalRecycler;
    }

    private void initHeaderView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mHeaderView = inflater.inflate(R.layout.card_tab_daily_header, mVerticalRecycler, false);
        mHeaderRecycler = (RecyclerView) mHeaderView.findViewById(R.id.rv_daily_header);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHeaderRecycler.setLayoutManager(linearLayoutManager);
        mHeaderRecycler.setFocusable(false);

        View itemView = inflater.inflate(R.layout.item_tab_daily, mHeaderRecycler, false);
        itemView.measure(0, 0);
        ViewGroup.LayoutParams params = mHeaderRecycler.getLayoutParams();
        params.height = itemView.getMeasuredHeight() + curveViewHeight;
        mHeaderRecycler.setLayoutParams(params);
    }


    public void fillData(int weatherDataId, WeatherInfoLoader weatherInfoLoader) {
        dayList.clear();
        DayForecast.fillData(mContext.getApplicationContext(), weatherInfoLoader, weatherDataId, dayList);
        if (dayList.isEmpty()) {
            setVisibility(GONE);
            return;
        }
        if (isRightRead) {
            Collections.reverse(dayList);
        }
        setVisibility(VISIBLE);

        hasYesterday = dayList.get(0).isYesterday();
        initTempScale(dayList);

        mHTempPoints.clear();
        mLTempPoints.clear();

        for (int i = 0; i < dayList.size(); i++) {
            DayForecast forecast = dayList.get(i);

            int curveHeight = curveViewHeight - curveViewTopPadding - curveViewBottomPadding
                    - highTempHeight - highTempBottomPadding
                    - lowTempHeight - lowTempTopPadding;

            //temp point
            int highY = curveViewTopPadding + highTempHeight + highTempBottomPadding +
                    curveHeight * (highestTemp - forecast.getHighTemp()) / tempDiff;
            int lowY = curveViewTopPadding + highTempHeight + highTempBottomPadding +
                    curveHeight * (highestTemp - forecast.getLowTemp()) / tempDiff;
            forecast.setHighY(highY);
            forecast.setLowY(lowY);

            Point mHTempPoint = new Point((int) (curveViewItemWidth * 0.5), highY);
            Point mLTempPoint = new Point((int) (curveViewItemWidth * 0.5), lowY);

            if (i == 0) {
                mHTempPoints.add(mHTempPoint);
                mLTempPoints.add(mLTempPoint);
            }
            mHTempPoints.add(mHTempPoint);
            mLTempPoints.add(mLTempPoint);

            //the last item
            if (i == dayList.size() - 1) {
                mHTempPoints.add(mHTempPoint);
                mLTempPoints.add(mLTempPoint);

                initMidPoints(mHTempPoints, mLTempPoints);
                initMidMidPoint(mHMidPoints, mLMidPoints);
                initControlPoint(mHTempPoints, mLTempPoints, mHMidPoints, mLMidPoints,
                        mHMidMidPoints, mLMidMidPoints);
            }
        }

        if (horizontalAdapter == null) {
            horizontalAdapter = new DailyHorizontalAdapter(mContext, dayList);
            mHeaderRecycler.setAdapter(horizontalAdapter);
            if (isRightRead) {
                mHeaderRecycler.scrollToPosition(dayList.size() - 1);
            } else {
                mHeaderRecycler.scrollToPosition(0);
            }
            if (Preferences.isDailySlideGuideShow(mContext)) {
                final int end = isRightRead ? -dp2px(8) : dp2px(8);
                ValueAnimator animator = ValueAnimator.ofInt(0, end);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int offset = (int) animation.getAnimatedValue();
                        if (isRightRead) {
                            if (offset <= end / 2) {
                                offset = offset - end;
                            }
                        } else {
                            if (offset >= end / 2) {
                                offset = offset - end;
                            }
                        }
                        mHeaderRecycler.scrollBy(offset, 0);
                    }
                });
                animator.setDuration(3000);
                animator.start();
                Preferences.setDailySlideGuideShow(mContext, false);
            }
        }


        if (verticalAdapter == null) {
            verticalAdapter = new DailyVerticalAdapter(mContext, dayList);
            verticalAdapter.setHeaderView(mHeaderView);
            mVerticalRecycler.setAdapter(verticalAdapter);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            TransitionManager.beginDelayedTransition((ViewGroup) getParent());
//        }
        horizontalAdapter.notifyDataSetChanged();
        verticalAdapter.notifyDataSetChanged();

    }

    private class DailyHorizontalAdapter extends RecyclerView.Adapter<DailyHorizontalAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private List<DayForecast> mDays;

        public DailyHorizontalAdapter(Context context, List<DayForecast> mDays) {
            mInflater = LayoutInflater.from(context);
            this.mDays = mDays;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_tab_daily,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.mItemView = (RelativeLayout) view.findViewById(R.id.ll_daily_item);
            viewHolder.mDotLine = (DottedLineView) view.findViewById(R.id.dotted_line_daily_item);
            viewHolder.mWeekText = (TextView) view.findViewById(R.id.text_daily_item_week);
            viewHolder.mDateText = (TextView) view.findViewById(R.id.text_daily_item_date);
            viewHolder.mIconImg = (ImageView) view.findViewById(R.id.img_daily_item_icon);
            viewHolder.curveView = (NewDailyLineCurveView) view.findViewById(R.id.daily_line_view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(curveViewItemWidth, curveViewHeight);
            viewHolder.curveView.setLayoutParams(params);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ViewGroup.LayoutParams params = holder.mItemView.getLayoutParams();
            params.width = curveViewItemWidth;
            holder.mItemView.setLayoutParams(params);
            if (position == 0) {
                holder.mDotLine.setVisibility(GONE);
            } else {
                holder.mDotLine.setVisibility(VISIBLE);
            }
            DayForecast day = mDays.get(position);
            if (day.isYesterday() || day.isToday()) {
                if (day.isYesterday()) {
                    holder.mWeekText.setText(getResources().getString(R.string.yesterday).toUpperCase());
                    holder.mWeekText.setTextColor(getResources().getColor(R.color.black_30));
                    holder.mDateText.setTextColor(getResources().getColor(R.color.black_25));
                    holder.mIconImg.setColorFilter(Color.argb(128, 255, 255, 255));
                } else {
                    holder.mWeekText.setText(getResources().getString(R.string.today).toUpperCase());
                    holder.mWeekText.setTextColor(getResources().getColor(R.color.black_60));
                    holder.mDateText.setTextColor(getResources().getColor(R.color.black_50));
                    holder.mIconImg.setColorFilter(null);
                }
            } else {
                holder.mWeekText.setText(day.getWeekName());
                holder.mWeekText.setTextColor(getResources().getColor(R.color.black_60));
                holder.mDateText.setTextColor(getResources().getColor(R.color.black_50));
                holder.mIconImg.setColorFilter(null);
            }
            holder.mDotLine.setLineColor(R.color.black_10);
            holder.mDateText.setText(day.getDateName());
            holder.mIconImg.setImageResource(day.getIconResId());

            Path highPath = holder.curveView.getHighPath();
            Path lowPath = holder.curveView.getLowPath();
            highPath.reset();
            lowPath.reset();


            if (position == 0 &&
                    (mHTempPoints.size() > position + 2 && mLTempPoints.size() > position + 2) &&
                    (mHControlPoints.size() > position * 2 + 2 && mLControlPoints.size() > position * 2 + 2)) {
                highPath.moveTo(mHTempPoints.get(position).x - curveViewItemWidth, mHTempPoints.get(position).y);
                lowPath.moveTo(mLTempPoints.get(position).x - curveViewItemWidth, mLTempPoints.get(position).y);
                highPath.quadTo(0, mHControlPoints.get(position).y,
                        mHTempPoints.get(position + 1).x, mHTempPoints.get(position + 1).y);
                lowPath.quadTo(0, mLControlPoints.get(position).y,
                        mLTempPoints.get(position + 1).x, mLTempPoints.get(position + 1).y);


                highPath.cubicTo(curveViewItemWidth, mHControlPoints.get(position * 2 + 1).y,
                        curveViewItemWidth, mHControlPoints.get(position * 2 + 2).y,
                        mHTempPoints.get(position + 2).x + curveViewItemWidth, mHTempPoints.get(position + 2).y);
                lowPath.cubicTo(curveViewItemWidth, mLControlPoints.get(position * 2 + 1).y,
                        curveViewItemWidth, mLControlPoints.get(position * 2 + 2).y,
                        mLTempPoints.get(position + 2).x + curveViewItemWidth, mLTempPoints.get(position + 2).y);

            } else if (position < mHTempPoints.size() - 3 &&
                    (mHTempPoints.size() > position + 2 && mLTempPoints.size() > position + 2) &&
                    (mHControlPoints.size() > position * 2 + 2 && mLControlPoints.size() > position * 2 + 2)) {
                highPath.moveTo(mHTempPoints.get(position).x - curveViewItemWidth, mHTempPoints.get(position).y);
                lowPath.moveTo(mLTempPoints.get(position).x - curveViewItemWidth, mLTempPoints.get(position).y);

                highPath.cubicTo(0, mHControlPoints.get(position * 2 - 1).y,
                        0, mHControlPoints.get(position * 2).y,
                        mHTempPoints.get(position + 1).x, mHTempPoints.get(position + 1).y);
                lowPath.cubicTo(0, mLControlPoints.get(position * 2 - 1).y,
                        0, mLControlPoints.get(position * 2).y,
                        mLTempPoints.get(position + 1).x, mLTempPoints.get(position + 1).y);

                highPath.cubicTo(curveViewItemWidth, mHControlPoints.get(position * 2 + 1).y,
                        curveViewItemWidth, mHControlPoints.get(position * 2 + 2).y,
                        mHTempPoints.get(position + 2).x + curveViewItemWidth, mHTempPoints.get(position + 2).y);
                lowPath.cubicTo(curveViewItemWidth, mLControlPoints.get(position * 2 + 1).y,
                        curveViewItemWidth, mLControlPoints.get(position * 2 + 2).y,
                        mLTempPoints.get(position + 2).x + curveViewItemWidth, mLTempPoints.get(position + 2).y);
            } else if (position == mHTempPoints.size() - 3 &&
                    (mHTempPoints.size() > position + 2 && mLTempPoints.size() > position + 2) &&
                    (mHControlPoints.size() > position * 2 + 1 && mLControlPoints.size() > position * 2 + 1)) {
                highPath.moveTo(mHTempPoints.get(position).x - curveViewItemWidth, mHTempPoints.get(position).y);
                lowPath.moveTo(mLTempPoints.get(position).x - curveViewItemWidth, mLTempPoints.get(position).y);

                highPath.cubicTo(0, mHControlPoints.get(position * 2 - 1).y,
                        0, mHControlPoints.get(position * 2).y,
                        mHTempPoints.get(position + 1).x, mHTempPoints.get(position + 1).y);
                lowPath.cubicTo(0, mLControlPoints.get(position * 2 - 1).y,
                        0, mLControlPoints.get(position * 2).y,
                        mLTempPoints.get(position + 1).x, mLTempPoints.get(position + 1).y);

                highPath.quadTo(curveViewItemWidth, mHControlPoints.get(position * 2 + 1).y,
                        mHTempPoints.get(position + 2).x + curveViewItemWidth, mHTempPoints.get(position + 2).y);
                lowPath.quadTo(curveViewItemWidth, mLControlPoints.get(position * 2 + 1).y,
                        mLTempPoints.get(position + 2).x + curveViewItemWidth, mLTempPoints.get(position + 2).y);
            }
            if (day.isToday()) {
                dotOutRadius = CommonUtils.dip2px(mContext, 4.5f);
                dotInRadius = CommonUtils.dip2px(mContext, 3f);
            }
            holder.curveView.setSizeAndPosition(dailyView, day);
            holder.curveView.refreshView();
            if (day.isToday()) {
                initDimens();
            }
        }


        @Override
        public int getItemCount() {
            return mDays.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            RelativeLayout mItemView;
            DottedLineView mDotLine;
            TextView mWeekText;
            TextView mDateText;
            ImageView mIconImg;
            NewDailyLineCurveView curveView;
        }
    }

    private class DailyVerticalAdapter extends RecyclerView.Adapter<NewDailyView.DailyVerticalAdapter.ViewHolder> {

        public static final int TYPE_HEADER = 0;  //header
        public static final int TYPE_NORMAL = 2;  //normal item

        private View headerView;

        private LayoutInflater mInflater;
        private List<DayForecast> mDays;

        public DailyVerticalAdapter(Context context, List<DayForecast> days) {
            mInflater = LayoutInflater.from(context);
            if (isRightRead) {
                mDays = new ArrayList<>();
                mDays.addAll(days);
                Collections.reverse(mDays);
            } else {
                this.mDays = days;
            }
        }

        public View getHeaderView() {
            return headerView;
        }

        public void setHeaderView(View headerView) {
            this.headerView = headerView;
            notifyItemInserted(0);
        }

        @Override
        public int getItemViewType(int position) {
            if (headerView == null) {
                return TYPE_NORMAL;
            }
            if (position == 0) {
                return TYPE_HEADER;
            }
            return TYPE_NORMAL;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (headerView != null && viewType == TYPE_HEADER) {
                return new ViewHolder(headerView);
            }

            View view = mInflater.inflate(R.layout.item_daily_detail_vertical, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.mWeekText = (TextView) view.findViewById(R.id.text_daily_detail_week_new);
            holder.mDateText = (TextView) view.findViewById(R.id.text_daily_detail_date_new);
            holder.mTodayText = (TextView) view.findViewById(R.id.text_daily_detail_today_new);
            holder.mIconImg = (ImageView) view.findViewById(R.id.img_daily_detail_icon_new);
            holder.mConditionText = (TextView) view.findViewById(R.id.text_daily_detail_condition_new);
            holder.mTempHighLowText = (TextView) view.findViewById(R.id.text_daily_detail_high_low_temp);

            holder.mWindText = (TextView) view.findViewById(R.id.text_daily_detail_value_wind);
            holder.mUvText = (TextView) view.findViewById(R.id.text_daily_detail_value_uv);
            holder.mSunriseText = (TextView) view.findViewById(R.id.text_daily_detail_value_sunrise);
            holder.mSunsetText = (TextView) view.findViewById(R.id.text_daily_detail_value_sunset);
            holder.mMoonriseText = (TextView) view.findViewById(R.id.text_daily_detail_value_moonrise);
            holder.mMoonsetText = (TextView) view.findViewById(R.id.text_daily_detail_value_moonset);
            holder.mMoonRightEmpty = (TextView) view.findViewById(R.id.empty_width);
            holder.mRealOrProbValueText = (TextView) view.findViewById(R.id.text_daily_detail_value_realfeel_or_prob);
            holder.mRealOrProbDescText = (TextView) view.findViewById(R.id.text_daily_detail_desc_realfeel_or_prob);
            holder.mRainAmountText = (TextView) view.findViewById(R.id.text_daily_detail_value_rain_amount);

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_NORMAL) {
                DayForecast day = mDays.get(headerView == null ? position : position - 1);
                holder.mWeekText.setText(day.getWeekName());
                holder.mDateText.setText(day.getDateName());
                if (day.isYesterday() || day.isToday()) {
                    holder.mTodayText.setVisibility(View.VISIBLE);
                    if (day.isYesterday()) {
                        holder.mTodayText.setText(getResources().getString(R.string.yesterday).toUpperCase());
                    } else if (day.isToday()) {
                        holder.mTodayText.setText(getResources().getString(R.string.today).toUpperCase());
                    }
                } else {
                    holder.mTodayText.setVisibility(View.GONE);
                }

//                holder.mIconImg.setImageBitmap(day.getIconBitmap());
                holder.mIconImg.setImageResource(day.getIconResId());
//                if (isIconColorCanChange && !Preferences.isNewIcon(mContext)) {
//                    holder.mIconImg.setColorFilter(getResources().getColor(R.color.black_60));
//                }

                holder.mConditionText.setText(day.getCondition());

                //high low temp
                holder.mTempHighLowText.setText(day.getHighLowTemp());
                //wind
                String rawWind = WeatherDataFormatUtils.getWindDirection(mContext, day.getWindDirection()) + day.getWindSpeed() + day.getWindSpeedUnit();
                int index = rawWind.indexOf(day.getWindSpeedUnit());
                Spannable windSpannable = new SpannableString(rawWind);
                windSpannable.setSpan(new AbsoluteSizeSpan((int) (holder.mWindText.getTextSize() * 5 / 8)), index, windSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mWindText.setText(windSpannable);
                //uv
                holder.mUvText.setText(day.getUvMax());
                //sunrise
                holder.mSunriseText.setText(day.getSunrise());
                //sunrset
                holder.mSunsetText.setText(day.getSunset());
                //moonrise mooset
                if (day.isMoonRiseExist() || day.isMoonSetExist()) {
                    ((View) holder.mMoonriseText.getParent()).setVisibility(VISIBLE);
                    holder.mMoonRightEmpty.setVisibility(VISIBLE);
                    holder.mMoonriseText.setText(day.getMoonrise());
                    holder.mMoonsetText.setText(day.getMoonset());
                } else {
                    ((View) holder.mMoonriseText.getParent()).setVisibility(GONE);
                    holder.mMoonRightEmpty.setVisibility(GONE);
                }
                //rain prob  or  real feel high low temp
                if (day.getRealFeelHigh().equals("-999")) {
                    //use rain pro
                    Spannable rainProSpannable = new SpannableString(day.getRainProb() + "%");
                    rainProSpannable.setSpan(new AbsoluteSizeSpan((int) (holder.mRealOrProbValueText.getTextSize() * 5 / 8)), rainProSpannable.length() - 1, rainProSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.mRealOrProbValueText.setText(rainProSpannable);
                    holder.mRealOrProbDescText.setText("降雨概率");
                } else {
                    //use high low temp
                    holder.mRealOrProbValueText.setText(day.getRealFeelHigh() + AmberSdkConstants.TEMP_UNIT + "/" + day.getRealFeelLow() + AmberSdkConstants.TEMP_UNIT);
                    holder.mRealOrProbDescText.setText("体感温度");
                }
                //rain amount
                Spannable rainAmountSpannable = new SpannableString(day.getRainAmount() + "mm");
                rainAmountSpannable.setSpan(new AbsoluteSizeSpan((int) (holder.mRainAmountText.getTextSize() * 5 / 8)), rainAmountSpannable.length() - 2, rainAmountSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mRainAmountText.setText(rainAmountSpannable);

            }
        }

        @Override
        public int getItemCount() {
            if (headerView == null) {
                return mDays.size();
            } else {
                return mDays.size() + 1;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View arg0) {
                super(arg0);
            }

            TextView mWeekText;
            TextView mDateText;
            TextView mTodayText;
            ImageView mIconImg;
            TextView mConditionText;
            TextView mTempHighLowText;

            TextView mWindText;
            TextView mUvText;
            TextView mSunriseText;
            TextView mSunsetText;
            TextView mMoonriseText;
            TextView mMoonsetText;
            TextView mMoonRightEmpty;
            TextView mRealOrProbValueText;
            TextView mRealOrProbDescText;
            TextView mRainAmountText;

        }
    }


    private void initTempScale(List<DayForecast> list) {
        if (list.size() > 0) {
            highestTemp = list.get(0).getHighTemp();
            int lowestTemp = highestTemp;
            for (DayForecast forecast : list) {
                int high = forecast.getHighTemp();
                int low = forecast.getLowTemp();
                if (high > highestTemp) {
                    highestTemp = high;
                }
                if (low < lowestTemp) {
                    lowestTemp = low;
                }
            }
            tempDiff = highestTemp == lowestTemp ? 1 : highestTemp - lowestTemp;
        }
    }


    private void initControlPoint(List<Point> mHTempPoints, List<Point> mLTempPoints,
                                  List<Point> mHMidPoints, List<Point> mLMidPoints,
                                  List<Point> mHMidMidPoints, List<Point> mLMidMidPoints) {
        mHControlPoints.clear();
        mLControlPoints.clear();
        for (int i = 0; i < mHTempPoints.size(); i++) {
            if (i != 0 && i != mHTempPoints.size() - 1) {
                Point beforeH = new Point();
                Point beforeL = new Point();
                Point afterH = new Point();
                Point afterL = new Point();
                beforeH.x = mHTempPoints.get(i).x - mHMidMidPoints.get(i - 1).x + mHMidPoints.get(i - 1).x;
                beforeH.y = mHTempPoints.get(i).y - mHMidMidPoints.get(i - 1).y + mHMidPoints.get(i - 1).y;
                beforeL.x = mLTempPoints.get(i).x - mLMidMidPoints.get(i - 1).x + mLMidPoints.get(i - 1).x;
                beforeL.y = mLTempPoints.get(i).y - mLMidMidPoints.get(i - 1).y + mLMidPoints.get(i - 1).y;
                afterH.x = mHTempPoints.get(i).x - mHMidMidPoints.get(i - 1).x + mHMidPoints.get(i).x;
                afterH.y = mHTempPoints.get(i).y - mHMidMidPoints.get(i - 1).y + mHMidPoints.get(i).y;
                afterL.x = mLTempPoints.get(i).x - mLMidMidPoints.get(i - 1).x + mLMidPoints.get(i).x;
                afterL.y = mLTempPoints.get(i).y - mLMidMidPoints.get(i - 1).y + mLMidPoints.get(i).y;
                mHControlPoints.add(beforeH);
                mLControlPoints.add(beforeL);
                mHControlPoints.add(afterH);
                mLControlPoints.add(afterL);
            }
        }
    }

    private void initMidMidPoint(List<Point> mHMidPoints, List<Point> mLMidPoints) {
        mHMidMidPoints.clear();
        mLMidMidPoints.clear();
        for (int i = 0; i < mHMidPoints.size(); i++) {
            if (i == mHMidPoints.size() - 1) {
                return;
            } else {
                Point midMidPoint = new Point((mHMidPoints.get(i).x + mHMidPoints.get(i + 1).x) / 2,
                        (mHMidPoints.get(i).y + mHMidPoints.get(i + 1).y) / 2);
                mHMidMidPoints.add(midMidPoint);
                midMidPoint = new Point((mLMidPoints.get(i).x + mLMidPoints.get(i + 1).x) / 2,
                        (mLMidPoints.get(i).y + mLMidPoints.get(i + 1).y) / 2);
                mLMidMidPoints.add(midMidPoint);
            }
        }
    }

    private void initMidPoints(List<Point> mHTempPoints, List<Point> mLTempPoints) {
        mHMidPoints.clear();
        mLMidPoints.clear();
        for (int i = 0; i < mHTempPoints.size(); i++) {
            if (i == mHTempPoints.size() - 1) {
                return;
            } else {
                Point midPoint = new Point((mHTempPoints.get(i).x + mHTempPoints.get(i + 1).x) / 2,
                        (mHTempPoints.get(i).y + mHTempPoints.get(i + 1).y) / 2);
                mHMidPoints.add(midPoint);
                midPoint = new Point((mLTempPoints.get(i).x + mLTempPoints.get(i + 1).x) / 2,
                        (mLTempPoints.get(i).y + mLTempPoints.get(i + 1).y) / 2);
                mLMidPoints.add(midPoint);
            }
        }
    }

    public int dp2px(float dp) {
        return CommonUtils.dip2px(mContext, dp);
    }

    public int getCurveViewItemWidth() {
        return curveViewItemWidth;
    }

    public int getCurveViewHeight() {
        return curveViewHeight;
    }

    public int getCurveViewTopPadding() {
        return curveViewTopPadding;
    }

    public int getCurveViewBottomPadding() {
        return curveViewBottomPadding;
    }

    public int getHighTempHeight() {
        return highTempHeight;
    }

    public int getHighTempBottomPadding() {
        return highTempBottomPadding;
    }

    public int getLowTempHeight() {
        return lowTempHeight;
    }

    public int getLowTempTopPadding() {
        return lowTempTopPadding;
    }

    public int getDotOutRadius() {
        return dotOutRadius;
    }

    public int getDotInRadius() {
        return dotInRadius;
    }

    public int getLineStrokeWidth() {
        return lineStrokeWidth;
    }
}
