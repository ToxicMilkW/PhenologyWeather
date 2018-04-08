package com.iap.phenologyweather.view.hiistory;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.MonthForecast;
import com.iap.phenologyweather.data.loader.LocationInfoLoader;
import com.iap.phenologyweather.data.model.HistoryMonth;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.network.NetClient;
import com.iap.phenologyweather.network.netresult.HistoryMonthResult;
import com.iap.phenologyweather.utils.Preferences;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public class HistoryMonthView extends LinearLayout {

    private Context context;
    private Location currLocation;
    private HistoryMonthCanvasView monthCanvasView;
    private TextView mDescText;

    public HistoryMonthView(Context context) {
        super(context);
        init(context);
    }

    public HistoryMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HistoryMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View.inflate(context, R.layout.history_month_view, this);
        monthCanvasView = (HistoryMonthCanvasView) findViewById(R.id.history_month_canvas_view);
        mDescText = (TextView) findViewById(R.id.text_month_history_desc);
        int weatherDataId = Preferences.readCurrWeatherDataId(context);
        currLocation = LocationInfoLoader.getInstance(context).queryLocationById(weatherDataId);
        readyToShow();
    }

    private void readyToShow() {
        //from db first
        setVisibility(GONE);
        requestHistory();
    }

    private void requestHistory() {
        NetClient.getHistoryMonthRetrofitInstance().requestHistoryMonth(currLocation.getLat(), currLocation.getLon())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HistoryMonthResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HistoryMonthResult result) {
                        if ("ok".equals(result.getStatus())) {
                            HistoryMonth month = result.getData();
                            XLog.d(month);
                            handleData(month);
                            setVisibility(VISIBLE);
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

    private void handleData(HistoryMonth month) {
        int lowTemp = 999, highTemp = -999;
        float maxRainfall = -1;
        int maxRainDay = -1, minRainDay = 9999;
        List<MonthForecast> monthForecasts = MonthForecast.parseMonthForecastData(context, month);
        for (MonthForecast mf : monthForecasts) {
            int h = mf.getHighTemp(), l = mf.getLowTemp();
            float maxRf = mf.getPrecip();
            int rd = mf.getRainDay();
            if (h > highTemp) {
                highTemp = h;
            }
            if (l < lowTemp) {
                lowTemp = l;
            }
            if (maxRf > maxRainfall) {
                maxRainfall = maxRf;
            }
            if (rd > maxRainDay) {
                maxRainDay = rd;
            }
            if (rd < minRainDay) {
                minRainDay = rd;
            }
        }
        monthCanvasView.fillData(monthForecasts, highTemp, lowTemp, maxRainDay, minRainDay, maxRainfall);
        mDescText.setText(MonthForecast.getIntroductionText(monthForecasts, currLocation.getLat(), context));
    }
}
