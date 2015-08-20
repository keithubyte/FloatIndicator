package com.linkin.floatindicator;

import android.graphics.Rect;
import android.view.View;

/**
 * @author liangjunfeng
 * @since 2015/8/19 13:27
 */
public class ViewHelper {

    public static Rect getBounds(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int width = view.getWidth();
        int height = view.getHeight();
        return new Rect(location[0], location[1], location[0] + width, location[1] + height);
    }

}
