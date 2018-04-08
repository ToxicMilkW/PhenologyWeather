package com.iap.phenologyweather.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.fragment.BaseFragment;
import com.iap.phenologyweather.fragment.DailyFragment;
import com.iap.phenologyweather.fragment.HistoryFragment;
import com.iap.phenologyweather.fragment.HourlyFragment;
import com.iap.phenologyweather.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class WeatherDetailActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private TabLayout tabLayout;
    private List<BaseFragment> fragments = new ArrayList<>();

    private int weatherDataId = 1;
    private String cityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        weatherDataId = getIntent().getIntExtra(Constants.EXTRA_WEATHER_DATA_ID, 1);
        cityName = getIntent().getStringExtra(Constants.EXTRA_CITY_NAME);

        initSystemBar();

        initViews();

        initFragments();

    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.action_bar_bg_blue));
            window.setNavigationBarColor(Color.BLACK);
        }
    }

    private void initFragments() {
        fragments.add(new HourlyFragment());
        fragments.add(new DailyFragment());
        fragments.add(new HistoryFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewpager);
    }

    private void initViews() {
        RelativeLayout actionBarLayout = (RelativeLayout) findViewById(R.id.rl_action_bar);
        actionBarLayout.setBackgroundResource(R.color.action_bar_bg_blue);
        ImageView back = (ImageView) findViewById(R.id.img_actionbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.text_actionbar_title);
        title.setText(cityName);
        viewpager = (ViewPager) findViewById(R.id.weather_detail_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.weather_detail_tabLayout);

    }

    public int getWeatherDataId() {
        return weatherDataId;
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getPageTitle();
        }
    }
}
