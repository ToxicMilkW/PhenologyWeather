package com.iap.phenologyweather.view.hourly;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.HourForecast;
import com.iap.phenologyweather.data.loader.WeatherInfoLoader;
import com.iap.phenologyweather.data.model.ConfigData;
import com.iap.phenologyweather.utils.CommonUtils;
import com.iap.phenologyweather.utils.IconLoader;
import com.iap.phenologyweather.utils.Preferences;
import com.iap.phenologyweather.view.custom.MyHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxueqing on 2016/9/1.
 */

public class NewHourlyView extends LinearLayout {

    private Context mContext;
    private LinearLayout mHourlyIconLl;
    private NewHourlyLineView mHourLineView;
    private MyHorizontalScrollView mScrollView;
    private View mHeaderView;
    private RecyclerView mDetailRecycler;
    private ViewTreeObserver.OnGlobalLayoutListener mLastIconListener;
    private boolean isLastIconLayout = false;

    private boolean isRightRead = false;

    private List<HourForecast> mHourList = new ArrayList<>();
    private List<ImageView> mIconList = new ArrayList<>();

    //****dimens****
    private int overviewItemWidth;
    private int detailItemWidth;
    private int contentHeight;
    //overview
    private int tempDiff = 1;
    private int highestTemp;
    private int overPaddingTop;
    private int overPaddingBottom;
    private int overTempHeight;
    private int overTempPaddingBottom;
    private int dotOutRadius, dotInRadius;
    private int tempLineStrokeWidth;
    private int iconSize;
    private int iconFrameHeight;
    private int bottomLineStrokeWidth;
    private int verticalLineStrokeWidth;
    private int timeTopPadding;
    private int timeHeight;
    private HourlyAdapter adapter;
    //****dimens****


    public NewHourlyView(Context context) {
        super(context);
        this.mContext = context;
        initDimens();
        initViews();
    }

    private void initDimens() {
        overviewItemWidth = dp2px(60);
        detailItemWidth = dp2px(56);
        contentHeight = dp2px(115);

        overPaddingTop = dp2px(4);
        overPaddingBottom = dp2px(4);
        overTempHeight = dp2px(10.4f);
        overTempPaddingBottom = dp2px(8);
        dotOutRadius = dp2px(2f);
        dotInRadius = dp2px(1f);
        tempLineStrokeWidth = dp2px(0.7f);
        iconSize = dp2px(28);
        iconFrameHeight = dp2px(50);
        bottomLineStrokeWidth = dp2px(0.8f);
        verticalLineStrokeWidth = dp2px(0.3f);
        timeTopPadding = dp2px(6);
        timeHeight = dp2px(9f);

    }

    private void initHeaderEvent() {
        mScrollView.setOnScrollListener(new MyHorizontalScrollView.OnScrollListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                updateIconPosition(x);
            }
        });

        mLastIconListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isLastIconLayout) {
                    return;
                }
                isLastIconLayout = true;
                if (isRightRead) {
                    mScrollView.scrollTo(mHourLineView.getWidth(), 0);
                    updateIconPosition(mScrollView.getScrollX());
                } else {
                    mScrollView.scrollTo(0, 0);
                    updateIconPosition(0);
                }
                if (Preferences.isHourlySlideGuideShow(mContext)) {
                    int end = dp2px(100);
                    ValueAnimator animator;
                    if (isRightRead) {
                        animator = ValueAnimator.ofInt(mScrollView.getScrollX(), mScrollView.getScrollX() - end, mScrollView.getScrollX());
                    } else {
                        animator = ValueAnimator.ofInt(0, end, 0);
                    }
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mScrollView.scrollTo((Integer) animation.getAnimatedValue(), 0);
                        }
                    });
                    animator.setDuration(2500);
                    animator.start();
                    Preferences.setHourlySlideGuideShow(mContext, false);
                }
            }
        };
    }

    private void updateIconPosition(int scrollViewX) {
        int scrollWidth = mScrollView.getWidth();
        for (ImageView icon : mIconList) {
            LinearLayout linear = (LinearLayout) icon.getParent();
            int diffLeft = linear.getLeft() - scrollViewX;
            int diffRight = scrollViewX + scrollWidth - linear.getRight();
            if (diffLeft < 0 && diffRight > 0) {
                int newItemWidth = linear.getRight() - scrollViewX;
                if (newItemWidth > icon.getWidth()) {
                    float translationX = icon.getLeft() - (newItemWidth - icon.getWidth()) / 2f;
                    icon.setTranslationX(translationX);
                } else {
                    float translationX = (linear.getWidth() - icon.getWidth()) / 2f;
                    icon.setTranslationX(translationX);
                }
            } else if (diffLeft > 0 && diffRight < 0) {
                int newItemWidth = scrollViewX + scrollWidth - linear.getLeft();
                if (newItemWidth > icon.getWidth()) {
                    float translationX = (newItemWidth - icon.getWidth()) / 2f - icon.getLeft();
                    icon.setTranslationX(translationX);
                } else {
                    float translationX = (icon.getWidth() - linear.getWidth()) / 2f;
                    icon.setTranslationX(translationX);
                }
            } else if (diffLeft < 0 && diffRight < 0) {
                float translationX = (scrollWidth - icon.getWidth()) / 2f + scrollViewX - linear.getLeft() - icon.getLeft();
                icon.setTranslationX(translationX);
            } else if (diffLeft > 0 && diffRight > 0) {
                icon.setTranslationX(0);
            }
        }
    }


    private void initViews() {
        View.inflate(mContext, R.layout.card_tab_hourly, this);
        mDetailRecycler = (RecyclerView) findViewById(R.id.rv_hourly);
        mDetailRecycler.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDetailRecycler.setLayoutManager(layoutManager);
        initHeaderView();
        setVisibility(GONE);
    }

    private void initHeaderView() {
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.card_tab_hourly_header, mDetailRecycler, false);
        mScrollView = (MyHorizontalScrollView) mHeaderView.findViewById(R.id.hsv_hourly);
        mHourlyIconLl = (LinearLayout) mHeaderView.findViewById(R.id.ll_hourly_icon);
        mHourLineView = (NewHourlyLineView) mHeaderView.findViewById(R.id.hourly_line_view);
        initHeaderEvent();
    }

    public void fillData(int weatherDataId, WeatherInfoLoader weatherInfoLoader) {
        mHourList.clear();
        HourForecast.fillData(mContext, weatherInfoLoader, weatherDataId, mHourList);
        if (mHourList.isEmpty()) {
            return;
        }
        fillDetail();
        setVisibility(VISIBLE);
    }

    private void fillOverView() {
        if (mHourList.isEmpty()) {
            return;
        }
        //overview
        initTempScale(mHourList);
        for (HourForecast hour : mHourList) {
            int tempLineHeight = contentHeight - overPaddingTop - overPaddingBottom - overTempHeight - overTempPaddingBottom
                    - iconFrameHeight - bottomLineStrokeWidth - timeHeight - timeTopPadding;
            //temp point
            int tempY = overPaddingTop + overTempHeight + overTempPaddingBottom
                    + tempLineHeight * (highestTemp - hour.getIntTemp()) / tempDiff;
            hour.setY(tempY);
        }
        mHourLineView.fillData(this, mHourList);
        mHourlyIconLl.setLayoutParams(mHourLineView.getLayoutParams());
        mHourlyIconLl.setPadding(overviewItemWidth / 2, contentHeight - overPaddingBottom - timeHeight
                        - timeTopPadding - bottomLineStrokeWidth - iconFrameHeight,
                overviewItemWidth / 2, overPaddingBottom + timeHeight + timeTopPadding + bottomLineStrokeWidth);
        mHourlyIconLl.removeAllViews();
        mIconList.clear();
        isLastIconLayout = false;
        int lastStartIndex = 0;
        for (int i = 0; i < mHourList.size(); i++) {
            if (i == 0 || (i != mHourList.size() - 1
                    && mHourList.get(i).getIconResId() == mHourList.get(i - 1).getIconResId())) {
                continue;
            }
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((i - lastStartIndex) * overviewItemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(params);

            ImageView mIconImg = new ImageView(mContext);
            mIconImg.setImageResource(mHourList.get(i - 1).getIconResId());

            linearLayout.addView(mIconImg);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(iconSize, iconSize);
            mIconImg.setLayoutParams(iconParams);

            if (isRightRead) {
                mHourlyIconLl.addView(linearLayout, 0);
            } else {
                mHourlyIconLl.addView(linearLayout);
            }
            mIconList.add(mIconImg);
            lastStartIndex = i;
            if (i == mHourList.size() - 1) {
                mIconImg.getViewTreeObserver().addOnGlobalLayoutListener(mLastIconListener);
            }
        }
    }

    private void initTempScale(List<HourForecast> hourList) {
        if (!hourList.isEmpty()) {
            int highestIndex = 0;
            int lowestIndex = 0;
            int high;
            high = hourList.get(0).getIntTemp();
            int low = high;
            for (int i = 0; i < hourList.size(); i++) {
                HourForecast hour = hourList.get(i);
                int temp = hour.getIntTemp();
                if (temp > high) {
                    high = temp;
                    highestIndex = i;
                }
                if (temp < low) {
                    low = temp;
                    lowestIndex = i;
                }
            }
            highestTemp = high;
            tempDiff = high == low ? 1 : high - low;
            if (highestIndex != lowestIndex) {
                hourList.get(highestIndex).setHighest(true);
                hourList.get(lowestIndex).setLowest(true);
            }
        }
    }

    private void fillDetail() {
        if (mHourList.isEmpty()) {
            return;
        }
        //detail
        if (adapter == null) {
            adapter = new HourlyAdapter(mContext, mHourList);
            adapter.setHeaderView(mHeaderView);
            mDetailRecycler.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        fillOverView();

    }

    private class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

        public static final int TYPE_HEADER = 0;  //header
        public static final int TYPE_NORMAL = 2;  //normal item

        private View mHeaderView;

        private LayoutInflater mInflater;
        private List<HourForecast> mHours;

        public HourlyAdapter(Context context, List<HourForecast> mHours) {
            mInflater = LayoutInflater.from(context);
            this.mHours = mHours;
        }

        public View getHeaderView() {
            return mHeaderView;
        }

        public void setHeaderView(View headerView) {
            mHeaderView = headerView;
            notifyItemInserted(0);
        }

        @Override
        public int getItemViewType(int position) {
            if (mHeaderView == null) {
                return TYPE_NORMAL;
            }
            if (position == 0) {
                return TYPE_HEADER;
            }
            return TYPE_NORMAL;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mHeaderView != null && viewType == TYPE_HEADER) {
                return new ViewHolder(mHeaderView);
            }

            View view = mInflater.inflate(R.layout.item_tab_hourly_detail, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.mItemView = (LinearLayout) view.findViewById(R.id.ll_hourly_item);
            holder.mTimeText = (TextView) view.findViewById(R.id.text_hourly_item_time);
            holder.mIconImg = (ImageView) view.findViewById(R.id.img_hourly_item_icon);
            holder.mTempText = (TextView) view.findViewById(R.id.text_hourly_item_temp);
            holder.mWindText = (TextView) view.findViewById(R.id.text_hourly_item_wind);
            holder.mWindDerectionText = (TextView) view.findViewById(R.id.text_hourly_item_wind_direction);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_NORMAL) {

                HourForecast hour = mHours.get(mHeaderView == null ? position : position - 1);
                if (hour.getNowId() == HourForecast.NOW_ID_IS_NOW) {
                    holder.mTimeText.setText(getResources().getString(R.string.now).toUpperCase());
                } else {
                    holder.mTimeText.setText(hour.getTime());
                }
                holder.mIconImg.setImageResource(hour.getIconResId());
//                holder.mIconImg.setImageDrawable(iconManager.getContext().getResources().getDrawable(hour.getIconResId()));
//                if (isIconColorCanChange && !Preferences.isNewIcon(mContext)) {
//                    holder.mIconImg.setColorFilter(getResources().getColor(R.color.main_card_black_60));
//                }
                holder.mTempText.setText(hour.getTemp().trim());
                holder.mWindDerectionText.setText(hour.getWindDirection());
                String rawWindStr = hour.getWindSpeed() + hour.getWindSpeedUnit();
                int index = rawWindStr.indexOf(hour.getWindSpeedUnit());
                Spannable spannable = new SpannableString(rawWindStr);
                spannable.setSpan(new AbsoluteSizeSpan((int) (holder.mWindText.getTextSize() * 5 / 8)), index, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mWindText.setText(spannable);
            }
        }

        @Override
        public int getItemCount() {
            if (mHeaderView == null) {
                return mHours.size();
            } else {
                return mHours.size() + 1;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View arg0) {
                super(arg0);
            }

            LinearLayout mItemView;
            TextView mTimeText;
            ImageView mIconImg;
            TextView mTempText;
            TextView mWindText;
            TextView mWindDerectionText;
        }
    }

    public int dp2px(float dp) {
        return CommonUtils.dip2px(mContext, dp);
    }

    public int getOverviewItemWidth() {
        return overviewItemWidth;
    }

    public int getDetailItemWidth() {
        return detailItemWidth;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    public int getOverPaddingTop() {
        return overPaddingTop;
    }

    public int getOverPaddingBottom() {
        return overPaddingBottom;
    }

    public int getOverTempHeight() {
        return overTempHeight;
    }

    public int getOverTempPaddingBottom() {
        return overTempPaddingBottom;
    }

    public int getDotOutRadius() {
        return dotOutRadius;
    }

    public int getDotInRadius() {
        return dotInRadius;
    }

    public int getTempLineStrokeWidth() {
        return tempLineStrokeWidth;
    }

    public int getIconFrameHeight() {
        return iconFrameHeight;
    }

    public int getBottomLineStrokeWidth() {
        return bottomLineStrokeWidth;
    }

    public int getVerticalLineStrokeWidth() {
        return verticalLineStrokeWidth;
    }

    public int getTimeTopPadding() {
        return timeTopPadding;
    }

    public int getTimeHeight() {
        return timeHeight;
    }
}
