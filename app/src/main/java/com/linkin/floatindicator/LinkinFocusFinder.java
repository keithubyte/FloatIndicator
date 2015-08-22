package com.linkin.floatindicator;

import android.view.FocusFinder;
import android.view.ViewGroup;

/**
 * @author liangjunfeng
 * @since 2015/8/22 17:13
 */
public class LinkinFocusFinder {

    private static LinkinFocusFinder sInstance;
    FocusFinder mFocusFinder;

    private LinkinFocusFinder(ViewGroup root) {

    }

    public static void init(ViewGroup root) {
        if (sInstance != null) {

        }
    }

}
