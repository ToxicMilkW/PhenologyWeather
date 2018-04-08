package com.iap.phenologyweather.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.provider.WeatherDatabaseManager;
import com.iap.phenologyweather.utils.Constants;
import com.iap.phenologyweather.utils.Preferences;
import com.iap.phenologyweather.view.CityPickerDialog;

import java.util.ArrayList;
import java.util.List;

public class CityManageActivity extends AppCompatActivity {

    private Context context;

    private LinearLayout rootView;

    private ImageView backImg;
    private TextView mTitleText;
    private CheckBox mEditCheckBox;

    private RecyclerView mRecyclerView;
    private ImageView mAddCityBtn;

    private int currentLocationIndex = 1;

    private List<Location> locations = new ArrayList<>();
    private CityAdapter adapter;


    private CityPickerDialog cityPickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        context = this;

        currentLocationIndex = Preferences.readCurrWeatherDataId(context);

        initSystemBar();

        initViews();

        initEvent();

        initData();
    }


    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.action_bar_bg_blue));
            window.setNavigationBarColor(Color.BLACK);
        }
    }

    private void initEvent() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                changeAddLocation();
            }
        });

        mEditCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setText(getString(isChecked ?
                        R.string.city_manage_actionbar_cancel : R.string.city_manage_actionbar_edit));
                adapter.notifyDataSetChanged();
                backImg.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                mAddCityBtn.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });

        mAddCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityPickerDialog == null) {
                    cityPickerDialog = new CityPickerDialog(context);
                    cityPickerDialog.setOnOkClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDataChanged();
                            finish();
                        }
                    });
                }
                cityPickerDialog.show();
            }
        });

    }

    private void changeAddLocation() {
        if (mRecyclerView.getBottom() > rootView.getHeight() - mAddCityBtn.getHeight()) {
            mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rootView.getHeight() - mRecyclerView.getTop() - mAddCityBtn.getHeight()));
        } else {
            mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void initData() {
        locations.clear();
        locations.addAll(WeatherDatabaseManager.getInstance(context).queryAllLocations());
        adapter = new CityAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
    }

    private void initViews() {
        rootView = (LinearLayout) findViewById(R.id.ll_city_manage_root);
        findViewById(R.id.rl_action_bar).setBackgroundResource(R.color.action_bar_bg_blue);
        backImg = (ImageView) findViewById(R.id.img_actionbar_back);
        mTitleText = (TextView) findViewById(R.id.text_actionbar_title);
        mTitleText.setText("城市管理");
        mEditCheckBox = (CheckBox) findViewById(R.id.cb_city_manage_edit);
        mEditCheckBox.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_city_manage);
        mAddCityBtn = (ImageView) findViewById(R.id.btn_city_manage_add_city);
    }

    private class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

        private LayoutInflater inflater;

        public CityAdapter() {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_city_manage, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.rootView = (LinearLayout) view.findViewById(R.id.ll_city_item_root);
            holder.selectedImg = (ImageView) view.findViewById(R.id.img_city_item_selected);
            holder.cityNameText = (TextView) view.findViewById(R.id.text_city_item_name);
            holder.deleteImg = (ImageView) view.findViewById(R.id.img_city_item_delete);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Location location = locations.get(position);
            final int weatherDataId = location.getId().intValue();
            holder.cityNameText.setText(location.getLevel2());
            final boolean isAutoLocation = Constants.AUTO_ID == weatherDataId;
            if (!mEditCheckBox.isChecked()) {
                holder.selectedImg.setVisibility(currentLocationIndex == weatherDataId ? View.VISIBLE : View.INVISIBLE);
                if (isAutoLocation) {
                    holder.deleteImg.setVisibility(View.VISIBLE);
                    holder.deleteImg.setImageResource(R.drawable.ic_locate);
                } else {
                    holder.deleteImg.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.selectedImg.setVisibility(View.GONE);
                holder.deleteImg.setVisibility(View.VISIBLE);
                holder.deleteImg.setImageResource(isAutoLocation ? R.drawable.ic_locate : R.drawable.ic_delete_city);
                holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isAutoLocation) {
                            WeatherDatabaseManager manager = WeatherDatabaseManager.getInstance(context);
                            manager.deleteLocationByKey(weatherDataId);
                            manager.deleteWeatherRawInfoByCityId(weatherDataId);
                            manager.deleteMainPhenosByCityId(weatherDataId);
                            locations.remove(position);
                            adapter.notifyDataSetChanged();
                            if (currentLocationIndex == weatherDataId) {
                                currentLocationIndex = Constants.AUTO_ID;
                                Preferences.saveCurrWeatherDataId(context, currentLocationIndex);
                                setDataChanged();
                            }
                            changeAddLocation();
                        }
                    }
                });
            }
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mEditCheckBox.isChecked()) {
                        Preferences.saveCurrWeatherDataId(context, weatherDataId);
                        if (currentLocationIndex != weatherDataId) {
                            setDataChanged();
                        }
                        finish();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return locations == null ? 0 : locations.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }

            LinearLayout rootView;
            ImageView selectedImg;
            TextView cityNameText;
            ImageView deleteImg;
        }
    }


    private void setDataChanged() {
        setResult(RESULT_OK);
    }

}
