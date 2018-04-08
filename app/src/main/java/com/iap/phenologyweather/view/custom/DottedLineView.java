package com.iap.phenologyweather.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.iap.phenologyweather.R;
import com.iap.phenologyweather.utils.CommonUtils;


/**
 * Created by chenxueqing on 2016/8/26.
 */

public class DottedLineView extends View {

    private Context mContext;
    private Path path;
    private Paint paint;
    private PathEffect effects;

    public DottedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.black_100));
        path = new Path();
        //dot gap and width
        effects = new DashPathEffect(new float[]{CommonUtils.dip2px(mContext, 3),
                CommonUtils.dip2px(mContext, 2), CommonUtils.dip2px(mContext, 3), CommonUtils.dip2px(mContext, 2)}, 0);
        paint.setPathEffect(effects);
    }

    public void setLineColor(int id) {
        paint.setColor(getResources().getColor(id));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(0, 0);//start position
        path.lineTo(0, getHeight());//end position
        canvas.drawPath(path, paint);
    }
}
