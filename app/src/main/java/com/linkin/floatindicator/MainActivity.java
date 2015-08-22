package com.linkin.floatindicator;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnFocusChangeListener {

    private FocusFrameLayout mContainer;

    private Button smallView;
    private Button mediumView;
    private Button largeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainer = (FocusFrameLayout) findViewById(R.id.container);

        smallView = (Button) findViewById(R.id.small);
        mediumView = (Button) findViewById(R.id.medium);
        largeView = (Button) findViewById(R.id.large);

        smallView.setOnFocusChangeListener(this);
        mediumView.setOnFocusChangeListener(this);
        largeView.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Rect rect = ViewHelper.getBounds(v);
            mContainer.anim(36, rect);
            // Log.e("FloatIndicator", "left = " + rect.left + ", top = " + rect.top + ", right = " + rect.right + ", bottom = " + rect.bottom);
        }
    }
}
