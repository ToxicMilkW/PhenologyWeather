package com.iap.phenologyweather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iap.phenologyweather.R;
import com.iap.phenologyweather.network.NetClient;
import com.iap.phenologyweather.network.netresult.LandscapeResult;
import com.iap.phenologyweather.utils.CommonUtils;
import com.iap.phenologyweather.utils.Constants;
import com.iap.phenologyweather.utils.DimenUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LandscapeActivity extends AppCompatActivity {

    private Context context;

    private ScrollView mScrollView;

    private ImageView mLandscapeImg;
    private TextView mLandscapeText;
    private TextView mAddressText;
    private TextView mAddressDescText;
    private GridLayout mCanWatchGrid;
    private TextView mFeatureText;
    private ImageView mFunnyImg;
    private ImageView mPoemImg;

    private LinearLayout mAddressLayout;
    private LinearLayout mAddressDescLayout;
    private LinearLayout mFeatureLayout;
    private LinearLayout mFunnyLayout;
    private LinearLayout mCanWatchLayout;
    private LinearLayout mPoemLayout;

    private ProgressDialog mProgressDialog;

    private int currentLanId;
    private LandscapeResult.Landscape landscape;
    private View.OnClickListener canWatchListener;
    private List<LandscapeResult.Landscape.AssociatedSpot> mSpots = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);
        context = this;
        initSystemBar();

        initViews();

        initEvents();

        currentLanId = getIntent().getIntExtra(Constants.EXTRA_LAN_ID, -1);

        requestLandscape();
    }

    private void initEvents() {
        canWatchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ((ViewGroup) v.getParent()).indexOfChild(v);
                if (position >= mSpots.size()) {
                    return;
                }
                Intent i = new Intent(context, PhenologyDetailActivity.class);
                i.putExtra(Constants.EXTRA_SPOT_ID, mSpots.get(position).getSpot_id());
                context.startActivity(i);
            }
        };
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

    private void initViews() {
        findViewById(R.id.img_actionbar_back)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        TextView title = (TextView) findViewById(R.id.text_actionbar_title);
        title.setText("景观详情");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int systemBarHeight = DimenUtils.getStatusHeight(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, systemBarHeight);
            findViewById(R.id.system_bar_space).setLayoutParams(params);
        }

        mScrollView = (ScrollView) findViewById(R.id.sv_landscape);
        mScrollView.setVisibility(View.GONE);
        mLandscapeImg = (ImageView) findViewById(R.id.img_landscape_pic);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = width * 2 / 3;
        mLandscapeImg.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        mLandscapeText = (TextView) findViewById(R.id.text_landscape_name);
        mAddressText = (TextView) findViewById(R.id.text_landscape_address);
        mAddressDescText = (TextView) findViewById(R.id.text_landscape_desc);
        mCanWatchGrid = (GridLayout) findViewById(R.id.gl_landscape_can_watch_what);
        mFeatureText = (TextView) findViewById(R.id.text_landscape_feature);
        mFunnyImg = (ImageView) findViewById(R.id.img_landscape_funny);
        mPoemImg = (ImageView) findViewById(R.id.img_landscape_poem);


        mAddressLayout = (LinearLayout) findViewById(R.id.ll_landscape_address);
        mAddressDescLayout = (LinearLayout) findViewById(R.id.ll_landscape_desc);
        mFeatureLayout = (LinearLayout) findViewById(R.id.ll_landscape_feature);
        mFunnyLayout = (LinearLayout) findViewById(R.id.ll_landscape_funny);
        mCanWatchLayout = (LinearLayout) findViewById(R.id.ll_landscape_can_watch_what);
        mPoemLayout = (LinearLayout) findViewById(R.id.ll_landscape_poem);

    }

    private void requestLandscape() {
        showProgress();

        NetClient.getLandscapeRetrofitInstance().requestLandscapeById(currentLanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LandscapeResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LandscapeResult result) {
                        dismissProgress();
                        if ("ok".equals(result.getStatus())) {
                            landscape = result.getData();
                            updateUi();
                        } else {
                            Toast.makeText(context, "景点详情请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dismissProgress();
                        Toast.makeText(context, "景点详情请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateUi() {
        if (landscape == null) {
            return;
        }

        mScrollView.setVisibility(View.VISIBLE);

        try {
            Glide.with(context)
                    .load(landscape.getImage())
//                    .placeholder(R.drawable.bg_default).centerCrop()
//                    .error(R.drawable.bg_default).centerCrop()
                    .into(mLandscapeImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLandscapeText.setText(landscape.getName());
        if (TextUtils.isEmpty(landscape.getAddress())) {
            mAddressLayout.setVisibility(View.GONE);
        } else {
            mAddressLayout.setVisibility(View.VISIBLE);
            mAddressText.setText(landscape.getAddress());
        }

        if (TextUtils.isEmpty(landscape.getDescription())) {
            mAddressDescLayout.setVisibility(View.GONE);
        } else {
            mAddressDescLayout.setVisibility(View.VISIBLE);
            mAddressDescText.setText(landscape.getDescription());
        }

        if (TextUtils.isEmpty(landscape.getFeature())) {
            mFeatureLayout.setVisibility(View.GONE);
        } else {
            mFeatureLayout.setVisibility(View.VISIBLE);
            mFeatureText.setText(landscape.getFeature());
        }

        if (TextUtils.isEmpty(landscape.getFunny_image())) {
            mFunnyLayout.setVisibility(View.GONE);
        } else {
            mFunnyLayout.setVisibility(View.VISIBLE);
            try {
                Glide.with(context)
                        .load(landscape.getFunny_image())
//                        .placeholder(R.drawable.bg_default).centerCrop()
//                        .error(R.drawable.bg_default).centerCrop()
                        .into(mFunnyImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<LandscapeResult.Landscape.AssociatedSpot> spots = landscape.getAssociated_spot();
        if (spots == null || spots.isEmpty()) {
            mCanWatchLayout.setVisibility(View.GONE);
        } else {
            mSpots.clear();
            mSpots.addAll(spots);
            mCanWatchLayout.setVisibility(View.VISIBLE);
            mCanWatchGrid.removeAllViews();

            int columnCount = mCanWatchGrid.getColumnCount();
            int margin = CommonUtils.dip2px(context, 4);
            for (int i = 0; i < spots.size(); i++) {
                GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount, 1.0f);
                GridLayout.Spec columnSpec = GridLayout.spec(i % columnCount, 1.0f);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                LandscapeResult.Landscape.AssociatedSpot spot = spots.get(i);
                TextView canWatchText = (TextView) LayoutInflater.from(context).inflate(R.layout.btn_can_watch, mCanWatchGrid, false);
                params.setMargins(i % columnCount == 0 ? 0 : margin , margin, i % columnCount == columnCount - 1 ? 0 : margin, margin);
                canWatchText.setLayoutParams(params);
                canWatchText.setText(spot.getName());
                canWatchText.setOnClickListener(canWatchListener);
                mCanWatchGrid.addView(canWatchText, params);
            }
        }


        if (TextUtils.isEmpty(landscape.getPoetry_image())) {
            mPoemLayout.setVisibility(View.GONE);
        } else {
            mPoemLayout.setVisibility(View.VISIBLE);
            try {
                Glide.with(context)
                        .load(landscape.getPoetry_image())
//                        .placeholder(R.drawable.bg_default).centerCrop()
//                        .error(R.drawable.bg_default).centerCrop()
                        .into(mPoemImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
}
