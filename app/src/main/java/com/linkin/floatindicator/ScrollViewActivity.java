package com.linkin.floatindicator;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

/**
 * @author liangjunfeng
 * @since 2015/8/22 17:36
 */
public class ScrollViewActivity extends Activity implements View.OnFocusChangeListener {

    private FocusFrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        mContainer = (FocusFrameLayout) findViewById(R.id.container);

        findViewById(R.id.item_0).setOnFocusChangeListener(this);
        findViewById(R.id.item_1).setOnFocusChangeListener(this);
        findViewById(R.id.item_2).setOnFocusChangeListener(this);
        findViewById(R.id.item_3).setOnFocusChangeListener(this);
        findViewById(R.id.item_4).setOnFocusChangeListener(this);
        findViewById(R.id.item_5).setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Rect rect = ViewHelper.getBounds(v);
            mContainer.anim(30, rect);
        }
    }
}
