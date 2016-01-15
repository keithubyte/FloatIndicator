package com.linkin.floatindicator;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import java.lang.ref.SoftReference;

/**
 * @author liangjunfeng
 * @since 2015/8/18 19:19
 */
public class FocusFrameLayout extends FrameLayout {

    private static final String TAG = "FocusFrameLayout";
    private static final int DEFAULT_ANIM_FRAME = 30;

    private Drawable mDrawable;
    private Rect mDrawablePaddingRect;
    private Interpolator mFrameInterpolator;
    private TimeInterpolator mDelayInterpolator;
    private FocusThread mFocusThread;

    int tFrame;
    int mFrame;

    int initL;
    int initT;
    int initR;
    int initB;

    float deltaL;
    float deltaT;
    float deltaR;
    float deltaB;

    public FocusFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public FocusFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FocusFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDrawable = context.getResources().getDrawable(R.drawable.ytm_common_focus);
        if (mDrawable != null) {
            mDrawable.setBounds(0, 0, 0, 0);
            mDrawablePaddingRect = new Rect();
            mDrawable.getPadding(mDrawablePaddingRect);

            Log.e(TAG, "left = " + mDrawablePaddingRect.left
                    + ", top = " + mDrawablePaddingRect.top
                    + ", right = " + mDrawablePaddingRect.right
                    + ", bottom = " + mDrawablePaddingRect.bottom);
        }

        mFocusThread = new FocusThread(this);
        mFocusThread.start();

        mFrameInterpolator = new DecelerateInterpolator(4.0F);
        mDelayInterpolator = new DelayInterpolator();

        getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                anim(DEFAULT_ANIM_FRAME, ViewHelper.getBounds(newFocus));
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mDrawable.draw(canvas);
    }

    public void anim(int frame, Rect dstRect) {
        tFrame = frame;
        mFrame = 0;

        Rect srcRect = mDrawable.getBounds();

        dstRect.left = dstRect.left - mDrawablePaddingRect.left;
        dstRect.top = dstRect.top - mDrawablePaddingRect.top;
        dstRect.right = dstRect.right + mDrawablePaddingRect.right;
        dstRect.bottom = dstRect.bottom + mDrawablePaddingRect.bottom;

        initL = srcRect.left;
        initT = srcRect.top;
        initR = srcRect.right;
        initB = srcRect.bottom;

        deltaL = (dstRect.left - initL) / (float) frame;
        deltaT = (dstRect.top - initT) / (float) frame;
        deltaR = (dstRect.right - initR) / (float) frame;
        deltaB = (dstRect.bottom - initB) / (float) frame;

        move();
    }

    public void move() {
        if (mFrame++ < tFrame) {
            Rect srcRect = mDrawable.getBounds();

            float progress = (float) mFrame / (float) tFrame;
            float radio = mFrameInterpolator.getInterpolation(progress);
            srcRect.left = (int) (initL + (deltaL * mFrame * radio));
            srcRect.top = (int) (initT + (deltaT * mFrame * radio));
            srcRect.right = (int) (initR + (deltaR * mFrame * radio));
            srcRect.bottom = (int) (initB + (deltaB * mFrame * radio));

            long delay = (long) (mDelayInterpolator.getInterpolation(progress) * 20L);
            mFocusThread.getHandler().sendEmptyMessageDelayed(MSG_MOVE, delay);

        }
    }

    private static final int MSG_MOVE = 0;
    private static final int MSG_ANIM = 1;

    private static class FocusThread extends Thread {

        private FocusHandler mHandler;
        private FocusFrameLayout mLayout;

        public FocusThread(FocusFrameLayout layout) {
            mLayout = layout;
        }

        @Override
        public void run() {
            Looper.prepare();
            mHandler = new FocusHandler(mLayout);
            Looper.loop();
        }

        public Handler getHandler() {
            return mHandler;
        }
    }

    private static class FocusHandler extends Handler {
        private SoftReference<FocusFrameLayout> mRef;

        public FocusHandler(FocusFrameLayout layout) {
            mRef = new SoftReference<>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            FocusFrameLayout layout = mRef.get();
            if (layout != null) {
                switch (msg.what) {
                    case MSG_MOVE:
                        layout.postInvalidate();
                        layout.move();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private class DelayInterpolator implements TimeInterpolator {

        @Override
        public float getInterpolation(float t) {
            return t * t;
        }
    }
}
