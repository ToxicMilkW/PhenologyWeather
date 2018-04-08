package com.iap.phenologyweather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.iap.phenologyweather.R;
import com.iap.phenologyweather.data.loader.LocationInfoLoader;
import com.iap.phenologyweather.data.loader.WeatherInfoLoader;
import com.iap.phenologyweather.data.model.Location;
import com.iap.phenologyweather.data.model.MainPhenology;
import com.iap.phenologyweather.data.model.MainWeather;
import com.iap.phenologyweather.locate.AMapHelper;
import com.iap.phenologyweather.network.NetClient;
import com.iap.phenologyweather.network.netresult.MainPhenologyResult;
import com.iap.phenologyweather.provider.WeatherDatabaseManager;
import com.iap.phenologyweather.request.weather.CityParams;
import com.iap.phenologyweather.request.weather.WeatherDataFetcher;
import com.iap.phenologyweather.request.weather.WeatherDataFetcherInterface;
import com.iap.phenologyweather.utils.CardUtils;
import com.iap.phenologyweather.utils.Constants;
import com.iap.phenologyweather.utils.DCTUtils;
import com.iap.phenologyweather.utils.DimenUtils;
import com.iap.phenologyweather.utils.IconLoader;
import com.iap.phenologyweather.utils.Preferences;
import com.iap.phenologyweather.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MainActivity extends CheckPermissionsActivity implements AMapLocationListener {

    private Context context;

    private RelativeLayout actionBarRl;
    private TextView mCityNameText;
    private ImageView addCityImg;

    private RecyclerView mRecycler;

    private TextView mCurrTempText;
    private ImageView mIconImg;
    private TextView mCurrConditionText;
    private TextView mHighLowTempText;
    private TextView mFeelText;
    private LinearLayout mHourLayout;
    private LinearLayout mDayLayout;
    private LinearLayout mWeatherMoreLayout;
    private TextView mAqiText;
    private Typeface numberRegular;


    //aqi
    private int mAqi;

    private Handler mHandler = new Handler();
    private CityParams currCityParams = new CityParams();

    private ProgressDialog mProgressDialog;
    private AMapHelper aMapHelper;
    private LinearLayout mAqiLayout;

    private Items items = new Items();
    private MultiTypeAdapter adapter;
    private View weatherView;
    private MainWeather mainWeather = new MainWeather();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        aMapHelper = new AMapHelper(getApplicationContext());

        initSystemBar();

        initTypeface();

        initViews();

        initEvents();

        if (!isDataExist(context)) {
            LocationInfoLoader.getInstance(context).initCurrentLocation();
        }

        initData();

    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.BLACK);
        }
    }

    private void initData() {
        int weatherDataId = Preferences.readCurrWeatherDataId(context);
        Log.d("cxq", "current weatherDataId:" + weatherDataId);
        //读取location数据库获取当前地址信息给currCityParams
        Location currLocation = LocationInfoLoader.getInstance(context).queryLocationById(weatherDataId);
        loadLocation2CurrCityParams(currLocation);
        Log.d("cxq", currCityParams.toString());

        showProgress();

        //从数据库获取信息
        updateWeatherUIData();
        updatePhenologyUIData();
        showAqi();

        if (currLocation.getLat() == 0 && currLocation.getLon() == 0) {

        }

        //判断是否是定位城市
        if (weatherDataId == Constants.AUTO_ID) {
            //locate
            aMapHelper.locate(this);
        } else {
            requestWeatherData();
            requestPhenology();
            requestAqi();
        }
    }

    private void initTypeface() {
        numberRegular = Typeface.createFromAsset(context.getAssets(), "sf_regular.otf");
    }

    private void initEvents() {

        actionBarRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CityManageActivity.class);
                startActivityForResult(i, Constants.MAIN_TO_CITY_REQUEST_CODE);
            }
        });
    }

    private void loadLocation2CurrCityParams(Location location) {
        currCityParams.setWeatherDataID(location.getId().intValue());
        currCityParams.setProvince(location.getLevel1());
        currCityParams.setCity(location.getLevel2());
        currCityParams.setDistrict(location.getLevel3());
        currCityParams.setLatitude((float) location.getLat());
        currCityParams.setLongitude((float) location.getLon());
    }

    private void requestPhenology() {

        NetClient.getMainPheloRetrofitInstance().requestMainByLocation(
                currCityParams.getProvince(),
                currCityParams.getCity(),
                currCityParams.getDistrict())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<MainPhenologyResult>() {
                    @Override
                    public void accept(MainPhenologyResult result) throws Exception {
                        if ("ok".equals(result.getStatus())) {
                            List<MainPhenology> mainPhenologies = result.getMainPhenologies();
                            if (mainPhenologies == null || mainPhenologies.isEmpty()) {
                                return;
                            }
                            for (MainPhenology mainPhenology : mainPhenologies) {
                                mainPhenology.setWeatherDataId(currCityParams.getWeatherDataID());
                            }
                            WeatherDatabaseManager.getInstance(context).deleteMainPhenosByCityId(currCityParams.getWeatherDataID());
                            WeatherDatabaseManager.getInstance(context).insertMainPhenos(mainPhenologies);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MainPhenologyResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MainPhenologyResult result) {
                        if ("ok".equals(result.getStatus())) {
                            notifyData(result.getMainPhenologies());
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


    private void initViews() {

        findViewById(R.id.img_action_bar_more).setVisibility(View.VISIBLE);
        actionBarRl = (RelativeLayout) findViewById(R.id.rl_action_bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int systemBarHeight = DimenUtils.getStatusHeight(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, systemBarHeight);
            findViewById(R.id.system_bar_space).setLayoutParams(params);
        }

        weatherView = LayoutInflater.from(context).inflate(R.layout.main_header_view, mRecycler, false);
        weatherView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        initWeatherView(weatherView);

        mRecycler = (RecyclerView) findViewById(R.id.rv_main);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        adapter = new MultiTypeAdapter();
        /* 注册类型和 View 的对应关系 */
        adapter.register(MainWeather.class, new MainWeatherBinder());
        adapter.register(MainPhenology.class, new MainPhenoloBinder());
        mRecycler.setAdapter(adapter);
        items.add(new MainWeather());
        adapter.setItems(items);
        adapter.notifyDataSetChanged();

        mCityNameText = (TextView) findViewById(R.id.text_actionbar_title);
        addCityImg = (ImageView) findViewById(R.id.img_actionbar_menu);
        addCityImg.setVisibility(View.VISIBLE);
        findViewById(R.id.img_actionbar_back).setVisibility(View.GONE);

    }

    private void updateWeatherUIData() {

        mCityNameText.setText(currCityParams.getCity());
        final boolean hasDbData = false;
        //从数据库获取天气
        final WeatherInfoLoader weatherInfoLoader = WeatherInfoLoader.getInstance(context, currCityParams.getWeatherDataID());
        weatherInfoLoader.getAllData(new WeatherInfoLoader.OnLoadFullWeatherDataListener() {
            @Override
            public void onSuccess() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgress();
                        mCurrTempText.setText(weatherInfoLoader.getCurrentIntTemp());
                        boolean isLight = DCTUtils.isCurrentCityIsLight(context, weatherInfoLoader, currCityParams.getWeatherDataID());
                        mIconImg.setImageResource(IconLoader.getWeatherImageId(weatherInfoLoader.getCurrentIcon(), isLight));
                        mCurrConditionText.setText(weatherInfoLoader.getCurrentCondition());
                        mHighLowTempText.setText(weatherInfoLoader.getCurrentIntLowTemp()
                                + "~" + weatherInfoLoader.getCurrentIntHighTemp());
                        mFeelText.setText(weatherInfoLoader.getCurrentIntRealFeel());
                        for (int i = 0; i < mHourLayout.getChildCount(); i++) {
                            View view = mHourLayout.getChildAt(i);
                            TextView time = (TextView) view.findViewById(R.id.text_main_hour_time);
                            ImageView icon = (ImageView) view.findViewById(R.id.img_main_hour_icon);

                            time.setText(weatherInfoLoader.getHourName(i));
                            icon.setImageResource(IconLoader.getWeatherImageId(weatherInfoLoader.getHourIcon(i), weatherInfoLoader.isHourLight(i)));
                        }
                        for (int i = 0, j = 0; i < mDayLayout.getChildCount(); i++) {
                            View view = mDayLayout.getChildAt(i);
                            try {
                                TextView name = (TextView) view.findViewById(R.id.text_main_day_name);
                                TextView highLowTemp = (TextView) view.findViewById(R.id.text_main_day_high_low_temp);
                                TextView condition = (TextView) view.findViewById(R.id.text_main_day_condition);
                                ImageView icon = (ImageView) view.findViewById(R.id.img_main_day_icon);
                                name.setText(j == 0 ? "今天" : "明天");
                                highLowTemp.setText(weatherInfoLoader.getDayIntHighTemp(j) + "/" + weatherInfoLoader.getDayIntLowTemp(j));
                                condition.setText(weatherInfoLoader.getDayCondition(j));
                                icon.setImageResource(IconLoader.getWeatherImageId(weatherInfoLoader.getDayIcon(j), true));
                                j++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailed(String str) {

            }
        }, context, currCityParams.getWeatherDataID());

    }

    private void updatePhenologyUIData() {
        List<MainPhenology> mainPhenologies = WeatherDatabaseManager.getInstance(context)
                .queryMainPhenosByCityId(currCityParams.getWeatherDataID());
        notifyData(mainPhenologies);
    }

    private void notifyData(List<MainPhenology> mainPhenologies) {
        if (mainPhenologies != null && !mainPhenologies.isEmpty()) {
            items.clear();
            items.add(mainWeather);
            items.addAll(mainPhenologies);
            adapter.notifyDataSetChanged();
        }
    }

    private void requestWeatherData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher();
                weatherDataFetcher.fetchAndParseWeatherData(context, currCityParams, new WeatherDataFetcherInterface.OnFetchResultEventListener() {
                    @Override
                    public void onSucceed() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateWeatherUIData();
                            }
                        });
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onNoData() {

                    }

                    @Override
                    public void onNoKey() {

                    }
                });


            }
        }).start();
    }

    private void requestAqi() {
        //aqi
        long lastAqiTime = Preferences.readAqiLastUpdateTime(context, currCityParams.getWeatherDataID());
        if (System.currentTimeMillis() - lastAqiTime > Constants.ONE_HOUR
                || mAqi < 0) {
            requestAqiFromNet(currCityParams.getLatitude(),
                    currCityParams.getLongitude(), currCityParams.getWeatherDataID());
        } else {
            showAqi();
        }

    }

    private void showAqi() {
        mAqi = Preferences.readAqiValue(context, currCityParams.getWeatherDataID());
        if (mAqi > 0) {
            mAqiLayout.setVisibility(View.VISIBLE);
            mAqiText.setText(String.valueOf(mAqi));
            if (mAqi < 50) {
                mAqiLayout.setBackgroundResource(R.drawable.bg_aqi_1);
            } else if (mAqi < 100) {
                mAqiLayout.setBackgroundResource(R.drawable.bg_aqi_2);
            } else if (mAqi < 150) {
                mAqiLayout.setBackgroundResource(R.drawable.bg_aqi_3);
            } else if (mAqi < 200) {
                mAqiLayout.setBackgroundResource(R.drawable.bg_aqi_4);
            } else if (mAqi < 300) {
                mAqiLayout.setBackgroundResource(R.drawable.bg_aqi_5);
            } else {
                mAqiLayout.setBackgroundResource(R.drawable.bg_aqi_6);
            }
        } else {
            mAqiLayout.setVisibility(View.GONE);
        }
    }

    private void requestAqiFromNet(float latitude, float longitude, final int weatherDataID) {
        String url;
        url = CardUtils.AQI_API_URL + CardUtils.LAT + latitude + CardUtils.LNG + longitude;
        RequestQueue mQueue = VolleySingleton.getInstance(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (null == response) {
                    showAqi();
                    return;
                }
                String status = response.optString(CardUtils.STATUS);
                if (status != null && CardUtils.OK.equals(status)) {
                    JSONObject data = response.optJSONObject(CardUtils.DATA);
                    if (data != null) {
                        mAqi = data.optInt(CardUtils.AQI);
                        Preferences.saveAqiValue(context, weatherDataID, mAqi);
                        Preferences.saveAqiLastUpdateTime(context, weatherDataID, System.currentTimeMillis());
                        showAqi();
//                        JSONObject detailDensity = data.optJSONObject(CardUtils.DETAIL_DENSITY);
//                        if (null != detailDensity) {
//                            mPm25 = detailDensity.optInt(CardUtils.PM25);
//                            mCo = detailDensity.optInt(CardUtils.CO);
//                            mPm10 = detailDensity.optInt(CardUtils.PM10);
//                            mNo2 = detailDensity.optInt(CardUtils.NO2);
//                            mO3 = detailDensity.optInt(CardUtils.O3);
//                            mSo2 = detailDensity.optInt(CardUtils.SO2);
//                        } else {
//                            mPm25 = -999;
//                            mCo = -999;
//                            mPm10 = -999;
//                            mNo2 = -999;
//                            mO3 = -999;
//                            mSo2 = -999;
//                        }
//                        Preferences.setDetailDensity(context, weatherDataID, mPm25, mCo, mPm10, mSo2, mO3, mNo2);
//                        analyticalData(weatherDataID);
                    } else {
                        showAqi();
                    }
                } else {
                    showAqi();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showAqi();
            }
        });
        mQueue.add(jsonObjectRequest);
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (currCityParams != null && currCityParams.getWeatherDataID() != Constants.AUTO_ID) {
            return;
        }
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
//                Toast.makeText(context, "经度：" + amapLocation.getLongitude() + "纬度：" + amapLocation.getLatitude(), Toast.LENGTH_SHORT).show();
                Location autoLocation = LocationInfoLoader.getInstance(context).queryLocationById(Constants.AUTO_ID);
                if (autoLocation == null) {
                    autoLocation = new Location();
                }
                autoLocation.setLevel0(amapLocation.getCountry());
                autoLocation.setLevel1(amapLocation.getProvince());
                autoLocation.setLevel2(amapLocation.getCity());
                autoLocation.setLevel3(amapLocation.getDistrict());
                autoLocation.setLat(amapLocation.getLatitude());
                autoLocation.setLon(amapLocation.getLongitude());

                if (autoLocation.getId() > 0) {
                    WeatherDatabaseManager.getInstance(context).updateLocation(autoLocation);
                } else {
                    autoLocation.setId(1L);
                    WeatherDatabaseManager.getInstance(context).insertLocation(autoLocation);
                }

                loadLocation2CurrCityParams(autoLocation);

                mCityNameText.setText(currCityParams.getCity());
                requestWeatherData();
                requestPhenology();
                requestAqi();

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                dismissProgress();
                Toast.makeText(context, "定位失败", Toast.LENGTH_SHORT).show();
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

    }


    private boolean isDataExist(Context context) {
        List<Integer> cityIdList = LocationInfoLoader.getInstance(context).getCityIds();
        return cityIdList.size() > 0;
    }

    private void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
        }
        mProgressDialog.show();
    }

    private void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void afterAllPermissionAllowed() {
        super.afterAllPermissionAllowed();
        showProgress();
        aMapHelper.locate(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("cxq", "requestCode=" + requestCode + " resultCode=" + resultCode);
        if (requestCode == Constants.MAIN_TO_CITY_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            initData();
        }
    }


    class MainWeatherBinder extends ItemViewBinder<MainWeather, MainWeatherBinder.ViewHolder> {

        @NonNull
        @Override
        protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            return new ViewHolder(weatherView);
        }

        @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MainWeather item) {

        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

    }

    private void initWeatherView(View itemView) {
        mCurrTempText = (TextView) itemView.findViewById(R.id.text_main_current_temp);
        mCurrTempText.setTypeface(numberRegular);
        mIconImg = (ImageView) itemView.findViewById(R.id.img_main_icon);
        mCurrConditionText = (TextView) itemView.findViewById(R.id.text_main_current_condition);
        mHighLowTempText = (TextView) itemView.findViewById(R.id.text_main_high_low_temp);
        mHourLayout = (LinearLayout) itemView.findViewById(R.id.ll_main_hour);
        mDayLayout = (LinearLayout) itemView.findViewById(R.id.ll_main_day);
        mWeatherMoreLayout = (LinearLayout) itemView.findViewById(R.id.ll_weather_more);
        mAqiText = (TextView) itemView.findViewById(R.id.text_main_aqi);
        mAqiLayout = (LinearLayout) itemView.findViewById(R.id.ll_aqi);
        mFeelText = (TextView) itemView.findViewById(R.id.text_main_real_feel);
        mFeelText.setTypeface(numberRegular);

        mWeatherMoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WeatherDetailActivity.class);
                i.putExtra(Constants.EXTRA_WEATHER_DATA_ID, currCityParams.getWeatherDataID());
                i.putExtra(Constants.EXTRA_CITY_NAME, currCityParams.getCity());
                startActivity(i);
            }
        });
    }

    class MainPhenoloBinder extends ItemViewBinder<MainPhenology, MainPhenoloBinder.ViewHolder> {

        @NonNull
        @Override
        protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View root = inflater.inflate(R.layout.item_main_phenolo, parent, false);
            return new ViewHolder(root, parent);
        }

        @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MainPhenology item) {
            holder.item = item;
            String title = item.getTitle();
            if (title == null) {
                title = "";
            }
            holder.mTitleText.setText(title);
            try {
                Glide.with(context).load(item.getImage())
//                        .placeholder(R.drawable.bg_default).centerCrop()
//                        .error(R.drawable.bg_default).centerCrop()
                        .into(holder.mImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            MainPhenology item;
            ImageView mImg;
            TextView mTitleText;

            public ViewHolder(View itemView, ViewGroup parent) {
                super(itemView);
                int imgWidth = parent.getWidth();
                mImg = (ImageView) itemView.findViewById(R.id.img_item_main_pic);
                mImg.setLayoutParams(new RelativeLayout.LayoutParams(imgWidth, imgWidth * 2 / 3));
                mTitleText = (TextView) itemView.findViewById(R.id.text_item_main_title);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getId() < 0) {
                            return;
                        }
                        if ("spot".equals(item.getType())) {
                            Intent i = new Intent(MainActivity.this, PhenologyDetailActivity.class);
                            i.putExtra(Constants.EXTRA_SPOT_ID, item.getId());
                            context.startActivity(i);
                        } else if ("landscape".equals(item.getType())) {
                            Intent i = new Intent(MainActivity.this, LandscapeActivity.class);
                            i.putExtra(Constants.EXTRA_LAN_ID, item.getId());
                            context.startActivity(i);
                        }
                    }
                });
            }
        }
    }
}
