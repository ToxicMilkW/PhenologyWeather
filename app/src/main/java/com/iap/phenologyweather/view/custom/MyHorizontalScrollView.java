package com.iap.phenologyweather.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by chenxueqing on 2016/9/1.
 */

public class MyHorizontalScrollView extends HorizontalScrollView {

    private OnScrollListener listener = null;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollListener(OnScrollListener listener){
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public interface OnScrollListener{
        void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
