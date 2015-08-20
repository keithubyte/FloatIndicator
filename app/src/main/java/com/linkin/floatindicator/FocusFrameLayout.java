package com.linkin.floatindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import java.lang.ref.SoftReference;

/**
 * @author liangjunfeng
 * @since 2015/8/18 19:19
 */
public class FocusFrameLayout extends FrameLayout {

    private static final String TAG = "FocusFrameLayout";

    private Handler mHandler;

    private NinePatchDrawable mDrawable;
    private Rect mDrawablePaddingRect;

    private AccelerateDecelerateInterpolator mInterpolator;

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
        mDrawable = (NinePatchDrawable) context.getResources().getDrawable(R.drawable.ytm_common_focus);
        if (mDrawable != null) {
            mDrawable.setBounds(0, 0, 0, 0);
            mDrawablePaddingRect = new Rect();
            mDrawable.getPadding(mDrawablePaddingRect);
        }
        mHandler = new FocusHandler(this);
        mInterpolator = new AccelerateDecelerateInterpolator();
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
            srcRect.left = (int) (initL + (deltaL * mFrame));
            srcRect.top = (int) (initT + (deltaT * mFrame));
            srcRect.right = (int) (initR + (deltaR * mFrame));
            srcRect.bottom = (int) (initB + (deltaB * mFrame));
/*            srcRect.left = initL + (int) mInterpolator.getInterpolation(deltaL * mFrame);
            srcRect.top = initT + (int) mInterpolator.getInterpolation(deltaT * mFrame);
            srcRect.right = initR + (int) mInterpolator.getInterpolation(deltaR * mFrame);
            srcRect.bottom = initB + (int) mInterpolator.getInterpolation(deltaB * mFrame);
            Log.e(TAG, "deltaL * mFrame = " + (deltaL * mFrame));
            Log.e(TAG, "deltaL * mFrame's interpolation = " + mInterpolator.getInterpolation(deltaL * mFrame));*/
            mHandler.sendEmptyMessage(0);
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
                    case 0:
                        layout.invalidate();
                        layout.move();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
