package com.iap.phenologyweather.view.hiistory;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.loader.LocationInfoLoader;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.network.NetClient;
import com.iap.phenologyweather.network.netresult.HistoryDayResult;
import com.iap.phenologyweather.utils.Constants;
import com.iap.phenologyweather.utils.Preferences;
import com.iap.phenologyweather.view.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public class HistoryDayView extends LinearLayout {

    private Context context;

    private TextView mDateText, mHighTempText, mLowTempText, mRainfallText, mPrecipText;
    private LinearLayout dateLayout;
    private Location currLocation;
    private SimpleDateFormat requestDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
    private SimpleDateFormat showDateFormat = new SimpleDateFormat("MM月dd日", Locale.US);
    private String currRequestDate;
    private DatePickerDialog dialog;
    private ProgressBar progressBar;
    private View valueLayout;

    public HistoryDayView(Context context) {
        super(context);
        init(context);
    }

    public HistoryDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HistoryDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View.inflate(context, R.layout.history_day_view, this);
        initViews();
        initEvent();
        initLocation();
        currRequestDate = requestDateFormat.format(System.currentTimeMillis());
        requestDayHistory();
    }

    private void initLocation() {
        int weatherDataId = Preferences.readCurrWeatherDataId(context);
        currLocation = LocationInfoLoader.getInstance(context).queryLocationById(weatherDataId);
    }

    private void requestDayHistory() {
        showDate();
        valueLayout.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        NetClient.getHistoryDayRetrofitInstance().requestHistoryDay(currLocation.getLat(), currLocation.getLon(), currRequestDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HistoryDayResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HistoryDayResult result) {
                        if ("ok".equals(result.getStatus())) {
                            HistoryDayResult.HistoryDay day = result.getData();
                            XLog.d(day.toString());
                            updateUI(day);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateUI(HistoryDayResult.HistoryDay day) {
        valueLayout.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        if (day == null) {
            return;
        }
        String highTempValue, lowTempValue, rainfallValue, precipValue;
        try {
            highTempValue = String.valueOf(Math.round(Double.parseDouble(day.getTmax())));
        } catch (Exception e) {
            e.printStackTrace();
            highTempValue = "--";
        }
        try {
            lowTempValue = String.valueOf(Math.round(Double.parseDouble(day.getTmin())));
        } catch (Exception e) {
            e.printStackTrace();
            lowTempValue = "--";
        }
        try {
            rainfallValue = String.valueOf((double) Math.round(Double.parseDouble(day.getPrcp()) * 10) / 10);
        } catch (Exception e) {
            e.printStackTrace();
            rainfallValue = "--";
        }
        try {
            precipValue = String.valueOf(Math.round(Double.parseDouble(day.getPrcp_posibility()) * 100));
        } catch (Exception e) {
            e.printStackTrace();
            precipValue = "--";
        }
        mHighTempText.setText("历史平均最高气温:" + highTempValue + Constants.DU);
        mLowTempText.setText("历史平均最低气温:" + lowTempValue + Constants.DU);
        mRainfallText.setText("历史平均降雨量:" + rainfallValue + "mm");
        mPrecipText.setText("历史降水概率:" + precipValue + "%");
    }

    private void initEvent() {
        dateLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick date
                if (dialog == null) {
                    dialog = new DatePickerDialog(context);
                    dialog.setOnSelectListener(new DatePickerDialog.OnSelectListener() {
                        @Override
                        public void onDateSelected(String date) {
                            currRequestDate = date;
                            requestDayHistory();
                        }
                    });
                }
                dialog.show();
            }
        });
    }

    private void showDate() {
        try {
            Date date = requestDateFormat.parse(currRequestDate);
            String dateText = showDateFormat.format(date);
            mDateText.setText(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        mDateText = (TextView) findViewById(R.id.text_day_history_date);
        mHighTempText = (TextView) findViewById(R.id.text_day_history_high_temp);
        mLowTempText = (TextView) findViewById(R.id.text_day_history_low_temp);
        mRainfallText = (TextView) findViewById(R.id.text_day_history_rainfall);
        mPrecipText = (TextView) findViewById(R.id.text_day_history_precip);
        dateLayout = (LinearLayout) findViewById(R.id.ll_day_history_date);
        valueLayout = findViewById(R.id.ll_day_history_value);
        valueLayout.setVisibility(GONE);
        progressBar = (ProgressBar) findViewById(R.id.pb_day_history);
        progressBar.setVisibility(VISIBLE);
    }
}
