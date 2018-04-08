package com.iap.phenologyweather.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.model.ChinaCity;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.provider.WeatherDatabaseManager;
import com.iap.phenologyweather.utils.Preferences;
import com.iap.phenologyweather.view.custom.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxueqing on 2017/3/2.
 */

public class CityPickerDialog extends Dialog {

    private Context context;
    private WheelView provincePicker;
    private WheelView cityPicker;
    private TextView cancelText;
    private TextView okText;
    private View.OnClickListener listener;
    private WeatherDatabaseManager databaseManager;
    private ChinaCity selectedCity;
    private List<ChinaCity> provinces;
    private List<ChinaCity> cities;

    public CityPickerDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        databaseManager = WeatherDatabaseManager.getInstance(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_city_picker);

        initViews();

        initEvent();

        initData();
    }

    private void initData() {
        provinces = databaseManager.queryChinaCitiesByPID(0);
        List<String> provinceStrs = new ArrayList<>();
        for (ChinaCity chinaCity : provinces) {
            provinceStrs.add(chinaCity.getName());
        }
        provincePicker.setData(provinceStrs);
        provincePicker.setDefault(0);
    }

    private void initEvent() {
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        provincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int index, String text) {
                refreshCityPicker(provinces.get(index).getId());
            }

            @Override
            public void selecting(int index, String text) {

            }
        });

        cityPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int index, String text) {
                selectedCity = cities.get(index);
            }

            @Override
            public void selecting(int index, String text) {

            }
        });

        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCity == null) {
                    Log.d("cxq", "selectedCity == null");
                    dismiss();
                    return;
                }
                Location location = new Location();
                location.setLevel1(provincePicker.getSelectedText());
                location.setLevel2(selectedCity.getName());
                location.setLat(selectedCity.getLat());
                location.setLon(selectedCity.getLng());
                int id = databaseManager.insertLocation(location);
                Preferences.saveCurrWeatherDataId(context, id);
                if (listener != null) {
                    listener.onClick(v);
                }
                dismiss();
            }
        });
    }

    private void refreshCityPicker(int id) {

        cities = databaseManager.queryChinaCitiesByPID(id);
        List<String> cityStrs = new ArrayList<>();
        for (ChinaCity chinaCity : cities) {
            cityStrs.add(chinaCity.getName());
        }
        Log.d("cxq", "size:" + cities.size());
        cityPicker.setData(cityStrs);
        cityPicker.setDefault(0);
    }

    private void initViews() {
        provincePicker = (WheelView) findViewById(R.id.dialog_picker_1);
        cityPicker = (WheelView) findViewById(R.id.dialog_picker_2);
        cancelText = (TextView) findViewById(R.id.text_picker_cancel);
        okText = (TextView) findViewById(R.id.text_picker_ok);
    }

    public void setOnOkClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

}
