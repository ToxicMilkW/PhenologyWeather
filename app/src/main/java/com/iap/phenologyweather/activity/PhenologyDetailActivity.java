package com.iap.phenologyweather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.elvishew.xlog.XLog;
import com.iap.phenologyweather.R;
import com.iap.phenologyweather.network.NetClient;
import com.iap.phenologyweather.network.netresult.PhenologyDetailResult;
import com.iap.phenologyweather.utils.Constants;
import com.iap.phenologyweather.utils.DimenUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PhenologyDetailActivity extends AppCompatActivity {

    private Context mContext;

    private ScrollView mScrollView;

    private ImageView mDetailImg;
    private TextView mSpeciesNameText;
    private TextView mSpeciesDescTitleText;
    private TextView mSpeciesDescText;
    private TextView mWatchTimeText;
    private TextView mPredictTimeText;
    private TextView mPredictDescText;
    private TextView mSpotDescTitleText;
    private TextView mSpotDescPrimaryText;
    private TextView mSpotDescSecondText;
    private TextView mSpotMapTitleText;
    private LinearLayout mWatchTimeLayout;
    private LinearLayout mPredictTimeLayout;
    private LinearLayout mPredictDescLayout;
    private LinearLayout mSpeciesDescLayout;
    private LinearLayout mPoemLayout;
    private TextView mPoemTitle;
    private ImageView mPoemImg;

    private MapView mMapView;

    private ProgressDialog mProgressDialog;

    private PhenologyDetailResult.PhenologyDetail phenologyDetail;
    private int currentSpotId;
    private Marker currMarker;

    private AMap aMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phenology_detail);
        mContext = this;

        initSystemBar();

        initViews();
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }


        currentSpotId = getIntent().getIntExtra(Constants.EXTRA_SPOT_ID, 0);
        initAMap();
        requestPhenologyDetail();

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

    private void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(getString(R.string.loading));
        }
        mProgressDialog.show();
    }

    private void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void initAMap() {
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);

        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                }
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("cxq", "marker spot id:" + marker.getObject());
                currMarker = marker;
                currentSpotId = (int) marker.getObject();
                requestPhenologyDetail();
                return false;
            }
        });

    }

    private void requestPhenologyDetail() {
        showProgress();

        NetClient.getPhenoDetailRetrofitInstance().requestPhenologyBySpotId(currentSpotId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PhenologyDetailResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PhenologyDetailResult result) {
                        dismissProgress();
                        if ("ok".equals(result.getStatus())) {
                            phenologyDetail = result.getPhenologyDetail();
                            updateUi();
                        } else {
                            Toast.makeText(mContext, "物候详情请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dismissProgress();
                        Toast.makeText(mContext, "物候详情请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateUi() {
        if (phenologyDetail == null) {
            return;
        }
        mScrollView.setVisibility(View.VISIBLE);
        try {
            Glide.with(mContext)
                    .load(phenologyDetail.getSpecies_image())
//                    .placeholder(R.drawable.bg_default).centerCrop()
//                    .error(R.drawable.bg_default).centerCrop()
                    .into(mDetailImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSpeciesNameText.setText(phenologyDetail.getSpecies_name());
        if (TextUtils.isEmpty(phenologyDetail.getSpecies_description())) {
            mSpeciesDescLayout.setVisibility(View.GONE);
        } else {
            mSpeciesDescLayout.setVisibility(View.VISIBLE);
            mSpeciesDescTitleText.setText(phenologyDetail.getSpecies_name() + "简介");
            mSpeciesDescText.setText(phenologyDetail.getSpecies_description());
        }
        if (TextUtils.isEmpty(phenologyDetail.getWatch_time_description())) {
            mWatchTimeLayout.setVisibility(View.GONE);
        } else {
            mWatchTimeText.setText(phenologyDetail.getWatch_time_description());
            mWatchTimeLayout.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(phenologyDetail.getPredict_watch_time_description())) {
            mPredictTimeLayout.setVisibility(View.GONE);
        } else {
            mPredictTimeText.setText(phenologyDetail.getPredict_watch_time_description());
            mPredictTimeLayout.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(phenologyDetail.getPredict_description())) {
            mPredictDescLayout.setVisibility(View.GONE);
        } else {
            mPredictDescText.setText(phenologyDetail.getPredict_description());
            mPredictDescLayout.setVisibility(View.VISIBLE);
        }

        mSpotDescTitleText.setText(phenologyDetail.getSpot_name() + phenologyDetail.getSpecies_name() + "简介");

        if (TextUtils.isEmpty(phenologyDetail.getSpot_primary_description())) {
            mSpotDescPrimaryText.setVisibility(View.GONE);
        } else {
            mSpotDescPrimaryText.setVisibility(View.VISIBLE);
            mSpotDescPrimaryText.setText(phenologyDetail.getSpot_primary_description());
        }
        if (TextUtils.isEmpty(phenologyDetail.getSpot_description())) {
            mSpotDescSecondText.setVisibility(View.GONE);
        } else {
            mSpotDescSecondText.setVisibility(View.VISIBLE);
            mSpotDescSecondText.setText(phenologyDetail.getSpot_description());
        }

        mSpotMapTitleText.setText("附近可观赏" + phenologyDetail.getSpecies_name() + "的地点");



        //map
        if (aMap.getMapScreenMarkers() != null && !aMap.getMapScreenMarkers().isEmpty()) {
            if (currMarker != null) {
                currMarker.setTitle(phenologyDetail.getSpot_name());
                currMarker.showInfoWindow();
            }
        } else {
            for (PhenologyDetailResult.PhenologyDetail.SpotLocation spotLocation : phenologyDetail.getSpot_location()) {
                final Marker marker =   aMap.addMarker(new MarkerOptions().position(
                        new LatLng(spotLocation.getLatitude(),
                                spotLocation.getLongitude())).title(""));
                marker.setObject(spotLocation.getSpot_id());
                XLog.d("经纬度: " + spotLocation.getLatitude() + spotLocation.getLongitude());

                XLog.d("getSpot_id: " + spotLocation.getSpot_id() + "   currentSpotId: " + currentSpotId);
                if (spotLocation.getSpot_id() == currentSpotId) {
                    aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            new LatLng(spotLocation.getLatitude(), spotLocation.getLongitude()),10,0,0)));
                    marker.setTitle(phenologyDetail.getSpot_name());
                    marker.showInfoWindow();
                }
            }
        }

        if (TextUtils.isEmpty(phenologyDetail.getPoetry_image())) {
            mPoemLayout.setVisibility(View.GONE);
        } else {
            mPoemLayout.setVisibility(View.VISIBLE);
            mPoemTitle.setText(phenologyDetail.getSpecies_name() + "诗词鉴赏");
            try {
                Glide.with(mContext)
                        .load(phenologyDetail.getPoetry_image())
//                        .placeholder(R.drawable.bg_default).centerCrop()
//                        .error(R.drawable.bg_default).centerCrop()
                        .into(mPoemImg);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void initViews() {
        findViewById(R.id.img_actionbar_back)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        TextView title = (TextView) findViewById(R.id.text_actionbar_title);
        title.setText("物候详情");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int systemBarHeight = DimenUtils.getStatusHeight(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, systemBarHeight);
            findViewById(R.id.system_bar_space).setLayoutParams(params);
        }

        mScrollView = (ScrollView) findViewById(R.id.sv_detail);
        mScrollView.setVisibility(View.GONE);
        mDetailImg = (ImageView) findViewById(R.id.img_detail_pic);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = width * 2 / 3;
        mDetailImg.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        mSpeciesNameText = (TextView) findViewById(R.id.text_detail_species_name);
        mSpeciesDescText = (TextView) findViewById(R.id.text_detail_species_desc);
        mSpeciesDescTitleText = (TextView) findViewById(R.id.text_detail_species_desc_title);
        mWatchTimeText = (TextView) findViewById(R.id.text_detail_watch_time);
        mPredictTimeText = (TextView) findViewById(R.id.text_detail_predict_time);
        mSpotMapTitleText = (TextView) findViewById(R.id.text_detail_location_title);
        mPredictTimeLayout = (LinearLayout) findViewById(R.id.ll_detail_predict_time);
        mMapView = (MapView) findViewById(R.id.detail_map);
        mPoemLayout = (LinearLayout) findViewById(R.id.ll_detail_poem);
        mPoemTitle = (TextView) findViewById(R.id.text_detail_poem_title);
        mPoemImg = (ImageView) findViewById(R.id.img_detail_poem);

        mPredictDescText = (TextView) findViewById(R.id.text_detail_predict_desc);
        mSpotDescTitleText = (TextView) findViewById(R.id.text_detail_spot_desc_title);
        mSpotDescPrimaryText = (TextView) findViewById(R.id.text_detail_spot_primary_desc);
        mSpotDescSecondText = (TextView) findViewById(R.id.text_detail_spot_second_desc);

        mWatchTimeLayout = (LinearLayout) findViewById(R.id.ll_detail_watch);
        mPredictDescLayout = (LinearLayout) findViewById(R.id.ll_detail_predict_desc);
        mSpeciesDescLayout = (LinearLayout) findViewById(R.id.ll_species_desc);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
