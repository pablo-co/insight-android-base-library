package edu.mit.lastmite.insight_library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchFrameLayout extends FrameLayout {

    public interface OnDragListener {
        void onDrag(MotionEvent motionEvent);
    }

    private OnDragListener mOnDragListener;

    public TouchFrameLayout(Context context) {
        super(context);
    }

    public TouchFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnDragListener != null) {
            mOnDragListener.onDrag(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnDragListener(OnDragListener mOnDragListener) {
        this.mOnDragListener = mOnDragListener;
    }
}