package com.iap.phenologyweather.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.iap.phenologyweather.R;
import com.iap.phenologyweather.view.custom.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by chenxueqing on 2017/4/21.
 */

public class DatePickerDialog extends Dialog {

    private Context context;
    private WheelView monthPicker;
    private WheelView dayPicker;
    private TextView cancelText;
    private TextView okText;
    private TextView titleText;
    private List<String> months = new ArrayList<>();
    private List<String> days = new ArrayList<>();
    private OnSelectListener listener;
    private Calendar c = Calendar.getInstance();
    private boolean hasShown = false;

    public DatePickerDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
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
        for (int i = 1; i <= 12; i++) {
            months.add(i + "月");
        }
        monthPicker.setData(months);
        monthPicker.setDefault(c.get(Calendar.MONTH));
    }

    private void initEvent() {
        monthPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int index, String text) {
                refreshDayPicker(index);
            }

            @Override
            public void selecting(int index, String text) {

            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int monthNum = monthPicker.getSelectedIndex() + 1;
                int dayNum = dayPicker.getSelectedIndex() + 1;
                String result = String.format("2016%02d%02d", monthNum, dayNum);
                XLog.d(result);
                if (listener != null) {
                    listener.onDateSelected(result);
                }
                dismiss();
            }
        });
    }

    private void refreshDayPicker(int monthIndex) {
        c.clear();
        c.set(Calendar.YEAR, 2016);
        c.set(Calendar.MONTH, monthIndex);
        int dayCount = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        days.clear();
        for (int i = 1; i <= dayCount; i++) {
            days.add(i + "日");
        }
        dayPicker.setData(days);
        if (!hasShown) {
            Calendar c1 = Calendar.getInstance();
            dayPicker.setDefault(c1.get(Calendar.DAY_OF_MONTH) - 1);
            hasShown = true;
        } else {
            dayPicker.setDefault(0);
        }
    }

    private void initViews() {
        titleText = (TextView) findViewById(R.id.dialog_picker_title);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("选择日期");
        monthPicker = (WheelView) findViewById(R.id.dialog_picker_1);
        dayPicker = (WheelView) findViewById(R.id.dialog_picker_2);
        cancelText = (TextView) findViewById(R.id.text_picker_cancel);
        okText = (TextView) findViewById(R.id.text_picker_ok);
    }

    public void setOnSelectListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public interface OnSelectListener {
        void onDateSelected(String date);
    }
}
