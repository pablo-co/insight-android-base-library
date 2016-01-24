package edu.mit.lastmite.insight_library.view;

import android.content.Context;
import android.util.AttributeSet;

import com.rey.material.widget.ImageButton;

public class SquareImageButton extends ImageButton {

    public SquareImageButton(Context context) {
        super(context);
    }

    public SquareImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int squareDim = 0;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = this.getMeasuredHeight();
        int width = this.getMeasuredWidth();
        int curSquareDim = Math.max(width, height);

        if (curSquareDim > squareDim) {
            squareDim = curSquareDim;
        }

        setMeasuredDimension(squareDim, squareDim);
    }

}